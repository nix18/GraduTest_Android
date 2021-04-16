package com.myapp.gradutest_android.utils.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.myapp.gradutest_android.utils.NetRunnable;

/*
网络相关子线程
 */
public class networkTask implements NetRunnable {
    private String url;
    private Handler handler;
    private Integer method;

    @Override
    public NetRunnable setParam(Handler handler, String url, Integer method) {
        this.handler=handler;
        this.url=url;
        this.method= method;
        return this;
    }

    @Override
    public void run() {
        Log.i("myLog","网络子线程执行");
        getJson getJson=new getJson();
        try {
            Message msg=getJson.get(url,method);
            handler.sendMessage(msg);
        }catch (Exception e){
            //使用msg置空来触发上层Exception
            Message msg=new Message();
            handler.sendMessage(msg);
        }
    }
}
