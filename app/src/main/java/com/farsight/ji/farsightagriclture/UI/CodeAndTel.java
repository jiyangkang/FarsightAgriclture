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
import com.farsight.ji.farsightagriclture.Tools.StringTool;

/**
 * Created by jiyangkang on 2016/1/28 0028.
 */
public class CodeAndTel extends Fragment implements View.OnClickListener{

    private View view;
    private EditText edtTelNum;

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

        view.findViewById(R.id.code_get).setOnClickListener(this);
        view.findViewById(R.id.code_submit).setOnClickListener(this);
        edtTelNum = (EditText) view.findViewById(R.id.edt_tet_num);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.code_get:
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.code_imge);
                TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                tv_title.setText("二维码");
                ImageView imageView = (ImageView) window.findViewById(R.id.img_code);
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.smarthouses));
                break;
            case R.id.code_submit:
                String str = edtTelNum.getText().toString();
                Log.d("STring", str.length()+"");
                if ((!str.equalsIgnoreCase("")) && (str.length()== 11)){
                    byte[] datas = str.getBytes();
                    if (datas[0] != 0x31){
                        handler.sendEmptyMessage(1);
                    }
                    byte[] CMD = new byte[16];
                    CMD[0] = NodeInfo.CMD_HEAD;
                    CMD[1] = NodeInfo.CMD_TEL_NUM;
                    for (int i = 0; i < 11; i++){
                        CMD[2+i] = datas[i];
                    }
                    CMD[13] = CMD[14] = CMD[15] = 0x00;

                    Log.d("STring", CMD.length+":"+StringTool.printHex(CMD));

                    try {
                        TotalDatas.qSend.put(CMD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    handler.sendEmptyMessage(2);
                }
                break;
        }
    }
}
