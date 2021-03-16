package com.myapp.gradutest_android.utils.net;

import com.google.gson.Gson;


public class toJson {
    /**
     * 将Json中的数据装入对象，并返回此对象
     * @param object 对象类
     * @param json 要装入的Json
     * @param <T> 泛型
     * @return 装入完成的对象
     */
    public static <T> T convertToJson(Class<T> object,String json){
        T obj;
        Gson gson=new Gson();
        obj=gson.fromJson(json,object);
        return obj;
    }
}
