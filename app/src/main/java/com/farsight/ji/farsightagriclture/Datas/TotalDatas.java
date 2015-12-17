package com.farsight.ji.farsightagriclture.Datas;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 所有要操作的数据
 * Created by jiyan on 2015/11/28.
 */
public class TotalDatas {

    public static byte netType = NodeInfo.NET_TYPE_ZIGBEE;//1201

    public static boolean isUDP = false;
    public static final String ISUDP = "UDPMODE";
    public static String userId = "admin";
    public static final int ERROR = -9999;


    public static ArrayBlockingQueue<byte[]> qData = new ArrayBlockingQueue<byte[]>(32);
    public static ArrayBlockingQueue<byte[]> qSend = new ArrayBlockingQueue<byte[]>(32);

    public static String hostsIp = "192.168.0.200";
    public static final String OUTIP = "http://iot.farsightdev.com/SAWebService.asmx";//地址
    public static final String NAMESPACE = "http://tempuri.org/";//命名空间
    public static final String DEVICE = "000106";//设备号

//    public final static Integer sendPort = 8080;
//    public final static Integer receivePort = 5000;
    public final static Integer sendPort = 20001;
    public final static Integer receivePort = 20000;
}
