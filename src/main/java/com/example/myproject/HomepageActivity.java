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


public class HomepageActivity extends AppCompatActivity implements MenuListener,View.OnClickListener {


    @InjectView(R.id.home)
    ImageView home;
    @InjectView(R.id.add)
    CircleImageView add;
    @InjectView(R.id.news)
    ImageView news;


    private static final int FINISH_CROP = 1;
    private static final int CROP_PHOTO = 2;
    private final String cutImageName = "cutProfilePic.jpg"; //裁剪后的图片的输出路径
    private final String fileProvider = "com.pumpkin.kuaipai.fileprovider";
    private Uri finalAvatarUri;

    SpringMenu menu;




    LinearLayout layoutOrderManage;
    LinearLayout layoutCollection;
    LinearLayout layoutCustomerService;
    LinearLayout layoutSetting;
    CircleImageView userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.inject(this);
        initMenu();


        HomeReplace(new HomeFragment());
    }


    void initMenu() {

        menu = new SpringMenu(this, R.layout.menu_mine);
        menu.setMenuListener(this);
        menu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(5, 5));
        menu.setFadeEnable(true);
        menu.setDirection(SpringMenu.DIRECTION_LEFT);
        menu.setDragOffset(0.4f);


        userAvatar = menu.getMenuView().findViewById(R.id.civ_avatar);
        layoutOrderManage =  menu.getMenuView().findViewById(R.id.layout_order_manage);
        layoutCollection =  menu.getMenuView().findViewById(R.id.layout_collection);
        layoutCustomerService =  menu.getMenuView().findViewById(R.id.layout_customer_service);
        layoutSetting =  menu.getMenuView().findViewById(R.id.layout_setting);

        layoutSetting.setOnClickListener(this);
        layoutCollection.setOnClickListener(this);
        layoutCustomerService.setOnClickListener(this);
        layoutOrderManage.setOnClickListener(this);
        userAvatar.setOnClickListener(this);


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

    @Override
    public void onMenuOpen() {

    }

    @Override
    public void onMenuClose() {

    }

    @Override
    public void onProgressUpdate(float value, boolean bouncing) {

    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CROP_PHOTO);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                startActivityForResult(cropPhoto(data.getData()), FINISH_CROP);
                break;
            case FINISH_CROP:
                try{
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(finalAvatarUri));
                    if(bitmap==null){
                        userAvatar.setImageResource(R.mipmap.default_avatar);
                    }else{
                        File avatarFile = new File(new URI(finalAvatarUri.toString()));

                        userAvatar.setImageBitmap(bitmap);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;


        }
    }


    private Intent cropPhoto(Uri oriImageUri) {

        try {

            Intent intent = new Intent("com.android.camera.action.CROP");

            File cutFile = new File(HomepageActivity.this.getExternalCacheDir(), cutImageName); //裁剪之后的图片file

            Uri outputImageUri = FileProvider.getUriForFile(HomepageActivity.this, fileProvider, cutFile);

            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", ConvertUtils.dp2px(96));
            intent.putExtra("outputY", ConvertUtils.dp2px(96));
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            if (oriImageUri != null) {
                intent.setDataAndType(oriImageUri, "image/*");
            }
            if (outputImageUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);
                finalAvatarUri = outputImageUri;//更改成员变量imageUri，也就是将其指向裁剪后的图片，在onActivityResult的CROP_PHOTE分支中要用到
            }

            intent.putExtra("noFaceDetection", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_order_manage:
                break;
            case R.id.layout_collection:
                break;
            case R.id.layout_customer_service:
                break;
            case R.id.layout_setting:
                break;
            case R.id.civ_avatar:
                openAlbum();

                break;
        }
    }
}
