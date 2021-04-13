package com.myapp.gradutest_android.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.CreditDetail;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CreditDetailRecyclerViewAdapter extends RecyclerView.Adapter<CreditDetailRecyclerViewAdapter.ViewHolder> implements MyRecyclerViewAdapter<CreditDetail>{
    private ArrayList<CreditDetail> localDataSet;
    private MyRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView credit_desc;
        private final TextView credit_change;
        private final TextView credit_time;

        public ViewHolder(View view) {
            super(view);
            credit_desc = view.findViewById(R.id.credit_desc_credit_detail);
            credit_change = view.findViewById(R.id.credit_change_credit_detail);
            credit_time = view.findViewById(R.id.credit_time_credit_detail);
        }

        public TextView getCredit_desc() {
            return credit_desc;
        }

        public TextView getCredit_change() {
            return credit_change;
        }

        public TextView getCredit_time() {
            return credit_time;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CreditDetailRecyclerViewAdapter(ArrayList<CreditDetail> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public CreditDetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.textview_credit_detail, viewGroup, false);

        return new CreditDetailRecyclerViewAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(CreditDetailRecyclerViewAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(localDataSet.get(position).getId()!=null) {
            viewHolder.getCredit_desc().setText(localDataSet.get(position).getCredit_desc());
            if(localDataSet.get(position).getCredit_num() >= 0){
                String credit_change_text = "+"+localDataSet.get(position).getCredit_num().toString();
                viewHolder.getCredit_change().setText(credit_change_text);
                viewHolder.getCredit_change().setTextColor(Color.parseColor("#DC143C"));
            }else {
                viewHolder.getCredit_change().setText(localDataSet.get(position).getCredit_num().toString());
                viewHolder.getCredit_change().setTextColor(Color.parseColor("#32CD32"));
            }
            viewHolder.getCredit_time().setText(new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(localDataSet.get(position).getCredit_time()));
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

    public void add(CreditDetail creditDetail, int position) {
        localDataSet.add(position,creditDetail);
        notifyItemInserted(position);
    }

    public void remove(int position){
        localDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(CreditDetailRecyclerViewAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
