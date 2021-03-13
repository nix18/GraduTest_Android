package com.myapp.gradutest_android.domain;

import lombok.Data;

/*
用户类
 */
@Data
public class User {
    protected String uid;
    protected String user_name;
    protected String user_profile;
    protected String user_token;

    public User(String uid, String user_name, String user_profile, String user_token) {
        this.uid = uid;
        this.user_name = user_name;
        this.user_profile = user_profile;
        this.user_token = user_token;
    }
}
