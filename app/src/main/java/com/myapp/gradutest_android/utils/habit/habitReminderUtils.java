package com.myapp.gradutest_android.utils.habit;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import com.myapp.gradutest_android.utils.msg.miniToast;

import java.util.Calendar;
import java.util.TimeZone;

public class habitReminderUtils {

    // ContentProvider的uri
    private Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
    private Uri eventUri = CalendarContract.Events.CONTENT_URI;
    private Uri reminderUri = CalendarContract.Reminders.CONTENT_URI;

    private ContentResolver contentResolver;
    private Context thisContext;
    private ContentValues event;
    private Integer calendarId;

    public habitReminderUtils(Context context) {
        thisContext=context;
    }

    /**
     * 组装日历事件
     *
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param eventTitle    事件标题
     * @param eventDes      事件描述
     * @param eventLocation 事件地点
     */
    public habitReminderUtils eventBuilder(long startTime, long endTime, String eventTitle, String eventDes,
                                     String eventLocation) {
        event = new ContentValues();
        // 事件开始时间
        event.put(CalendarContract.Events.DTSTART, startTime);
        // 事件结束时间
        event.put(CalendarContract.Events.DTEND, endTime);
        // 事件标题
        event.put(CalendarContract.Events.TITLE, eventTitle);
        // 事件描述(对应手机系统日历备注栏)
        event.put(CalendarContract.Events.DESCRIPTION, eventDes);
        // 事件地点
        event.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        // 事件时区
        event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        // 定义事件的显示，默认即可
        event.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
        // 事件的状态
        event.put(CalendarContract.Events.STATUS, 0);
        // 设置事件提醒警报可用
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        // 设置事件忙
        event.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        // 设置事件重复规则
        event.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20210525T000000Z;WKST=SU;BYDAY=MO,WE,FR");

        return this;
    }

    /**
     * 检查是否有日历表,有返回日历id，没有-1
     * */
    @SuppressLint("MissingPermission")
    private int isHaveCalender() {
        // 查询日历表的cursor
        Cursor cursor = contentResolver.query(calendarUri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0){
            return -1;
        }else {
            // 如果有日历表
            try {
                cursor.moveToFirst();
                // 通过cursor返回日历表的第一行的属性值 第一个日历的id
                return cursor.getInt(cursor.getColumnIndex(CalendarContract.Calendars._ID));
            }finally {
                cursor.close();
            }
        }
    }

    /**
     * 添加日历表
     **/
    @SuppressLint("MissingPermission")
    private long addCalendar(){

        // 时区
        TimeZone timeZone = TimeZone.getDefault();
        // 配置Calendar
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, "好习惯养成系统的日历表");
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, "好习惯养成系统");
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "好习惯养成系统");
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "好习惯养成系统");
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "好习惯养成系统");
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);
        value.put(CalendarContract.CALLER_IS_SYNCADAPTER,true);

        // 插入calendar
        Uri insertCalendarUri = contentResolver.insert(calendarUri,value);

        if (insertCalendarUri == null){
            return -1;
        }else {
            // return Integer.parseInt(insertCalendarUri.toString());
            return ContentUris.parseId(insertCalendarUri);
        }
    }

    /**
     *  添加日历事件
     * */
    @SuppressLint("MissingPermission")
    public void addEvent(){

        // 创建contentResolver
        contentResolver = thisContext.getContentResolver();

        // 日历表id
        int calendarId = isHaveCalender();
        if (calendarId == -1){
            addCalendar();
            calendarId = isHaveCalender();
        }
        event.put(CalendarContract.Events.CALENDAR_ID, calendarId);

        // startMillis
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2021,8,15);
        long startMillis = beginTime.getTimeInMillis();

        // endMillis
        Calendar endTime = Calendar.getInstance();
        endTime.set(2021,8,15);
        long endMillis = endTime.getTimeInMillis();

        // 添加event
        Uri insertEventUri = contentResolver.insert(eventUri,event);
        if (insertEventUri == null){
            miniToast.Toast(thisContext,"添加event失败");
        }

        // 添加提醒
        long eventId = ContentUris.parseId(insertEventUri);
        ContentValues valueReminder = new ContentValues();
        valueReminder.put(CalendarContract.Reminders.EVENT_ID,eventId);
        valueReminder.put(CalendarContract.Reminders.MINUTES,5);
        valueReminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);
        Uri insertReminderUri = contentResolver.insert(reminderUri,valueReminder);
        if (insertReminderUri == null){
            miniToast.Toast(thisContext,"添加reminder失败");
        }
    }
}
