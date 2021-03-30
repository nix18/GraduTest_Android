package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class habitListAsync extends AsyncTask<String,Integer,String> {

    protected Activity myActivity;
    private MyRecyclerViewAdapter mAdapter;
    ArrayList<GoodHabit> habits;
    ArrayList<String> myData = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler getDataHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","getDataHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){
                try {
                    JSONObject jsonObject=new JSONObject(val);
                    String json=jsonObject.getString("result");
                    habits = toJson.jsonToObjs(GoodHabit.class,json);
                    Log.i("myLog","habits初始化完成 "+habits.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public habitListAsync(Activity activity,MyRecyclerViewAdapter myRecyclerViewAdapter,ArrayList<String> data) {
        myActivity=activity;
        mAdapter=myRecyclerViewAdapter;
        myData=data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        int i=0;
        int count=mAdapter.getItemCount();
        if(count!=0){
            for(int j=0;j<count;j++) {
                mAdapter.remove(0);
            }
        }
        for(GoodHabit habit:habits) {
            mAdapter.add("好习惯"+habit.toString(),i);
            i++;
        }
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url=myActivity.getString(R.string.host)+"/habitplaza";
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,url));
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}