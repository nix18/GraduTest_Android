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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.gradutest_android.asyncTask.userCreditAsync;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.offLineMode;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class My_Info_Activity extends AppCompatActivity {

    private TextView user_name;
    private TextView user_profile;
    private Button log_out_btn;
    private Button update_info_btn;
    private Button clock_in_btn;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_my__info);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initView();
        MMKV mmkv=MMKV.defaultMMKV();
        String uName=mmkv.decodeString("user_name","defaultName");
        String uProfile=mmkv.decodeString("user_profile","");
        user_name.setText(uName);
        user_profile.setText(uProfile);

        //在fragment里设定点击动作须用setOnClickListener
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.host)+"/logout?uid="+mmkv.decodeInt("uid",0)+
                        "&token="+mmkv.decodeString("user_token","");
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(logOutHandler,url)).start();
            }
        });
        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_Info_Activity.this,Update_Info_Activity.class);
                startActivity(intent);
            }
        });
        clock_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.host)+"/qiandao?uid="+mmkv.decodeInt("uid",0)+
                        "&token="+mmkv.decodeString("user_token","");
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(clockInHandler,url)).start();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new userCreditAsync(My_Info_Activity.this).execute();

        //更新用户信息
        MMKV mmkv=MMKV.defaultMMKV();
        String uName=mmkv.decodeString("user_name","defaultName");
        String uProfile=mmkv.decodeString("user_profile","");
        user_name.setText(uName);
        user_profile.setText(uProfile);
    }

    protected void initView(){
        user_name=findViewById(R.id.user_name_container_my_info);
        user_profile=findViewById(R.id.user_profile_container_my_info);
        log_out_btn=findViewById(R.id.log_out_btn_my_info);
        update_info_btn=findViewById(R.id.update_info_btn_my_info);
        clock_in_btn=findViewById(R.id.clock_in_btn_my_info);
        back_btn = findViewById(R.id.img_my_info);
    }

    @SuppressLint("HandlerLeak")
    Handler logOutHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","logOutHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){

                //清除MMKV
                MMKV mmkv=MMKV.defaultMMKV();
                mmkv.clearAll();

                //返回登录界面
                Toast toast=Toast.makeText(My_Info_Activity.this, "登出成功", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(My_Info_Activity.this,Log_In_Activity.class);
                startActivity(intent);
                My_Info_Activity.this.finish();
            }else {
                Toast toast=Toast.makeText(My_Info_Activity.this.getApplicationContext(), "登出失败", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler clockInHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","clockInHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            MyMessage myMessage = toJson.jsonToObj(MyMessage.class,val);
            Toast.makeText(My_Info_Activity.this,myMessage.getMsg(),Toast.LENGTH_SHORT).show();
            new userCreditAsync(My_Info_Activity.this).execute();
        }
    };
}