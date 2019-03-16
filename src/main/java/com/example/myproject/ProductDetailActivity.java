package com.example.myproject;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.myproject.bean.ProductBean;
import com.example.myproject.bean.ResultMessage;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.netease.nim.uikit.api.NimUIKit;

import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    Date date;
    Date now_date;
    Timer timer = new Timer();
    SimpleDateFormat format;
    @InjectView(R.id.range)
    TextView range;
    @InjectView(R.id.get_fav)
    Button getFav;

    String makeup_url;
    String add_fav;
    String now_price;
    String now_range;
    String now_id;
    String publishUserId;
    String start_price;
    double longitude;
    double latitude;

    @InjectView(R.id.map)
    ImageView map;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);

        makeup_url = "http://120.79.87.68:5000/updataIndent";
        add_fav = "http://120.79.87.68:5000/insertCollect";


        Intent intent = getIntent();
        final ProductBean ProductBean = (com.example.myproject.bean.ProductBean) intent.getSerializableExtra("productBean");
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(ProductBean.getData().getEnd_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        now_date = new Date(System.currentTimeMillis());
                        long diff = date.getTime() - now_date.getTime();
                        long days = diff / (1000 * 60 * 60 * 24);
                        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                        long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
                        freeTime.setText(hours + ":" + minutes + ":" + seconds);
                    }
                });
            }
        }, 0, 1000);
        infor.setText(ProductBean.getData().getName());
        intro.setText(ProductBean.getData().getInformation());
        prime.setText(ProductBean.getData().getPrice());

        latitude = Double.valueOf(ProductBean.getData().latitude);
        longitude = Double.valueOf(ProductBean.getData().longitude);

        publishUserId=ProductBean.getData().user_ID;
        now_id = ProductBean.getData().getID();
        now_price = Float.valueOf(ProductBean.getData().getCurrent_price())+Float.valueOf(ProductBean.getData().getStart_price())+"";
        start_price = ProductBean.getData().getStart_price();
        now_range = ProductBean.getData().getScope();

        auction.setText(now_price);
        range.setText(now_range);
        Glide.with(this)
                .load(ProductBean.getData().getPicture())
                .into(imageView);

    }

    @OnClick({R.id.connection, R.id.makeup, R.id.get_fav, R.id.map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.connection:
                NimUIKit.startP2PSession(this, publishUserId);
                break;
            case R.id.makeup:
                OkGo.<String>post(makeup_url)
                        .params("user_ID", SPUtils.getInstance().getString("str_login_number"))
                        .params("product_ID", now_id)
                        .params("my_price", Float.valueOf(now_price) + Float.valueOf(now_range)-Float.valueOf(start_price))
                        .params("state", "0")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                ResultMessage resultMessage = new Gson().fromJson(response.body(), ResultMessage.class);
                                if (resultMessage.getMessage().equals("success")) {
                                    ToastUtils.showShort("出价成功！");
                                } else {
                                    ToastUtils.showShort("加价失败，请刷新后重试！");
                                }
                            }
                        });
                break;
            case R.id.get_fav:
                OkGo.<String>post(add_fav)
                        .params("user_ID", SPUtils.getInstance().getString("str_login_number"))
                        .params("product_ID", now_id)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                ResultMessage resultMessage = new Gson().fromJson(response.body(), ResultMessage.class);
                                if (resultMessage.getMessage().equals("success")) {
                                    ToastUtils.showShort("收藏成功！");
                                } else {
                                    return;
                                }
                            }
                        });
                break;
            case R.id.map:
                Intent intent = new Intent(this,LocationActivity.class);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                startActivity(intent);

                break;
        }
    }
}
