package com.example.myproject.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.myproject.R;
import com.example.myproject.bean.FavoritesBean;

import java.util.List;

public class FavoritesAdapter extends BaseQuickAdapter<FavoritesBean.favoritesBean, BaseViewHolder> {

    ImageView picture;
    Context context;

    public FavoritesAdapter(int layoutResId, @Nullable List<FavoritesBean.favoritesBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoritesBean.favoritesBean item) {
        picture = helper.getView(R.id.fav_image);
        Glide.with(context)
                .load(item.picture)
                .into(picture);
        helper.setText(R.id.fav_name,item.information);
        helper.setText(R.id.fav_price,item.price);
        helper.setText(R.id.fav_curprice,item.current_price);
    }
}
