package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.Share_Img_Activity;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.domain.HabitBundle;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.domain.RunningHabit;
import com.myapp.gradutest_android.domain.UserConfig;
import com.myapp.gradutest_android.utils.habit.habitReminderUtils;
import com.myapp.gradutest_android.utils.habit.habitUtils;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class runningHabitListAsync extends AsyncTask<String,Integer,String> {

    protected Activity myActivity;
    protected MyRecyclerViewAdapter mAdapter;
    protected String myUrl;
    protected ArrayList<HabitBundle> habitBundles = new ArrayList<>();
    protected ArrayList<RunningHabit> runningHabits;
    protected int i = 0; //拼接计数器

    public runningHabitListAsync(Activity myActivity, MyRecyclerViewAdapter mAdapter, String myUrl) {
        this.myActivity = myActivity;
        this.mAdapter = mAdapter;
        this.myUrl = myUrl;
    }

    @SuppressLint("HandlerLeak")
    Handler getDataHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","getDataHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code = -1;
            try {
                code = getJson.getStatusCode(val);
            }catch (Exception e){
                e.printStackTrace();
                miniToast.Toast(myActivity,"获取养成中习惯失败");
            }
            if(code == 0){
                try {
                    JSONObject jsonObject=new JSONObject(val);
                    String json=jsonObject.getString("result");
                    runningHabits = toJson.jsonToObjs(RunningHabit.class,json);
                    MMKV habit_mmkv = MMKV.mmkvWithID("habits");
                    habit_mmkv.encode("habits_my_running",json);
                }catch (Exception e){
                    e.printStackTrace();
                    miniToast.Toast(myActivity,"解析养成中习惯失败");
                }
            }else {
                miniToast.Toast(myActivity,"获取养成中习惯失败");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler concatHabitBundleHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","concatHabitBundleHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code = -1;
            try {
                code = getJson.getStatusCode(val);
            }catch (Exception e){
                e.printStackTrace();
                miniToast.Toast(myActivity,"组装HabitBundle失败");
            }
            if(code == 0){
                try {
                    JSONObject jsonObject = new JSONObject(val);
                    String json=jsonObject.getString("result");
                    GoodHabit goodHabit = toJson.jsonToObj(GoodHabit.class,json);
                    habitBundles.add(new HabitBundle(runningHabits.get(i),goodHabit));
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                    miniToast.Toast(myActivity,"组装HabitBundle失败");
                }
            }else {
                miniToast.Toast(myActivity,"组装HabitBundle失败");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler habitClockInHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","habitClockInHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            MyMessage myMsg;
            myMsg = toJson.jsonToObj(MyMessage.class,val);
            if(myMsg == null) {
                miniToast.Toast(myActivity, "操作失败");
            }else {
                miniToast.Toast(myActivity,myMsg.getMsg());
            }

            //刷新页面
            MMKV mmkv = MMKV.defaultMMKV();
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                    .appendPath("selMyRunningHabits")
                    .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                    .appendQueryParameter("token", mmkv.decodeString("user_token",""));
            String url = builder.build().toString();
            new runningHabitListAsync(myActivity,mAdapter,url).execute();
        }
    };

    @Override
    protected void onPostExecute(String s) {
        //初始清除列表
        int i = 0;
        int count = mAdapter.getItemCount();
        if (count != 0) {
            for (int j = 0; j < count; j++) {
                mAdapter.remove(0);
            }
        }
        if(habitBundles != null && habitBundles.size() != 0){
            for (HabitBundle habitBundle : habitBundles) {
                mAdapter.add(habitBundle, i);
                i++;
            }

            int finalI = i;
            mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    if(position < finalI){
                        TextView rhid = view.findViewById(R.id.text_rhid_main);
                        TextView user_config = view.findViewById(R.id.text_user_config_main);
                        String config = user_config.getText().toString();
                        config = config.substring(1,config.length()-1);
                        config = config.replaceAll("\\\\",""); //反转义
                        UserConfig userConfig = toJson.jsonToObj(UserConfig.class,config);
                        if(habitUtils.chkHabitClockIn(userConfig)) {
                            MMKV mmkv = MMKV.defaultMMKV();
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                                    .appendPath("habitclockin")
                                    .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid", 0)))
                                    .appendQueryParameter("token", mmkv.decodeString("user_token", ""))
                                    .appendQueryParameter("rhid", rhid.getText().toString());
                            String url = builder.build().toString();
                            networkTask networkTask = new networkTask();
                            new Thread(networkTask.setParam(habitClockInHandler, url, 1)).start();
                        }else {
                            miniToast.Toast(myActivity,"不在用户设置的打卡时间内，无法打卡");
                        }
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    if(position < finalI){
                        TextView rhid = view.findViewById(R.id.text_rhid_main);
                        TextView habit_name = view.findViewById(R.id.habit_name_textview_main);
                        TextView habit_content = view.findViewById(R.id.text_habit_content_main);
                        TextView user_config = view.findViewById(R.id.text_user_config_main);
                        TextView persist_days = view.findViewById(R.id.persist_days_textview_main);
                        TextView target_days = view.findViewById(R.id.target_days_textview_main);
                        //将字符串变回UserConfig对象
                        String config = user_config.getText().toString();
                        config = config.substring(1,config.length()-1);
                        config = config.replaceAll("\\\\",""); //反转义
                        UserConfig userConfig = toJson.jsonToObj(UserConfig.class,config);
                        AlertDialog alertDialog = miniToast.getDialog(myActivity,"请选择操作","\n注意：放弃好习惯不返还积分\n");
                        boolean isFull = Integer.parseInt(persist_days.getText().toString())>=Integer.parseInt(target_days.getText().toString());
                        alertDialog.setButton(isFull?"完成好习惯":"放弃好习惯", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MMKV mmkv = MMKV.defaultMMKV();
                                Uri.Builder builder = new Uri.Builder();
                                if(!isFull) {
                                    miniToast.Toast(myActivity, "你放弃了好习惯");
                                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                                            .appendPath("giveuphabit")
                                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                            .appendQueryParameter("rhid",rhid.getText().toString());
                                }else {
                                    miniToast.Toast(myActivity, "你完成了好习惯");
                                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                                            .appendPath("giveuphabit")
                                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                            .appendQueryParameter("rhid",rhid.getText().toString())
                                            .appendQueryParameter("isfull", String.valueOf(isFull));
                                }

                                String url = builder.build().toString();
                                networkTask networkTask = new networkTask();
                                new Thread(networkTask.setParam(habitClockInHandler,url,1)).start();;
                                habitReminderUtils.deleteEventById(myActivity,userConfig.getEventId());
                            }
                        });
                        alertDialog.setButton2("重新添加日程", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long eventId = habitUtils.setHabitReminder(myActivity, userConfig.getStart_time(), userConfig.getEnd_time(),
                                        userConfig.getRemind_time(), habit_name.getText().toString(),
                                        habit_content.getText().toString(), "好习惯养成系统", userConfig.getRemind_days_str());
                                if(eventId != 0) {
                                    userConfig.setEventId(eventId);
                                    String config = toJson.objToJsonStr(userConfig);
                                    MMKV mmkv = MMKV.defaultMMKV();
                                    Uri.Builder builder = new Uri.Builder();
                                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                                            .appendPath("updateRunningHabit")
                                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                            .appendQueryParameter("rhid",rhid.getText().toString())
                                            .appendQueryParameter("user_config","\""+config+"\"");
                                    String url = builder.build().toString();
                                    networkTask networkTask = new networkTask();
                                    new Thread(networkTask.setParam(habitClockInHandler,url,1)).start();
                                    miniToast.Toast(myActivity, "日程添加成功");
                                }
                            }
                        });
                        alertDialog.setButton3("分享好习惯", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MMKV mmkv = MMKV.defaultMMKV();
                                Intent intent = new Intent(myActivity, Share_Img_Activity.class);
                                intent.putExtra("user_name", mmkv.decodeString("user_name"));
                                intent.putExtra("habit_name",habit_name.getText().toString());
                                intent.putExtra("persist_days",persist_days.getText().toString());
                                myActivity.startActivity(intent);
                            }
                        });
                       alertDialog.show();
                    }
                }
            });
        }
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... string) {
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,myUrl,1));
        try {
            t.start();
            t.join(); // 防止取不到对象
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MMKV mmkv = MMKV.defaultMMKV();
        int count = 0;
        while (runningHabits == null){
            try {
                if(count>100){
                    Log.i("myLog","获取数据失败");
                    runningHabits = new ArrayList<>();
                    break;
                }
                Thread.sleep(5);
                Log.i("myLog","等待数据");
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MMKV mmkv_habits = MMKV.mmkvWithID("habits");
        String habits_square = mmkv_habits.decodeString("habits");
        if(habits_square != null) {
            ArrayList<GoodHabit> tempHabits = toJson.jsonToObjs(GoodHabit.class,habits_square);
            for (RunningHabit runningHabit : runningHabits) {

                //组装HabitBundle列表，为了减少网络I/O，首先查询缓存
                GoodHabit temp = habitUtils.selHabitByHidOrdered(tempHabits, runningHabit.getHid());
                if (temp != null) {
                    habitBundles.add(new HabitBundle(runningHabit, temp));
                    i++;
                } else {
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                            .appendPath("selHabitByHid")
                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid", 0)))
                            .appendQueryParameter("token", mmkv.decodeString("user_token", ""))
                            .appendQueryParameter("hid", runningHabit.getHid().toString());
                    String url = builder.build().toString();
                    networkTask networkTask1 = new networkTask();
                    Thread t1 = new Thread(networkTask1.setParam(concatHabitBundleHandler, url, 1));
                    try {
                        t1.start();
                        t1.join(); // 防止取不到对象
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            for (RunningHabit runningHabit : runningHabits) {

                //组装HabitBundle列表
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                        .appendPath("selHabitByHid")
                        .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid", 0)))
                        .appendQueryParameter("token", mmkv.decodeString("user_token", ""))
                        .appendQueryParameter("hid", runningHabit.getHid().toString());
                String url = builder.build().toString();
                networkTask networkTask1 = new networkTask();
                Thread t1 = new Thread(networkTask1.setParam(concatHabitBundleHandler, url, 1));
                try {
                    t1.start();
                    t1.join(); // 防止取不到对象
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
