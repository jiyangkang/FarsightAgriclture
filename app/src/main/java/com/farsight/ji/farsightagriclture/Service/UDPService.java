package com.farsight.ji.farsightagriclture.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.Tools.StringTool;
import com.farsight.ji.farsightagriclture.Tools.UDPTool;

/**
 * UDP服务
 * Created by jiyan on 2015/11/28.
 */
public class UDPService extends Service {

    private UDPTool udpTool = null;
    private boolean threadOn = false;
    private SendCMDThread sendCMDThread = null;
    private GetDatasThread getDatasThread = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        udpTool = new UDPTool(this, TotalDatas.hostsIp, TotalDatas.receivePort, TotalDatas.sendPort);
        threadOn = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadClose();
    }

    public void threadClose() {
        threadOn = false;

        if (getDatasThread != null) {
            getDatasThread = null;
        }

        if (sendCMDThread != null) {
            sendCMDThread = null;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        sendCMDThread = new SendCMDThread();
        sendCMDThread.start();

        getDatasThread = new GetDatasThread();
        getDatasThread.start();

        return super.onStartCommand(intent, flags, startId);

    }



    private class SendCMDThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn && udpTool.wifiManager.isWifiEnabled()) {
                try {
                    udpTool.sendToWifi(TotalDatas.qSend.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetDatasThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (threadOn && udpTool.wifiManager.isWifiEnabled()) {
                byte[] datas = udpTool.receivFromWifi(12);
                if (datas != null) {
                    try {

                        Log.d("RECEIVE", StringTool.hexToString(datas));
                        TotalDatas.qData.put(datas);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
