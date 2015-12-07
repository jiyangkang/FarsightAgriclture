package com.farsight.ji.farsightagriclture.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.DrawButton;

import org.w3c.dom.Node;

/**
 * Created by jiyan on 2015/12/7.
 */
public class ChangeFragement extends Fragment implements View.OnTouchListener {

    private View view;
    private DrawButton btnChange485, btnChangeZigbee;
    private TextView textNet;
    private String netType;
    private NetTypeBroadCastReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change, container, false);

        initShow();
        btnChange485.setOnTouchListener(this);
        btnChangeZigbee.setOnTouchListener(this);
        receiver = new NetTypeBroadCastReceiver();
        view.getContext().registerReceiver(receiver, intentFilter);
        setNet(TotalDatas.netType);
        return view;
    }

    private void initShow() {
        btnChange485 = (DrawButton) view.findViewById(R.id.btn_change_net_485);
        if (TotalDatas.netType != NodeInfo.NET_TYPE_485) {
            btnChange485.setBitmapDefault(R.drawable.part_net_485_clicked);
        }else {
            btnChange485.setBitmapDefault(R.drawable.part_net_485_noclicked);
        }
        btnChange485.invalidate();

        btnChangeZigbee = (DrawButton) view.findViewById(R.id.btn_change_net_zigbee);
        if (TotalDatas.netType != NodeInfo.NET_TYPE_ZIGBEE){
            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_clicked);

        }else{
            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_noclicked);
        }
        btnChangeZigbee.invalidate();

        textNet = (TextView) view.findViewById(R.id.text_net);

        intentFilter = new IntentFilter();
        intentFilter.addAction(NodeInfo.NET_TYPE);
    }

    private void setNet(byte type) {
        switch (type) {
            case NodeInfo.NET_TYPE_ZIGBEE:
                textNet.setText("ZIGBEE网络");
                break;
            case NodeInfo.NET_TYPE_485:
                textNet.setText("485网络");
                break;
        }
    }

    private class NetTypeBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            byte type = intent.getByteExtra(NodeInfo.NET_TYPE, (byte)0x00);
            switch (intent.getAction()){
                case NodeInfo.NET_TYPE:
                    switch (type){
                        case NodeInfo.NET_TYPE_485:
                            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_noclicked);
                            btnChangeZigbee.setClickable(true);
                            btnChangeZigbee.invalidate();
                            btnChange485.setBitmapDefault(R.drawable.part_net_485_clicked);
                            btnChange485.setClickable(false);
                            btnChange485.invalidate();
                            setNet(type);
                            break;
                        case NodeInfo.NET_TYPE_ZIGBEE:
                            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_clicked);
                            btnChangeZigbee.setClickable(false);
                            btnChangeZigbee.invalidate();
                            btnChange485.setBitmapDefault(R.drawable.part_net_485_noclicked);
                            btnChange485.setClickable(true);
                            btnChange485.invalidate();
                            setNet(type);
                            break;
                    }
                    break;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        view.getContext().unregisterReceiver(receiver);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.btn_change_net_485:
                if (TotalDatas.netType != NodeInfo.NET_TYPE_485) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            btnChange485.setBitmapDefault(R.drawable.part_net_485_clicked);
                            btnChange485.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            btnChange485.setBitmapDefault(R.drawable.part_net_485_noclicked);
                            try {
                                TotalDatas.qSend.put(NodeInfo.CHANGE_485);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            btnChange485.invalidate();
                            break;
                    }
                }
                break;
            case R.id.btn_change_net_zigbee:
                if (TotalDatas.netType != NodeInfo.NET_TYPE_ZIGBEE) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_clicked);
                            btnChangeZigbee.invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                            btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_noclicked);
                            try {
                                TotalDatas.qSend.put(NodeInfo.CHANGE_ZIGBEE);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            btnChangeZigbee.invalidate();
                            break;
                    }
                }
                break;
        }

        return true;
    }
}
