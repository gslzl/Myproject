package com.example.myproject.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myproject.R;
import com.example.myproject.bean.BannerBean;

import java.util.List;

public class SearchAdapter extends BaseQuickAdapter<BannerBean.bannerBean, BaseViewHolder> {

        ImageView picture;
        Context context ;
public SearchAdapter(int layoutResId, @Nullable List<BannerBean.bannerBean> data, Context context) {
        super(layoutResId, data);
        this.context=context;
        }

@Override
protected void convert(BaseViewHolder helper, BannerBean.bannerBean item) {

        picture = helper.getView(R.id.search_picture);
        Glide.with(context)
        .load(item.picture)
        .into(picture);
        helper.setText(R.id.search_information,item.name);
        helper.setText(R.id.search_number,item.person_number);
        helper.setText(R.id.search_price,item.current_price);
        helper.setText(R.id.search_state,item.state);
        }
}
