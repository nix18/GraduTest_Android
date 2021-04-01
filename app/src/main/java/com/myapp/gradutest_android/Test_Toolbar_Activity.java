package com.myapp.gradutest_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Test_Toolbar_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView img;//点击该头像 弹出抽屉
    private ImageView headImg;//抽屉里的头像
    private TextView nickName;//用户名


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不要标题栏了（因为标题栏的 NavigationIcon不容易改，所以我们主页面自己画了）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test__toolbar);
        findViews();
        /**
         * 头像的动态设置，因为每个用户的头像都不一样，所以这不能在xml里写死
         * headView非常有必要，因为Android的findViewById一般找不到更深层布局的视图
         * 所以要想动态修改抽屉里头部（head_view.xml）的控件，必须通过headView
         */
        View headView = navigationView.getHeaderView(0);//get hedaView
        headImg = headView.findViewById(R.id.userheadimg_head_view);//不通过headView找不到
        headImg.setImageResource(R.mipmap.ic_launcher_round);
        //设置昵称
        nickName = headView.findViewById(R.id.nickName);
        nickName.setText("SkySong");

        //点击头像划出测边框
        img.setOnClickListener(v -> {
            if(drawerLayout.isDrawerOpen(navigationView)){
                drawerLayout.closeDrawer(navigationView);
            }else{
                drawerLayout.openDrawer(navigationView);
            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item_personal://个人
                    Toast.makeText(this,"个人页面",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(navigationView);
                    break;
                case R.id.item_setting://设置
                    Toast.makeText(this,"设置页面",Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(navigationView);
                    break;
            }
            return true;
        });

    }

    private void findViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        img = findViewById(R.id.img);
    }
}