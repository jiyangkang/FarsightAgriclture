package com.farsight.ji.farsightagriclture.Node;

/**
 * Created by jiyan on 2015/11/28.
 */
public class NodeVoir extends NodeCtrl{

    private byte[] value = new byte[3];

    public NodeVoir(byte[] datas){
        super(datas);
        for (int i = 0; i <value.length; i++){
            value[i] = datas[5+i];
        }
    }

    public void setAllValue(byte[] datas){
        super.setAllValue(datas);
        for (int i = 0; i <value.length; i++){
            value[i] = datas[5+i];
        }
    }

    public byte[] getValue(){
        return value;
    }
}
