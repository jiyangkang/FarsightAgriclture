package com.farsight.ji.farsightagriclture.Tools;

import android.util.Log;

/**
 * 字符串与16进制工具
 * Created by jiyan on 2015/11/28.
 */
public class StringTool {
    /**
     * 打印16进制数组
     * @param b 16进制数组
     * @return 字符串
     */
    public static String printHex(byte[] b) {
        String hex = null;
        String result = "";
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            if (i != b.length -1) {
                result += (hex + " ");
            } else{
                result += hex;
            }
        }
        return result;
    }

    /**
     * 将16进制数组转换为字符串
     * @param b 16进制数组
     * @return 字符串
     */
    public static String hexToString(byte[] b){
        String str = null;
        StringBuilder strBuilder = new StringBuilder();
        for (byte aB : b) {
            String st = Integer.toHexString(aB & 0xff);
            if (st.length() == 1) {
                strBuilder.append("0");
                strBuilder.append(st);
            } else {
                strBuilder.append(st);
            }
        }
        str = strBuilder.toString();
        return str;
    }

    /**
     * 将16进制字符串带空格转换为数组
     * @param str 字符串
     * @return 16进制数组
     */
    public static byte[] getHex(String str) {
        byte[] strH = str.getBytes();
        byte[] result = new byte[(strH.length + 1) / 3];
        Log.d("SSS", result.length + "");
        for (int i = 0; i < strH.length; i++) {
            if (strH[i] > 64) {
                strH[i] -= 55;
            } else {
                strH[i] -= 48;
            }
        }

        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (strH[i*3]*16 + strH[i*3+1]);
        }
        return result;
    }

    public static byte[][] getHexSer(String str){
        byte[][] datas;
        byte[] strH = str.getBytes();
        int l = strH.length;
        int n;
        if(l%24 == 0){
            n = l/24;
            for (int i = 0; i < strH.length; i++) {
                if (strH[i] > 96){
                    strH[i] -= 97;
                }else if (strH[i] > 64) {
                    strH[i] -= 55;
                } else {
                    strH[i] -= 48;
                }
            }
        }else{
            return null;
        }
        datas = new byte[n][12];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < 12 ; j++){
                datas[i][j] = (byte) (strH[i*24 + j*2]*16 + strH[i*24 + j*2 +1]);
            }
        }

        return datas;
    }
}
