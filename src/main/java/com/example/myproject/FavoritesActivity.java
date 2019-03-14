package com.example.myproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.blankj.utilcode.util.SPUtils;
import com.example.myproject.adapter.FavoritesAdapter;
import com.example.myproject.bean.FavoritesBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FavoritesActivity extends AppCompatActivity {

    String state;
    String fav_url;
    RecyclerView favorites;
    List<FavoritesBean.favoritesBean> favoritesBeans = new ArrayList<>();
    List<FavoritesBean.favoritesBean> nullBeans = new ArrayList<>();

    FavoritesAdapter favoritesAdapter;
    @InjectView(R.id.radioButton_pay)
    RadioButton radioButtonPay;
    @InjectView(R.id.radioButton_sell)
    RadioButton radioButtonSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.inject(this);

        fav_url = "http://120.79.87.68:5000/getCollect";
        state = "0";

        favorites = findViewById(R.id.favorites);
        favoritesAdapter = new FavoritesAdapter(R.layout.layout_favorites, favoritesBeans,this);
        favorites.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));

        getFavorites();
        favorites.setAdapter(favoritesAdapter);

    }

    private void getFavorites() {
        favoritesBeans.clear();
        OkGo.<String>post(fav_url)
                .params("user_ID", SPUtils.getInstance().getString("str_login_number"))
                .params("state",state)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        FavoritesBean favoritesBean = new Gson().fromJson(response.body(),FavoritesBean.class);
                        if(favoritesBean.code.equals("1")){
                            favoritesBeans.addAll(favoritesBean.getData());
                            favoritesAdapter.setNewData(favoritesBeans);
                        }
                    }
                });
    }

    @OnClick({R.id.radioButton_pay, R.id.radioButton_sell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radioButton_pay:
                state = "0";
                break;
            case R.id.radioButton_sell:
                state = "1";
                break;
        }
        getFavorites();

    }
}
