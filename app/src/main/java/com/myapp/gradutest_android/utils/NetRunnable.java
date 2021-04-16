package com.myapp.gradutest_android.utils;


import android.os.Handler;

public interface NetRunnable extends Runnable {
    NetRunnable setParam(Handler handler, String url, Integer method);
}
