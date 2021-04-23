package com.myapp.gradutest_android.asyncTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.domain.Goods;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class goodsListAsync extends AsyncTask<String,Integer,String>{

    protected Activity myActivity;
    protected MyRecyclerViewAdapter mAdapter;
    protected ArrayList<Goods> goods;
    protected String myUrl;

    @SuppressLint("HandlerLeak")
    Handler getDataHandler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog", "getDataHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code = -1;
            try {
                code = getJson.getStatusCode(val);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (code == 0) {
                JSONObject jsonObject= null;
                try {
                    jsonObject = new JSONObject(val);
                    String json=jsonObject.getString("result");
                    goods = toJson.jsonToObjs(Goods.class,json);
                    int i = 0;
                    int count = mAdapter.getItemCount();
                    if (count != 0) {
                        for (int j = 0; j < count; j++) {
                            mAdapter.remove(0);
                        }
                    }
                    for (Goods good:goods){
                        mAdapter.add(good,i);
                        i++;
                    }
                    mAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            TextView gid = view.findViewById(R.id.text_gid_exchange_goods);
                            MMKV mmkv = MMKV.defaultMMKV();
                            AlertDialog dialog = miniToast.getDialog(myActivity,"请选择操作","");
                            dialog.setButton("兑换", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri.Builder builder = new Uri.Builder();
                                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                                            .appendPath("exchangeGoods")
                                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                                            .appendQueryParameter("gid",gid.getText().toString());
                                    String url = builder.build().toString();
                                    networkTask networkTask = new networkTask();
                                    new Thread(networkTask.setParam(exchangeGoodsHandler,url,1)).start();
                                }
                            });
                            dialog.setButton2("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler exchangeGoodsHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","exchangeGoodsHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            MyMessage myMsg;
            myMsg = toJson.jsonToObj(MyMessage.class,val);
            if(myMsg == null) {
                miniToast.Toast(myActivity, "操作失败");
            }else {
                miniToast.Toast(myActivity,myMsg.getMsg());
                if(myMsg.getCode() != -1) {
                    //刷新页面
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https").encodedAuthority(myActivity.getString(R.string.host_core))
                            .appendPath("getExchangeGoods");
                    String url = builder.build().toString();
                    new goodsListAsync(myActivity, mAdapter, url).execute();
                }
            }
        }
    };


    public goodsListAsync(Activity myActivity, MyRecyclerViewAdapter mAdapter, String myUrl) {
        this.myActivity = myActivity;
        this.mAdapter = mAdapter;
        this.myUrl = myUrl;
    }

    @Override
    protected String doInBackground(String... strings) {
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,myUrl,0));
        try {
            t.start();
            t.join(); // 防止取不到对象
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
