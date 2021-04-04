package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

//程序入口
public class Log_In_Activity extends AppCompatActivity {

    private TextView sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_log__in);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);

        //初始化MMKV共享存储
        MMKV.initialize(this);

        //检查token是否有效
        MMKV mmkv=MMKV.defaultMMKV();
        String url=this.getString(R.string.host)+"/chkToken?uid="+mmkv.decodeInt("uid",0)+"&token="+mmkv.decodeString("user_token","");
        networkTask networkTask=new networkTask();
        new Thread(networkTask.setParam(chkTokenHandler,url)).start();

        //装入登录Fragment
        FragmentTransaction transaction;
        Fragment_Log_In fragment_logIn =new Fragment_Log_In();
        transaction=getSupportFragmentManager().beginTransaction();//无须使用FragmentManager
        transaction.add(R.id.login_container, fragment_logIn);
        transaction.commit();
        initView();
    }

    protected void initView(){
        sign_in = findViewById(R.id.sign_in_btn_log_in);
        sign_in.setOnClickListener(v -> {
            Intent intent=new Intent(Log_In_Activity.this,Sign_In_Activity.class);
            startActivity(intent);
        });
    }

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
}