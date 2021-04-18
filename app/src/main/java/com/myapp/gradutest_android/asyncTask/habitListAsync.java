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

import com.myapp.gradutest_android.Habit_Info_Activity;
import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.msg.miniToast;
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
    protected MyRecyclerViewAdapter mAdapter;
    protected ArrayList<GoodHabit> habits;
    protected String myUrl;
    protected RefreshLayout refreshLayout;
    protected Integer originActivity;

    @SuppressLint("HandlerLeak")
    Handler getDataHandler=new Handler(){
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
                if(refreshLayout != null)
                    refreshLayout.finishRefresh(1000,false,false);
            }
            if(code == 0){
                try {
                    JSONObject jsonObject=new JSONObject(val);
                    String json=jsonObject.getString("result");
                    habits = toJson.jsonToObjs(GoodHabit.class,json);
                    MMKV habit_mmkv=MMKV.mmkvWithID("habits");
                    switch (originActivity) {
                        case 0:
                            habit_mmkv.encode("habits", json);
                            break;
                        case 1:
                            habit_mmkv.encode("habits_my", json);
                            break;
                        case 2:
                            habit_mmkv.encode("habits_all", json);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                if(refreshLayout != null)
                    refreshLayout.finishRefresh(1000,false,false);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler delHabitHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","delHabitHandler执行");
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
            builder.scheme("http").encodedAuthority(myActivity.getString(R.string.host_core))
                    .appendPath("selmyhabits")
                    .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                    .appendQueryParameter("token", mmkv.decodeString("user_token",""));
            String url = builder.build().toString();
            new habitListAsync(myActivity,mAdapter,url,1).execute();
        }
    };

    /**
     *
     * @param activity 传入的Activity
     * @param myRecyclerViewAdapter 传入的RecyclerViewAdapter
     * @param url 要访问的url
     * @param origin 来源Activity 0：Fragment_Square 1：My_Habits_Activity 2：All_Habits_Activity
     */
    public habitListAsync(Activity activity, MyRecyclerViewAdapter myRecyclerViewAdapter, String url, Integer origin) {
        myActivity = activity;
        mAdapter = myRecyclerViewAdapter;
        myUrl = url;
        originActivity = origin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        if(habits != null && habits.size() != 0) {
            int i = 0;
            int count = mAdapter.getItemCount();
            if (count != 0) {
                for (int j = 0; j < count; j++) {
                    mAdapter.remove(0);
                }
            }
            for (GoodHabit habit : habits) {
                mAdapter.add(habit, i);
                i++;
            }


            int finalI = i;
            mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                Intent intent = new Intent(myActivity, Habit_Info_Activity.class);

                @Override
                public void onItemClick(View view, int position) {
                    if(position < finalI){
                        TextView hid = view.findViewById(R.id.text_hid_fm_square); //重用id
                        intent.putExtra("hid", Integer.parseInt(hid.getText().toString()));
                        intent.putExtra("origin",originActivity);
                        myActivity.startActivity(intent);
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    if(position < finalI){
                        if(originActivity == 1) {
                            String hid = ((TextView) view.findViewById(R.id.text_hid_fm_square)).getText().toString();
                            AlertDialog alertDialog = miniToast.getDialog(myActivity, "请选择操作", "");
                            alertDialog.setButton("删除习惯", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    miniToast.Toast(myActivity, "删除" + hid);
                                    MMKV mmkv = MMKV.defaultMMKV();
                                    Uri.Builder builder = new Uri.Builder();
                                    builder.scheme("http").encodedAuthority(myActivity.getString(R.string.host_core))
                                            .appendPath("delhabit")
                                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                            .appendQueryParameter("hid",hid);
                                    String url = builder.build().toString();
                                    networkTask networkTask = new networkTask();
                                    new Thread(networkTask.setParam(delHabitHandler,url,1)).start();;
                                }
                            });
                            alertDialog.show();
                        }
                    }
                }
            });

            if(refreshLayout != null)
                refreshLayout.finishRefresh(1000);
        }else {
            if(refreshLayout != null)
                refreshLayout.finishRefresh(1000,false,false);
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
        refreshLayout = myActivity.findViewById(R.id.refresh_layout_fm_square);
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,myUrl,0));
        try {
            t.start();
            t.join(); // 防止取不到对象
        } catch (InterruptedException e) {
            e.printStackTrace();
            if(refreshLayout != null)
                refreshLayout.finishRefresh(1000,false,false);
        }
        return null;
    }
}
