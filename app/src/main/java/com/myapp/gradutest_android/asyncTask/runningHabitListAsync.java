package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.domain.HabitBundle;
import com.myapp.gradutest_android.domain.RunningHabit;
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

    @Override
    protected void onPostExecute(String s) {
        Log.i("myLog",habitBundles.toString());
        if(habitBundles != null && habitBundles.size() != 0){
            int i = 0;
            int count = mAdapter.getItemCount();
            if (count != 0) {
                for (int j = 0; j < count; j++) {
                    mAdapter.remove(0);
                }
            }
            for (HabitBundle habitBundle : habitBundles) {
                mAdapter.add(habitBundle, i);
                i++;
            }
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
        while (runningHabits == null){
            try {
                Thread.sleep(100);
                Log.i("myLog","等待数据");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (RunningHabit runningHabit:runningHabits){
            //组装HabitBundle列表
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").encodedAuthority(myActivity.getString(R.string.host_core))
                    .appendPath("selHabitByHid")
                    .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                    .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                    .appendQueryParameter("hid", runningHabit.getHid().toString());
            String url = builder.build().toString();
            networkTask networkTask1=new networkTask();
            Thread t1=new Thread(networkTask1.setParam(concatHabitBundleHandler,url,1));
            try {
                t1.start();
                t1.join(); // 防止取不到对象
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
