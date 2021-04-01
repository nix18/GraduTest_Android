package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.myapp.gradutest_android.Habit_Info_Activity;
import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class habitListAsync extends AsyncTask<String,Integer,String> {

    protected Activity myActivity;
    private MyRecyclerViewAdapter mAdapter;
    ArrayList<GoodHabit> habits;
    ArrayList<String> myData = new ArrayList<>();
    RefreshLayout refreshLayout;

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
                    MMKV habit_mmkv=MMKV.mmkvWithID("habits");
                    habit_mmkv.encode("habits",json);
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
            mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                Intent intent=new Intent(myActivity, Habit_Info_Activity.class);
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(myActivity, "clicked " + position,
                            Toast.LENGTH_SHORT).show();
                    intent.putExtra("position",position);
                    myActivity.startActivity(intent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(myActivity, "long clicked " + position,
                            Toast.LENGTH_SHORT).show();
                }
            });
            i++;
        }
        refreshLayout = myActivity.findViewById(R.id.refresh_layout_fm_square);
        if(refreshLayout != null)
            refreshLayout.finishRefresh(1000);
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
            t.join(); // 防止取不到对象
        } catch (InterruptedException e) {
            e.printStackTrace();
            refreshLayout.finishRefresh(1000,false,false);
        }
        return null;
    }
}
