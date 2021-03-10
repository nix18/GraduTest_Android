package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.myapp.gradutest_android.utils.MyRunnable;
import com.myapp.gradutest_android.utils.net.getJson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","Handler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            TextView output = findViewById(R.id.output);
            output.setText(val);
        }
    };

    MyRunnable networkTask=new MyRunnable() {
        private String url;

        @Override
        public MyRunnable setParam(String... param) {
            this.url=param[0];
            return this;
        }

        @Override
        public void run() {
            Log.i("myLog","网络子线程执行");
            getJson getJson=new getJson();
            Message msg=getJson.get(url,1);
            handler.sendMessage(msg);
        }
    };

    public void log_in_onclick(View view){
        EditText user_name=findViewById(R.id.user_name_input);
        EditText user_pwd=findViewById(R.id.user_pwd_input);
        String url="HTTP://10.0.2.2:8000/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
        new Thread(networkTask.setParam(url)).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}