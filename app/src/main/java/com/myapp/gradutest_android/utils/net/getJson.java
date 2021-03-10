package com.myapp.gradutest_android.utils.net;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class getJson {
    /**
     * 传入url字符串 返回Message型数据 发送至Handler处理
     * @param url 网址
     * @param method 请求方法 0：GET，1:POST
     * @return 返回Message型数据
     */
    public Message get(String url, int method){
        Message msg = null;
        try {
            URL myUrl=new URL(url);
            HttpURLConnection urlConnection=(HttpURLConnection) myUrl.openConnection();
            urlConnection.setRequestMethod(method==0?"GET":"POST");
            urlConnection.setRequestProperty("Connection","close");
            InputStream inputStream=urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(100);
            int current=0;
            while ((current=bufferedInputStream.read())!=-1){
                byteArrayOutputStream.write(current);
            }
            String myJson=byteArrayOutputStream.toString();
            msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", myJson);
            msg.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
}
