package com.myapp.gradutest_android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

public class Screen_Error_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_screen__error);
    }
}