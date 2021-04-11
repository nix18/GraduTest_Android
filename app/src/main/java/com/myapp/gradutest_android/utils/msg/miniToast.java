package com.myapp.gradutest_android.utils.msg;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class miniToast {
    public static void Toast(Context context, String msg){
        Toast.makeText(context, msg,
                Toast.LENGTH_SHORT).show();
    }
    public static AlertDialog getDialog(Context context, String title, String msg){
        return new AlertDialog.Builder(context).setTitle(title).setMessage(msg).create();
    }
}
