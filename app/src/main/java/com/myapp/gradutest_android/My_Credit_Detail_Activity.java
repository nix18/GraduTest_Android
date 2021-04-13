package com.myapp.gradutest_android;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.adapter.CreditDetailRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.creditDetailAsync;
import com.myapp.gradutest_android.domain.CreditDetail;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class My_Credit_Detail_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private CreditDetailRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView back_btn;

    private ImageView more_info_btn;

    private ArrayList<CreditDetail> data;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__credit__detail);
        data = new ArrayList<>();
        initData();
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(My_Credit_Detail_Activity.this,"更多信息","\n这里是积分的收支情况\n").show();
            }
        });
        //异步加载数据
        MMKV mmkv = MMKV.defaultMMKV();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                .appendPath("creditDetail")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        url = builder.build().toString();
        new creditDetailAsync(this,mAdapter,url).execute();
    }

    private void initData(){
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //打入空数据初始化，异步初始化数据
        mAdapter = new CreditDetailRecyclerViewAdapter(data);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.credit_detail_list_credit_detail);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        back_btn = findViewById(R.id.img_back_credit_detail);
        more_info_btn = findViewById(R.id.img_more_info_credit_detail);
    }
}