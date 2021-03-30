package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.gradutest_android.adapter.MyRecyclerViewAdapter;
import com.myapp.gradutest_android.asyncTask.habitListAsync;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

import org.json.JSONException;
import org.json.JSONObject;

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


/*    @SuppressLint("HandlerLeak")
    Handler getDataHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","getDataHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){
                try {
                    JSONObject jsonObject=new JSONObject(val);
                    String json=jsonObject.getString("result");
                    habits = toJson.jsonToObjs(GoodHabit.class,json);
                    Log.i("myLog","habits初始化完成 "+habits.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };*/

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

/*    private void getData(){
        String url=thisActivity.getString(R.string.host)+"/habitplaza";
        networkTask networkTask=new networkTask();
        Thread t=new Thread(networkTask.setParam(getDataHandler,url));
        t.start();
        for(GoodHabit habit:habits) {
            data.add("好习惯"+habit.getHabit_name());
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment__square, container, false);
        initData(view);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new habitListAsync(getActivity(),mAdapter,data).execute();
    }
}