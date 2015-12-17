package com.farsight.ji.farsightagriclture.Node;

/**
 * Created by jiyan on 2015/11/28.
 */
public class NodeVoir extends NodeCtrl{

    private short[] value = new short[3];

    public NodeVoir(byte[] datas){
        super(datas);
        for (int i = 0; i < value.length; i++){
            value[i] = (short)(datas[5+i] & 0x00ff) ;
        }
    }

    public void setAllValue(byte[] datas){
        super.setAllValue(datas);
        for (int i = 0; i <value.length; i++){
            value[i] = (short)(datas[5+i] & 0x00ff);
        }
    }

    public short[] getValue(){
        return value;
    }
}
