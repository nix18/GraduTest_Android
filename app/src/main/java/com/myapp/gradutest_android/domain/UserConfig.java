package com.myapp.gradutest_android.domain;

import java.util.Date;

import lombok.Data;

@Data
public class UserConfig{
    Date start_time;
    Date end_time;
    Date remind_time;
    String remind_days_str;
    Long eventId;

    public UserConfig() {
    }

    public UserConfig(Date start_time, Date end_time, Date remind_time, String remind_days_str, Long eventId) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.remind_time = remind_time;
        this.remind_days_str = remind_days_str;
        this.eventId = eventId;
    }
}
