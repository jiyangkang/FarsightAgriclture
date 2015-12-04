package com.farsight.ji.farsightagriclture.Tools;

import android.util.Log;

import com.farsight.ji.farsightagriclture.Datas.TotalDatas;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Soap 工具类
 * Created by jiyan on 2015/12/3.
 */
public class SoapTool {

    private SoapObject soapObject;
    private SoapPrimitive soapPrimitive;
    private SoapSerializationEnvelope envelope = null;
    private HttpTransportSE httpTransportSE = null;

    public SoapTool() {
        Log.d("SOAP", "SOAP---CREATE");
    }

    /**
     * 发送
     * @param data 发送到服务器的指令
     * @return 错误返回null，正常返回获取的值
     */
    public String CliPut(String data) {

        try {

            soapObject = new SoapObject(TotalDatas.NAMESPACE, "CliPut");
            soapObject.addProperty("data", data);

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;

            httpTransportSE = new HttpTransportSE(TotalDatas.OUTIP);

        } catch (Exception e){
            Log.d("SOAP" , "soap包装请求异常");
            e.printStackTrace();
            return null;
        }

        try {
            httpTransportSE.call(null, envelope);
        }catch (Exception e){
            Log.d("SOAP" , "soap服务调用异常");
            e.printStackTrace();
            return null;
        }

        try {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();

        }catch (Exception e){
            Log.d("SOAP" , "soap获取回复异常");
            e.printStackTrace();
            return null;
        }

        if (soapPrimitive != null){
            return soapPrimitive.toString();
        } else {
            return null;
        }

    }

    /**
     * 接收
     * @param data 发送到服务器的字段
     * @return 从服务器得到的字段或错误为null
     */
    public String CliGet(String data) {

        try {

            soapObject = new SoapObject(TotalDatas.NAMESPACE, "CliGet");
            soapObject.addProperty("data", data);

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;

            httpTransportSE = new HttpTransportSE(TotalDatas.OUTIP);

        } catch (Exception e){
            Log.d("SOAP" , "soap包装请求异常");
            e.printStackTrace();
            return null;
        }

        try {
            httpTransportSE.call(null, envelope);
        }catch (Exception e){
            Log.d("SOAP" , "soap服务调用异常");
            e.printStackTrace();
            return null;
        }

        try {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();

        }catch (Exception e){
            Log.d("SOAP" , "soap获取回复异常");
            e.printStackTrace();
            return null;
        }

        if (soapPrimitive != null){
            return soapPrimitive.toString();
        } else {
            return null;
        }

    }

    public String UserCheck(String name, String pswd){

        try {

            soapObject = new SoapObject(TotalDatas.NAMESPACE, "UserCheck");
            soapObject.addProperty("name", name);
            soapObject.addProperty("pwd", pswd);

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = soapObject;
            envelope.dotNet = true;

            httpTransportSE = new HttpTransportSE(TotalDatas.OUTIP);

        } catch (Exception e){
            Log.d("SOAP" , "soap包装请求异常");
            e.printStackTrace();
            return null;
        }

        try {
            httpTransportSE.call(null, envelope);
        }catch (Exception e){
            Log.d("SOAP" , "soap服务调用异常");
            e.printStackTrace();
            return null;
        }

        try {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();

        }catch (Exception e){
            Log.d("SOAP" , "soap获取回复异常");
            e.printStackTrace();
            return null;
        }

        if (soapPrimitive != null){
            return soapPrimitive.toString();
        } else {
            return null;
        }
    }
}
