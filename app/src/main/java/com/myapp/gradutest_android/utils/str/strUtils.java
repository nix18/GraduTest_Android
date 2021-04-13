package com.myapp.gradutest_android.utils.str;

import java.util.ArrayList;

public class strUtils {
    public static ArrayList<String> tuple2arrayList(String string){
        int i = 0; //字符指针
        ArrayList<String> list = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        while (i<string.length()){
            if(string.charAt(i) == '"'){
                i++;
                while (string.charAt(i) != '"') {
                    temp.append(string.charAt(i));
                    i++;
                }
                list.add(temp.toString());
                temp = temp.delete(0,temp.length());
            }
            i++;
        }
        return list;
    }
}
