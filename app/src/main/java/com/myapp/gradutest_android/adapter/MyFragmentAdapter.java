package com.myapp.gradutest_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.myapp.gradutest_android.MainActivity;
import com.myapp.gradutest_android.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MyFragmentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context mContext;

    public MyFragmentAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.background, data);
        mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NotNull BaseViewHolder helper, String item) {
        if(Objects.equals(item, "我的")){
            LinearLayout mainLinerLayout = (LinearLayout) helper.getView(R.id.background);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.fragment__my, null);
            mainLinerLayout.addView(view);
/*            TextView tvContent=helper.getView(R.id.output);
            tvContent.setText("Pager"+item);*/
        }
    }
}
