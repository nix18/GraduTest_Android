package com.myapp.gradutest_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 通过this.getActivity()获取主activity中的方法 通过view来改动布局
        View view=inflater.inflate(R.layout.fragment__my__info, container, false);
        TextView user_name=view.findViewById(R.id.user_name_container_my_info);
        TextView user_profile=view.findViewById(R.id.user_profile_container_my_info);
        SharedPreferences sp=this.getActivity().getSharedPreferences("loginToken", Context.MODE_PRIVATE);
        String uName=sp.getString("user_name","defaultName");
        String uProfile=sp.getString("user_profile","");
        user_name.setText(uName);
        user_profile.setText(uProfile);
        return view;
    }
}