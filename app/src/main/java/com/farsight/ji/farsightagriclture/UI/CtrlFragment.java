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
    private CtrlableState warmNode, humifyNode, fanNode, lampNode, drenchingNode, shadeNode, alarmNode;
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
        setNode(warmNode, -2, 100);
        warmNode.invalidate();

        humifyNode = (CtrlableState) view.findViewById(R.id.node_humify);
        humifyNode.setName(NodeInfo.TYPE_HUMIFY);
        setNode(humifyNode, -2, 100);
        humifyNode.invalidate();

        fanNode = (CtrlableState) view.findViewById(R.id.node_fan);
        fanNode.setName(NodeInfo.TYPE_FAN);
        setNode(fanNode, -2, 100);
        fanNode.invalidate();

        lampNode = (CtrlableState) view.findViewById(R.id.node_lamp);
        lampNode.setName(NodeInfo.TYPE_LAMP);
        setNode(lampNode, -2, 100);
        lampNode.invalidate();

        drenchingNode = (CtrlableState) view.findViewById(R.id.node_drenching);
        drenchingNode.setName(NodeInfo.TYPE_DRENCHING);
        setNode(drenchingNode, -2, 100);
        drenchingNode.invalidate();

        shadeNode = (CtrlableState) view.findViewById(R.id.node_shade);
        shadeNode.setName(NodeInfo.TYPE_SHADE);
        setNode(shadeNode, -2, 100);
        shadeNode.invalidate();

        alarmNode = (CtrlableState) view.findViewById(R.id.node_alarm);
        alarmNode.setName(NodeInfo.TYPE_ALARM);
        setNode(alarmNode, -2, 100);
        alarmNode.invalidate();
    }

    private void setNode(CtrlableState node, int state, int power) {
        node.setPower(power, state);
        node.invalidate();
    }


    private class DueBroadCastReceiver extends BroadcastReceiver {
        int[] get = new int[2];

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case NodeInfo.WARM://加热器
                    get = intent.getIntArrayExtra(NodeInfo.WARM);
                    setNode(warmNode, get[0], get[1]);
                    break;
                case NodeInfo.WARM_D:
                    setNode(warmNode, -1, 100);
                    break;
                case NodeInfo.HUMIFY://加湿器
                    get = intent.getIntArrayExtra(NodeInfo.HUMIFY);
                    setNode(humifyNode, get[0], get[1]);
                    break;
                case NodeInfo.HUMIFY_D:
                    setNode(humifyNode, -1, 100);
                    break;
                case NodeInfo.FAN://风扇
                    get = intent.getIntArrayExtra(NodeInfo.FAN);
                    setNode(fanNode, get[0], get[1]);
                    break;
                case NodeInfo.FAN_D:
                    setNode(fanNode, -1, 100);
                    break;
                case NodeInfo.DRENCHING://喷淋
                    get = intent.getIntArrayExtra(NodeInfo.DRENCHING);
                    setNode(drenchingNode, get[0], get[1]);
                    break;
                case NodeInfo.DRENCHING_D:
                    setNode(drenchingNode, -1, 100);
                    break;
                case NodeInfo.LAMP://照明
                    get = intent.getIntArrayExtra(NodeInfo.LAMP);
                    setNode(lampNode, get[0], get[1]);
                    break;
                case NodeInfo.LAMP_D:
                    setNode(lampNode, -1, 100);
                    break;
                case NodeInfo.SHADE://遮阳棚
                    get = intent.getIntArrayExtra(NodeInfo.SHADE);
                    setNode(shadeNode, get[0], get[1]);
                    break;
                case NodeInfo.SHADE_D:
                    setNode(shadeNode, -1, 100);
                    break;
                case NodeInfo.ALARM://警报
                    get = intent.getIntArrayExtra(NodeInfo.ALARM);
                    setNode(alarmNode, get[0], get[1]);
                    break;
                case NodeInfo.ALARM_D:
                    setNode(alarmNode, -1, 100);
                    break;

            }
        }
    }


}
