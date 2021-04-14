package com.myapp.gradutest_android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ButtonView extends LinearLayout{

    public TextView start_text;
    public TextView end_text;
    public ButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.button_view, this);
        start_text = findViewById(R.id.start_text_button_view);
        end_text = findViewById(R.id.end_text_button_view);
    }

    public ButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}