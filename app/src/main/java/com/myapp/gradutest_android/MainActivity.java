package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myapp.gradutest_android.adapter.ScreenSlidePagerAdapter;
import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private List<String> evenList=new ArrayList<>();
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabhost_main);
        viewPager = findViewById(R.id.viewpager_main);
        pagerAdapter=new ScreenSlidePagerAdapter(this);
        evenList.add("主页");
        evenList.add("广场");
        evenList.add("我的");
        viewPager.setAdapter(pagerAdapter);
        TabLayoutMediator  tabLayoutMediator=  new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(evenList.get(position)));
        tabLayoutMediator.attach();

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


    @SuppressLint("HandlerLeak")
    /*
      消息处理
     */
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","Handler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            User user=toJson.convertToJson(User.class,val);
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), code==0?"登录成功":"登录失败", Snackbar.LENGTH_LONG)
                    .setAction("确定", v -> {
                        //
                    });
            snackbar.show();
            Log.i("myLog",user.getUser_name());
            TextView output = findViewById(R.id.output);
            output.setText(user.toString());
        }
    };

    /*
    登录点击动作
     */
    public void log_in_onclick(View view){
        EditText user_name=findViewById(R.id.user_name_input_my);
        EditText user_pwd=findViewById(R.id.user_pwd_input_my);
        String url=this.getString(R.string.host)+"/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
        networkTask networkTask=new networkTask();
        new Thread(networkTask.setParam(handler,url)).start();
    }

    public void sign_in_onclick(View view){
        Intent intent=new Intent(MainActivity.this,Sign_In_Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}