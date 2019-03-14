package com.example.myproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myproject.ProductDetailActivity;
import com.example.myproject.R;
import com.example.myproject.adapter.BrvahAdapter;
import com.example.myproject.adapter.SpaceItem;
import com.example.myproject.bean.BannerBean;
import com.example.myproject.bean.ProductBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment {

    List<BannerBean.bannerBean> catalogBean = new ArrayList<>();
    List<String> Id = new ArrayList<>();
    String infor;
    String searchUrl;
    BrvahAdapter catalogAdapter;


    @InjectView(R.id.head_image)
    CircleImageView headImage;
    @InjectView(R.id.search_new)
    SearchView searchNew;
    @InjectView(R.id.rv_catalog)
    RecyclerView rvCatalog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        ButterKnife.inject(this, view);
        searchUrl = "http://120.79.87.68:5000/search";
        infor = "http://120.79.87.68:5000/getProduct";

        SpaceItem spaceItem = new SpaceItem(35);
        rvCatalog.addItemDecoration(spaceItem);

        catalogAdapter = new BrvahAdapter(R.layout.layout_product, catalogBean, getActivity());
        rvCatalog.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvCatalog.setAdapter(catalogAdapter);

        catalogBean.clear();
        getCatalog();

        searchNew.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                OkGo.<String>post(searchUrl)
                        .params("key_words",s)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BannerBean bannerBean = new Gson().fromJson(response.body(),BannerBean.class);
                                if(bannerBean.getCode().equals("1")){
                                    catalogBean.clear();
                                    catalogBean.addAll(bannerBean.getData());
                                    catalogAdapter.setNewData(catalogBean);
                                    for (BannerBean.bannerBean element : bannerBean.getData()) {
                                        Id.add(element.getID());
                                    }
                                    catalogAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                                else {
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

    private void getCatalog() {
        Bundle bundle = getArguments();
        BannerBean catalog_banner = (BannerBean) bundle.getSerializable("catalog_banner");
        catalogBean.addAll(catalog_banner.getData());
        catalogAdapter.setNewData(catalogBean);
        for (BannerBean.bannerBean element : catalog_banner.getData()) {
            Id.add(element.getID());
        }
        catalogAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
}
