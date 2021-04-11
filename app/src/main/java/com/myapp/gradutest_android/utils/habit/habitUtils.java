package com.myapp.gradutest_android.utils.habit;

import com.myapp.gradutest_android.domain.GoodHabit;

import java.util.ArrayList;

public class habitUtils {
    /**
     * 通过Hid查找好习惯
     * @param habits 好习惯列表
     * @param hid 要找的好习惯Hid
     * @return 查找到的好习惯，找不到返回null
     */
    public static GoodHabit selHabitByHid(ArrayList<GoodHabit> habits, int hid){
        int low,high,mid;
        low = 0;
        high = habits.size()-1;
        while (low <= high){
            mid = (low+high)/2;
            if(habits.get(mid).getHid() == hid)
                return habits.get(mid);
            if (habits.get(mid).getHid() > hid)
                high = mid-1;
            if (habits.get(mid).getHid() < hid)
                low = mid+1;
        }
        return null;
    }
}
