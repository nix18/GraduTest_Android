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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

public class Sign_In_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_sign__in);
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
            MyMessage message = toJson.convertToJson(MyMessage.class,val);
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message.getMsg(), Snackbar.LENGTH_LONG)
                    .setAction("确定", v -> {
                        //
                    });
            snackbar.show();
            TextView output = findViewById(R.id.output_sign_in);
            output.setText(message.toString());
        }
    };

    public void sign_in_onclick(View view){
        EditText user_name=findViewById(R.id.user_name_input_sign_in);
        EditText user_profile=findViewById(R.id.user_profile_input_sign_in);
        EditText user_pwd=findViewById(R.id.user_pwd_input_sign_in);
        RadioButton rule_checked=findViewById(R.id.rule_checked_sign_in);
        if(rule_checked.isChecked()){
            String url=this.getString(R.string.host)+"/register?uname="+user_name.getText()+"&uprofile="+user_profile.getText()+"&upwd="+user_pwd.getText();
            networkTask networkTask=new networkTask();
            new Thread(networkTask.setParam(handler,url)).start();
        }else {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "请先阅读并同意用户守则", Snackbar.LENGTH_LONG)
                    .setAction("确定", v -> {
                        //
                    });
            snackbar.show();
        }
    }

    public void back_onclick(View view){
        finish();
    }
}