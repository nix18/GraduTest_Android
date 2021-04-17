package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.domain.RunningHabit;
import com.myapp.gradutest_android.domain.UserConfig;
import com.myapp.gradutest_android.utils.habit.habitReminderUtils;
import com.myapp.gradutest_android.utils.habit.habitUtils;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Habit_Info_Activity extends AppCompatActivity {

    private Activity thisActivity;
    private TextView habit_name_text;
    private ImageView back_btn;
    private TextView buy_habit_btn;
    private ButtonView start_time_btn;
    private ButtonView end_time_btn;
    private ButtonView remind_time_btn;
    private WeekSelectView week_sel_btn;
    private ButtonView credit_in_btn;
    private GoodHabit thisHabit;
    private RunningHabit runningHabit;
    private Date start_day;
    private Date end_day;
    private Date remind_time;
    private Date start_day_temp;
    private String checked_days = "";
    private UserConfig userConfig = new UserConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_habit__info);
        initView();
        initData();
        thisActivity = this;
        runningHabit = new RunningHabit();
        start_day = Calendar.getInstance().getTime();
        end_day = start_day;
        start_day_temp = start_day;
        remind_time = start_day;

        //初始化RunningHabit
        initRunningHabit();

        String now = new SimpleDateFormat("yyyy-MM-dd").format(start_day);
        String nowTime = new SimpleDateFormat("a hh:mm").format(start_day);

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
                        start_day = date;
                        userConfig.setStart_time(date);
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
                        end_day = date;
                        userConfig.setEnd_time(date);
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
                        remind_time = date;
                        userConfig.setRemind_time(date);
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

        //初始化投入积分选项
        //生成积分梯度用于提醒
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

        buy_habit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV mmkv = MMKV.defaultMMKV();
                List<Integer> checked_ids = week_sel_btn.week_group.getCheckedChipIds();
                checked_days = habitUtils.getWeekStr(checked_ids);
                userConfig.setRemind_days_str(checked_days);
                runningHabit.setTarget_days(habitUtils.getTargetDays(getApplicationContext(),start_day,end_day,checked_ids));
                Log.i("myLog","进入好习惯养成计划完整性");
                if(chkRunningHabit()){

                    //添加日程 获取eventId
                    long eventId = habitUtils.setHabitReminder(thisActivity, start_day, end_day, remind_time, thisHabit.getHabit_name(),
                            thisHabit.getHabit_content(), "好习惯养成系统", checked_days);
                    habitReminderUtils.deleteEventById(thisActivity,eventId);
                    if(eventId != 0){
                        userConfig.setEventId(eventId);
                        String config = toJson.objToJsonStr(userConfig);
                        runningHabit.setUser_config(config);
                        miniToast.Toast(thisActivity,"日程添加成功");

                        //提交好习惯养成计划
                        Uri.Builder builder = new Uri.Builder();
                        builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                                .appendPath("buyhabit")
                                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                .appendQueryParameter("hid", runningHabit.getHid().toString())
                                .appendQueryParameter("user_config", toJson.objToJsonStr(runningHabit.getUser_config()))
                                .appendQueryParameter("target_days", runningHabit.getTarget_days().toString())
                                .appendQueryParameter("capital", runningHabit.getCapital().toString());
                        String url = builder.build().toString();
                        networkTask networkTask = new networkTask();
                        new Thread(networkTask.setParam(buyHabitHandler,url,1)).start();
                    }else {
                        miniToast.Toast(thisActivity,"日程添加失败");
                    }
                }else {
                    miniToast.getDialog(thisActivity,"错误","习惯配置错误").show();
                }
            }
        });
    }

    public void initView(){
        habit_name_text = findViewById(R.id.habit_name_habit_info);
        back_btn = findViewById(R.id.img_back_habit_info);
        buy_habit_btn = findViewById(R.id.buy_habit_habit_info);
        start_time_btn = findViewById(R.id.start_time_habit_info);
        end_time_btn = findViewById(R.id.end_time_habit_info);
        remind_time_btn = findViewById(R.id.remind_time_habit_info);
        week_sel_btn = findViewById(R.id.week_sel_btn_habit_info);
        credit_in_btn = findViewById(R.id.credit_in_btn_habit_info);
        habit_name_text.setFocusable(false);
    }

    public void initData(){
        MMKV habit_mmkv=MMKV.mmkvWithID("habits");
        Intent intent = getIntent();
        String json = "";
        if (intent != null) {
            try {
                int hid = intent.getIntExtra("hid",0);
                int originActivity = intent.getIntExtra("origin",0);

                //判断来源以便复用
                switch (originActivity){
                    case 0:
                        json = habit_mmkv.decodeString("habits");
                        break;
                    case 1:
                        json = habit_mmkv.decodeString("habits_my");
                        break;
                }

                ArrayList<GoodHabit> habits= toJson.jsonToObjs(GoodHabit.class,json);
                switch (originActivity){
                    case 0:
                        //hid乱序无法二分法
                        thisHabit = habitUtils.selHabitByHidOrdered(habits,hid);
                        break;
                    case 1:
                    case 2:
                        thisHabit = habitUtils.selHabitByHid(habits,hid);
                        break;
                }
                Log.i("myTag",thisHabit.toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public void initRunningHabit(){
        try {
            runningHabit.setHid(thisHabit.getHid());
        }catch (NullPointerException e){
            e.printStackTrace();
            Intent intent = new Intent(thisActivity,MainActivity.class);
            startActivity(intent);
        }
        userConfig.setStart_time(start_day);
        userConfig.setEnd_time(end_day);
        userConfig.setRemind_time(start_day);
        userConfig.setRemind_days_str("MO,TU,WE,TH,FR,SA,SU");
        userConfig.setEventId((long) 0);
        String config = toJson.objToJsonStr(userConfig);
        runningHabit.setUser_config(config);
        runningHabit.setTarget_days(0);
        runningHabit.setCapital(50);

        //设置week_group全选
        week_sel_btn.week_group.check(R.id.mon_btn_week_sel);
        week_sel_btn.week_group.check(R.id.tues_btn_week_sel);
        week_sel_btn.week_group.check(R.id.wed_btn_week_sel);
        week_sel_btn.week_group.check(R.id.thur_btn_week_sel);
        week_sel_btn.week_group.check(R.id.fri_btn_week_sel);
        week_sel_btn.week_group.check(R.id.sat_btn_week_sel);
        week_sel_btn.week_group.check(R.id.sun_btn_week_sel);
    }

    //检查数据合法性
    public boolean chkRunningHabit(){
        if(runningHabit.getTarget_days() >= 1){
            if(runningHabit.getCapital() >= 10){
                if(start_day.getTime() < end_day.getTime()){
                    return start_day_temp.getTime() <= start_day.getTime();
                }
            }
        }
        return false;
    }

    @SuppressLint("HandlerLeak")
    Handler buyHabitHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog", "buyHabitHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code = getJson.getStatusCode(val);
            if (code == 0) {
                miniToast.Toast(thisActivity,"购买好习惯成功");
                finish();
            }else {
                miniToast.getDialog(thisActivity,"错误", "服务器错误").show();
            }
        }
    };
}