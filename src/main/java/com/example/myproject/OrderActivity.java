package com.example.myproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.example.myproject.adapter.OrderAdapter;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    RecyclerView order;
    OrderAdapter orderAdapter;
    ArrayList orderlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        order = findViewById(R.id.order);
        orderlist = new ArrayList();
        for (int i = 0; i < 2; i++) {
            orderlist.add(i+"");
        }
        orderAdapter = new OrderAdapter(R.layout.layout_order,orderlist);
        order.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        order.setAdapter(orderAdapter);
    }
}
