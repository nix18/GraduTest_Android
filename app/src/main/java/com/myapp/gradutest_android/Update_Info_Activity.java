package com.myapp.gradutest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Update_Info_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__info);
        TextView user_name=findViewById(R.id.user_name_input_update_info);
        TextView user_profile=findViewById(R.id.user_profile_input_update_info);
        TextView user_pwd=findViewById(R.id.user_pwd_input_update_info);
        Button back_btn=findViewById(R.id.back_btn_update_info);
        SharedPreferences sp=getSharedPreferences("loginToken", Context.MODE_PRIVATE);
        user_name.setText(sp.getString("user_name",""));
        user_profile.setHint(sp.getString("user_profile",""));
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}