package com.myapp.gradutest_android.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.myapp.gradutest_android.Fragment_Blank;
import com.myapp.gradutest_android.Fragment_My;

import org.jetbrains.annotations.NotNull;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            case 1:
                return new Fragment_Blank();
            case 2:return new Fragment_My();
        }
        return new Fragment_My();
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

