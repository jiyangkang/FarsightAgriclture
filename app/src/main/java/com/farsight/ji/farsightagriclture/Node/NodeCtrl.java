package com.farsight.ji.farsightagriclture.Node;

/**
 *
 * Created by jiyan on 2015/11/28.
 */
public class NodeCtrl {
    private byte[] datas;
    private byte type;
    private byte addrH;
    private byte addrL;
    private byte state;
    public int timeOut = -1;

    public NodeCtrl(byte[] datas){
        this.datas = datas;
        type = datas[4];
        addrH = datas[3];
        addrL = datas[2];
        state = datas[6];
        timeOut = 20;
    }

    public void setAllValue(byte[] datas){
        this.datas = datas;
        state = datas[6];
        timeOut = 20;
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

}
