package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.myproject.bean.ProductBean;
import com.netease.nim.uikit.api.NimUIKit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity {

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.infor)
    TextView infor;
    @InjectView(R.id.prime)
    TextView prime;
    @InjectView(R.id.auction)
    TextView auction;
    @InjectView(R.id.free_time)
    TextView freeTime;
    @InjectView(R.id.intro)
    TextView intro;
    @InjectView(R.id.connection)
    Button connection;
    @InjectView(R.id.makeup)
    Button makeup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);
        Intent intent  = getIntent();
        if (intent.hasExtra("ProductBeann")) {

            ProductBean ProductBean = (com.example.myproject.bean.ProductBean) intent.getSerializableExtra("ProductBeann");
            title.setText(ProductBean.getData().getName());
            infor.setText(ProductBean.getData().getInformation());
            auction.setText(ProductBean.getData().getCurrent_price());
            Glide.with(this)
                    .load(ProductBean.getData().getPicture())
                    .into(imageView);
        }else{


            ToastUtils.showShort("no");

        }

    }

    @OnClick({R.id.connection, R.id.makeup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.connection:
                NimUIKit.startP2PSession(this, "1771138872");
                break;
            case R.id.makeup:
                break;
        }
    }
}
