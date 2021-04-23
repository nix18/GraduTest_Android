package com.myapp.gradutest_android;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.adapter.ExchangeGoodsRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.goodsListAsync;
import com.myapp.gradutest_android.domain.Goods;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

import java.util.ArrayList;

public class Exchange_Goods_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ExchangeGoodsRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView back_btn;

    private ImageView more_info_btn;

    private ArrayList<Goods> data;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange__goods);
        statusBarUtils.patchFullScreen(this);
        data = new ArrayList<>();
        initData();
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Exchange_Goods_Activity.this,"更多信息","\n积分兑换的详细规则\n\n以软件内通知为准").show();
            }
        });
        //异步加载数据
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").encodedAuthority(getString(R.string.host_core))
                .appendPath("getExchangeGoods");
        url = builder.build().toString();
        new goodsListAsync(this,mAdapter,url).execute();
    }

    private void initData(){
        mLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        //打入空数据初始化，异步初始化数据
        mAdapter = new ExchangeGoodsRecyclerViewAdapter(data);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.goods_list_exchange_goods);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        back_btn = findViewById(R.id.img_back_exchange_goods);
        more_info_btn = findViewById(R.id.img_more_info_exchange_goods);
    }
}