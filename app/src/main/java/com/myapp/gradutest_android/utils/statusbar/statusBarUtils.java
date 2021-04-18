package com.myapp.gradutest_android.utils.statusbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.myapp.gradutest_android.R;

public class statusBarUtils {

    //设置Activity对应的顶部状态栏的颜色
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置Dialog对应的顶部状态栏的颜色
    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void patchFullScreen(Activity activity){
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        statusBarUtils.setWindowStatusBarColor(activity, R.color.white);
    }

    public static int getScreenWidth(Activity activity){
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity activity){
        WindowManager wm = activity.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }
}