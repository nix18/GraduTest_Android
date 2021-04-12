package com.myapp.gradutest_android.domain;

import java.util.Date;

import lombok.Data;

@Data
public class CreditDetail {
    private Integer id;
    private Integer uid;
    private Integer credit_num;
    private String credit_desc;
    private Date credit_time;

    public CreditDetail() {
    }

    public CreditDetail(Integer id, Integer uid, Integer credit_num, String credit_desc, Date credit_time) {
        this.id = id;
        this.uid = uid;
        this.credit_num = credit_num;
        this.credit_desc = credit_desc;
        this.credit_time = credit_time;
    }
}
