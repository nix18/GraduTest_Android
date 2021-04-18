package com.myapp.gradutest_android.utils.habit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.myapp.gradutest_android.utils.msg.miniToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class habitShareUtils {

    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";

    // 判断是否安装指定app
    public static boolean isInstallApp(Context context, String app_package){
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (app_package.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    //直接使用系统API分享图片到QQ（图片不能超过512KB）
    public static void shareImageToQQ(Bitmap bitmap, Activity mActivity) {
        if (habitShareUtils.isInstallApp(mActivity,habitShareUtils.PACKAGE_MOBILE_QQ)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        mActivity.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");

                ComponentName componentName = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");

                shareIntent.setComponent(componentName);

                mActivity.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (Exception e) {
                e.printStackTrace();
                miniToast.Toast(mActivity,"分享到QQ失败");
            }
        }
    }

    //直接使用系统API分享图片到微信（图片不能超过512KB）
    public static void shareImageToWX(Bitmap bitmap, Activity mActivity) {
        if (habitShareUtils.isInstallApp(mActivity,habitShareUtils.PACKAGE_WECHAT)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        mActivity.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");

                ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");

                shareIntent.setComponent(componentName);

                mActivity.startActivity(Intent.createChooser(shareIntent, "Share"));
            } catch (Exception e) {
                e.printStackTrace();
                miniToast.Toast(mActivity,"分享到微信失败");
            }
        }
    }

    //生成图片
    public static Bitmap genBitmap(View v, int width, int height) {
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }

    public static void saveBitmap(Bitmap bitmap, String fileName) {
        FileOutputStream fos;
        try {
            File root = Environment.getExternalStorageDirectory();
            Log.i("myLog","路径"+root.getAbsolutePath());
            File file = new File(root, fileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
