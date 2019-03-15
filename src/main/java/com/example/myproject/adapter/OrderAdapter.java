package com.example.myproject.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myproject.OrderActivity;
import com.example.myproject.R;
import com.example.myproject.bean.IndentBean;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<IndentBean.indentBean, BaseViewHolder> {

    ImageView picture;
    Context context;
    public OrderAdapter(int layoutResId, @Nullable List<IndentBean.indentBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, IndentBean.indentBean item) {

        String[] time = item.time.split(" ");

        picture = helper.getView(R.id.product_png);
        Glide.with(context)
                .load(item.picture)
                .into(picture);
        helper.setText(R.id.product_name,item.name);
        helper.setText(R.id.price_before,item.price);
        helper.setText(R.id.price_now,item.my_price);
        helper.setText(R.id.time_date,time[0]);
        helper.setText(R.id.time_clock,time[1]);
        helper.addOnClickListener(R.id.radioButton_topay);
        helper.addOnClickListener(R.id.radioButton_exit);

        if (OrderActivity.type==1) {
            helper.getView(R.id.radioButton_topay).setVisibility(View.INVISIBLE);
            helper.getView(R.id.radioButton_exit).setVisibility(View.INVISIBLE);
            helper.getView(R.id.wait).setVisibility(View.INVISIBLE);
        }else{
            helper.getView(R.id.radioButton_topay).setVisibility(View.VISIBLE);
            helper.getView(R.id.wait).setVisibility(View.VISIBLE);
            helper.getView(R.id.radioButton_exit).setVisibility(View.VISIBLE);
        }


    }

}
