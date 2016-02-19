package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.DrawButton;
import com.farsight.ji.farsightagriclture.Tools.StringTool;

/**
 * 二维码和报警电话设置
 * Created by jiyangkang on 2016/1/28 0028.
 */
public class CodeAndTel extends Fragment implements View.OnTouchListener{

    private View view;
    private EditText edtTelNum;
    private DrawButton btnCode, btnSubmit;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(view.getContext(),"电话号码非法", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(view.getContext(),"请注意电话长度为11位", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_code_tel, container, false);

        btnCode = (DrawButton) view.findViewById(R.id.code_get);
        btnCode.setBitmapDefault(R.drawable.code_tel_noclicked);
        btnCode.invalidate();
        btnCode.setOnTouchListener(this);
        btnSubmit = (DrawButton) view.findViewById(R.id.code_submit);
        btnSubmit.setBitmapDefault(R.drawable.setting_submit_noclicked);
        btnSubmit.invalidate();
        btnSubmit.setOnTouchListener(this);
        edtTelNum = (EditText) view.findViewById(R.id.edt_tet_num);

        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.code_get:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnCode.setBitmapDefault(R.drawable.code_tel_clicked);
                        btnCode.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        btnCode.setBitmapDefault(R.drawable.code_tel_noclicked);
                        btnCode.invalidate();
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.show();
                        Window window = alertDialog.getWindow();
                        window.setContentView(R.layout.code_imge);
                        TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                        tv_title.setText("二维码");
                        ImageView imageView = (ImageView) window.findViewById(R.id.img_code);
                        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.smarthouses));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.code_submit:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnSubmit.setBitmapDefault(R.drawable.setting_submit_clicked);
                        btnSubmit.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        btnSubmit.setBitmapDefault(R.drawable.setting_submit_noclicked);
                        btnSubmit.invalidate();
                        String str = edtTelNum.getText().toString();
                        Log.d("STring", str.length() + "");
                        if ((!str.equalsIgnoreCase("")) && (str.length() == 11)) {
                            byte[] datas = str.getBytes();
                            if (datas[0] != 0x31) {
                                handler.sendEmptyMessage(1);
                            }
                            byte[] CMD = new byte[16];
                            CMD[0] = NodeInfo.CMD_HEAD;
                            CMD[1] = NodeInfo.CMD_TEL_NUM;
                            CMD[2] = 0x00;
                            //将电话号码装入控制命令中--23 65 00 (号码) 00 00--16位
                            System.arraycopy(datas, 0, CMD, 3, datas.length);
                            CMD[14] = CMD[15] = 0x00;

                            Log.d("STring", CMD.length + ":" + StringTool.printHex(CMD));

                            try {//发送电话号码到平台
                                TotalDatas.qSend.put(CMD);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else {
                            handler.sendEmptyMessage(2);
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
        return true;
    }
}
