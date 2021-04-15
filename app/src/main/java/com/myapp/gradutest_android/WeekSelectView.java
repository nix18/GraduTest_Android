package com.myapp.gradutest_android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.chip.ChipGroup;

public class WeekSelectView extends LinearLayout {

    public TextView start_text;
    public ChipGroup week_group;

    public WeekSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.week_select_view, this);
        start_text = findViewById(R.id.start_text_week_select_view);
        week_group = findViewById(R.id.week_sel_group_week_select_view);
    }

    public WeekSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
