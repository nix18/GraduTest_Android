package com.myapp.gradutest_android;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.habitListAsync;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Square#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Square extends Fragment {

    private View view;

    private RecyclerView mRecyclerView;

    private MyRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private Activity thisActivity;

    private MMKV mmkv;

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout toolbarLayout;


    ArrayList<GoodHabit> habits;
    ArrayList<String> data;

    public Fragment_Square() {
        // Required empty public constructor
    }

    public static Fragment_Square newInstance() {
        return new Fragment_Square();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=getActivity();
        mmkv=MMKV.defaultMMKV();
        data = new ArrayList<String>();
        habits=new ArrayList<GoodHabit>();
    }


    private void initData(View view){
        mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyRecyclerViewAdapter(data);
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.habit_list_fm_square);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__square, container, false);
        initData(view);
        initView(view);
        mRecyclerView.setNestedScrollingEnabled(true);
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.refresh_layout_fm_square);
        refreshLayout.setRefreshHeader(new ClassicsHeader(view.getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //延迟一秒钟再执行任务
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new habitListAsync(getActivity(),mAdapter,data).execute();
                    }
                }, 1000);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new habitListAsync(getActivity(),mAdapter,data).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle("习惯广场");
        appBarLayout.setExpanded(false);
    }
}