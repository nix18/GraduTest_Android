package com.myapp.gradutest_android.utils.msg;

import android.content.Context;
import android.widget.Toast;

public class miniToast {
    public static void Toast(Context context, String msg){
        Toast.makeText(context, msg,
                Toast.LENGTH_SHORT).show();
    }
}
