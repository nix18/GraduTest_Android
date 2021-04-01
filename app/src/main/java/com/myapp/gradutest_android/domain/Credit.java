package com.myapp.gradutest_android.domain;

import lombok.Data;

@Data
public class Credit {
    protected Integer Uid;
    protected Integer CreditSum;

    public Credit(Integer uid, Integer creditSum) {
        Uid = uid;
        CreditSum = creditSum;
    }
}
