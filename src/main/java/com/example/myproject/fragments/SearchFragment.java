package com.example.myproject.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myproject.HomepageActivity;
import com.example.myproject.MyApp;
import com.example.myproject.ProductDetailActivity;
import com.example.myproject.R;
import com.example.myproject.adapter.SearchAdapter;
import com.example.myproject.adapter.SpaceItem;
import com.example.myproject.bean.BannerBean;
import com.example.myproject.bean.ProductBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView rv_search;
    SearchAdapter searchAdapter;
    View view;
    List<BannerBean.bannerBean> searchBean = new ArrayList<>();
    List<String> Id = new ArrayList<>();
    String infor;
    String searchUrl;
    @InjectView(R.id.search_new)
    SearchView searchNew;

    private ImageView entranceAvatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        entranceAvatar = view.findViewById(R.id.head_image);

        File file = new File(Environment.getExternalStorageDirectory(), MyApp.AVATAR_FILE_NAME);
        if (file.exists()) {
            entranceAvatar.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + MyApp.AVATAR_FILE_NAME));
        }
        entranceAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity homepageActivity = (HomepageActivity) getActivity();
                homepageActivity.openMenu();
            }
        });




        infor = "http://120.79.87.68:5000/getProduct";
        searchUrl = "http://120.79.87.68:5000/search";
        rv_search = view.findViewById(R.id.rv_search);
        SpaceItem spaceItem = new SpaceItem(35);
        rv_search.addItemDecoration(spaceItem);
        searchAdapter = new SearchAdapter(R.layout.layout_search, searchBean, getActivity());
        rv_search.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_search.setAdapter(searchAdapter);

        searchBean.clear();
        getTransitiveData();

        ButterKnife.inject(this, view);
        searchNew.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                OkGo.<String>post(searchUrl)
                        .params("key_words", s)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BannerBean bannerBean = new Gson().fromJson(response.body(), BannerBean.class);
                                if (bannerBean.getCode().equals("1")) {
                                    searchBean.clear();
                                    searchBean.addAll(bannerBean.getData());
                                    searchAdapter.setNewData(searchBean);
                                    for (BannerBean.bannerBean element : bannerBean.getData()) {
                                        Id.add(element.getID());
                                    }
                                    searchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                            OkGo.<String>post(infor)
                                                    .params("ID", Id.get(position))
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(Response<String> response) {
                                                            ProductBean productBean = new Gson().fromJson(response.body(), ProductBean.class);
                                                            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                                            intent.putExtra("productBean", productBean);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    });
                                } else {
                                    ToastUtils.showShort("无检索内容！");
                                    return;
                                }
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return view;
    }

    private void getTransitiveData() {
        Bundle bundle = getArguments();
        BannerBean search_banner = (BannerBean) bundle.getSerializable("search_banner");
        searchBean.addAll(search_banner.getData());
        searchAdapter.setNewData(searchBean);


        for (BannerBean.bannerBean element : search_banner.getData()) {
            Id.add(element.getID());
        }
        searchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OkGo.<String>post(infor)
                        .params("ID", Id.get(position))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                ProductBean productBean = new Gson().fromJson(response.body(), ProductBean.class);
                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                intent.putExtra("productBean", productBean);
                                startActivity(intent);
                            }
                        });
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void setEntranceAvatar(Bitmap bitmap) {
        entranceAvatar.setImageBitmap(bitmap);

    }
}
