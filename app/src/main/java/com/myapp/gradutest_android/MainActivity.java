package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myapp.gradutest_android.adapter.ScreenSlidePagerAdapter;
import com.myapp.gradutest_android.utils.net.offLineMode;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private List<String> evenList=new ArrayList<>();
    private FragmentStateAdapter pagerAdapter;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //抽屉部分
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView headImg;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_main_with_drawer);
        tabLayout = findViewById(R.id.tabhost_main);
        viewPager = findViewById(R.id.viewpager_main);
        nestedScrollView=findViewById(R.id.nestedScrollView_main);
        nestedScrollView.setFillViewport(true);
        pagerAdapter=new ScreenSlidePagerAdapter(this);
        evenList.add("主页");
        evenList.add("广场");
        evenList.add("我的");
        viewPager.setAdapter(pagerAdapter);
        TabLayoutMediator  tabLayoutMediator=  new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(evenList.get(position)));
        tabLayoutMediator.attach();
        initNav();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }


    protected void initView(){
        drawerLayout = findViewById(R.id.drawer_layout_main_with_drawer);
        navigationView = findViewById(R.id.nav_view_main_with_drawer);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout_main);
        toolbar = findViewById(R.id.toolbar_main);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void initNav(){
        initView();
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        View headView = navigationView.getHeaderView(0);
        headImg = headView.findViewById(R.id.user_img_head_view);//不通过headView找不到
        Glide.with(this).load(R.drawable.ic_app_icon).transform(new CenterCrop()).into(headImg);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if(drawerLayout.isDrawerOpen(navigationView)){
                drawerLayout.closeDrawer(navigationView);
            }else{
                drawerLayout.openDrawer(navigationView);
            }
        });
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item_personal://个人
                    if(!offLineMode.getOffLineMode()){
                    Toast.makeText(this, "个人页面", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, My_Info_Activity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(navigationView);
                }
                    break;
                case R.id.item_setting://设置
                    Toast.makeText(this,"设置页面",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Settings_Activity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(navigationView);
                    break;
            }
            return true;
        });
        navigationView.setItemIconTintList(null);
        toolbar.setOverflowIcon(getDrawable(R.mipmap.icon_add));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.habit_add:
                        if(!offLineMode.getOffLineMode()){
                        Intent intent = new Intent(MainActivity.this, Add_Habit_Activity.class);
                        startActivity(intent);
                    }
                        break;
                    case R.id.habit_by_me:
                        if(!offLineMode.getOffLineMode()){
                        Intent intent = new Intent(MainActivity.this, My_Habits_Activity.class);
                        startActivity(intent);
                    }
                        break;
                    case R.id.habit_all:
                        if(!offLineMode.getOffLineMode()){
                            Intent intent = new Intent(MainActivity.this, All_Habits_Activity.class);
                            startActivity(intent);
                    }
                        break;
                }
                return true;
            }
        });
    }

    /** 创建菜单 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    //重写使下拉栏显示图标
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}