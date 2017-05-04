package com.farsight.ji.farsightagriclture.Tools;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * WIFI使用UDP交互模式
 * Created by jiyan on 2015/11/28.
 */
public class UDPTool {
    private Context context;
    private String host;
    private Integer receivPort, sendPort;
    public WifiManager wifiManager;

    private InetAddress inetAddress;


    /**
     * 构造函数
     *
     * @param context  传入的上下文
     * @param host     主机IP地址
     * @param port     接收端端口
     * @param sendPort 发送端端口
     */
    public UDPTool(Context context, String host, Integer port, Integer sendPort) {
        super();
        this.context = context;
        this.host = host;
        this.receivPort = port;
        this.sendPort = sendPort;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    inetAddress = InetAddress.getByName(UDPTool.this.host);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 接收
     *
     * @param num 要接收的数据长度
     * @return !=null接收到的数据byte[]数据， ==null出错
     */
    public byte[] receivFromWifi(Integer num) {
        byte[] datas = new byte[num];
        try {
//            DatagramSocket socket = new DatagramSocket(receivPort);
            DatagramSocket socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(TotalDatas.receivePort));
            socket.setBroadcast(true);
            socket.setReceiveBufferSize(datas.length);
            DatagramPacket packet = new DatagramPacket(datas, datas.length);

            socket.receive(packet);
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

    /**
     * 发送
     *
     * @param datas：要发送的数据
     * @return 是否发送成功
     */
    public boolean sendToWifi(final byte[] datas) {
        try {
            DatagramSocket socket = new DatagramSocket(sendPort);
            socket.setBroadcast(true);
            socket.setSendBufferSize(datas.length);
            DatagramPacket packet = new DatagramPacket(datas, datas.length, inetAddress, sendPort);

            socket.send(packet);
            socket.close();
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
