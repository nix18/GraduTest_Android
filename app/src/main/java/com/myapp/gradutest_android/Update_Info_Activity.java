package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class Update_Info_Activity extends AppCompatActivity {

    private AppCompatActivity thisActivity;
    private String user_profile_new;
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_update__info);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);
        thisActivity=this;
        mmkv=MMKV.defaultMMKV();
        TextView user_name=findViewById(R.id.user_name_input_update_info);
        TextView user_profile=findViewById(R.id.user_profile_input_update_info);
        TextView user_pwd=findViewById(R.id.user_pwd_input_update_info);
        Button back_btn=findViewById(R.id.back_btn_update_info);
        Button update_info_btn=findViewById(R.id.update_info_btn_update_info);
        ImageView imageView = findViewById(R.id.img_back_update_info);
        imageView.setOnClickListener(v -> finish());
        user_name.setText(mmkv.decodeString("user_name",""));
        user_profile.setHint(mmkv.decodeString("user_profile",""));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=thisActivity.getString(R.string.host)+"/updateUser?uid="+
                        mmkv.decodeInt("uid",0)+"&token="+mmkv.decodeString("user_token","")+
                        "&uprofile="+user_profile.getText()+"&upwd="+user_pwd.getText();
                user_profile_new= user_profile.getText().toString();
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(updateInfoHandler,url)).start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler updateInfoHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","logOutHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            Toast toast;
            if(code == 0){
                toast = Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT);
                if(user_profile_new.length()!=0){
                    mmkv.encode("user_profile",user_profile_new);
                }
            }else {
                toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
            }
            toast.show();
        }
    };
}