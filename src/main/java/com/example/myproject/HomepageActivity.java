package com.example.myproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.myproject.fragments.HomeFragment;
import com.example.myproject.fragments.NewsFragment;
import com.example.myproject.utils.MyUtils;
import com.example.myproject.utils.PhotoPickUtil;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.myproject.MyApp.AVATAR_FILE_NAME;
import static com.example.myproject.utils.PhotoPickUtil.CROP_PHOTO;
import static com.example.myproject.utils.PhotoPickUtil.FINISH_CROP;
import static com.example.myproject.utils.PhotoPickUtil.REQUEST_PERMISSION;
import static com.example.myproject.utils.PhotoPickUtil.cropPhoto;


public class HomepageActivity extends AppCompatActivity implements View.OnClickListener, MenuListener {


    @InjectView(R.id.home)
    ImageView home;
    @InjectView(R.id.add)
    CircleImageView add;
    @InjectView(R.id.news)
    ImageView news;



    HomeFragment homeFragment;
    NewsFragment newsFragment;
    SpringMenu menu;

    LinearLayout layoutOrderManage;
    LinearLayout layoutCollection;
    LinearLayout layoutCustomerService;
    LinearLayout layoutSetting;
    CircleImageView userAvatar;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.inject(this);
        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();
        initMenu();
        HomeReplace(homeFragment);


    }

    public void openMenu() {
        menu.openMenu();
    }


    void initMenu() {

        menu = new SpringMenu(this, R.layout.menu_mine);
        menu.setMenuListener(this);
        menu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(5, 5));
        menu.setFadeEnable(true);
        menu.setDirection(SpringMenu.DIRECTION_LEFT);
        menu.setDragOffset(0.4f);

        name = menu.findViewById(R.id.tv_nickname);
        userAvatar = menu.getMenuView().findViewById(R.id.civ_avatar);
        layoutOrderManage = menu.getMenuView().findViewById(R.id.layout_order_manage);
        layoutCollection = menu.getMenuView().findViewById(R.id.layout_collection);
        layoutCustomerService = menu.getMenuView().findViewById(R.id.layout_customer_service);
        layoutSetting = menu.getMenuView().findViewById(R.id.layout_setting);

        name.setText(SPUtils.getInstance().getString("str_nick_name"));
        layoutSetting.setOnClickListener(this);
        layoutCollection.setOnClickListener(this);
        layoutCustomerService.setOnClickListener(this);
        layoutOrderManage.setOnClickListener(this);
        userAvatar.setOnClickListener(this);
        File file = new File(Environment.getExternalStorageDirectory(), AVATAR_FILE_NAME);
        if (file.exists())
            userAvatar.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + AVATAR_FILE_NAME));

    }


    @OnClick({R.id.home, R.id.add, R.id.news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home:
                HomeReplace(homeFragment);
                break;
            case R.id.add:
                Intent intent = new Intent(this,PublishActivity.class);
                startActivity(intent);
                break;
            case R.id.news:
                HomeReplace(newsFragment);
                break;
        }
    }

    public void HomeReplace(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.home_fragment, fragment);

        fragmentTransaction.commit();

    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onMenuOpen() {

    }

    @Override
    public void onMenuClose() {

    }

    @Override
    public void onProgressUpdate(float value, boolean bouncing) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_order_manage:
                Intent intent_order = new Intent(this,OrderActivity.class);
                startActivity(intent_order);
                break;
            case R.id.layout_collection:
                Intent intent_collection = new Intent(this,FavoritesActivity.class);
                startActivity(intent_collection);
                break;
            case R.id.layout_customer_service:
                break;
            case R.id.layout_setting:
                Intent intent_set = new Intent(this,SetActivity.class);
                startActivity(intent_set);
                break;
            case R.id.civ_avatar:
                PhotoPickUtil.openAlbum(this);

                break;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoPickUtil.openAlbum(this);
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                if(data==null) return;
                startActivityForResult(cropPhoto(data.getData()), FINISH_CROP);
                break;
            case FINISH_CROP:
                try {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    homeFragment.setEntranceAvatar(bitmap);
                    newsFragment.setEntranceAvatar(bitmap);
                    userAvatar.setImageBitmap(bitmap);

                    File file = new File(Environment.getExternalStorageDirectory(), AVATAR_FILE_NAME);
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    //压缩
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    MyUtils.uploadAvatar(file, SPUtils.getInstance().getString("str_login_number"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


        }
    }







}
