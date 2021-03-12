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

import com.google.android.material.snackbar.Snackbar;
import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

import org.json.JSONException;
import org.json.JSONObject;

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
            int code= getJson.getStatusCode(val);
            User user=toJson.convertToJson(User.class,val);
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), code==0?"登录成功":"登录失败", Snackbar.LENGTH_LONG)
                    .setAction("确定", v -> {
                        //
                    });
            snackbar.show();
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
        String url=this.getString(R.string.host)+"/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
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