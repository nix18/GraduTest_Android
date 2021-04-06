package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.listener.AppBarLayoutStateChangeListener;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.offLineMode;
import com.tencent.mmkv.MMKV;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_My#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_My extends Fragment {

    private FragmentActivity thisActivity;

    private View view;

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout toolbarLayout;

    private Button my_info_btn;

    private Button my_credit_btn;

    private Button my_data_analyze_btn;

    private Button settings_btn;

    private Button log_out_btn;

    private Toolbar toolbar;

    public Fragment_My() {
        // Required empty public constructor
    }

    public static Fragment_My newInstance(String param1, String param2) {
        return new Fragment_My();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 通过this.getActivity()获取主activity中的方法 通过view来改动布局
        view=inflater.inflate(R.layout.fragment__my, container, false);
        MMKV mmkv=MMKV.defaultMMKV();
        setBtnIcon();
        my_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),My_Info_Activity.class);
                startActivity(intent);
            }
        });
        my_credit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),My_Credit_Activity.class);
                startActivity(intent);
            }
        });
        if(offLineMode.getOffLineMode()) {
            my_info_btn.setClickable(false);
            my_credit_btn.setClickable(false);
        }
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisActivity,Settings_Activity.class);
                startActivity(intent);
            }
        });
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mmkv.decodeInt("uid") != -1) {
                    String url = getString(R.string.host) + "/logout?uid=" + mmkv.decodeInt("uid", 0) +
                            "&token=" + mmkv.decodeString("user_token", "");
                    networkTask networkTask = new networkTask();
                    new Thread(networkTask.setParam(logOutHandler, url)).start();
                }else {
                    //设置离线模式为否
                    offLineMode.setOffLineMode(false);

                    //返回登录界面
                    Toast toast=Toast.makeText(getContext(), "登出成功", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent=new Intent(getActivity(),Log_In_Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //维护当前Fragment值
        MMKV mmkv_sys = MMKV.mmkvWithID("shared_sys");
        mmkv_sys.encode("currFrag","My");
        MMKV mmkv = MMKV.defaultMMKV();
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);

        //设置顶部文字动态变化
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle(mmkv.decodeString("user_name"));
        appBarLayout.setExpanded(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayoutStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarLayoutStateChangeListener.State state) {
                MMKV mmkv_in = MMKV.mmkvWithID("shared_sys");
                String curr = mmkv_in.decodeString("currFrag");
                switch (state){
                    case EXPANDED:
                    case INTERMEDIATE:
                        if(Objects.equals(curr, "My"))
                        toolbarLayout.setTitle(mmkv.decodeString("user_name")+"，你好");
                        break;
                    case COLLAPSED:
                        if(Objects.equals(curr, "My"))
                        toolbarLayout.setTitle(mmkv.decodeString("user_name"));
                        break;
                }
            }
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setBtnIcon(){
        my_info_btn = view.findViewById(R.id.my_info_btn_fm_my);
        my_credit_btn = view.findViewById(R.id.my_credit_btn_fm_my);
        my_data_analyze_btn = view.findViewById(R.id.my_data_analyze_btn_fm_my);
        settings_btn = view.findViewById(R.id.settings_btn_fm_my);
        log_out_btn = view.findViewById(R.id.log_out_btn_fm_my);
        Drawable drawable_info = getResources().getDrawable(R.drawable.___29);
        drawable_info.setBounds(60,0,140,80);
        Drawable drawable_credit = getResources().getDrawable(R.drawable.___30);
        drawable_credit.setBounds(60,0,140,80);
        Drawable drawable_my_data_analyze = getResources().getDrawable(R.drawable.___79);
        drawable_my_data_analyze.setBounds(60,0,140,80);
        Drawable drawable_settings = getResources().getDrawable(R.drawable.___16);
        drawable_settings.setBounds(60,0,140,80);
        Drawable drawable_log_out = getResources().getDrawable(R.drawable.___46);
        drawable_log_out.setBounds(60,0,140,80);
        Drawable drawable_next = getResources().getDrawable(R.drawable._____199);
        drawable_next.setBounds(-60,0,20,80);
        my_info_btn.setCompoundDrawables(drawable_info,null,drawable_next,null);
        my_credit_btn.setCompoundDrawables(drawable_credit,null,drawable_next,null);
        my_data_analyze_btn.setCompoundDrawables(drawable_my_data_analyze,null,drawable_next,null);
        settings_btn.setCompoundDrawables(drawable_settings,null,drawable_next,null);
        log_out_btn.setCompoundDrawables(drawable_log_out,null,drawable_next,null);
    }

    @SuppressLint("HandlerLeak")
    Handler logOutHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","logOutHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){

                //清除MMKV
                MMKV mmkv = MMKV.defaultMMKV();
                mmkv.clearAll();

                //返回登录界面
                Toast toast=Toast.makeText(getContext(), "登出成功", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(getActivity(),Log_In_Activity.class);
                startActivity(intent);
                getActivity().finish();
            }else {
                Toast toast=Toast.makeText(getContext(), "登出失败", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
}