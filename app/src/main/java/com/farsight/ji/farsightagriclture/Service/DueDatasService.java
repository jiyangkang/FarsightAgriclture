package com.farsight.ji.farsightagriclture.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.farsight.ji.farsightagriclture.Datas.NodeInfo;
import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.Node.NodeCtrl;
import com.farsight.ji.farsightagriclture.Node.NodeVoir;

/**
 * 数据处理并发送广播
 * Created by jiyan on 2015/11/28.
 */
public class DueDatasService extends Service {

    private NodeVoir envTempAndHumiFirst, envTempAndHumiSecond;
    private NodeVoir soilTempAndHumiFirst, soilTempAndHumiSecond;
    private NodeVoir lightFirst, lightSecond;
    private NodeVoir carbonDioxid;
    private NodeVoir infrared;

    private NodeCtrl warm;
    private NodeCtrl humify;
    private NodeCtrl fan;
    private NodeCtrl lamp;
    private NodeCtrl drenching;




    private NodeCtrl shade;
    private NodeCtrl alarm;

    private boolean threadOn = false;

    private GetDataThread getDataThread = null;
    private ReduceThread reduceThread = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        threadOn = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadClose();
    }

    private void threadClose() {
        threadOn = false;
        if (getDataThread != null) {
            getDataThread.interrupt();
            getDataThread = null;
        }

        if (reduceThread != null) {
            reduceThread.interrupt();
            reduceThread = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getDataThread = new GetDataThread();
        reduceThread = new ReduceThread();
        getDataThread.start();
        reduceThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void voirBroadCast(NodeVoir nodeVoir, String action) {

        int value1, value2;
        int[] send = new int[2];
        Intent intent = new Intent();

        switch (action) {
            case NodeInfo.ENVTEMPFIRST:
                value1 = nodeVoir.getValue()[1];
                value2 = nodeVoir.getValue()[2];
                break;
            case NodeInfo.ENVTEMPSECOND:
                value1 = nodeVoir.getValue()[1];
                value2 = nodeVoir.getValue()[2];
                break;
            case NodeInfo.SOILTEMPFIRST:
                value1 = nodeVoir.getValue()[0];
                value2 = (nodeVoir.getValue()[1] << 8 | nodeVoir.getValue()[2]) / 100;
                break;
            case NodeInfo.SOILTEMPSECOND:
                value1 = nodeVoir.getValue()[0];
                value2 = (nodeVoir.getValue()[1] << 8 | nodeVoir.getValue()[2]) / 100;
                break;
            case NodeInfo.LIGHTFIRST:
                value1 = nodeVoir.getValue()[1] << 8 | nodeVoir.getValue()[2];
                value2 = nodeVoir.getValue()[0];
                break;
            case NodeInfo.LIGHTSECOND:
                value1 = nodeVoir.getValue()[1] << 8 | nodeVoir.getValue()[2];
                value2 = nodeVoir.getValue()[0];
                break;
            case NodeInfo.INFRARED:
                value1 = nodeVoir.getState();
                value2 = -1;
                break;
            case NodeInfo.CARBONDIOXID:
                value1 = nodeVoir.getValue()[1] << 8 | nodeVoir.getValue()[2];
                value2 = -1;
                break;
            default:
                value1 = -1;
                value2 = -1;
                break;
        }

        send[0] = value1;
        send[1] = value2;

        intent.setAction(action);
        intent.putExtra(action, send);
        sendBroadcast(intent);
    }


    private void ctrlBroadCast(NodeCtrl nodeCtrl, String action) {
        int state;
        Intent intent = new Intent();

        state = nodeCtrl.getState();

        intent.setAction(action);
        intent.putExtra(action, state);
        sendBroadcast(intent);
    }

    private class GetDataThread extends Thread {
        @Override
        public void run() {
            super.run();
            byte[] datas;
            try {
                while (threadOn && (datas = TotalDatas.qData.take()) != null) {
                    //有线无线判断--1201
                    if (datas[1] != TotalDatas.netType) {
                        TotalDatas.netType = datas[1];
                        Intent intent = new Intent();
                        intent.setAction(NodeInfo.NET_TYPE);
                        intent.putExtra(NodeInfo.NET_TYPE, datas[1]);
                        sendBroadcast(intent);
                    }
                    switch (datas[4]) {
                        case NodeInfo.TYPE_TEMP://温湿度节点数据
                            switch (datas[2]) {
                                case NodeInfo.ADDR_FIRST_TEMP://环境温湿度1
                                    if (envTempAndHumiFirst != null) {
                                        envTempAndHumiFirst.setAllValue(datas);
                                    } else {
                                        envTempAndHumiFirst = new NodeVoir(datas);
                                    }
                                    voirBroadCast(envTempAndHumiFirst, NodeInfo.ENVTEMPFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_TEMP://环境温湿度2
                                    if (envTempAndHumiSecond != null) {
                                        envTempAndHumiSecond.setAllValue(datas);
                                    } else {
                                        envTempAndHumiSecond = new NodeVoir(datas);
                                    }
                                    voirBroadCast(envTempAndHumiSecond, NodeInfo.ENVTEMPSECOND);
                                    break;
                                case NodeInfo.ADDR_FIRST_SOIL://土壤温湿度1
                                    if (soilTempAndHumiFirst != null) {
                                        soilTempAndHumiFirst.setAllValue(datas);
                                    } else {
                                        soilTempAndHumiFirst = new NodeVoir(datas);
                                    }
                                    voirBroadCast(soilTempAndHumiFirst, NodeInfo.SOILTEMPFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_SOIL://土壤温湿度2
                                    if (soilTempAndHumiSecond != null) {
                                        soilTempAndHumiSecond.setAllValue(datas);
                                    } else {
                                        soilTempAndHumiSecond = new NodeVoir(datas);
                                    }
                                    voirBroadCast(soilTempAndHumiSecond, NodeInfo.SOILTEMPSECOND);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_LIGHT:
                            switch (datas[2]) {
                                case NodeInfo.ADDR_FIRST_LIGHT:
                                    if (lightFirst != null) {
                                        lightFirst.setAllValue(datas);
                                    } else {
                                        lightFirst = new NodeVoir(datas);
                                    }

                                    voirBroadCast(lightFirst, NodeInfo.LIGHTFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_LIGHT:
                                    if (lightSecond != null) {
                                        lightSecond.setAllValue(datas);
                                    } else {
                                        lightSecond = new NodeVoir(datas);
                                    }

                                    voirBroadCast(lightSecond, NodeInfo.LIGHTSECOND);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_CARBON_DIOXID://二氧化碳检测--只有一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_CARBON_DIOXID:
                                    if (carbonDioxid != null) {
                                        carbonDioxid.setAllValue(datas);
                                    } else {
                                        carbonDioxid = new NodeVoir(datas);
                                    }
                                    voirBroadCast(carbonDioxid, NodeInfo.CARBONDIOXID);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_INFRA_RED://红外--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_INFRARED:
                                    if (infrared != null) {
                                        infrared.setAllValue(datas);
                                    } else {
                                        infrared = new NodeVoir(datas);
                                    }
                                    voirBroadCast(infrared, NodeInfo.INFRARED);
                                    break;
                            }
                            break;


                        case NodeInfo.TYPE_WARM://加热器--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_WARM:
                                    if (warm != null) {
                                        warm.setAllValue(datas);
                                    } else {
                                        warm = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(warm, NodeInfo.WARM);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_HUMIFY://加湿器--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_HUMIFY:
                                    if (humify != null) {
                                        humify.setAllValue(datas);
                                    } else {
                                        humify = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(humify, NodeInfo.HUMIFY);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_FAN://排风扇--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_FAN:
                                    if (fan != null) {
                                        fan.setAllValue(datas);
                                    } else {
                                        fan = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(fan, NodeInfo.FAN);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_LAMP://照明灯--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_LAMP:
                                    Log.d("照明","照明");
                                    if (lamp != null) {
                                        lamp.setAllValue(datas);
                                    } else {
                                        lamp = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(lamp, NodeInfo.LAMP);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_DRENCHING://喷淋设备--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_DRENCHING:
                                    if (drenching != null) {
                                        drenching.setAllValue(datas);
                                    } else {
                                        drenching = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(drenching, NodeInfo.DRENCHING);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_SHADE://遮阳棚--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_SHADE:
                                    if (shade != null) {
                                        shade.setAllValue(datas);
                                    } else {
                                        shade = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(shade, NodeInfo.SHADE);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_ALARM://警报器--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_ALARM:
                                    if (alarm != null) {
                                        alarm.setAllValue(datas);
                                    } else {
                                        alarm = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(alarm, NodeInfo.ALARM);
                                    break;
                            }
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReduceThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn) {
                reduceTimeoutVoir(envTempAndHumiFirst, NodeInfo.ENVTEMPFIRST_D);
                reduceTimeoutVoir(envTempAndHumiSecond, NodeInfo.ENVTEMPSECOND_D);
                reduceTimeoutVoir(soilTempAndHumiFirst, NodeInfo.SOILTEMPFIRST_D);
                reduceTimeoutVoir(soilTempAndHumiSecond, NodeInfo.SOILTEMPSECOND_D);
                reduceTimeoutVoir(lightFirst, NodeInfo.LIGHTFIRST_D);
                reduceTimeoutVoir(lightSecond, NodeInfo.LIGHTSECOND_D);
                reduceTimeoutVoir(carbonDioxid, NodeInfo.CARBONDIOXID_D);
                reduceTimeoutVoir(infrared, NodeInfo.INFRARED_D);
                reduceTimeoutCtrl(warm, NodeInfo.WARM_D);
                reduceTimeoutCtrl(humify, NodeInfo.HUMIFY_D);
                reduceTimeoutCtrl(fan, NodeInfo.FAN_D);
                reduceTimeoutCtrl(lamp, NodeInfo.LAMP_D);
                reduceTimeoutCtrl(drenching, NodeInfo.DRENCHING_D);
                reduceTimeoutCtrl(shade, NodeInfo.SHADE_D);
                reduceTimeoutCtrl(alarm, NodeInfo.ALARM_D);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reduceTimeoutVoir(NodeVoir nodeVoir, String action) {
        if (nodeVoir != null) {
            if (nodeVoir.timeOut != 0) {
                nodeVoir.timeOut--;
            } else {
                nodeVoir = null;
                sendDisconnectBroadCast(action);
            }
        }
    }

    private void reduceTimeoutCtrl(NodeCtrl nodeCtrl, String action) {
        if (nodeCtrl != null) {
            if (nodeCtrl.timeOut != 0) {
                nodeCtrl.timeOut--;
            } else {
                nodeCtrl = null;
                sendDisconnectBroadCast(action);
            }
        }
    }

    private void sendDisconnectBroadCast(String aciton) {
        Intent intent = new Intent();
        intent.setAction(aciton);
        intent.putExtra(aciton, aciton);
        sendBroadcast(intent);
    }

}
