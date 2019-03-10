package com.example.myproject.fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.example.myproject.R;
import com.example.myproject.adapter.BrvahAdapter;
import com.example.myproject.adapter.SpaceItem;
import com.example.myproject.bean.BannerBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.example.myproject.HomepageActivity;
import com.example.myproject.MyApp;


import com.recker.flybanner.FlyBanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    View view;
    String getUrl;
    String inforUrl;

    int i = 0;


    CircleImageView entranceAvatar;

    List<String> imgesUrl = new ArrayList<>();
    List<String> imgesID = new ArrayList<>();

    FlyBanner banner;
    RecyclerView product;
    BrvahAdapter madapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    NestedScrollView nestedScrollView;
    List<BannerBean.bannerBean> bannerBeans = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.i("初始化oncreate");
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.banner);
        entranceAvatar = view.findViewById(R.id.head_image);
        File file = new File(Environment.getExternalStorageDirectory(), MyApp.AVATAR_FILE_NAME);
        if (file.exists())
            entranceAvatar.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + MyApp.AVATAR_FILE_NAME));

        entranceAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity homepageActivity = (HomepageActivity) getActivity();
                homepageActivity.openMenu();
            }
        });
        product = view.findViewById(R.id.product);
        product.setNestedScrollingEnabled(false);
        nestedScrollView = view.findViewById(R.id.nest_sv);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        SpaceItem spaceItem = new SpaceItem(15);
        product.addItemDecoration(spaceItem);
        madapter = new BrvahAdapter(R.layout.layout_product, bannerBeans, getActivity());
        product.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        product.setAdapter(madapter);

        //        initBanner();
        initProduct();
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initProduct();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (nestedScrollView.getChildAt(0).getMeasuredHeight() - nestedScrollView.getMeasuredHeight())) {
                    i = i + 1;
                    initProduct();

                }
            }
        });

        return view;
    }

    private void initProduct() {
        inforUrl = "http://120.79.87.68:5000/getPageProduct";
        OkGo.<String>post(inforUrl)
                .params("page", i)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BannerBean showproduct = new Gson().fromJson(response.body(), BannerBean.class);
                        if (showproduct.getData() == null) {
                            ToastUtils.showShort("没有更多数据了");
                            return;
                        }
                        bannerBeans.addAll(showproduct.getData());
                        madapter.setNewData(bannerBeans);
                        LogUtils.i(showproduct.getData());
                    }
                });
    }

    private void initBanner() {
        getUrl = "http://120.79.87.68:5000/getBanner";
        inforUrl = "http://120.79.87.68:5000/getProduct";
        OkGo.<String>get(getUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        BannerBean bannerBean = new Gson().fromJson(response.body(), BannerBean.class);
                        for (BannerBean.bannerBean element : bannerBean.getData()) {
                            imgesUrl.add(element.getPicture());
                            imgesID.add(element.getID());
                        }
                        banner.setImagesUrl(imgesUrl);
                    }
                });
        banner.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OkGo.<String>post(inforUrl)
                        .params("ID", imgesID.get(position))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                LogUtils.i(response.body());
                            }
                        });
            }
        });
    }


    public void setEntranceAvatar(Bitmap bitmap) {
        entranceAvatar.setImageBitmap(bitmap);

    }


}
