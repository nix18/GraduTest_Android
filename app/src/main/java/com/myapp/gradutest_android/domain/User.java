package com.myapp.gradutest_android.domain;

/*
用户类
 */
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

    public String getUid() {
        return uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_profile='" + user_profile + '\'' +
                ", user_token='" + user_token + '\'' +
                '}';
    }
}
