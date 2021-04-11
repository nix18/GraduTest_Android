package com.myapp.gradutest_android;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class Update_Info_Activity extends AppCompatActivity {

    private String user_profile_new;
    private MMKV mmkv;
    private TextView user_name;
    private TextView user_profile;
    private TextView user_pwd;
    private Button update_info_btn;
    private ImageView back_btn;
    private ImageView more_info_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_update__info);
        mmkv=MMKV.defaultMMKV();
        initView();
        user_name.setText(mmkv.decodeString("user_name",""));
        user_profile.setHint(mmkv.decodeString("user_profile",""));
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Update_Info_Activity.this,"更多信息"," 请按照要求填写个人信息\n 注：用户名暂时无法更改").show();
            }
        });
        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.host)+"/updateUser?uid="+
                        mmkv.decodeInt("uid",0)+"&token="+mmkv.decodeString("user_token","")+
                        "&uprofile="+user_profile.getText()+"&upwd="+user_pwd.getText();
                user_profile_new= user_profile.getText().toString();
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(updateInfoHandler,url,1)).start();
            }
        });
    }

    public void initView(){
        user_name=findViewById(R.id.user_name_input_update_info);
        user_profile=findViewById(R.id.user_profile_input_update_info);
        user_pwd=findViewById(R.id.user_pwd_input_update_info);
        update_info_btn=findViewById(R.id.update_info_btn_update_info);
        back_btn = findViewById(R.id.img_back_update_info);
        more_info_btn = findViewById(R.id.img_more_info_update_info);
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