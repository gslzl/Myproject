package com.example.myproject;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;

import java.util.List;


public class MyApp extends Application {


    public static final String UPLOAD_AVATAR_URL = "http://120.79.87.68:5000/insertAvatar";
    public static final String AVATAR_FILE_NAME = "spec_avatar.png";
    private static final int SDK_APPID = 1400190762;
    private static final String ACCOUNT_TYPE = "36862";
    private static final String tag = "IM配置-----";


    @Override
    public void onCreate() {

        //初始化 SDK 基本配置
        TIMSdkConfig config = new TIMSdkConfig(SDK_APPID)
                .setAccoutType(ACCOUNT_TYPE)
                .enableLogPrint(true)              // 是否在控制台打印Log?
                .setLogLevel(TIMLogLevel.DEBUG)    // Log输出级别（debug级别会很多）
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/");
        // Log文件存放在哪里？

//初始化 SDK


        super.onCreate();
        Utils.init(this);
        OkGo.getInstance().init(this);
        TIMManager.getInstance().init(getApplicationContext(), config);
        initUserConfig();

    }

    void initUserConfig() {

        //基本用户配置
        TIMUserConfig userConfig = new TIMUserConfig();

        //设置用户状态变更事件监听器
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                //被其他终端踢下线
                Log.i(tag, "onForceOffline");
            }

            @Override
            public void onUserSigExpired() {
                //用户签名过期了，需要刷新 userSig 重新登录 SDK
                Log.i(tag, "onUserSigExpired");
            }
        })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Log.i(tag, "onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        Log.i(tag, "onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Log.i(tag, "onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                        Log.i(tag, "onGroupTipsEvent, type: " + elem.getTipsType());
                    }
                })
                //设置会话刷新监听器
                .setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(tag, "onRefresh");
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> conversations) {
                        Log.i(tag, "onRefreshConversation, conversation size: " + conversations.size());
                    }
                });

//消息扩展用户配置
        userConfig = new TIMUserConfigMsgExt(userConfig)
                //禁用消息存储
                .enableStorage(true)
                //开启消息已读回执
                .enableReadReceipt(true);

//将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);
        //基本用户配置


        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器
            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {//收到新消息
                //消息的内容解析请参考消息收发文档中的消息解析说明
                return true; //返回true将终止回调链，不再调用下一个新消息监听器
            }
        });
    }

}
