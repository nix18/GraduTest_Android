package com.myapp.gradutest_android.domain;

import java.util.Date;

import lombok.Data;

@Data
public class GoodHabit {
    protected Integer hid;
    protected Integer create_uid;
    protected String habit_name;
    protected String habit_content;
    protected String habit_category;
    protected Integer habit_heat;
    protected Date habit_create_time;
    protected Boolean habit_isvisible;

    public GoodHabit(){}

    public GoodHabit(Integer hid, Integer create_uid, String habit_name, String habit_content, String habit_category, Integer habit_heat, Date habit_create_time, Boolean habit_isvisible) {
        this.hid = hid;
        this.create_uid = create_uid;
        this.habit_name = habit_name;
        this.habit_content = habit_content;
        this.habit_category = habit_category;
        this.habit_heat = habit_heat;
        this.habit_create_time = habit_create_time;
        this.habit_isvisible = habit_isvisible;
    }
}
