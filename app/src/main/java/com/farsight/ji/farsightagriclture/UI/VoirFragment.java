package com.farsight.ji.farsightagriclture.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.R;
import com.farsight.ji.farsightagriclture.Tools.ShowState;

/**
 * 监控页面
 * Created by jiyan on 2015/11/28.
 */
public class VoirFragment extends Fragment {

    private View view;

    private ShowState envNodeFirst, envNodeSecond, soilNodeFirst, soilNodeSecond;
    private ShowState lightNodeFirst, lightNodeSecond;
    private ShowState infraredNode;
    private ShowState carbonDioxideNode;
    private IntentFilter intentFilter;
    private DueBroadCastReceiver receiver;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registeIntentfilter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_voir, container, false);

        initShow();

        receiver = new DueBroadCastReceiver();
        view.getContext().registerReceiver(receiver, intentFilter);

        return view;
    }



    private void registeIntentfilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(NodeInfo.ENVTEMPFIRST);
        intentFilter.addAction(NodeInfo.ENVTEMPFIRST_D);
        intentFilter.addAction(NodeInfo.ENVTEMPSECOND);
        intentFilter.addAction(NodeInfo.ENVTEMPSECOND_D);
        intentFilter.addAction(NodeInfo.SOILTEMPFIRST);
        intentFilter.addAction(NodeInfo.SOILTEMPFIRST_D);
        intentFilter.addAction(NodeInfo.SOILTEMPSECOND);
        intentFilter.addAction(NodeInfo.SOILTEMPSECOND_D);
        intentFilter.addAction(NodeInfo.LIGHTFIRST);
        intentFilter.addAction(NodeInfo.LIGHTFIRST_D);
        intentFilter.addAction(NodeInfo.LIGHTSECOND);
        intentFilter.addAction(NodeInfo.LIGHTSECOND_D);
        intentFilter.addAction(NodeInfo.CARBONDIOXID);
        intentFilter.addAction(NodeInfo.CARBONDIOXID_D);
        intentFilter.addAction(NodeInfo.INFRARED);
        intentFilter.addAction(NodeInfo.INFRARED_D);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.getContext().unregisterReceiver(receiver);
    }

    private void initShow() {
        envNodeFirst = (ShowState) view.findViewById(R.id.node_env_1);
        envNodeFirst.setBitmapNode(R.drawable.env);
        envNodeFirst.setStrValue("未连接", " ");
        envNodeFirst.setName(NodeInfo.ENVTEMPFIRST);
        envNodeFirst.invalidate();

        envNodeSecond = (ShowState) view.findViewById(R.id.node_env_2);
        envNodeSecond.setBitmapNode(R.drawable.env);
        envNodeSecond.setStrValue("未连接", " ");
        envNodeSecond.setName(NodeInfo.ENVTEMPSECOND);
        envNodeSecond.invalidate();

        soilNodeFirst = (ShowState) view.findViewById(R.id.node_soil_1);
        soilNodeFirst.setBitmapNode(R.drawable.soil);
        soilNodeFirst.setStrValue("未连接", " ");
        soilNodeFirst.setName(NodeInfo.SOILTEMPFIRST);
        soilNodeFirst.invalidate();

        soilNodeSecond = (ShowState) view.findViewById(R.id.node_soil_2);
        soilNodeSecond.setBitmapNode(R.drawable.soil);
        soilNodeSecond.setStrValue("未连接", " ");
        soilNodeSecond.setName(NodeInfo.SOILTEMPSECOND);
        soilNodeSecond.invalidate();

        lightNodeFirst = (ShowState) view.findViewById(R.id.node_light_1);
        lightNodeFirst.setBitmapNode(R.drawable.light);
        lightNodeFirst.setStrValue("未连接", null);
        lightNodeFirst.setName(NodeInfo.LIGHTFIRST);
        lightNodeFirst.invalidate();

        lightNodeSecond = (ShowState) view.findViewById(R.id.node_light_2);
        lightNodeSecond.setBitmapNode(R.drawable.light);
        lightNodeSecond.setStrValue("未连接", null);
        lightNodeSecond.setName(NodeInfo.LIGHTSECOND);
        lightNodeSecond.invalidate();

        carbonDioxideNode = (ShowState) view.findViewById(R.id.node_carbon_dioxide);
        carbonDioxideNode.setBitmapNode(R.drawable.co2);
        carbonDioxideNode.setStrValue("未连接", null);
        carbonDioxideNode.setName(NodeInfo.CARBONDIOXID);
        carbonDioxideNode.invalidate();

        infraredNode = (ShowState) view.findViewById(R.id.node_infrered);
        infraredNode.setBitmapNode(R.drawable.infrared);
        infraredNode.setStrValue("未连接", null);
        infraredNode.setName(NodeInfo.INFRARED);
        infraredNode.invalidate();

    }

    private void setTempNode(ShowState node, int[] values, String action) {
        int [] a = values;

        node.setStrValue("温度：" + a[0] + "℃，", "湿度：" + a[1] + "%，电量：" + a[2] + "%");
        node.setPower(a[2]);
        node.invalidate();
    }

    private void setLightNode(ShowState node, int[] values, String action) {
        int [] a =values;
        node.setStrValue("光照强度：" + a[0] + "lux，" + "电量：" + a[1] + "%", null);
        node.setPower(a[1]);
        node.invalidate();
    }

    private void setCarbonDioxidNode(ShowState node, int[] values, String action){
        int[] a = values;
        node.setStrValue("二氧化碳浓度：" +a[0] + "ppm" + "电量：" + a[1] + "%", null);
        node.setPower(a[1]);
        node.invalidate();
    }

    private void setInfraredNode(ShowState node, int[] values, String action){
        int[] a = values;
        if (a[0] == 0){
            node.setStrValue("正常，" + action, null);
        }else{
            node.setStrValue("有人闯入", null);
        }
        node.setPower(a[1]);
        node.invalidate();
    }

    public void disConnect(ShowState node, String action){
        node.setStrValue("断开连接", null);
        node.setPower(100);
        node.invalidate();
    }

    private class DueBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int[] ints;
            String strings;
            switch (action){
                case NodeInfo.ENVTEMPFIRST://环境温湿度
                    ints = intent.getIntArrayExtra(NodeInfo.ENVTEMPFIRST);
                    setTempNode(envNodeFirst, ints, NodeInfo.ENVTEMPFIRST);
                    break;
                case NodeInfo.ENVTEMPFIRST_D:
                    strings = intent.getStringExtra(NodeInfo.ENVTEMPFIRST_D);
                    disConnect(envNodeFirst, strings);
                    break;
                case NodeInfo.ENVTEMPSECOND:
                    ints = intent.getIntArrayExtra(NodeInfo.ENVTEMPFIRST);
                    setTempNode(envNodeSecond, ints,NodeInfo.ENVTEMPSECOND);
                    break;
                case NodeInfo.ENVTEMPSECOND_D:
                    strings = intent.getStringExtra(NodeInfo.ENVTEMPSECOND_D);
                    disConnect(envNodeSecond, strings);
                    break;
                case NodeInfo.SOILTEMPFIRST://土壤温湿度
                    ints = intent.getIntArrayExtra(NodeInfo.SOILTEMPFIRST);
                    setTempNode(soilNodeFirst, ints, NodeInfo.SOILTEMPFIRST);
                    break;
                case NodeInfo.SOILTEMPFIRST_D:
                    strings = intent.getStringExtra(NodeInfo.SOILTEMPFIRST_D);
                    disConnect(soilNodeSecond, strings);
                    break;
                case NodeInfo.SOILTEMPSECOND:
                    ints = intent.getIntArrayExtra(NodeInfo.SOILTEMPSECOND);
                    setTempNode(soilNodeSecond, ints, NodeInfo.SOILTEMPSECOND);
                    break;
                case NodeInfo.SOILTEMPSECOND_D:
                    strings = intent.getStringExtra(NodeInfo.SOILTEMPSECOND_D);
                    disConnect(soilNodeSecond, strings);
                    break;
                case NodeInfo.LIGHTFIRST://光照强度
                    ints = intent.getIntArrayExtra(NodeInfo.LIGHTFIRST);
                    setLightNode(lightNodeFirst, ints, NodeInfo.LIGHTFIRST);
                    break;
                case NodeInfo.LIGHTFIRST_D:
                    strings = intent.getStringExtra(NodeInfo.LIGHTFIRST_D);
                    disConnect(lightNodeFirst, strings);
                    break;
                case NodeInfo.LIGHTSECOND:
                    ints = intent.getIntArrayExtra(NodeInfo.LIGHTSECOND);
                    setLightNode(lightNodeSecond, ints, NodeInfo.LIGHTSECOND);
                    break;
                case NodeInfo.LIGHTSECOND_D:
                    strings = intent.getStringExtra(NodeInfo.LIGHTSECOND_D);
                    disConnect(lightNodeSecond, strings);
                    break;
                case NodeInfo.CARBONDIOXID://二氧化碳浓度
                    ints = intent.getIntArrayExtra(NodeInfo.CARBONDIOXID);
                    setCarbonDioxidNode(carbonDioxideNode, ints, NodeInfo.CARBONDIOXID);
                    break;
                case NodeInfo.CARBONDIOXID_D:
                    strings = intent.getStringExtra(NodeInfo.CARBONDIOXID_D);
                    disConnect(carbonDioxideNode, strings);
                    break;
                case NodeInfo.INFRARED://红外感应器
                    ints = intent.getIntArrayExtra(NodeInfo.INFRARED);
                    setInfraredNode(infraredNode, ints, NodeInfo.INFRARED);
                    break;
                case NodeInfo.INFRARED_D:
                    strings = intent.getStringExtra(NodeInfo.INFRARED_D);
                    disConnect(infraredNode, strings);
                    break;
            }
        }
    }
}
