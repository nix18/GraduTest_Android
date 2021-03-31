package com.myapp.gradutest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class Habit_Info_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__habit__info);
        TextView habit_info_text=findViewById(R.id.habitinfo_habit_info);
        MMKV habit_mmkv=MMKV.mmkvWithID("habits");
        Intent intent = getIntent();
        if (intent != null) {
            try {
                int position = intent.getIntExtra("position",0);
                String json=habit_mmkv.decodeString("habits");
                ArrayList<GoodHabit> habits= toJson.jsonToObjs(GoodHabit.class,json);
                habit_info_text.setText(habits.get(position).toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}