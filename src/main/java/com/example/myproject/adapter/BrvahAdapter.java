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

public class BrvahAdapter extends BaseQuickAdapter<BannerBean.bannerBean, BaseViewHolder>{

    ImageView picture;
    Context context ;
    public BrvahAdapter(int layoutResId, @Nullable List<BannerBean.bannerBean> data, Context context) {
        super(layoutResId, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerBean.bannerBean item) {

        picture = helper.getView(R.id.tl_picture);
        Glide.with(context)
                .load(item.picture)
                .crossFade()
                .into(picture);
        helper.setText(R.id.tl_information,item.information);
        helper.setText(R.id.number,item.person_number);
        helper.setText(R.id.price,item.current_price);
        helper.setText(R.id.state,item.state);
    }
}

