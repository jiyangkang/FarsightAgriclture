package com.farsight.ji.farsightagriclture.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.CtrlableState;

/**
 * 控制页面
 * Created by jiyan on 2015/11/28.
 */
public class CtrlFragment extends Fragment {

    private View view;
    private CtrlableState warmNode, humifyNode, fanNode, lampNode, drenchingNode, shadeNode, alarmNode, cameraNode;
    private IntentFilter intentFilter;
    private DueBroadCastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerIntentFilter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.getContext().unregisterReceiver(receiver);
    }

    private void registerIntentFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(NodeInfo.WARM);
        intentFilter.addAction(NodeInfo.WARM_D);

        intentFilter.addAction(NodeInfo.HUMIFY);
        intentFilter.addAction(NodeInfo.HUMIFY_D);

        intentFilter.addAction(NodeInfo.FAN);
        intentFilter.addAction(NodeInfo.FAN_D);

        intentFilter.addAction(NodeInfo.LAMP);
        intentFilter.addAction(NodeInfo.LAMP_D);

        intentFilter.addAction(NodeInfo.DRENCHING);
        intentFilter.addAction(NodeInfo.DRENCHING_D);

        intentFilter.addAction(NodeInfo.SHADE);
        intentFilter.addAction(NodeInfo.SHADE_D);

        intentFilter.addAction(NodeInfo.ALARM);
        intentFilter.addAction(NodeInfo.ALARM_D);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ctrl, container, false);

        initShow();

        receiver = new DueBroadCastReceiver();
        view.getContext().registerReceiver(receiver, intentFilter);

        return view;
    }


    private void initShow() {
        warmNode = (CtrlableState) view.findViewById(R.id.node_warm);
        warmNode.setName(NodeInfo.TYPE_WARM);
        warmNode.setBitmapdefault(R.drawable.warm);
        setNode(warmNode, -2);

        humifyNode = (CtrlableState) view.findViewById(R.id.node_humify);
        humifyNode.setName(NodeInfo.TYPE_HUMIFY);
        humifyNode.setBitmapdefault(R.drawable.humify);
        setNode(humifyNode, -2);

        fanNode = (CtrlableState) view.findViewById(R.id.node_fan);
        fanNode.setName(NodeInfo.TYPE_FAN);
        fanNode.setBitmapdefault(R.drawable.fan);
        setNode(fanNode, -2);

        lampNode = (CtrlableState) view.findViewById(R.id.node_lamp);
        lampNode.setName(NodeInfo.TYPE_LAMP);
        lampNode.setBitmapdefault(R.drawable.lamp);
        setNode(lampNode, -2);

        drenchingNode = (CtrlableState) view.findViewById(R.id.node_drenching);
        drenchingNode.setName(NodeInfo.TYPE_DRENCHING);
        drenchingNode.setBitmapdefault(R.drawable.drenching);
        setNode(drenchingNode, -2);

        shadeNode = (CtrlableState) view.findViewById(R.id.node_shade);
        shadeNode.setName(NodeInfo.TYPE_SHADE);
        shadeNode.setBitmapdefault(R.drawable.shade);
        setNode(shadeNode, -2);

        alarmNode = (CtrlableState) view.findViewById(R.id.node_alarm);
        alarmNode.setName(NodeInfo.TYPE_ALARM);
        alarmNode.setBitmapdefault(R.drawable.alarm);
        setNode(alarmNode, -2);

        cameraNode = (CtrlableState) view.findViewById(R.id.node_camera);
        cameraNode.setName(NodeInfo.TYPE_ALARM);
        cameraNode.setBitmapdefault(R.drawable.camera);
        setNode(cameraNode, 2);
    }

    private void setNode(CtrlableState node, int state) {
        node.setState(state);
        node.invalidate();
    }


    private class DueBroadCastReceiver extends BroadcastReceiver {
        int get;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case NodeInfo.WARM://加热器
                    get = intent.getIntExtra(NodeInfo.WARM, -1);
                    setNode(warmNode, get);
                    break;
                case NodeInfo.WARM_D:
                    setNode(warmNode, -1);
                    break;
                case NodeInfo.HUMIFY://加湿器
                    get = intent.getIntExtra(NodeInfo.HUMIFY, -1);
                    setNode(humifyNode, get);
                    break;
                case NodeInfo.HUMIFY_D:
                    setNode(humifyNode, -1);
                    break;
                case NodeInfo.FAN://风扇
                    get = intent.getIntExtra(NodeInfo.FAN, -1);
                    setNode(fanNode, get);
                    break;
                case NodeInfo.FAN_D:
                    setNode(fanNode, -1);
                    break;
                case NodeInfo.DRENCHING://喷淋
                    get = intent.getIntExtra(NodeInfo.DRENCHING, -1);
                    setNode(drenchingNode, get);
                    break;
                case NodeInfo.DRENCHING_D:
                    setNode(drenchingNode, -1);
                    break;
                case NodeInfo.LAMP://照明
                    get = intent.getIntExtra(NodeInfo.LAMP, -1);
                    setNode(lampNode, get);
                    break;
                case NodeInfo.LAMP_D:
                    setNode(lampNode, -1);
                    break;
                case NodeInfo.SHADE://遮阳棚
                    get = intent.getIntExtra(NodeInfo.SHADE, -1);
                    setNode(shadeNode, get);
                    break;
                case NodeInfo.SHADE_D:
                    setNode(shadeNode, -1);
                    break;
                case NodeInfo.ALARM://警报
                    get = intent.getIntExtra(NodeInfo.ALARM, -1);
                    setNode(alarmNode, get);
                    break;
                case NodeInfo.ALARM_D:
                    setNode(alarmNode, -1);
                    break;

            }
        }
    }


}
