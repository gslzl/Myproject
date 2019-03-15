package com.example.myproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myproject.bean.PublishGoodsInfo;
import com.example.myproject.bean.ResultMessage;
import com.example.myproject.utils.PhotoPickUtil;
import com.example.myproject.widgets.SingleOptionsPicker;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.example.myproject.MyApp.PUBLISH_GOODS_IMG_NAME;
import static com.example.myproject.utils.PhotoPickUtil.CROP_PHOTO;
import static com.example.myproject.utils.PhotoPickUtil.FINISH_CROP;
import static com.example.myproject.utils.PhotoPickUtil.REQUEST_PERMISSION;
import static com.example.myproject.utils.PhotoPickUtil.cropPhoto;

public class PublishActivity extends AppCompatActivity {


    @InjectView(R.id.et_goods_name)
    EditText etGoodsName;
    @InjectView(R.id.et_goods_desc)
    EditText etGoodsDesc;
    @InjectView(R.id.iv_thumbnail)
    ImageView ivThumbnail;
    @InjectView(R.id.btn_add_photo)
    Button btnAddPhoto;
    @InjectView(R.id.et_start_price)
    EditText etStartPrice;
    @InjectView(R.id.et_ori_price)
    EditText etOriPrice;
    @InjectView(R.id.et_price_step)
    EditText etPriceStep;
    @InjectView(R.id.et_auction_time_length)
    EditText etAuctionTimeLength;
    @InjectView(R.id.tv_category)
    TextView tvCategory;
    @InjectView(R.id.btn_publish)
    Button btnPublish;
    public LocationClient mLocationClient;
    File goodsImageFile;

    private String latitude="";
    private String longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.inject(this);


        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();

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

    @OnClick({R.id.iv_thumbnail, R.id.btn_add_photo, R.id.tv_category, R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_thumbnail:
                break;
            case R.id.btn_add_photo:
                PhotoPickUtil.openAlbum(this);

                break;
            case R.id.tv_category:
                tvCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> regionList = new ArrayList<>();
                        regionList.add("手机数码");
                        regionList.add("生活百货");
                        regionList.add("电脑电器");
                        regionList.add("服装箱包");
                        regionList.add("玩具健身");
                        regionList.add("美妆护肤");
                        regionList.add("奢侈大牌");
                        regionList.add("金银珠宝");
                        regionList.add("虚拟物品");
                        SingleOptionsPicker.openOptionsPicker(PublishActivity.this, regionList, 1, tvCategory);
                    }
                });
                break;
            case R.id.btn_publish:
                OkGo.<String>post(MyApp.PUBLISH_GOODS_URL)
                        .tag(null)
                        .isMultipart(true)
                        .params("longitude", longitude)
                        .params("latitude", latitude)
                        .params("name", etGoodsName.getText().toString())
                        .params("catalog", getCategory(tvCategory.getText().toString()))
                        .params("information", etGoodsDesc.getText().toString())
                        .params("start_price", etStartPrice.getText().toString())
                        .params("price", etOriPrice.getText().toString())
                        .params("scope", etPriceStep.getText().toString())
                        .params("end_time", etAuctionTimeLength.getText().toString())
                        .params("user_ID", SPUtils.getInstance().getString("str_login_number"))
                        .params("file", goodsImageFile)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Gson gson = new Gson();
                                ResultMessage resultMessage = gson.fromJson(response.body(), ResultMessage.class);
                                if (resultMessage.getCode() == ResultMessage.SUCCESS) {
                                    finish();
                                    ToastUtils.showShort("发布成功");
                                } else {
                                    ToastUtils.showShort("发布失败，请检查网络");
                                }
                            }

                        });

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                if (data == null) return;
                startActivityForResult(cropPhoto(data.getData()), FINISH_CROP);
                break;
            case FINISH_CROP:
                try {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    baos.flush();
                    baos.close();
                    byte[] bytes = baos.toByteArray();
//                    int paddingStep = ConvertUtils.dp2px(1);
                    RoundedCorners roundedCorners = new RoundedCorners(60);
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(100, 100);

                    ivThumbnail.setImageBitmap(bitmap);

//                    Glide.with(this).load(bytes).apply(options).into(ivThumbnail);//会存在缓存问题

                    goodsImageFile = new File(Environment.getExternalStorageDirectory(), PUBLISH_GOODS_IMG_NAME);

                    if(goodsImageFile.exists()){
                        goodsImageFile.delete();
                        goodsImageFile = new File(Environment.getExternalStorageDirectory(), PUBLISH_GOODS_IMG_NAME);

                    }

                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(goodsImageFile));
                    //压缩
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    public String getCategory(String cateName) {
        switch (cateName) {
            case "手机数码":
                return "phone";
            case "生活百货":
                return "live";
            case "电器电脑":
                return "pc";
            case "服装箱包":
                return "bag";
            case "玩具健身":
                return "toys";
            case "美妆护肤":
                return "cosmetics";
            case "奢侈大牌":
                return "luxury";
            case "虚拟物品":
                return "vt";
            case "金银珠宝":
                return "gold";
            default:
                return "live";
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {//接收到位置
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                latitude = location.getLatitude() + "";
                longitude = location.getLongitude() + "";

            }
        }
    }


}
