package com.farsight.ji.farsightagriclture.Datas;

/**
 * 节点类型
 * Created by jiyan on 2015/11/28.
 */
public class NodeInfo {

    public static final byte[] OPEN = {0x23, 0x43, 0x00, 0x00, 0x00, 0x00, 0x31};//开
    public static final byte[] CLOSE = {0x23, 0x43, 0x00, 0x00, 0x00, 0x00, 0x30};//关
    public static final String NET_TYPE = "网络状态";
    public static final byte[] CHANGE_ZIGBEE = {0x23, 0x63, 0x5a, 0x00, 0x00, 0x00, 0x00};//网络切换
    public static final byte[] CHANGE_485 = {0x23, 0x63, 0x52, 0x00, 0x00, 0x00, 0x00};//网络切换

    public static final byte[] CHANGE_CONTRL_MODE_AUTO = {0x23, 0x62, 0x31, 0x00, 0x00, 0x00, 0x00};//智能模式
    public static final byte[] CHANGE_CONTRL_MODE_MENU = {0x23, 0x62, 0x30, 0x00, 0x00, 0x00, 0x00};//手动模式


    public static final int CMD_T_MAX = 3;
    public static final int CMD_T_MIN = 4;
    public static final int CMD_H_MAX = 5;
    public static final int CMD_H_MIN = 6;
    public static final int CMD_L_MAX_H = 7;
    public static final int CMD_L_MAX_L = 8;
    public static final int CMD_L_MIN_H = 9;
    public static final int CMD_L_MIN_L = 10;
    public static final int CMD_C_MAX_H = 11;
    public static final int CMD_C_MAX_L = 12;
    public static final int CMD_C_MIN_H = 13;
    public static final int CMD_C_MIN_L = 14;

    public static final int T_MAX = 120;
    public static final int T_MIN = -40;
    public static final int L_MAX = 10000;
    public static final int L_MIN = 0;
    public static final int C_MAX = 5000;
    public static final int C_MIN = 0;
    public static final int H_MAX = 100;
    public static final int H_MIN = 0;


    public static final byte NET_TYPE_ZIGBEE = 0x5a;
    public static final byte NET_TYPE_485 = 0x52;
    public static final byte CMD_HEAD = 0x23;
    public static final byte CMD_SEND_SETTING = 0x53;
    public static final byte CMD_TEL_NUM = 0x65;//设置电话号码



    public static final byte TYPE_TEMP  = 0x74;//温湿度
    public static final String ENVTEMPFIRST = "环境温湿度一";
    public static final String ENVTEMPFIRST_D = "环境温湿度一断开连接";
    public static final byte ADDR_FIRST_TEMP = 0x01;
    public static final String ENVTEMPSECOND = "环境温湿度二";
    public static final String ENVTEMPSECOND_D = "环境温湿度二断开连接";
    public static final byte ADDR_SECOND_TEMP = 0x02;
    public static final String SOILTEMPFIRST = "土壤温湿度一";
    public static final String SOILTEMPFIRST_D = "土壤温湿度一断开连接";
    public static final byte ADDR_FIRST_SOIL = 0x03;
    public static final String SOILTEMPSECOND = "土壤温湿度二";
    public static final String SOILTEMPSECOND_D = "土壤温湿度二断开连接";
    public static final byte ADDR_SECOND_SOIL = 0x04;
    public static final byte TYPE_LIGHT = 0x6c;//光感l=108=0x6c---2015
    public static final String LIGHTFIRST = "光感传感器一";
    public static final String LIGHTFIRST_D = "光感传感器一断开连接";
    public static final byte ADDR_FIRST_LIGHT = 0x05;
    public static final String LIGHTSECOND = "光感传感器二";
    public static final String LIGHTSECOND_D = "光感传感器二断开连接";
    public static final byte ADDR_SECOND_LIGHT = 0x06;
    public static final byte TYPE_CARBON_DIOXID = 0x63;//二氧化碳检测
    public static final String CARBONDIOXID = "二氧化碳浓度";
    public static final String CARBONDIOXID_D = "二氧化碳浓度断开连接";
    public static final byte ADDR_CARBON_DIOXID = 0x07;
    public static final byte TYPE_INFRA_RED = 0x69;//红外
    public static final String INFRARED = "红外感应器";
    public static final String INFRARED_D = "红外感应器断开连接";
    public static final byte ADDR_INFRARED = 0x08;


