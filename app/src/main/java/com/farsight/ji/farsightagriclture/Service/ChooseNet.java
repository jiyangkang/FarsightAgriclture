package com.farsight.ji.farsightagriclture.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;
import com.farsight.ji.farsightagriclture.Tools.StringTool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class ChooseNet extends Service {

    private UDPCheck udpCheck;
    private WifiManager wifiManager;

    private String hostName;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        udpCheck = new UDPCheck();
        udpCheck.start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (udpCheck != null) {
            udpCheck = null;
        }
    }

    private class UDPCheck extends Thread {
        @Override
        public void run() {
            while (!TotalDatas.isUDP) {
                Log.d("thread", "On");
                receivFromWifi(12);
            }
        }
    }

    public byte[] receivFromWifi(Integer num) {
        byte[] datas = new byte[num];
        try {
            DatagramSocket socket = new DatagramSocket(TotalDatas.receivePort);
            socket.setBroadcast(true);
            socket.setReceiveBufferSize(datas.length);

            DatagramPacket packet = new DatagramPacket(datas, datas.length);
            socket.receive(packet);
            if (datas[0] == 0x21) {
                hostName = packet.getAddress().getHostName();
                if (hostName != null) {
                    TotalDatas.hostsIp = hostName;
                    TotalDatas.isUDP = true;
                    Intent intent = new Intent();
                    intent.setAction(TotalDatas.ISUDP);
                    sendBroadcast(intent);
                }
            }
            socket.close();

            return packet.getData();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
