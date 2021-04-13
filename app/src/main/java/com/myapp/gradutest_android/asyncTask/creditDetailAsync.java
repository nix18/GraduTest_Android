package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.CreditDetail;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class creditDetailAsync extends AsyncTask<String,Integer,String> {

    protected Activity myActivity;
    protected MyRecyclerViewAdapter mAdapter;
    protected ArrayList<CreditDetail> creditDetails;
    protected String myUrl;

    public creditDetailAsync(Activity myActivity, MyRecyclerViewAdapter mAdapter, String myUrl) {
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
                miniToast.Toast(myActivity,"获取积分收支明细失败");
            }
            if(code == 0){
                try {
                    JSONObject jsonObject=new JSONObject(val);
                    String json=jsonObject.getString("result");
                    creditDetails = toJson.jsonToObjs(CreditDetail.class,json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                miniToast.Toast(myActivity,"获取积分收支明细失败");
            }
        }
    };

    @Override
    protected void onPostExecute(String s) {
        if(creditDetails != null && creditDetails.size() != 0){
            int i = 0;
            int count = mAdapter.getItemCount();
            if (count != 0) {
                for (int j = 0; j < count; j++) {
                    mAdapter.remove(0);
                }
            }
            for (CreditDetail creditDetail:creditDetails){
                mAdapter.add(creditDetail,i);
                i++;
            }
        }
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,myUrl,1));
        try {
            t.start();
            t.join(); // 防止取不到对象
        } catch (InterruptedException e) {
            e.printStackTrace();
            miniToast.Toast(myActivity,"获取积分收支明细失败");
        }
        return null;
    }
}
