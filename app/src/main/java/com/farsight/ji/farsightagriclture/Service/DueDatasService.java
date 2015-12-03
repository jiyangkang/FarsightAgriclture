package com.farsight.ji.farsightagriclture.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

    private void envTempBroadCast(byte[] datas, String action) {

        int temp, power, humify;
        int[] send = new int[3];
        Intent intent = new Intent();

        temp = datas[6];
        humify = datas[7];
        power = datas[10];

        send[0] = temp;
        send[1] = humify;
        send[2] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
        sendBroadcast(intent);
    }

    private void soilTemBroadCast(byte[] datas, String action) {
        int temp, power, humify;
        int[] send = new int[3];
        Intent intent = new Intent();

        temp = datas[5];
        humify = (datas[6] << 8 | datas[7]) / 100;
        power = datas[10];

        send[0] = temp;
        send[1] = humify;
        send[2] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
        sendBroadcast(intent);
    }

    private void lightBroadCast(byte[] datas, String action) {
        int isValidate, lux, power;
        int[] send = new int[3];
        Intent intent = new Intent();

        if (datas[5] == 0) {
            isValidate = 0;
            lux = datas[6] << 8 | datas[7];
            power = datas[10];
        } else {
            isValidate = 1;
            lux = 1;
            power = datas[10];
        }


        send[0] = isValidate;
        send[1] = lux;
        send[2] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
    }

    private void carbondioxidBroadCast(byte[] datas, String action) {
        int power, ppm;
        int[] send = new int[2];
        Intent intent = new Intent();

        ppm = datas[6] << 8 | datas[7];
        power = datas[10];

        send[0] = ppm;
        send[1] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
        sendBroadcast(intent);
    }

    private void infraredBroadCast(byte[] datas, String action) {
        int state, power;
        int[] send = new int[2];
        Intent intent = new Intent();

        state = datas[6];
        power = datas[10];

        send[0] = state;
        send[1] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
        sendBroadcast(intent);
    }

    private void ctrlBroadCast(byte[] datas, String action) {
        int state, power;
        int[] send = new int[2];
        Intent intent = new Intent();

        state = datas[6];
        power = datas[10];

        send[0] = state;
        send[1] = power;

        intent.setAction(action);
        intent.putExtra(action, send);
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
                    if(datas[1] != TotalDatas.netType){
                        TotalDatas.netType = datas[1];
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
                                    envTempBroadCast(datas, NodeInfo.ENVTEMPFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_TEMP://环境温湿度2
                                    if (envTempAndHumiSecond != null) {
                                        envTempAndHumiSecond.setAllValue(datas);
                                    } else {
                                        envTempAndHumiSecond = new NodeVoir(datas);
                                    }
                                    envTempBroadCast(datas, NodeInfo.ENVTEMPSECOND);
                                    break;
                                case NodeInfo.ADDR_FIRST_SOIL://土壤温湿度1
                                    if (soilTempAndHumiFirst != null) {
                                        soilTempAndHumiFirst.setAllValue(datas);
                                    } else {
                                        soilTempAndHumiFirst = new NodeVoir(datas);
                                    }
                                    soilTemBroadCast(datas, NodeInfo.SOILTEMPFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_SOIL://土壤温湿度2
                                    if (soilTempAndHumiSecond != null) {
                                        soilTempAndHumiSecond.setAllValue(datas);
                                    } else {
                                        soilTempAndHumiSecond = new NodeVoir(datas);
                                    }
                                    soilTemBroadCast(datas, NodeInfo.SOILTEMPSECOND);
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

                                    lightBroadCast(datas, NodeInfo.LIGHTFIRST);
                                    break;
                                case NodeInfo.ADDR_SECOND_LIGHT:
                                    if (lightSecond != null) {
                                        lightSecond.setAllValue(datas);
                                    } else {
                                        lightSecond = new NodeVoir(datas);
                                    }

                                    lightBroadCast(datas, NodeInfo.LIGHTSECOND);
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
                                    carbondioxidBroadCast(datas, NodeInfo.CARBONDIOXID);
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
                                    infraredBroadCast(datas, NodeInfo.INFRARED);
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
                                    ctrlBroadCast(datas, NodeInfo.WARM);
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
                                    ctrlBroadCast(datas, NodeInfo.HUMIFY);
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
                                    ctrlBroadCast(datas, NodeInfo.FAN);
                                    break;
                            }
                            break;
                        case NodeInfo.TYPE_LAMP://照明灯--一个
                            switch (datas[2]) {
                                case NodeInfo.ADDR_LAMP:
                                    if (lamp != null) {
                                        lamp.setAllValue(datas);
                                    } else {
                                        lamp = new NodeCtrl(datas);
                                    }
                                    ctrlBroadCast(datas, NodeInfo.LAMP);
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
                                    ctrlBroadCast(datas, NodeInfo.DRENCHING);
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
                                    ctrlBroadCast(datas, NodeInfo.SHADE);
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
                                    ctrlBroadCast(datas, NodeInfo.ALARM);
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
