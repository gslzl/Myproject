package com.example.myproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CategoryActivity extends AppCompatActivity {

    @InjectView(R.id.Phone)
    ImageView Phone;
    @InjectView(R.id.Live)
    ImageView Live;
    @InjectView(R.id.Pc)
    ImageView Pc;
    @InjectView(R.id.Bag)
    ImageView Bag;
    @InjectView(R.id.Toys)
    ImageView Toys;
    @InjectView(R.id.Cosmetics)
    ImageView Cosmetics;
    @InjectView(R.id.Luxury)
    ImageView Luxury;
    @InjectView(R.id.Gold)
    ImageView Gold;
    @InjectView(R.id.Vt)
    ImageView Vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.Phone, R.id.Live, R.id.Pc, R.id.Bag, R.id.Toys, R.id.Cosmetics, R.id.Luxury, R.id.Gold, R.id.Vt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Phone:
                break;
            case R.id.Live:
                break;
            case R.id.Pc:
                break;
            case R.id.Bag:
                break;
            case R.id.Toys:
                break;
            case R.id.Cosmetics:
                break;
            case R.id.Luxury:
                break;
            case R.id.Gold:
                break;
            case R.id.Vt:
                break;
        }
    }
}
