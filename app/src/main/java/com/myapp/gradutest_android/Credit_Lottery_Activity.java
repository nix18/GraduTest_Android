package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.str.strUtils;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Credit_Lottery_Activity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView more_info_btn;
    private CardView lottery_one_btn;
    private CardView lottery_ten_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit__lottery);
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Credit_Lottery_Activity.this,"更多信息","\n一次抽奖：" +
                        "\n使用10积分进行抽奖\n十次抽奖：\n使用100积分进行抽奖（不足时以使用数目为准）\n\n抽奖概率公示：\n每十次抽奖必出一次一等奖" +
                        "\n每九十次抽奖必出一次特等奖\n").show();
            }
        });
        lottery_ten_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMKV mmkv = MMKV.defaultMMKV();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                        .appendPath("creditLottery")
                        .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                        .appendQueryParameter("token", mmkv.decodeString("user_token",""));
                String url = builder.build().toString();
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(lotteryHandler,url,1)).start();
            }
        });
    }

    public void initView(){
        back_btn = findViewById(R.id.img_back_credit_lottery);
        more_info_btn = findViewById(R.id.img_more_info_credit_lottery);
        lottery_one_btn = findViewById(R.id.lottery_one_btn_credit_lottery);
        lottery_ten_btn = findViewById(R.id.lottery_ten_btn_credit_lottery);
    }

    @SuppressLint("HandlerLeak")
    Handler lotteryHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","lotteryHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                JSONObject jsonObject = new JSONObject(val);
                ArrayList<String> lotterys = strUtils.tuple2arrayList(jsonObject.getString("Index"));
                miniToast.getDialog(Credit_Lottery_Activity.this,"抽奖结果",lotterys.toString()).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}