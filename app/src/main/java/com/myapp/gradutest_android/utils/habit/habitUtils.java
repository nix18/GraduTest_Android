package com.myapp.gradutest_android.utils.habit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.GoodHabit;
import com.myapp.gradutest_android.utils.msg.miniToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public static ArrayList<Integer> getWeekInt(List<Integer> checked_ids){
        ArrayList<Integer> checked_days = new ArrayList<>();
        if(checked_ids.size() > 0) {
            for (Integer checked_id : checked_ids) {
                switch (checked_id) {
                    case R.id.mon_btn_week_sel:
                        checked_days.add(Calendar.MONDAY);
                        break;
                    case R.id.tues_btn_week_sel:
                        checked_days.add(Calendar.TUESDAY);
                        break;
                    case R.id.wed_btn_week_sel:
                        checked_days.add(Calendar.WEDNESDAY);
                        break;
                    case R.id.thur_btn_week_sel:
                        checked_days.add(Calendar.THURSDAY);
                        break;
                    case R.id.fri_btn_week_sel:
                        checked_days.add(Calendar.FRIDAY);
                        break;
                    case R.id.sat_btn_week_sel:
                        checked_days.add(Calendar.SATURDAY);
                        break;
                    case R.id.sun_btn_week_sel:
                        checked_days.add(Calendar.SUNDAY);
                        break;
                }
            }
        }
        return checked_days;
    }

    public  static Integer getTargetDays(Context context, Date start_time, Date end_time, List<Integer> checked_week_ids){
        int target_days = 0;
        if(checked_week_ids.size() == 7) {
            target_days =(int) ((end_time.getTime() - start_time.getTime())/(3600*1000*24));
            if(target_days == 0){
                miniToast.Toast(context,"相差时间过短，无法添加");
            }else if(target_days < 0) {
                target_days = Math.abs(target_days);
            }
        }else {
            target_days = 0;
            ArrayList<Integer> checked_days_id = habitUtils.getWeekInt(checked_week_ids);
            Calendar c_start = new GregorianCalendar();
            Calendar c_end = new GregorianCalendar();
            c_start.setTime(start_time);
            c_end.setTime(end_time);
            //统计从c_start到c_end由多少个选择的日期
            while (c_start.before(c_end)){
                if (checked_days_id.contains(c_start.get(Calendar.DAY_OF_WEEK))) {
                    target_days++;
                }
                c_start.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return target_days;
    }

    public static void setHabitReminder(Activity activity, Date start_time, Date end_time, String title, String des, String loc, String week_sel){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            Log.i("myLog","获取授权");
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},1);
        }else {
            Log.i("myLog", "添加日程");
            habitReminderUtils habitReminderUtils = new habitReminderUtils(activity).eventBuilder(
                    start_time.getTime(), end_time.getTime(), title, des, loc,
                    week_sel);
            habitReminderUtils.addEvent();
        }
    }
}
