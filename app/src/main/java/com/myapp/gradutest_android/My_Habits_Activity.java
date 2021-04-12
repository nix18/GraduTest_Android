package com.myapp.gradutest_android;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.adapter.MyHabitsRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.habitListAsync;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class My_Habits_Activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private MyHabitsRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView back_btn;

    private ImageView more_info_btn;

    private ArrayList<GoodHabit> data;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_my__habits);
        MMKV mmkv = MMKV.defaultMMKV();
        //打入空数据
        data = new ArrayList<GoodHabit>();
        initData();
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(My_Habits_Activity.this,"更多信息","这里是用户创建的所有好习惯\n").show();
            }
        });
        mRecyclerView.setNestedScrollingEnabled(true);
        //异步加载数据
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                .appendPath("selmyhabits")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        url = builder.build().toString();
        new habitListAsync(this,mAdapter,url).execute();
    }

    private void initData(){
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //打入空数据初始化，异步初始化数据
        mAdapter = new MyHabitsRecyclerViewAdapter(data);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.my_habit_list_my_habits);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
        back_btn = findViewById(R.id.img_back_my_habits);
        more_info_btn = findViewById(R.id.img_more_info_my_habits);
    }
}