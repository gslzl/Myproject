package com.example.myproject.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.ConvertUtils;


public class PhotoPickUtil {



    public static final int REQUEST_PERMISSION = 0;
    public static final int FINISH_CROP = 1;
    public static final int CROP_PHOTO = 2;

    public static  void openAlbum(Activity context) {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                context.startActivityForResult(intent, CROP_PHOTO);
            } else {
                context.requestPermissions(perms, REQUEST_PERMISSION);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            context.startActivityForResult(intent, CROP_PHOTO);
        }
    }


    public static Intent cropPhoto(Uri oriImageUri) {

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

}
