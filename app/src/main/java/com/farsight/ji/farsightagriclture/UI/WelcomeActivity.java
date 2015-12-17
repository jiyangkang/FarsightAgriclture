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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.DrawButton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 欢迎页面，选择内外网
 * Created by jiyan on 2015/12/2.
 */
public class WelcomeActivity extends Activity implements View.OnTouchListener {

    private CountThread countThread;
    private CheckUdp checkUdp;
    private DrawButton btn;
    private ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    if (checkUdp != null) {
                        checkUdp.interrupt();
                        checkUdp = null;
                    }
                    if (countThread != null) {
                        countThread.interrupt();
                        countThread = null;
                    }
                    btn.setBitmapDefault(R.drawable.scanudp);
                    btn.invalidate();
                    Toast.makeText(WelcomeActivity.this, "没有接收到内网UDP包", Toast.LENGTH_SHORT).show();
                    onCheckDialog();
                    break;
                case 2:
                    dialog.dismiss();
                    if (checkUdp != null) {
                        checkUdp.interrupt();
                        checkUdp = null;
                    }
                    if (countThread != null) {
                        countThread.interrupt();
                        countThread = null;
                    }
                    btn.setBitmapDefault(R.drawable.scanudp);
                    btn.invalidate();
                    Toast.makeText(WelcomeActivity.this, "收到UDP广播包", Toast.LENGTH_SHORT).show();
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
        dialog = ProgressDialog.show(this, null, "正在扫描UPD...");
        btn.setOnTouchListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkUdp != null) {
            checkUdp.interrupt();
            checkUdp = null;
        }
        if (countThread != null) {
            countThread.interrupt();
            countThread = null;
        }
    }

    private void initShow() {
        btn = (DrawButton) findViewById(R.id.btn_scan_udp);
        btn.setBitmapDefault(R.drawable.scanudpuseless);
        btn.invalidate();
    }

    private void onCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("未接受到内网UDP包");
        builder.setMessage("是否使用外网模式？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WelcomeActivity.this, Login.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.btn_scan_udp:

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("TTTTTouch","DDDDD");
                        break;
                    case MotionEvent.ACTION_UP:
                        if (btn.getBit() != R.drawable.scanudpuseless) {
                            Log.d("TTTTTouch","UUUUUP");
                            checkUdp = new CheckUdp();
                            checkUdp.start();
                            countThread = new CountThread();
                            countThread.start();
                            btn.setBitmapDefault(R.drawable.scanudpuseless);
                            btn.invalidate();
                            dialog = ProgressDialog.show(WelcomeActivity.this, null, "正在扫描UDP...");
                            break;
                        }
                }
                break;
        }

        return true;
    }

    private class CheckUdp extends Thread {
        @Override
        public void run() {
            super.run();
            while (!TotalDatas.isUDP) {
                byte[] datas = new byte[12];

                try {
//                    DatagramSocket socket = new DatagramSocket(TotalDatas.receivePort);
                    DatagramSocket socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.bind(new InetSocketAddress(TotalDatas.receivePort));
                    socket.setBroadcast(true);
                    socket.setReceiveBufferSize(datas.length);
                    DatagramPacket packet = new DatagramPacket(datas, datas.length);
                    socket.receive(packet);
                    if (datas[0] == 0x21) {
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

            if (i == 0) {//15秒内未接受到相应的UDP广播
                TotalDatas.isUDP = false;
                checkUdp.interrupt();
                checkUdp = null;
                handler.sendEmptyMessage(1);

            } else {//扫描到UDP广播
                TotalDatas.isUDP = true;
                checkUdp.interrupt();
                checkUdp = null;
                handler.sendEmptyMessage(2);
            }

        }
    }


}
