package com.example.myproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.myproject.adapter.OrderAdapter;
import com.example.myproject.bean.IndentBean;
import com.example.myproject.bean.ResultMessage;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OrderActivity extends AppCompatActivity {

    String state;
    String indenturl;
    String deleteurl;
    String updataurl;

    RecyclerView order;
    OrderAdapter orderAdapter;
    @InjectView(R.id.order_pay)
    RadioButton orderPay;
    @InjectView(R.id.order_sell)
    RadioButton orderSell;
    @InjectView(R.id.all)
    RadioButton all;
    @InjectView(R.id.order_ing)
    RadioButton orderIng;
    @InjectView(R.id.order_deliver)
    RadioButton orderDeliver;
    @InjectView(R.id.order_finish)
    RadioButton orderFinish;

    List<IndentBean.indentBean> indentBeans = new ArrayList<>();
    List<IndentBean.indentBean> nullBeans = new ArrayList<>();
    List<String> ID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.inject(this);
        state = "1";

        indenturl = "http://120.79.87.68:5000/getIndent";
        deleteurl = "http://120.79.87.68:5000/deleteIndent";
        deleteurl = "http://120.79.87.68:5000/updataIndent";

        order = findViewById(R.id.order);

        orderAdapter = new OrderAdapter(R.layout.layout_order, indentBeans,this);
        order.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        order.setAdapter(orderAdapter);


        getIndent();
        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.radioButton_topay:

                        break;
                    case R.id.radioButton_exit:
                        OkGo.<String>post(deleteurl)
                                .params("product_ID",ID.get(position))
                                .params("user_ID",SPUtils.getInstance().getString("str_login_number"))
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResultMessage resultMessage = new Gson().fromJson(response.body(),ResultMessage.class);
                                        if (resultMessage.getMessage().equals("success")){
                                            getIndent();
                                            ToastUtils.showShort("删除订单成功！");
                                        }
                                    }
                                });
                        break;

                }
            }
        });

    }

    private void getIndent() {
        indentBeans.clear();
        OkGo.<String>post(indenturl)
                .params("user_ID", SPUtils.getInstance().getString("str_login_number"))
                .params("state",state)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        IndentBean indentBean = new Gson().fromJson(response.body(),IndentBean.class);
                        if(indentBean.code.equals("0")){
                            indentBeans.addAll(nullBeans);
                            orderAdapter.setNewData(indentBeans);
                            ToastUtils.showShort("用户暂无订单！");
                        }
                        else {
                            indentBeans.addAll(indentBean.getData());
                            orderAdapter.setNewData(indentBeans);
                            for (IndentBean.indentBean element : indentBean.getData()) {
                                ID.add(element.getProduct_ID());
                            }
                        }
                    }
                });

    }


    public static int type =0;
    public static int type_new =0;
    @OnClick({R.id.order_pay, R.id.order_sell, R.id.all, R.id.order_ing, R.id.order_deliver, R.id.order_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_pay:
                indenturl = "http://120.79.87.68:5000/getIndent";
                type = 0;
                break;
            case R.id.order_sell:
                indenturl = "http://120.79.87.68:5000/getMyIndent";
                type =1;
                break;
            case R.id.all:
                state = "1";
                break;
            case R.id.order_ing:
                state = "0";
                break;
            case R.id.order_deliver:
                state = "2";
                break;
            case R.id.order_finish:
                state = "3";
                break;
        }
        getIndent();
    }
}
