package com.example.myproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myproject.ProductDetailActivity;
import com.example.myproject.R;
import com.example.myproject.adapter.SearchAdapter;
import com.example.myproject.adapter.SpaceItem;
import com.example.myproject.bean.BannerBean;
import com.example.myproject.bean.ProductBea;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        infor = "http://120.79.87.68:5000/getProduct";
        rv_search = view.findViewById(R.id.rv_search);
        SpaceItem spaceItem = new SpaceItem(15);
        rv_search.addItemDecoration(spaceItem);
        searchAdapter = new SearchAdapter(R.layout.layout_search, searchBean, getActivity());
        rv_search.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_search.setAdapter(searchAdapter);

        getTransitiveData();
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
                                ProductBea productBean = new Gson().fromJson(response.body(), ProductBea.class);
                                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                                intent.putExtra("productBean", productBean);
                                startActivity(intent);
                            }
                        });
            }
        });

    }

}
