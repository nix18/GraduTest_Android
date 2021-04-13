package com.myapp.gradutest_android.adapter;

import android.view.View;


//通过此接口统一调用RecyclerViewAdapter的方法，使用泛型统一调用
public interface MyRecyclerViewAdapter<T> {
    public void add(T t, int position);
    public void remove(int position);
    public int getItemCount();
    public void setOnItemClickListener(OnItemClickListener listener);
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
