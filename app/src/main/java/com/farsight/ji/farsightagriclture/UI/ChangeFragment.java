package com.farsight.ji.farsightagriclture.UI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

/**
 * 网络切换
 * Created by jiyan on 2015/12/7.
 */
public class ChangeFragment extends Fragment implements View.OnTouchListener {

    private View view;
    private DrawButton btnChange485, btnChangeZigbee;
    private TextView textNet;
    private String netType;
    private NetTypeBroadCastReceiver receiver;
    private IntentFilter intentFilter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setNet(NodeInfo.NET_TYPE_485);
                    break;
                case 2:
                    setNet(NodeInfo.NET_TYPE_ZIGBEE);
                    break;
                default:
                    setNet(TotalDatas.netType);
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
        view = inflater.inflate(R.layout.fragment_change, container, false);

        initShow();

        receiver = new NetTypeBroadCastReceiver();
        view.getContext().registerReceiver(receiver, intentFilter);

        setNet(TotalDatas.netType);



        btnChange485.setOnTouchListener(this);
        btnChangeZigbee.setOnTouchListener(this);

        return view;
    }

    private void initShow() {
        btnChange485 = (DrawButton) view.findViewById(R.id.btn_change_net_485);


        btnChangeZigbee = (DrawButton) view.findViewById(R.id.btn_change_net_zigbee);

        textNet = (TextView) view.findViewById(R.id.text_net);

        setNet(TotalDatas.netType);

        btnChange485.invalidate();
        btnChangeZigbee.invalidate();


        intentFilter = new IntentFilter();
        intentFilter.addAction(NodeInfo.NET_TYPE);
    }

    private void setNet(byte type) {
        switch (type) {
            case NodeInfo.NET_TYPE_ZIGBEE:
                btnChange485.setBitmapDefault(R.drawable.part_net_485_noclicked);
                btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_clicked);
                textNet.setText(NodeInfo.NET_TYPE_NAME_ZIGBEE);
                btnChange485.setClickable(true);
                btnChangeZigbee.setClickable(false);
                break;
            case NodeInfo.NET_TYPE_485:
                btnChange485.setBitmapDefault(R.drawable.part_net_485_clicked);
                btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_noclicked);
                textNet.setText(NodeInfo.NET_TYPE_NAME_485);
                btnChange485.setClickable(false);
                btnChangeZigbee.setClickable(true);
                break;
            default:
                btnChange485.setBitmapDefault(R.drawable.part_net_485_noclicked);
                btnChangeZigbee.setBitmapDefault(R.drawable.part_net_zigbee_noclicked);
                btnChange485.setClickable(false);
                btnChangeZigbee.setClickable(false);
                textNet.setText("未知网络");
                break;
        }
        btnChange485.invalidate();
        btnChangeZigbee.invalidate();
    }

    private class NetTypeBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte type = intent.getByteExtra(NodeInfo.NET_TYPE, (byte) 0x00);
            switch (intent.getAction()) {
                case NodeInfo.NET_TYPE:
                    switch (type) {
                        case NodeInfo.NET_TYPE_485:

                            handler.sendEmptyMessage(1);
                            break;
                        case NodeInfo.NET_TYPE_ZIGBEE:

                            handler.sendEmptyMessage(2);
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
                if (TotalDatas.netType != NodeInfo.NET_TYPE_485 && btnChange485.isClickable()) {
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
                if (TotalDatas.netType != NodeInfo.NET_TYPE_ZIGBEE && btnChangeZigbee.isClickable()) {
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
