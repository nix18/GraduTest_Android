package com.myapp.gradutest_android.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.gradutest_android.Fragment_Blank;
import com.myapp.gradutest_android.Fragment_Square;
import com.myapp.gradutest_android.Fragment_Error;
import com.myapp.gradutest_android.Fragment_My_Info;

import org.jetbrains.annotations.NotNull;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    /**
     * 定义点击Tab时展示的Fragment
     * @param position Tab位置
     * @return 指定的Fragment
     */
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new Fragment_Blank();
            case 1: return new Fragment_Square();
            case 2: return new Fragment_My_Info();
        }
        return new Fragment_Error();
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

