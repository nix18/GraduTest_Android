package com.myapp.gradutest_android;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.myapp.gradutest_android.adapter.MyHabitsRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.habitListAsync;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

public class All_Habits_Activity extends AppCompatActivity {

    private ImageView back_btn;

    private ImageView more_info_btn;

    private RecyclerView mRecyclerView;

    private MyHabitsRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<GoodHabit> data;

    private String url;

    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__habits);
        statusBarUtils.patchFullScreen(this);
        MMKV mmkv = MMKV.defaultMMKV();
        data = new ArrayList<>();
        initData();
        initView();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(All_Habits_Activity.this,"更多信息","\n这里展示了所有好习惯\n\n点击分类以分类查看").show();
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                        .appendPath("selhabits");
                String checkedUrl;
                switch (checkedId){
                    case R.id.all_btn:
                        chipGroup.check(checkedId);
                        checkedUrl = builder.build().toString();
                        new habitListAsync(All_Habits_Activity.this,mAdapter,checkedUrl,2).execute();
                        break;
                    case R.id.study_btn:
                        checkedUrl = builder.appendQueryParameter("hcategory","学习").build().toString();
                        new habitListAsync(All_Habits_Activity.this,mAdapter,checkedUrl,2).execute();
                        break;
                    case R.id.sport_btn:
                        checkedUrl = builder.appendQueryParameter("hcategory","运动").build().toString();
                        new habitListAsync(All_Habits_Activity.this,mAdapter,checkedUrl,2).execute();
                        break;
                    case R.id.entertainment_btn:
                        checkedUrl = builder.appendQueryParameter("hcategory","娱乐").build().toString();
                        new habitListAsync(All_Habits_Activity.this,mAdapter,checkedUrl,2).execute();
                        break;
                    case R.id.other_btn:
                        checkedUrl = builder.appendQueryParameter("hcategory","其他").build().toString();
                        new habitListAsync(All_Habits_Activity.this,mAdapter,checkedUrl,2).execute();
                        break;
                }
            }
        });
        chipGroup.check(R.id.all_btn);
    }

    private void initData(){
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //打入空数据初始化，异步初始化数据
        mAdapter = new MyHabitsRecyclerViewAdapter(data);
    }

    public void initView(){
        back_btn = findViewById(R.id.img_back_all_habits);
        more_info_btn = findViewById(R.id.img_more_info_all_habits);
        chipGroup = findViewById(R.id.cata_group_all_habits);
        mRecyclerView = findViewById(R.id.habit_list_all_habits);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }
}