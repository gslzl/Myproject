package com.example.myproject;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;



public class MyApp extends Application {


    public static final String UPLOAD_AVATAR_URL = "http://120.79.87.68:5000/insertAvatar";
    public static final String AVATAR_FILE_NAME = "spec_avatar.png";
    private static final String tag = "IM配置-----";


    @Override
    public void onCreate() {

        super.onCreate();
        Utils.init(this);
        OkGo.getInstance().init(this);
        LoginInfo  loginInfo = new LoginInfo("15736506524","281ecb719ca162f91cf78d0e11b59356");

        NIMClient.init(this, loginInfo,null);


        if (NIMUtil.isMainProcess(this)) {
            LogUtils.i("主线程");
            // 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
            NimUIKit.init(this);
        }
    }


}
