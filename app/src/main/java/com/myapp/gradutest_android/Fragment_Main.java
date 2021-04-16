package com.myapp.gradutest_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tencent.mmkv.MMKV;

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