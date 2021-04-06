package com.myapp.gradutest_android.utils;


import android.os.Handler;
import android.widget.TextView;

public interface MyRunnable extends Runnable {
    MyRunnable setParam(Handler handler, String url, Integer method);
}
