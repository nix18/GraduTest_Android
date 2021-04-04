package com.myapp.gradutest_android.utils.net;

import com.tencent.mmkv.MMKV;

public class offLineMode {
    public static void setOffLineMode(boolean mode){
        MMKV mmkv_sys = MMKV.mmkvWithID("shared_sys");
        mmkv_sys.encode("offLineMode",mode);
    }
    public static boolean getOffLineMode(){
        try {
            MMKV mmkv_sys = MMKV.mmkvWithID("shared_sys");
            boolean mode = mmkv_sys.decodeBool("offLineMode");
            return mode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
