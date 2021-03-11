package com.myapp.gradutest_android.domain;

/*
通知信息类
 */
public class MyMessage {
    protected Integer code;
    protected String Msg;

    public MyMessage(Integer code, String msg) {
        this.code = code;
        Msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code=" + code +
                ", Msg='" + Msg + '\'' +
                '}';
    }
}
