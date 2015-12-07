package com.farsight.ji.farsightagriclture.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.Tools.SoapTool;
import com.farsight.ji.farsightagriclture.Tools.StringTool;
import com.farsight.ji.farsightagriclture.UI.MainActivity;

/**
 *
 *
 * Created by jiyan on 2015/12/3.
 */
public class SoapService extends Service{

    private SoapTool send;
    private SoapTool get;
    private GetThread getThread;
    private SendThread sendThread;

    private boolean threadOn = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        send = new SoapTool();
        get = new SoapTool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        threadOn = true;
        getThread = new GetThread();
        getThread.start();
        sendThread = new SendThread();
        sendThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadOn = false;
        getThread = null;
        sendThread = null;
    }

    private class GetThread extends Thread{
        String strGet;
        byte[][] datas;
        @Override
        public void run() {
            super.run();
            while(threadOn){
                Log.d("SOAP", "GetThread");
                strGet = get.CliGet(TotalDatas.DEVICE);
                if (strGet != null){
                    Log.d("SOAP", strGet);
                    datas = StringTool.getHexSer(strGet);
                    if (datas != null){
                        for (byte[] data : datas) {
                            try {
                                TotalDatas.qData.put(data);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class SendThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadOn){
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    String sendString = StringTool.hexToString(TotalDatas.qSend.take());
                    stringBuilder.append(sendString);
                    stringBuilder.append(TotalDatas.userId);
                    stringBuilder.append(TotalDatas.DEVICE);

                    String sendTo = stringBuilder.toString();
                    Log.d("SendTo" , sendTo);
                    String request = send.CliPut(sendTo);
                    if (request != null && request.equalsIgnoreCase("ok")){
//                        Toast.makeText(MainActivity.this, "命令发送成功", Toast.LENGTH_SHORT).show();?
                        Log.d("SOAP", "OK");
                    }else {
                        Log.d("SOAP","Cant't send");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
