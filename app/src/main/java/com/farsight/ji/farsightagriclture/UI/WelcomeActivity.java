package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.SoapTool;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * 欢迎页面，选择内外网
 * Created by jiyan on 2015/12/2.
 */
public class WelcomeActivity extends Activity {

    private CountThread countThread;
    private ProgressDialog progressDialog;
    private SoapTool soapTool;
    private CheckUdp checkUdp;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(WelcomeActivity.this, "没有接收到内网UDP包", Toast.LENGTH_SHORT).show();
                    loginAlterDialog();
                    break;
                case 2:
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置全屏和无标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome_activity);

        initShow();
        countThread = new CountThread();
        countThread.start();

        checkUdp = new CheckUdp();
        checkUdp.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initShow() {
        soapTool = new SoapTool();
    }

    private class CheckUdp extends Thread {
        @Override
        public void run() {
            super.run();
            while (!TotalDatas.isUDP) {
                byte[] datas = new byte[12];

                try {
                    DatagramSocket socket = new DatagramSocket(TotalDatas.receivePort);
                    socket.setBroadcast(true);
                    socket.setReceiveBufferSize(datas.length);
                    DatagramPacket packet = new DatagramPacket(datas, datas.length);
                    socket.receive(packet);
                    if(datas[0] == 0x21){
                        TotalDatas.hostsIp = packet.getAddress().getHostName();
                        TotalDatas.isUDP = true;
                    }
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private class CountThread extends Thread {
        int i = 100;

        @Override
        public void run() {
            super.run();
            while (!TotalDatas.isUDP) {
                i--;
                if (i == 0)
                    break;
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (i == 0) {
                TotalDatas.isUDP = false;
                checkUdp.interrupt();
                checkUdp = null;
                handler.sendEmptyMessage(1);

            } else {
                TotalDatas.isUDP = true;
                checkUdp.interrupt();
                checkUdp = null;
                handler.sendEmptyMessage(2);
            }

        }
    }

    private void loginAlterDialog() {
        countThread = null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        final View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.login_dialog, null);
        builder.setView(view);
        final EditText edtName = (EditText) view.findViewById(R.id.edt_name);
        final EditText edtPswd = (EditText) view.findViewById(R.id.edt_pswd);

        builder.setPositiveButton(R.string.login_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = edtName.getText().toString();
                        final String pswd = edtPswd.getText().toString();
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (name.length() < 4) {
                            Toast.makeText(WelcomeActivity.this, "用户名不得少于4位", Toast.LENGTH_SHORT).show();
                        } else if (pswd.length() < 4) {
                            Toast.makeText(WelcomeActivity.this, "密码不得少于4位", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog = ProgressDialog.show(view.getContext(), null, "正在连接", true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String re ;
                                    Log.d("NAME", name +"  "+ pswd);
                                            if ((re =soapTool.UserCheck(name, pswd)) != null) {
                                                if (re.equalsIgnoreCase("error1")) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(WelcomeActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                                                } else if (re.equalsIgnoreCase("error2")) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(WelcomeActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    TotalDatas.userId = re;
                                                    progressDialog.dismiss();

                                                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                progressDialog.dismiss();
                                            }
                                }
                            }).start();

                        }

                    }
                }

        );

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();


        alertDialog.setTitle(R.string.login_title);

        alertDialog.show();

//        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//
//                countThread = new CountThread();
//                countThread.start();
//
//            }
//        });

    }

}
