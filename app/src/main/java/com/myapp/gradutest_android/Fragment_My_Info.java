package com.myapp.gradutest_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.gradutest_android.utils.net.getJson;
import com.myapp.gradutest_android.utils.net.networkTask;
import com.tencent.mmkv.MMKV;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_My_Info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_My_Info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentActivity thisActivity;

    public Fragment_My_Info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_My_Info.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_My_Info newInstance(String param1, String param2) {
        Fragment_My_Info fragment = new Fragment_My_Info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity=this.getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 通过this.getActivity()获取主activity中的方法 通过view来改动布局
        View view=inflater.inflate(R.layout.fragment__my_info, container, false);
        TextView user_name=view.findViewById(R.id.user_name_container_fm_my_info);
        TextView user_profile=view.findViewById(R.id.user_profile_container_fm_my_info);
        Button log_out_btn=view.findViewById(R.id.log_out_btn_fm_my_info);
        Button update_info_btn=view.findViewById(R.id.update_info_btn_fm_my_info);
        MMKV mmkv=MMKV.defaultMMKV();
        String uName=mmkv.decodeString("user_name","defaultName");
        String uProfile=mmkv.decodeString("user_profile","");
        user_name.setText(uName);
        user_profile.setText(uProfile);

        //在fragment里设定点击动作须用setOnClickListener
        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=thisActivity.getString(R.string.host)+"/logout?uid="+mmkv.decodeInt("uid",0)+
                        "&token="+mmkv.decodeString("user_token","");
                networkTask networkTask=new networkTask();
                new Thread(networkTask.setParam(logOutHandler,url)).start();
            }
        });
        update_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(thisActivity,Update_Info_Activity.class);
                getActivity().startActivityForResult(intent,1);
            }
        });
        return view;
    }


    @SuppressLint("HandlerLeak")
    Handler logOutHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("myLog","logOutHandler执行");
            Bundle data = msg.getData();
            String val = data.getString("value");
            int code= getJson.getStatusCode(val);
            if(code == 0){

                //清除MMKV
                MMKV mmkv=MMKV.defaultMMKV();
                mmkv.clearAll();

                //返回登录界面
                Toast toast=Toast.makeText(thisActivity.getApplicationContext(), "登出成功", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent=new Intent(thisActivity,Log_In_Activity.class);
                startActivity(intent);
                thisActivity.finish();
            }else {
                Toast toast=Toast.makeText(thisActivity.getApplicationContext(), "登出失败", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
}