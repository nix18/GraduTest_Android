package com.myapp.gradutest_android.utils.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * 将Json转换为对象
 */
public class toJson {
    /**
     * 将Json中的数据装入对象，并返回此对象
     * @param object 对象类
     * @param json 要装入的Json
     * @param <T> 泛型
     * @return 装入完成的对象
     */
    public static <T> T jsonToObj(Class<T> object,String json){
        T obj;
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        obj=gson.fromJson(json,object);
        return obj;
    }

    /**
     * 将Json中的数据装入对象List，并返回此对象List
     * @param object 对象类
     * @param json 要装入的Json
     * @param <T> 泛型
     * @return 装入完成的对象List
     */
    public static <T> ArrayList<T> jsonToObjs(Class<T> object,String json){
        ArrayList<T> objs = new ArrayList<T>();
        Log.i("myLog","开始转换");
        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        for(JsonElement data:jsonArray) {
            objs.add(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(data, object));
        }
        return objs;
    }
}