    public static final byte TYPE_WARM = 0x57;//加热器
    public static final String WARM = "加热器";
    public static final String WARM_D = "加热器断开连接";
    public static final byte ADDR_WARM = 0x09;
    public static final byte[] OPEN_WARM = {0x23, 0x43, 0x00, 0x57, 0x09, 0x00, 0x31};//开
    public static final byte[] CLOSE_WARM = {0x23, 0x43, 0x00, 0x57, 0x09, 0x00, 0x30};//关

    public static final byte TYPE_HUMIFY = 0x48;//加湿器
    public static final String HUMIFY = "加湿器";
    public static final String HUMIFY_D = "加湿器断开连接";
    public static final byte ADDR_HUMIFY = 0x0a;
    public static final byte[] OPEN_HUMIFY = {0x23, 0x43, 0x00, 0x48, 0x0a, 0x00, 0x31};//开
    public static final byte[] CLOSE_HUMIFY = {0x23, 0x43, 0x00, 0x48, 0x0a, 0x00, 0x30};//关

    public static final byte TYPE_FAN = 0x46;//风扇
    public static final String FAN = "风扇";
    public static final String FAN_D = "风扇断开连接";
    public static final byte ADDR_FAN = 0x0b;
    public static final byte[] OPEN_FAN = {0x23, 0x43, 0x00, 0x46, 0x0b, 0x00, 0x31};//开
    public static final byte[] CLOSE_FAN = {0x23, 0x43, 0x00, 0x46, 0x0b, 0x00, 0x30};//关

    public static final byte TYPE_LAMP = 0x4c;//照明
    public static final String LAMP = "照明";
    public static final String LAMP_D = "照明断开连接";
    public static final byte ADDR_LAMP = 0x0c;
    public static final byte[] OPEN_LAMP = {0x23, 0x43, 0x00, 0x4c, 0x0c, 0x00, 0x31};//开
    public static final byte[] CLOSE_LAMP = {0x23, 0x43, 0x00, 0x4c, 0x0c, 0x00, 0x30};//关

    public static final byte TYPE_DRENCHING = 0x44;//喷淋
    public static final String DRENCHING = "喷淋";
    public static final String DRENCHING_D = "喷淋断开连接";
    public static final byte ADDR_DRENCHING = 0x0d;
    public static final byte[] OPEN_DRENCHING = {0x23, 0x43, 0x00, 0x44, 0x0d, 0x00, 0x31};//开
    public static final byte[] CLOSE_DRENCHING = {0x23, 0x43, 0x00, 0x44, 0x0d, 0x00, 0x30};//关

    public static final byte TYPE_SHADE = 0x53;//遮阳棚
    public static final String SHADE = "遮阳棚";
    public static final String SHADE_D = "遮阳棚断开连接";
    public static final byte ADDR_SHADE = 0x0e;
    public static final byte[] OPEN_SHADE = {0x23, 0x43, 0x00, 0x53, 0x0e, 0x00, 0x31};//开
    public static final byte[] CLOSE_SHADE = {0x23, 0x43, 0x00, 0x53, 0x0e, 0x00, 0x30};//关

    public static final byte TYPE_ALARM = 0x41;//报警器
    public static final String ALARM = "报警器";
    public static final String ALARM_D = "报警器断开连接";
    public static final byte ADDR_ALARM = 0x0f;
    public static final byte[] OPEN_ALARM = {0x23, 0x43, 0x00, 0x41, 0x0f, 0x00, 0x31};//开
    public static final byte[] CLOSE_ALARM = {0x23, 0x43, 0x00, 0x41, 0x0f, 0x00, 0x30};//关

    public static final byte TYPE_CAMERA = 0x02;

}
