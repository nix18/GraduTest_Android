package com.myapp.gradutest_android.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.GoodHabit;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyHabitsRecyclerViewAdapter extends RecyclerView.Adapter<MyHabitsRecyclerViewAdapter.ViewHolder> implements MyRecyclerViewAdapter<GoodHabit> {

    private ArrayList<GoodHabit> localDataSet;
    private MyRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 链接你正在使用的xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView habit_name;
        private final TextView habit_category;
        private final TextView habit_heat;
        private final TextView habit_create_time;
        private final TextView habit_isvisible;
        private final TextView hid;

        public ViewHolder(View view) {
            super(view);
            habit_name = view.findViewById(R.id.habit_name_textview_my_habits);
            habit_category = view.findViewById(R.id.habit_category_textview_my_habits);
            habit_heat = view.findViewById(R.id.habit_heat_textview_my_habits);
            habit_create_time = view.findViewById(R.id.habit_create_time_textview_my_habits);
            habit_isvisible = view.findViewById(R.id.habit_isvisible_textview_my_habits);
            hid = view.findViewById(R.id.text_hid_fm_square);
        }

        public TextView getHabit_name() {
            return habit_name;
        }

        public TextView getHabit_category() {
            return habit_category;
        }

        public TextView getHabit_create_time() {
            return habit_create_time;
        }

        public TextView getHabit_isvisible() {
            return habit_isvisible;
        }

        public TextView getHabit_heat() {
            return habit_heat;
        }

        public TextView getHid(){
            return hid;
        }
    }

    /**
     * 初始化本地数据集
     */
    public MyHabitsRecyclerViewAdapter(ArrayList<GoodHabit> dataSet) {
        localDataSet = dataSet;
    }

    /**
     * 创建View
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.textview_my_habits, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * 替换View中内容
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //处理ViewHolder中内容
        if(localDataSet.get(position).getHid()!=null) {
            viewHolder.getHabit_name().setText(localDataSet.get(position).getHabit_name());
            viewHolder.getHabit_heat().setText("热度："+localDataSet.get(position).getHabit_heat().toString());
            viewHolder.getHabit_category().setText("分类："+localDataSet.get(position).getHabit_category());
            viewHolder.getHabit_create_time().setText("创建时间："+new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.CHINA).format(localDataSet.get(position).getHabit_create_time()));
            viewHolder.getHabit_isvisible().setText(localDataSet.get(position).getHabit_isvisible()?"通过审核":"等待审核");
            viewHolder.getHid().setText(localDataSet.get(position).getHid().toString());
        }
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, position);
                    return true;
                }
            });
        }
    }

    public void add(GoodHabit goodHabit, int position) {
        localDataSet.add(position,goodHabit);
        notifyItemInserted(position);
    }

    public void remove(int position){
        localDataSet.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void setOnItemClickListener(MyRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 返回数据集大小
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}

