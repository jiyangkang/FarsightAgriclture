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
    private byte[] value = new byte[3];

    public NodeCtrl(byte[] datas){
        this.datas = datas;
        type = datas[4];
        addrH = datas[2];
        addrL = datas[3];
        state = datas[7];
        for (int i = 0; i <value.length; i++){
            value[i] = datas[5+i];
        }
    }

    public void setAllValue(byte[] datas){
        this.datas = datas;
        state = datas[7];
        for (int i = 0; i <value.length; i++){
            value[i] = datas[5+i];
        }
    }
}
