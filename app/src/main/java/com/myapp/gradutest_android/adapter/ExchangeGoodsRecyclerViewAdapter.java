package com.myapp.gradutest_android.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.myapp.gradutest_android.R;
import com.myapp.gradutest_android.domain.Goods;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExchangeGoodsRecyclerViewAdapter extends RecyclerView.Adapter<ExchangeGoodsRecyclerViewAdapter.ViewHolder> implements MyRecyclerViewAdapter<Goods> {
    private ArrayList<Goods> localDataSet;
    private MyRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;

    /**
     * 链接你正在使用的xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView goods_name;
        private final ImageView goods_pic;
        private final TextView goods_price;
        private final TextView goods_stock;
        private final TextView gid;

        public ViewHolder(View view) {
            super(view);
            gid = view.findViewById(R.id.text_gid_exchange_goods);
            goods_name = view.findViewById(R.id.goods_name_exchange_goods);
            goods_pic = view.findViewById(R.id.goods_pic_exchange_goods);
            goods_price = view.findViewById(R.id.goods_price_exchange_goods);
            goods_stock = view.findViewById(R.id.goods_stock_exchange_goods);
        }

        public TextView getGoods_name() {
            return goods_name;
        }

        public ImageView getGoods_pic() {
            return goods_pic;
        }

        public TextView getGoods_price() {
            return goods_price;
        }

        public TextView getGoods_stock() {
            return goods_stock;
        }

        public TextView getGid() {
            return gid;
        }
    }

    /**
     * 初始化本地数据集
     */
    public ExchangeGoodsRecyclerViewAdapter(ArrayList<Goods> dataSet) {
        localDataSet = dataSet;
    }

    /**
     * 创建View
     */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.textview_exchange_goods, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * 替换View中内容
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        //处理ViewHolder中内容
        if(localDataSet.get(position).getGid()!=null) {
            viewHolder.getGoods_name().setText(localDataSet.get(position).getGoods_name());
            viewHolder.getGoods_price().setText(localDataSet.get(position).getGoods_price().toString());
            viewHolder.getGoods_pic().setImageBitmap(localDataSet.get(position).getGoodsPic());
            viewHolder.getGoods_stock().setText(localDataSet.get(position).getGoods_stock().toString());
            viewHolder.getGid().setText(localDataSet.get(position).getGid().toString());
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

    public void add(Goods goods, int position) {
        localDataSet.add(position,goods);
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
