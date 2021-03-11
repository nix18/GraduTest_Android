package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("HandlerLeak")
    /*
      消息处理
     */
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","Handler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            User user=toJson.convertToJson(User.class,val);
            TextView output = findViewById(R.id.output);
            output.setText(user.toString());
        }
    };

    /*
    登录点击动作
     */
    public void log_in_onclick(View view){
        EditText user_name=findViewById(R.id.user_name_input);
        EditText user_pwd=findViewById(R.id.user_pwd_input);
        String url="HTTP://10.0.2.2:8000/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
        networkTask networkTask=new networkTask();
        new Thread(networkTask.setParam(handler,url)).start();
    }

    public void sign_in_onclick(View view){
        Intent intent=new Intent(MainActivity.this,Sign_In_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}