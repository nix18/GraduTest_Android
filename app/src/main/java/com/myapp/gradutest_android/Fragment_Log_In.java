package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.myapp.gradutest_android.domain.User;
import com.myapp.gradutest_android.utils.msg.miniToast;
import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.myapp.gradutest_android.utils.net.offLineMode;
import com.myapp.gradutest_android.utils.net.toJson;
import com.tencent.mmkv.MMKV;

import java.util.Objects;

public class Fragment_Log_In extends Fragment {

    private View view;
    private CardView log_in;
    private TextView log_in_without_account;
    private TextView forget_pwd;
    private EditText user_name;
    private EditText user_pwd;
    private RadioButton rule_checked_my;

    public Fragment_Log_In() {
        // Required empty public constructor
    }


    public static Fragment_Log_In newInstance() {
        return new Fragment_Log_In();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment__log_in, container, false);
        initView();
        return view;
    }

    public void initView(){
        log_in = view.findViewById(R.id.log_in_btn_fm_log_in);
        log_in_without_account = view.findViewById(R.id.log_in_without_account_btn);
        forget_pwd = view.findViewById(R.id.forget_pwd_btn_fm_log_in);
        user_name=view.findViewById(R.id.user_name_input_fm_log_in);
        user_pwd=view.findViewById(R.id.user_pwd_input_fm_log_in);
        rule_checked_my=view.findViewById(R.id.rule_checked_fm_log_in);
    }

    @Override
    public void onResume() {
        super.onResume();
        log_in.setOnClickListener(v -> {
            if(user_name.getText().length() == 0) {
                user_name.requestFocus();
                miniToast.Toast(getContext(),"?????????????????????");
            }
            else if(user_pwd.getText().length() < 8 || user_pwd.getText().length() > 20) {
                user_pwd.requestFocus();
                miniToast.Toast(getContext(),"?????????????????????");
            }
            else if(rule_checked_my.isChecked()){
                String url= Objects.requireNonNull(getActivity()).getString(R.string.host)+"/login?uname="+user_name.getText()+"&upwd="+user_pwd.getText();
                Log.i("myTag","????????????");
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(loginHandler,url,1)).start();
            }else {
                miniToast.Toast(getContext(),"?????????????????????????????????");
            }
        });
        log_in_without_account.setOnClickListener(v -> {
            offLineMode.setOffLineMode(true);
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        });
        forget_pwd.setOnClickListener(v -> miniToast.Toast(getContext(),"?????????????????? admin@admin.com ????????????"));
    }

    @SuppressLint("HandlerLeak")
        /*
          ??????????????????
         */
        Handler loginHandler=new Handler() {
        @SuppressLint("ApplySharedPref")
        @Override
        public void handleMessage(@NonNull Message msg) {
            try {
                super.handleMessage(msg);
                Log.i("myLog","loginHandler??????");
                Bundle data = msg.getData();
                String val = data.getString("value");
                int code= getJson.getStatusCode(val);

                //????????????
                Toast.makeText(getContext(), code==0?"????????????":"????????????",
                        Toast.LENGTH_SHORT).show();

                //??????MMKV??????Token
                MMKV mmkv=MMKV.defaultMMKV();
                if(code == 0){
                    //??????????????????
                    User user= toJson.jsonToObj(User.class,val);
                    mmkv.encode("uid", user.getUid());
                    mmkv.encode("user_name", user.getUser_name());
                    mmkv.encode("user_profile", user.getUser_profile());
                    mmkv.encode("user_token", user.getUser_token());
                    //???????????????
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }
            }catch (Exception e){
                Intent intent=new Intent(getActivity(),Screen_Error_Activity.class);
                startActivity(intent);
            }
        }
    };
}