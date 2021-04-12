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

import java.util.ArrayList;

public class SquareRecyclerViewAdapter extends RecyclerView.Adapter<SquareRecyclerViewAdapter.ViewHolder> implements MyRecyclerViewAdapter {

    private ArrayList<GoodHabit> localDataSet;
    private MyRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView habit_name;
        private final TextView habit_heat;
        private final TextView hid;

        public ViewHolder(View view) {
            super(view);
            habit_name = (TextView) view.findViewById(R.id.habit_name_textview_fm_square);
            habit_heat = (TextView)view.findViewById(R.id.habit_heat_textview_fm_square);
            hid = (TextView) view.findViewById(R.id.text_hid_fm_square);
        }

        public TextView getHabit_name() {
            return habit_name;
        }

        public TextView getHabit_heat() {
            return habit_heat;
        }

        public TextView getHid(){
            return hid;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SquareRecyclerViewAdapter(ArrayList<GoodHabit> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.textview_fm_square, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(localDataSet.get(position).getHid()!=null) {
            viewHolder.getHabit_name().setText(localDataSet.get(position).getHabit_name());
            viewHolder.getHabit_heat().setText("热度："+localDataSet.get(position).getHabit_heat().toString());
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

    public void setOnItemClickListener(MyRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

