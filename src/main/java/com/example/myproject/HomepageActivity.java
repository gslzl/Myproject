package com.example.myproject;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.myproject.fragments.AddFragment;
import com.example.myproject.fragments.HomeFragment;
import com.example.myproject.fragments.NewsFragment;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;

import java.io.File;
import java.net.URI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomepageActivity extends AppCompatActivity  {


    @InjectView(R.id.home)
    ImageView home;
    @InjectView(R.id.add)
    CircleImageView add;
    @InjectView(R.id.news)
    ImageView news;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.inject(this);
        HomeReplace(new HomeFragment());
    }





    @OnClick({R.id.home, R.id.add, R.id.news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:
                HomeReplace(new HomeFragment());
                break;
            case R.id.add:
                HomeReplace(new AddFragment());
                break;
            case R.id.news:
                HomeReplace(new NewsFragment());
                break;
        }
    }
    public void HomeReplace(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return menu.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

}
