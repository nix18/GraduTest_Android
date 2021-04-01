package com.myapp.gradutest_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.tencent.mmkv.MMKV;

public class Add_Habit_Activity extends AppCompatActivity {

    private EditText habit_name;
    private EditText habit_content;
    private EditText habit_category;
    private Button back_btn;
    private Button submit_btn;
    private ImageView imageView;
    private MMKV mmkv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__habit);
        initView();
        mmkv = MMKV.defaultMMKV();
        imageView.setOnClickListener(v -> finish());
        back_btn.setOnClickListener(v -> finish());
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
                    new Thread(networkTask.setParam(addHabitHandler,url)).start();
                }
            }
        });
    }

    protected void initView(){
        habit_name = findViewById(R.id.input_habit_name_add_habit);
        habit_content = findViewById(R.id.input_habit_content_add_habit);
        habit_category = findViewById(R.id.input_habit_category_add_habit);
        back_btn = findViewById(R.id.back_btn_add_habit);
        submit_btn = findViewById(R.id.submit_btn_add_habit);
        imageView = findViewById(R.id.img_add_habit);
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