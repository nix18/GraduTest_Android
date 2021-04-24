package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.myapp.gradutest_android.domain.MyMessage;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.toJson;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;

public class Sign_In_Activity extends AppCompatActivity {

    private ImageView back_btn;
    private ImageView more_info_btn;
    private CardView sign_in_btn;
    private EditText user_name;
    private EditText user_profile;
    private EditText user_pwd;
    private RadioButton rule_checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        statusBarUtils.patchFullScreen(this);
        setContentView(R.layout.activity_sign__in);
        initView();
    }

    public void initView(){
        back_btn = findViewById(R.id.img_back_sign_in);
        more_info_btn = findViewById(R.id.img_more_info_sign_in);
        sign_in_btn = findViewById(R.id.sign_in_btn_sign_in);
        user_name=findViewById(R.id.user_name_input_sign_in);
        user_profile=findViewById(R.id.user_profile_input_sign_in);
        user_pwd=findViewById(R.id.user_pwd_input_sign_in);
        rule_checked=findViewById(R.id.rule_checked_sign_in);
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Sign_In_Activity.this,"更多信息","请按照规则输入注册信息").show();
            }
        });
        sign_in_btn.setOnClickListener(v -> {
            if(user_name.getText().length() == 0){
                user_name.requestFocus();
                miniToast.Toast(this,"用户名不能为空");
            }else if(user_name.getText().toString().contains("WX_")){
                user_name.requestFocus();
                miniToast.Toast(this,"用户名不合法");
            }
            else if(user_profile.getText().length() == 0){
                user_profile.requestFocus();
                miniToast.Toast(this,"用户简介不能为空");
            }
            else if(user_pwd.getText().length() < 8 || user_pwd.getText().length() > 20){
                user_pwd.requestFocus();
                miniToast.Toast(this,"密码不符合要求");
            }
            else if(rule_checked.isChecked()){
                String url=Sign_In_Activity.this.getString(R.string.host)+"/register?uname="+user_name.getText()+"&uprofile="+user_profile.getText()+"&upwd="+user_pwd.getText();
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(signInHandler,url,1)).start();
            }else {
                miniToast.Toast(this,"请先阅读并同意用户协议");
            }
        });
    }

    @SuppressLint("HandlerLeak")
    /*
      消息处理
     */
    Handler signInHandler =new Handler() {
    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        Log.i("myLog","Handler执行");
        Bundle data = msg.getData();
        String val = data.getString("value");
        MyMessage message = toJson.jsonToObj(MyMessage.class,val);
        Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), message.getMsg(), Snackbar.LENGTH_LONG)
                .setAction("返回登录", v -> finish());
        snackbar.show();
        }
    };
}