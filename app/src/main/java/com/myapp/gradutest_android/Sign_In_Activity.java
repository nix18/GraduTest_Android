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
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

public class Sign_In_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String url=this.getString(R.string.host)+"/register?uname="+user_name.getText()+"&uprofile="+user_profile.getText()+"&upwd="+user_pwd.getText();
        networkTask networkTask=new networkTask();
        new Thread(networkTask.setParam(handler,url)).start();
    }

    public void back_onclick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}