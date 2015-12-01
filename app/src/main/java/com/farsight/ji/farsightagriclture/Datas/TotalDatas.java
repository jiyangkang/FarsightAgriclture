package com.farsight.ji.farsightagriclture.Datas;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 所有要操作的数据
 * Created by jiyan on 2015/11/28.
 */
public class TotalDatas {

    public static byte netType = NodeInfo.NET_TYPE_ZIGBEE;//1201

    public static ArrayBlockingQueue<byte[]> qData = new ArrayBlockingQueue<byte[]>(32);
    public static ArrayBlockingQueue<byte[]> qSend = new ArrayBlockingQueue<byte[]>(32);

    public final static String hostsIp = "192.168.0.15";
    public final static Integer sendPort = 8080;
    public final static Integer receivePort = 5000;
}
