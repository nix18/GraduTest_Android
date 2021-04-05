package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class My_Info_Activity extends AppCompatActivity {

    private TextView user_name;
    private TextView user_profile;
    private CardView update_info_btn;
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

        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My_Info_Activity.this,Update_Info_Activity.class);
                startActivity(intent);
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
        update_info_btn=findViewById(R.id.update_info_btn_my_info);
        back_btn = findViewById(R.id.img_back_my_info);
    }

}