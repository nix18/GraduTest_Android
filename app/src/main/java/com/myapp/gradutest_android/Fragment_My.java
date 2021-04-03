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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.asyncTask.userCreditAsync;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

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
        setBtnIcon();
        my_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),My_Info_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MMKV mmkv = MMKV.defaultMMKV();
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle(mmkv.decodeString("user_name"));
        appBarLayout.setExpanded(false);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setBtnIcon(){
        my_info_btn = view.findViewById(R.id.my_info_btn_my_info);
        my_credit_btn = view.findViewById(R.id.my_credit_btn_my_info);
        my_data_analyze_btn = view.findViewById(R.id.my_data_analyze_btn_my_info);
        settings_btn = view.findViewById(R.id.settings_my_info);
        Drawable drawable_info = getResources().getDrawable(R.drawable._106);
        drawable_info.setBounds(60,0,140,80);
        Drawable drawable_credit = getResources().getDrawable(R.drawable._132);
        drawable_credit.setBounds(60,0,140,80);
        Drawable drawable_my_data_analyze = getResources().getDrawable(R.drawable._183);
        drawable_my_data_analyze.setBounds(60,0,140,80);
        Drawable drawable_settings = getResources().getDrawable(R.drawable._64);
        drawable_settings.setBounds(60,0,140,80);
        Drawable drawable_next = getResources().getDrawable(R.drawable._19);
        drawable_next.setBounds(-60,0,20,80);
        my_info_btn.setCompoundDrawables(drawable_info,null,drawable_next,null);
        my_credit_btn.setCompoundDrawables(drawable_credit,null,drawable_next,null);
        my_data_analyze_btn.setCompoundDrawables(drawable_my_data_analyze,null,drawable_next,null);
        settings_btn.setCompoundDrawables(drawable_settings,null,drawable_next,null);
    }
}