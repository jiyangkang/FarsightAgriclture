package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;

import org.w3c.dom.Node;


/**
 * Created by jiyangkang on 2016/1/26 0026.
 */
public class ThresholdSettingFragment extends Fragment implements OnClickListener {


    private View view;
    private EditText edtTempH, edtTempL, edtHumH, edtHumL, edtLightH, edtLightL, edtCo2H, edtCo2L;
    private String str;
    private IntentFilter intentFilter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String massge;
            switch (msg.what) {
                case 1:
                    massge = (String) msg.obj;
                    Toast.makeText(view.getContext(), massge, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        initShow();

        view.findViewById(R.id.setting_auto).setOnClickListener(this);
        view.findViewById(R.id.setting_menu).setOnClickListener(this);
        view.findViewById(R.id.setting_submit).setOnClickListener(this);

        return view;
    }

    private void initShow() {
        edtTempH = (EditText) view.findViewById(R.id.et_temp_h);
        edtTempL = (EditText) view.findViewById(R.id.et_tmp_l);
        edtHumH = (EditText) view.findViewById(R.id.et_hum_h);
        edtHumL = (EditText) view.findViewById(R.id.et_hum_l);
        edtLightH = (EditText) view.findViewById(R.id.et_light_h);
        edtLightL = (EditText) view.findViewById(R.id.et_light_l);
        edtCo2H = (EditText) view.findViewById(R.id.et_co_h);
        edtCo2L = (EditText) view.findViewById(R.id.et_co_l);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_auto:
                sendCmd(1);
                break;
            case R.id.setting_menu:
                sendCmd(2);
                break;
            case R.id.setting_submit:
                sendCmd(3);
                break;
            default:
                break;
        }
    }

    private void sendHandlerMessage(String name, int errorType) {
        Message msg = new Message();
        msg.what = 1;
        switch (errorType) {
            case 1:
                msg.obj = "请设定" + name + "最大值";
                break;
            case 2:
                msg.obj = "请设定" + name + "最小值";
                break;
            case 3:
                msg.obj = name + "最大值不得小于最小值";
                break;
            case 4:
                msg.obj = name + "设定超出量程";
                break;
        }
        handler.sendMessage(msg);
    }

    private byte[] CMD = null;

    private void setCMD(int typeNode) {
        if (CMD == null){
            CMD = new byte[16];
        }
        String valueH, valueL, name;
        byte[] byteValueH;
        byte[] byteValueL;
        int pH_H, pH_L, pL_H, pL_L;
        int max, min;
        int intValueH, intValueL;
        switch (typeNode) {
            case 1:
                name = "温度";
                pH_H = 0;
                pL_H = 0;
                pH_L = NodeInfo.CMD_T_MAX;
                pL_L = NodeInfo.CMD_T_MIN;
                max = NodeInfo.T_MAX;
                min = NodeInfo.T_MIN;
                valueH = edtTempH.getText().toString();
                valueL = edtTempL.getText().toString();
                break;
            case 2:
                name = "湿度";
                pH_H = 0;
                pL_H = 0;
                max = NodeInfo.H_MAX;
                min = NodeInfo.H_MIN;
                pH_L = NodeInfo.CMD_H_MAX;
                pL_L = NodeInfo.CMD_H_MIN;
                valueH = edtHumH.getText().toString();
                valueL = edtHumL.getText().toString();
                break;
            case 3:
                pH_H = NodeInfo.CMD_L_MAX_H;
                pL_H = NodeInfo.CMD_L_MIN_H;
                pH_L = NodeInfo.CMD_L_MAX_L;
                pL_L = NodeInfo.CMD_L_MIN_L;
                max = NodeInfo.L_MAX;
                min = NodeInfo.L_MIN;
                valueH = edtLightH.getText().toString();
                valueL = edtLightL.getText().toString();
                name = "光照";
                break;
            case 4:
                pH_H = NodeInfo.CMD_C_MAX_H;
                pL_H = NodeInfo.CMD_C_MIN_H;
                pH_L = NodeInfo.CMD_C_MAX_L;
                pL_L = NodeInfo.CMD_C_MIN_L;
                max = NodeInfo.C_MAX;
                min = NodeInfo.C_MIN;
                valueH = edtCo2H.getText().toString();
                valueL = edtCo2L.getText().toString();
                name = "二氧化碳";
                break;
            default:
                pH_H = 0;
                pL_H = 0;
                pH_L = 0;
                pL_L = 0;
                max = 0;
                min = 0;
                name = "...";
                valueH = "";
                valueL = "";
                break;
        }
        intValueH = getValue(valueH);
        intValueL = getValue(valueL);
        if (intValueH == TotalDatas.ERROR){
            sendHandlerMessage(name, 1);
            CMD = null;
            return;
        }
        if (intValueL == TotalDatas.ERROR){
            sendHandlerMessage(name, 2);
            CMD = null;
            return;
        }


        if (intValueH < intValueL){
            sendHandlerMessage(name, 3);
            CMD = null;
            return;
        } else if (intValueH > max || intValueL < min){

            sendHandlerMessage(name, 4);
            CMD = null;
            return;
        }

        byteValueH = new byte[2];
        byteValueH[0] = (byte)(intValueH >> 8 & 0x00ff);
        byteValueH[1] = (byte)(intValueH & 0x00ff);
        byteValueL = new byte[2];
        byteValueL[0] = (byte)(intValueL >> 8 & 0x00ff);
        byteValueL[1] = (byte)(intValueL & 0x00ff);

        CMD[pH_L] = byteValueH[1];
        CMD[pL_L] = byteValueL[1];
        if (pH_H != 0 || pL_H != 0){
            CMD[pH_H] = byteValueH[0];
            CMD[pL_H] = byteValueL[0];
        }

    }

    private void makeCmd() {

        setCMD(1);
        if (CMD != null) {
            setCMD(2);
        }
        if (CMD != null) {
            setCMD(3);
        }
        if (CMD != null) {
            setCMD(4);
        }

        if (CMD != null){
            CMD[0] = NodeInfo.CMD_HEAD;
            CMD[1] = NodeInfo.CMD_SEND_SETTING;
            CMD[2] = 0x00;
            CMD[15] = 0x00;
        }

    }

    private int getValue(String text) {
        int v;

        if (text.equalsIgnoreCase("")) {
            return TotalDatas.ERROR;
        } else {
            Log.e(text, text);
            v = Integer.parseInt(text);
            return v;
        }
    }


    private void sendCmd(int type) {
        switch (type) {
            case 1:
                try {
                    TotalDatas.qSend.put(NodeInfo.CHANGE_CONTRL_MODE_AUTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    TotalDatas.qSend.put(NodeInfo.CHANGE_CONTRL_MODE_MENU);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                makeCmd();
                if (CMD != null) try {
                    TotalDatas.qSend.put(CMD);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CMD = null;
                break;
        }
    }
}
