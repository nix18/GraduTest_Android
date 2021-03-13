package com.myapp.gradutest_android.domain;

import lombok.Data;

/*
通知信息类
 */
@Data
public class MyMessage {
    protected Integer code;
    protected String Msg;

    public MyMessage(Integer code, String msg) {
        this.code = code;
        Msg = msg;
    }
}
