package com.myapp.gradutest_android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.adapter.SquareRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.habitListAsync;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
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

    private SquareRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private Activity thisActivity;

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout toolbarLayout;

    private ArrayList<GoodHabit> data;

    private String url;

    public Fragment_Square() {
    }

    public static Fragment_Square newInstance() {
        return new Fragment_Square();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=getActivity();
        data = new ArrayList<>();
        url = thisActivity.getString(R.string.host)+"/habitplaza";
    }


    private void initData(View view){
        /*mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);*/
        mLayoutManager =  new GridLayoutManager(getContext(),3);
        //打入空数据初始化，异步初始化数据
        mAdapter = new SquareRecyclerViewAdapter(data);
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
        SmartRefreshLayout refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout_fm_square);
        refreshLayout.setRefreshHeader(new ClassicsHeader(view.getContext()));
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //延迟一秒钟再执行任务
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new habitListAsync(getActivity(),mAdapter,url,0).execute();
                    }
                }, 1000);
            }
        });
        new habitListAsync(getActivity(),mAdapter,url,0).execute();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //维护当前Fragment值
        MMKV mmkv_sys = MMKV.mmkvWithID("shared_sys");
        mmkv_sys.encode("currFrag","Square");
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle("习惯广场");
        appBarLayout.setExpanded(false);
    }
}