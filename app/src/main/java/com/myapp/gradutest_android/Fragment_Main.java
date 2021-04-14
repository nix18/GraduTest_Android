package com.myapp.gradutest_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.myapp.gradutest_android.utils.habit.habitReminderUtils;
import com.tencent.mmkv.MMKV;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Main extends Fragment {

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout toolbarLayout;

    private View view;

    public Fragment_Main() {
        // Required empty public constructor
    }

    public static Fragment_Main newInstance(String param1, String param2) {
        return new Fragment_Main();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__main, container, false);
        Button calendarbtn = view.findViewById(R.id.calendarTest);
        calendarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
                    Log.i("myLog","获取授权");
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},1);
                }else {
                    Log.i("myLog","添加日程");
                    Calendar time = Calendar.getInstance();
                    time.set(2021,4,15);
                    habitReminderUtils habitReminderUtils = new habitReminderUtils(getContext()).eventBuilder(
                            time.getTimeInMillis(),time.getTimeInMillis(),"测试","测试描述","好习惯"
                    );
                    habitReminderUtils.addEvent();
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
        mmkv_sys.encode("currFrag","Main");
        appBarLayout = getActivity().findViewById(R.id.appbar_main);
        toolbarLayout = getActivity().findViewById(R.id.collapsing_toolbar_layout_main);
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        toolbarLayout.setTitle("好习惯养成系统");
        appBarLayout.setExpanded(true);
    }
}