package com.example.myproject.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.myproject.HomepageActivity;
import com.example.myproject.R;
import com.example.myproject.bean.BannerBean;
import com.facebook.rebound.SpringConfig;
import com.google.gson.Gson;
import com.jpeng.jpspringmenu.MenuListener;
import com.jpeng.jpspringmenu.SpringMenu;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.recker.flybanner.FlyBanner;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements MenuListener, View.OnClickListener {
    View view;
    String getUrl;
    String inforUrl;

    private static final int REQUEST_PERMISSION = 0;
    private static final int FINISH_CROP = 1;
    private static final int CROP_PHOTO = 2;

    SpringMenu menu;

    LinearLayout layoutOrderManage;
    LinearLayout layoutCollection;
    LinearLayout layoutCustomerService;
    LinearLayout layoutSetting;
    CircleImageView userAvatar;
    CircleImageView entranceAvatar;

    List<String> imgesUrl = new ArrayList<>();
    List<String> imgesID = new ArrayList<>();
    FlyBanner banner;

    public HomeFragment() {
        // Required empty public constructor
    }

    void initMenu() {

        menu = new SpringMenu(getActivity(), R.layout.menu_mine);
        menu.setMenuListener(this);
        menu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(5, 5));
        menu.setFadeEnable(true);
        menu.setDirection(SpringMenu.DIRECTION_LEFT);
        menu.setDragOffset(0.4f);


        userAvatar = menu.getMenuView().findViewById(R.id.civ_avatar);
        layoutOrderManage = menu.getMenuView().findViewById(R.id.layout_order_manage);
        layoutCollection = menu.getMenuView().findViewById(R.id.layout_collection);
        layoutCustomerService = menu.getMenuView().findViewById(R.id.layout_customer_service);
        layoutSetting = menu.getMenuView().findViewById(R.id.layout_setting);

        layoutSetting.setOnClickListener(this);
        layoutCollection.setOnClickListener(this);
        layoutCustomerService.setOnClickListener(this);
        layoutOrderManage.setOnClickListener(this);
        userAvatar.setOnClickListener(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.banner);
        initMenu();
        entranceAvatar = view.findViewById(R.id.head_image);
        entranceAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.openMenu();
            }
        });
//        initBanner();

        return view;
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
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CROP_PHOTO);
            } else {
                ActivityCompat.requestPermissions(getActivity(), perms, REQUEST_PERMISSION);

            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, CROP_PHOTO);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                startActivityForResult(cropPhoto(data.getData()), FINISH_CROP);
                break;
            case FINISH_CROP:
                try {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) {
                        userAvatar.setImageResource(R.mipmap.default_avatar);
                    } else {
                        userAvatar.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


        }
    }


    private Intent cropPhoto(Uri oriImageUri) {

        try {

            Intent intent = new Intent("com.android.camera.action.CROP");

            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", ConvertUtils.dp2px(90));
            intent.putExtra("outputY", ConvertUtils.dp2px(90));
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            intent.setDataAndType(oriImageUri, "image/*");


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
