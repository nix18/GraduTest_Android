package com.myapp.gradutest_android.domain;

import lombok.Data;

@Data
public class HabitBundle {
    private RunningHabit runningHabit;
    private GoodHabit goodHabit;

    public HabitBundle() {
    }

    public HabitBundle(RunningHabit runningHabit, GoodHabit goodHabit) {
        this.runningHabit = runningHabit;
        this.goodHabit = goodHabit;
    }
}
