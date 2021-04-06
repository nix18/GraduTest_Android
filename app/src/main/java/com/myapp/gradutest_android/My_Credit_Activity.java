package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.gradutest_android.asyncTask.userCreditAsync;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class My_Credit_Activity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView more_info_btn;
    private TextView credit_sum;
    private CardView credit_list_btn;
    private CardView clock_in_btn;
    private CardView credit_exchange_btn;
    private CardView credit_lottery_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_my__credit);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initView();
        MMKV mmkv=MMKV.defaultMMKV();
        clock_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.host)+"/qiandao?uid="+mmkv.decodeInt("uid",0)+
                        "&token="+mmkv.decodeString("user_token","");
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(clockInHandler,url,1)).start();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new userCreditAsync(My_Credit_Activity.this).execute();
    }

    protected void initView(){
        back_btn = findViewById(R.id.img_back_my_credit);
        more_info_btn = findViewById(R.id.img_more_info_my_credit);
        credit_sum = findViewById(R.id.credit_sum_my_credit);
        credit_list_btn = findViewById(R.id.credit_list_my_credit);
        clock_in_btn = findViewById(R.id.clock_in_btn_my_credit);
        credit_exchange_btn = findViewById(R.id.credit_exchange_btn_my_credit);
        credit_lottery_btn = findViewById(R.id.credit_lottery_btn_my_credit);
    }

    @SuppressLint("HandlerLeak")
    Handler clockInHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","clockInHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            MyMessage myMessage = toJson.jsonToObj(MyMessage.class,val);
            Toast.makeText(My_Credit_Activity.this,myMessage.getMsg(),Toast.LENGTH_SHORT).show();
            new userCreditAsync(My_Credit_Activity.this).execute();
        }
    };
}