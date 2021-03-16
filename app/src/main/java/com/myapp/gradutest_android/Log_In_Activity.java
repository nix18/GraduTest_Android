package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

//程序入口
public class Log_In_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_log__in);

        //检查token是否有效
        SharedPreferences sp=getSharedPreferences("loginToken",Context.MODE_PRIVATE);
        String url=this.getString(R.string.host)+"/chkToken?uid="+sp.getInt("uid",0)+"&token="+sp.getString("user_token","");
        networkTask networkTask=new networkTask();
        new Thread(networkTask.setParam(chkTokenHandler,url)).start();

        //装入登录Fragment
        FragmentTransaction transaction;
        Fragment_Log_In fragment_logIn =new Fragment_Log_In();
        transaction=getSupportFragmentManager().beginTransaction();//无须使用FragmentManager
        transaction.add(R.id.login_container, fragment_logIn);
        transaction.commit();
    }

    @SuppressLint("HandlerLeak")
    /*
      登录消息处理
     */
    Handler loginHandler=new Handler() {
        @SuppressLint("ApplySharedPref")
        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                super.handleMessage(msg);
                Log.i("myLog","loginHandler执行");
                Bundle data = msg.getData();
                String val = data.getString("value");
                int code= getJson.getStatusCode(val);
                User user= toJson.convertToJson(User.class,val);

                //使用SharedPreferences保存Token
                SharedPreferences sp=getSharedPreferences("loginToken", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("uid",user.getUid());
                editor.putString("user_name",user.getUser_name());
                editor.putString("user_profile",user.getUser_profile());
                editor.putString("user_token",user.getUser_token());
                editor.commit();
                Log.i("myLog",sp.getAll().toString());

                //弹出提示
                Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), code==0?"登录成功":"登录失败", Snackbar.LENGTH_LONG)
                        .setAction("确定", v -> {
                            //
                        });
                snackbar.show();
                TextView output = findViewById(R.id.output);
                output.setText(user.toString());
                Intent intent=new Intent(Log_In_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }catch (Exception e){
                Intent intent=new Intent(Log_In_Activity.this,Screen_Error_Activity.class);
                startActivity(intent);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler chkTokenHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","chkTokenHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){
                Intent intent=new Intent(Log_In_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    /*
    登录点击动作
     */
    public void log_in_onclick(View view){
        EditText user_name=findViewById(R.id.user_name_input_fm_log_in);
        EditText user_pwd=findViewById(R.id.user_pwd_input_fm_log_in);
        RadioButton rule_checked_my=findViewById(R.id.rule_checked_fm_log_in);
        if(rule_checked_my.isChecked()){
            String url=this.getString(R.string.host)+"/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
            networkTask networkTask=new networkTask();
            new Thread(networkTask.setParam(loginHandler,url)).start();
        }else {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), "请先阅读并同意用户守则", Snackbar.LENGTH_LONG)
                    .setAction("确定", v -> {
                        //
                    });
            snackbar.show();
        }
    }

    public void sign_in_onclick(View view){
        Intent intent=new Intent(this,Sign_In_Activity.class);
        startActivity(intent);
    }
}