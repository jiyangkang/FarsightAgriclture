package com.farsight.ji.farsightagriclture.Node;

/**
 * Created by jiyan on 2015/11/28.
 */
public class NodeCtrl {
    private byte[] datas;
    private byte type;
    private byte addrH;
    private byte addrL;
    private byte state;
    private byte power;
    public int timeOut = -1;

    public NodeCtrl(byte[] datas){
        this.datas = datas;
        type = datas[4];
        addrH = datas[3];
        addrL = datas[2];
        state = datas[7];
        power = datas[10];
        timeOut = 10;
    }

    public void setAllValue(byte[] datas){
        this.datas = datas;
        state = datas[7];
        power = datas[10];
        timeOut = 10;
    }

    public byte[] getDatas(){
        return datas;
    }

    public byte getState(){
        return state;
    }

    public byte getType(){
        return type;
    }

    public byte getAddrH(){
        return addrH;
    }

    public byte getAddrL(){
        return addrL;
    }

    public byte getPower(){
        return power;
    }
}
