package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Service.ChooseNet;

/**
 * 欢迎页面，选择内外网
 * Created by jiyan on 2015/12/2.
 */
public class WelcomeActivity extends Activity {

    private IntentFilter intentFilter;
    private BroadCastReciver receiver;
    private TextView textView;
    private String host;

    private CountThread countThread;
    private ProgressDialog progressDialog;

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
        registerReceiver(receiver, intentFilter);
        Intent intendChoose = new Intent(this, ChooseNet.class);
        startService(intendChoose);
        countThread = new CountThread();
        countThread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initShow() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(TotalDatas.ISUDP);
        receiver = new BroadCastReciver();
    }

    private class BroadCastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case TotalDatas.ISUDP:
                    Intent intent1 = new Intent(WelcomeActivity.this, ChooseNet.class);
                    stopService(intent1);
                    break;
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
                Intent intent = new Intent(WelcomeActivity.this, ChooseNet.class);
                stopService(intent);
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }

            while(i > -100){
                i--;
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(2);

        }
    }

    private void loginAlterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
        View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.login_dialog, null);
        builder.setView(view);
        final EditText edtName = (EditText) view.findViewById(R.id.edt_name);
        final EditText edtPswd = (EditText) view.findViewById(R.id.edt_pswd);

        builder.setPositiveButton(R.string.login_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }

        );
        final AlertDialog alertDialog = builder.create();




        alertDialog.setTitle(R.string.login_title);

        alertDialog.show();

    }

}
