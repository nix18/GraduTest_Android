package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.statusbar.statusBarUtils;
import com.tencent.mmkv.MMKV;

public class Add_Habit_Activity extends AppCompatActivity {

    private EditText habit_name;
    private EditText habit_content;
    private EditText habit_category;
    private Button submit_btn;
    private ImageView back_btn;
    private ImageView more_info_btn;
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add__habit);
        statusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initView();
        mmkv = MMKV.defaultMMKV();
        back_btn.setOnClickListener(v -> finish());
        more_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniToast.getDialog(Add_Habit_Activity.this,"更多信息","请填写合法的好习惯内容").show();
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(habit_name.getText().length()==0){
                    habit_name.requestFocus();
                }else if(habit_content.getText().length()==0){
                    habit_content.requestFocus();
                }else{
                    Uri.Builder builder = new Uri.Builder();
                    Log.i("myTag",mmkv.toString());
                    builder.scheme("http")
                            .encodedAuthority(Add_Habit_Activity.this.getString(R.string.host_core))
                            .appendPath("addhabit")
                            .appendQueryParameter("uid", String.valueOf(mmkv.decodeInt("uid",0)))
                            .appendQueryParameter("token", mmkv.decodeString("user_token",""))
                            .appendQueryParameter("hname", String.valueOf(habit_name.getText()))
                            .appendQueryParameter("hcontent",String.valueOf(habit_content.getText()))
                            .appendQueryParameter("hcategory",String.valueOf(habit_category.getText()));
                    String url = builder.build().toString();
                    networkTask networkTask=new networkTask();
                    new Thread(networkTask.setParam(addHabitHandler,url,1)).start();
                }
            }
        });
    }

    protected void initView(){
        habit_name = findViewById(R.id.input_habit_name_add_habit);
        habit_content = findViewById(R.id.input_habit_content_add_habit);
        habit_category = findViewById(R.id.input_habit_category_add_habit);
        submit_btn = findViewById(R.id.submit_btn_add_habit);
        back_btn = findViewById(R.id.img_back_add_habit);
        more_info_btn = findViewById(R.id.img_more_info_add_habit);
    }

    @SuppressLint("HandlerLeak")
    Handler addHabitHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){
                Toast.makeText(Add_Habit_Activity.this,"添加好习惯成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Add_Habit_Activity.this,"添加好习惯失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}