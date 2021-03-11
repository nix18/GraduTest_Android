package com.myapp.gradutest_android.utils.net;

import com.google.gson.Gson;


public class toJson {
    public static <T> T convertToJson(Class<T> object,String json){
        T obj;
        Gson gson=new Gson();
        obj=gson.fromJson(json,object);
        return obj;
    }
}
