package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.domain.RunningHabit;
import com.myapp.gradutest_android.utils.habit.habitUtils;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Habit_Info_Activity extends AppCompatActivity {

    private TextView habit_info_text;
    private TextView habit_name_text;
    private ImageView back_btn;
    private ImageView more_info_btn;
    private ButtonView start_time_btn;
    private ButtonView end_time_btn;
    private ButtonView remind_time_btn;
    private WeekSelectView week_sel_btn;
    private ButtonView credit_in_btn;
    private GoodHabit thisHabit;
    private RunningHabit runningHabit;
    private JSONObject user_config = new JSONObject();
    private String checked_days = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_habit__info);
        initView();
        initData();
        runningHabit = new RunningHabit();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String nowTime = new SimpleDateFormat("a hh:mm").format(Calendar.getInstance().getTime());

        //初始化页面显示
        habit_name_text.setText(thisHabit.getHabit_name());
        start_time_btn.start_text.setText("开始日期");
        end_time_btn.start_text.setText("结束日期");
        remind_time_btn.start_text.setText("提醒时间");
        credit_in_btn.start_text.setText("投入积分");
        start_time_btn.end_text.setText(now);
        end_time_btn.end_text.setText(now);
        remind_time_btn.end_text.setText(nowTime);

        start_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(Habit_Info_Activity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        start_time_btn.end_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        runningHabit.setRunning_start_time(date);
                    }
                })
                        .setSubmitColor(getColor(R.color.orange))
                        .setCancelColor(getColor(R.color.orange))
                        .build();
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
            }
        });

        end_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(Habit_Info_Activity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        end_time_btn.end_text.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        if(runningHabit.getRunning_start_time() != null){
                            int target_days = chkDays(date,runningHabit.getRunning_start_time());
                            if(target_days > 0) runningHabit.setTarget_days(target_days);
                        }
                    }
                })
                        .setSubmitColor(getColor(R.color.orange))
                        .setCancelColor(getColor(R.color.orange))
                        .build();
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
            }
        });

        remind_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView pvTime = new TimePickerBuilder(Habit_Info_Activity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        remind_time_btn.end_text.setText(new SimpleDateFormat("a hh:mm").format(date));
                        try {
                            user_config.put("remind_time",new SimpleDateFormat("HH:mm").format(date));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                        .setType(new boolean[]{false, false, false, true, true, false})
                        .setSubmitColor(getColor(R.color.orange))
                        .setCancelColor(getColor(R.color.orange))
                        .build();
                pvTime.setDate(Calendar.getInstance());
                pvTime.show();
            }
        });

        //初始化投入积分
        //生成积分梯度
        ArrayList<Integer> credit_sel = new ArrayList<>();
        int i = 1;
        while (i < 50){
            credit_sel.add(i * 10);
            i++;
        }
        i = 5;
        while (i < 100){
            credit_sel.add(i * 100);
            i++;
        }

        credit_in_btn.end_text.setText("预计返还 "+"75"+" 积分");
        final int[] savedOption = {4};
        credit_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionsPickerView pvNoLinkOptions = new OptionsPickerBuilder(Habit_Info_Activity.this, new OnOptionsSelectListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        double multiple = 1;
                        if(credit_sel.get(options1)<100){
                            multiple = 1.5;
                        }else if(credit_sel.get(options1)<300){
                            multiple = 2;
                        }else if(credit_sel.get(options1)<600){
                            multiple = 2.5;
                        }else {
                            multiple = 3;
                        }
                        credit_in_btn.end_text.setText("预计返还 "+((int)(credit_sel.get(options1)*multiple))+" 积分");
                        runningHabit.setCapital(credit_sel.get(options1));
                        savedOption[0] = options1;
                    }
                })
                        .setSubmitColor(getColor(R.color.orange))
                        .setCancelColor(getColor(R.color.orange))
                        .setItemVisibleCount(6)
                        .build();
                pvNoLinkOptions.setPicker(credit_sel);
                pvNoLinkOptions.setSelectOptions(savedOption[0]);
                pvNoLinkOptions.show();
            }
        });
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> checked_ids = week_sel_btn.week_group.getCheckedChipIds();
                checked_days = habitUtils.getWeekStr(checked_ids);
                miniToast.getDialog(Habit_Info_Activity.this, "更多信息", "这里是习惯的详细信息\n" + checked_days).show();
            }
        });
    }

    public void initView(){
        habit_info_text = findViewById(R.id.habitinfo_habit_info);
        habit_name_text = findViewById(R.id.habit_name_habit_info);
        back_btn = findViewById(R.id.img_back_habit_info);
        more_info_btn = findViewById(R.id.img_more_info_habit_info);
        start_time_btn = findViewById(R.id.start_time_habit_info);
        end_time_btn = findViewById(R.id.end_time_habit_info);
        remind_time_btn = findViewById(R.id.remind_time_habit_info);
        week_sel_btn = findViewById(R.id.week_sel_btn_habit_info);
        credit_in_btn = findViewById(R.id.credit_in_btn_habit_info);
    }

    public void initData(){
        MMKV habit_mmkv=MMKV.mmkvWithID("habits");
        Intent intent = getIntent();
        if (intent != null) {
            try {
                int hid = intent.getIntExtra("hid",0);
                String json=habit_mmkv.decodeString("habits");
                ArrayList<GoodHabit> habits= toJson.jsonToObjs(GoodHabit.class,json);
                thisHabit = habitUtils.selHabitByHid(habits,hid);
                habit_info_text.setText(thisHabit.toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public int chkDays(Date date1, Date date2){
        int target_days =(int) (date1.getTime() - date2.getTime());
        if(target_days == 0){
            miniToast.Toast(getApplicationContext(),"相差时间过短，无法添加");
        }else if(target_days < 0) {
            return Math.abs(target_days);
        }
        return target_days;
    }
}