package com.myapp.gradutest_android.domain;

import java.util.Date;

import lombok.Data;

@Data
public class RunningHabit {
    protected Integer rhid;
    protected Integer hid;
    protected Integer uid;
    protected String user_config;
    protected Integer capital; //投入积分
    protected Integer bonus; //回报积分
    protected Integer persist_days;
    protected Integer target_days;
    protected Date last_qd_time;
    protected Date running_start_time;

    public RunningHabit() {
    }

    public RunningHabit(Integer rhid, Integer hid, Integer uid, String user_config, Integer bonus, Integer persist_days, Integer target_days, Date last_qd_time, Date running_start_time) {
        this.rhid = rhid;
        this.hid = hid;
        this.uid = uid;
        this.user_config = user_config;
        this.bonus = bonus;
        this.persist_days = persist_days;
        this.target_days = target_days;
        this.last_qd_time = last_qd_time;
        this.running_start_time = running_start_time;
    }
}
