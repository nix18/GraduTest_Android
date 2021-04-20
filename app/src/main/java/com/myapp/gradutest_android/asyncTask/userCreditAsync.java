package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.Credit;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;


@SuppressLint("StaticFieldLeak")
public class userCreditAsync extends AsyncTask<String,Integer,String> {

    protected Activity myActivity;
    protected TextView t;
    protected Credit credit;


    @SuppressLint("HandlerLeak")
    Handler updateCreditHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","updateCreditHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){
                credit = toJson.jsonToObj(Credit.class,val);
                MMKV mmkv=MMKV.defaultMMKV();
                mmkv.encode("user_credit",credit.getCreditSum());
            }
        }
    };

    public userCreditAsync(Activity activity) {
        myActivity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        t = myActivity.findViewById(R.id.credit_sum_my_credit);
        if(t == null){
            t = myActivity.findViewById(R.id.credit_sum_credit_lottery);
        }
        t.setText(credit.getCreditSum().toString());
    }

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... strings) {
        MMKV mmkv = MMKV.defaultMMKV();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").encodedAuthority(myActivity.getResources().getString(R.string.host_core))
                .appendPath("getCredit")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        String url = builder.build().toString();
        networkTask networkTask=new networkTask();
        Thread t = new Thread(networkTask.setParam(updateCreditHandler,url,1));
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
