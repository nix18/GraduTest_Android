package com.myapp.gradutest_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.offLineMode;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

//程序入口
public class Log_In_Activity extends AppCompatActivity {

    private TextView sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        statusBarUtils.patchFullScreen(this);

        //初始化MMKV共享存储
        MMKV.initialize(this);

        requestPermission();

        //检查token是否有效
        MMKV mmkv=MMKV.defaultMMKV();
        if(mmkv.decodeInt("uid",0) != -1) {
            String url = this.getString(R.string.host) + "/chkToken?uid=" + mmkv.decodeInt("uid", 0) + "&token=" + mmkv.decodeString("user_token", "");
            networkTask networkTask = new networkTask();
            new Thread(networkTask.setParam(chkTokenHandler, url,1)).start();
        }else {
            offLineMode.setOffLineMode(true);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_log__in);
        //装入登录Fragment
        FragmentTransaction transaction;
        Fragment_Log_In fragment_logIn =new Fragment_Log_In();
        transaction=getSupportFragmentManager().beginTransaction();//无须使用FragmentManager
        transaction.add(R.id.login_container, fragment_logIn);
        transaction.commit();
        initView();
    }

    protected void initView(){
        sign_in = findViewById(R.id.sign_in_btn_log_in);
        sign_in.setOnClickListener(v -> {
            Intent intent=new Intent(Log_In_Activity.this,Sign_In_Activity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("HandlerLeak")
    Handler chkTokenHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                super.handleMessage(msg);
                Log.i("myLog", "chkTokenHandler执行");
                Bundle data = msg.getData();
                String val = data.getString("value");
                int code = getJson.getStatusCode(val);
                if (code == 0) {
                    Intent intent = new Intent(Log_In_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e){
                offLineMode.setOffLineMode(true);
                miniToast.Toast(getApplicationContext(),"无法连接网络，自动进入离线模式");
                Intent intent=new Intent(Log_In_Activity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    };

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Log.i("myLog", "获取授权");
            miniToast.Toast(this,"请授权必要权限，否则软件可能无法使用");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        }
    }
}