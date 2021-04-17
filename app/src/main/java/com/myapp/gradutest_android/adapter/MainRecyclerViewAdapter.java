package com.myapp.gradutest_android.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.HabitBundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> implements MyRecyclerViewAdapter<HabitBundle>{

    private ArrayList<HabitBundle> localDataSet;
    private MyRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 链接你正在使用的xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView habit_name;
        private final TextView persist_days;
        private final TextView target_days;
        private final TextView rhid;
        private final TextView user_config;
        private final TextView habit_content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            habit_name = itemView.findViewById(R.id.habit_name_textview_main);
            persist_days = itemView.findViewById(R.id.persist_days_textview_main);
            target_days = itemView.findViewById(R.id.target_days_textview_main);
            rhid  = itemView.findViewById(R.id.text_rhid_main);
            user_config = itemView.findViewById(R.id.text_user_config_main);
            habit_content = itemView.findViewById(R.id.text_habit_content_main);
        }

        public TextView getHabit_name() {
            return habit_name;
        }

        public TextView getPersist_days() {
            return persist_days;
        }

        public TextView getTarget_days() {
            return target_days;
        }

        public TextView getRhid() {
            return rhid;
        }

        public TextView getUser_config() {
            return user_config;
        }

        public TextView getHabit_content() {
            return habit_content;
        }
    }

    /**
     * 初始化本地数据集
     */
    public MainRecyclerViewAdapter(ArrayList<HabitBundle> dataSet) {
        localDataSet = dataSet;
    }

    /**
     * 创建View
     */
    @NotNull
    @Override
    public MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.textview_main, viewGroup, false);
        return new MainRecyclerViewAdapter.ViewHolder(view);
    }

    /**
     * 替换View中内容
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        //处理ViewHolder中内容
        if(localDataSet.get(position).getRunningHabit() != null) {
            viewHolder.getHabit_name().setText(localDataSet.get(position).getGoodHabit().getHabit_name());
            viewHolder.getPersist_days().setText(localDataSet.get(position).getRunningHabit().getPersist_days().toString());
            viewHolder.getTarget_days().setText(localDataSet.get(position).getRunningHabit().getTarget_days().toString());
            viewHolder.getRhid().setText(localDataSet.get(position).getRunningHabit().getRhid().toString());
            viewHolder.getUser_config().setText(localDataSet.get(position).getRunningHabit().getUser_config());
            viewHolder.getHabit_content().setText(localDataSet.get(position).getGoodHabit().getHabit_content());
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

    public void add(HabitBundle habitBundle, int position) {
        localDataSet.add(position,habitBundle);
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
