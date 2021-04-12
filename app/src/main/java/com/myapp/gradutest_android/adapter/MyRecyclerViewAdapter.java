package com.myapp.gradutest_android.adapter;

import android.view.View;

import com.myapp.gradutest_android.domain.GoodHabit;


//通过此接口统一调用RecyclerViewAdapter的方法
public interface MyRecyclerViewAdapter {
    public void add(GoodHabit goodHabit, int position);
    public void remove(int position);
    public int getItemCount();
    public void setOnItemClickListener(OnItemClickListener listener);
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
