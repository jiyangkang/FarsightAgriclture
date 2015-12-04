package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.SoapTool;

/**
 *
 * Created by Jaque_Ji on 2015/12/5 0005.
 */
public class Login extends Activity implements View.OnTouchListener{

    private ImageButton btnCancel, btnLogin;
    private EditText editName, editPwd;
    private SoapTool soapTool;
    private ProgressDialog progressDialog;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    progressDialog.dismiss();
                    String re = (String) msg.obj;
                    TotalDatas.isUDP = false;
                    TotalDatas.userId = re;
                    Intent intent = new Intent(Login.this, MainActivity.class);
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
        setContentView(R.layout.login_dialog);

        initShow();

        btnCancel.setOnTouchListener(this);
        btnLogin.setOnTouchListener(this);

        soapTool = new SoapTool();
    }

    private void initShow() {
        btnCancel = (ImageButton) findViewById(R.id.btn_login_cancel);
        btnLogin = (ImageButton) findViewById(R.id.btn_login_login);
        editName = (EditText) findViewById(R.id.edt_name);
        editPwd = (EditText) findViewById(R.id.edt_pwd);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){
            case R.id.btn_login_cancel:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnCancel.setBackgroundResource(R.drawable.login_cancel_clicked);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnCancel.setBackgroundResource(R.drawable.login_cancel);
                        Intent intent = new Intent(Login.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.btn_login_login:
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnLogin.setBackgroundResource(R.drawable.login_login_clicked);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnLogin.setBackgroundResource(R.drawable.login_login);
                        checkUser();

                        break;
                }
                break;
        }

        return true;
    }

    private void checkUser(){
        final String name = editName.getText().toString();
        final String pwd = editPwd.getText().toString();

        if (name.length() < 5){
            Toast.makeText(Login.this,"用户名不得少于5位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pwd.length() < 5){
            Toast.makeText(Login.this,"密码不得少于5位", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = ProgressDialog.show(Login.this, null, "正在尝试登陆");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String re;
                if ((re = soapTool.UserCheck(name, pwd)) != null){
                    if (re.equalsIgnoreCase("error1")){
                        handler.sendEmptyMessage(1);
                    } else if (re.equalsIgnoreCase("error2")){
                        handler.sendEmptyMessage(2);
                    }else {
                        Message msg = new Message();
                        msg.what = 3;
                        msg.obj = re;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();

    }
}
