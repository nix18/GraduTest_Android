package com.myapp.gradutest_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.myapp.gradutest_android.utils.habit.habitShareUtils;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

public class Share_Img_Activity extends AppCompatActivity {

    protected Intent intent;
    protected Bitmap share_img;
    protected ImageView bg_img;
    protected ImageView share_btn;
    protected TextView user_name;
    protected TextView habit_name;
    protected TextView persist_days;
    protected String user_name_text;
    protected String habit_name_text;
    protected String persist_days_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__img);
        statusBarUtils.patchFullScreen(this);
        intent = getIntent();
        user_name_text = intent.getStringExtra("user_name");
        habit_name_text = intent.getStringExtra("habit_name");
        persist_days_text = intent.getStringExtra("persist_days");
        initView();
        user_name.setText(user_name_text);
        habit_name.setText(habit_name_text);
        persist_days.setText(persist_days_text);
        //随机设置背景图
        int sel_pic = (int)(1+Math.random()*(9)); //生成1-9随机数
        switch (sel_pic){
            case 1:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_1));
                break;
            case 2:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_2));
                break;
            case 3:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_3));
                break;
            case 4:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_4));
                break;
            case 5:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_5));
                break;
            case 6:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_6));
                break;
            case 7:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_7));
                break;
            case 8:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_8));
                break;
            default:
                bg_img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.bg_9));
                break;
        }
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = miniToast.getDialog(Share_Img_Activity.this,"分享到","");
                alertDialog.setButton("QQ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        habitShareUtils.shareImageToQQ(share_img,Share_Img_Activity.this);
                    }
                });
                alertDialog.setButton2( "微信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        habitShareUtils.shareImageToWX(share_img,Share_Img_Activity.this);
                    }
                });
                alertDialog.show();
            }
        });
        //获取截图
        share_img = habitShareUtils.genBitmap(getWindow().getDecorView(),
                statusBarUtils.getScreenWidth(this),statusBarUtils.getScreenHeight(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        share_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.___51));
    }

    public void initView(){
        bg_img = findViewById(R.id.img_share_img);
        share_btn = findViewById(R.id.share_btn_share_img);
        user_name = findViewById(R.id.user_name_share_img);
        habit_name = findViewById(R.id.habit_name_share_img);
        persist_days = findViewById(R.id.persist_days_share_img);
    }
}