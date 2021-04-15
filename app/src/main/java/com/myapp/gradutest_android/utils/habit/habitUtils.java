package com.myapp.gradutest_android.utils.habit;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.GoodHabit;

import java.util.ArrayList;
import java.util.List;

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

    public static String getWeekStr(List<Integer> checked_ids){
        String checked_days = "";
        if(checked_ids.size() > 0) {
            for (Integer checked_id : checked_ids) {
                switch (checked_id) {
                    case R.id.mon_btn_week_sel:
                        checked_days = checked_days.concat("MO,");
                        break;
                    case R.id.tues_btn_week_sel:
                        checked_days = checked_days.concat("TU,");
                        break;
                    case R.id.wed_btn_week_sel:
                        checked_days = checked_days.concat("WE,");
                        break;
                    case R.id.thur_btn_week_sel:
                        checked_days = checked_days.concat("TH,");
                        break;
                    case R.id.fri_btn_week_sel:
                        checked_days = checked_days.concat("FR,");
                        break;
                    case R.id.sat_btn_week_sel:
                        checked_days = checked_days.concat("SA,");
                        break;
                    case R.id.sun_btn_week_sel:
                        checked_days = checked_days.concat("SU,");
                        break;
                }
            }
            checked_days = checked_days.substring(0, checked_days.length() - 1);
        }
        return checked_days;
    }
}
