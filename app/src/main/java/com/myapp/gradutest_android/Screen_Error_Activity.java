package com.myapp.gradutest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

public class Screen_Error_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_screen__error);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);
    }
}