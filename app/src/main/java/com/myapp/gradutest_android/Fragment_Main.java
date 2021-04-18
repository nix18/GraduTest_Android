package com.myapp.gradutest_android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.adapter.MainRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.runningHabitListAsync;
import com.myapp.gradutest_android.domain.HabitBundle;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Main extends Fragment {

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout toolbarLayout;

    private View view;

    private Activity thisActivity;

    private RecyclerView mRecyclerView;

    private MainRecyclerViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<HabitBundle> data;

    private String url;

    public Fragment_Main() {
    }

    public static Fragment_Main newInstance(String param1, String param2) {
        return new Fragment_Main();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=getActivity();
        data = new ArrayList<>();
        MMKV mmkv = MMKV.defaultMMKV();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").encodedAuthority(getString(R.string.host_core))
                .appendPath("selMyRunningHabits")
                .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                .appendQueryParameter("token", mmkv.decodeString("user_token",""));
        url = builder.build().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__main, container, false);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //维护当前Fragment值
        MMKV mmkv_sys = MMKV.mmkvWithID("shared_sys");
        mmkv_sys.encode("currFrag","Main");
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle("好习惯养成系统");
        appBarLayout.setExpanded(true);
        new runningHabitListAsync(getActivity(),mAdapter,url).execute();
    }

    private void initData(){

        mLayoutManager =  new GridLayoutManager(getContext(),3);
        //打入空数据初始化，异步初始化数据
        mAdapter = new MainRecyclerViewAdapter(data);
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.running_habit_list_main);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }
}