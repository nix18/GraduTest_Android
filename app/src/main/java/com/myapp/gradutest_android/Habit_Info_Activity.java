package com.myapp.gradutest_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.habit.habitUtils;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class Habit_Info_Activity extends AppCompatActivity {

    private TextView habit_info_text;
    private ImageView back_btn;
    private ImageView more_info_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity__habit__info);
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Habit_Info_Activity.this,"更多信息","这里是习惯的详细信息").show();
            }
        });
        MMKV habit_mmkv=MMKV.mmkvWithID("habits");
        Intent intent = getIntent();
        if (intent != null) {
            try {
                int hid = intent.getIntExtra("hid",0);
                String json=habit_mmkv.decodeString("habits");
                ArrayList<GoodHabit> habits= toJson.jsonToObjs(GoodHabit.class,json);
                habit_info_text.setText(habitUtils.selHabitByHid(habits,hid).toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public void initView(){
        habit_info_text=findViewById(R.id.habitinfo_habit_info);
        back_btn = findViewById(R.id.img_back_habit_info);
        more_info_btn = findViewById(R.id.img_more_info_habit_info);
    }
}