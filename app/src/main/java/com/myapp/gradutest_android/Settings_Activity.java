package com.myapp.gradutest_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

public class Settings_Activity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView more_info_btn;
    private Button clear_cache_btn;
    private Button check_for_update_btn;
    private Button about_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_settings);
        initView();
        back_btn.setOnClickListener(v -> finish());
        check_for_update_btn.setOnClickListener(v -> miniToast.Toast(this,"当前已经是最新版本了"));
        about_btn.setOnClickListener(v -> miniToast.Toast(this,"V 1.0.0"));
    }

    protected void initView(){
        back_btn = findViewById(R.id.img_back_settings);
        more_info_btn = findViewById(R.id.img_more_info_settings);
        clear_cache_btn = findViewById(R.id.clear_cache_btn_settings);
        check_for_update_btn = findViewById(R.id.check_for_update_btn_settings);
        about_btn = findViewById(R.id.about_btn_settings);
    }
}