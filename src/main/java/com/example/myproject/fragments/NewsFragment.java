package com.example.myproject.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myproject.ChatActivity;
import com.example.myproject.HomepageActivity;
import com.example.myproject.MyApp;
import com.example.myproject.R;
import com.tencent.qcloud.uikit.business.session.model.SessionInfo;
import com.tencent.qcloud.uikit.business.session.view.SessionPanel;
import com.tencent.qcloud.uikit.business.session.view.wedgit.SessionClickListener;
import com.tencent.qcloud.uikit.common.BaseFragment;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends BaseFragment {

    CircleImageView entranceAvatar;

    private View baseView;
    private SessionPanel sessionPanel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         baseView = inflater.inflate(R.layout.fragment_news, container, false);
        entranceAvatar = baseView.findViewById(R.id.head_image);
        File file = new File(Environment.getExternalStorageDirectory(), MyApp.AVATAR_FILE_NAME);
        if (file.exists())
            entranceAvatar.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + MyApp.AVATAR_FILE_NAME));
        entranceAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity homepageActivity = (HomepageActivity) getActivity();
                homepageActivity.openMenu();
            }
        });
        initView();
        return baseView;
    }



    public void setEntranceAvatar(Bitmap bitmap) {
        entranceAvatar.setImageBitmap(bitmap);

    }


    private void initView() {
        // 获取会话列表组件，
        sessionPanel = baseView.findViewById(R.id.session_panel);
        // 会话面板初始化默认功能
        sessionPanel.initDefault();
        // 这里设置会话列表点击的跳转逻辑，告诉添加完SessionPanel后会话被点击后该如何处理
        sessionPanel.setSessionClick(new SessionClickListener() {
            @Override
            public void onSessionClick(SessionInfo session) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                if (session.isGroup()) {
//                    ChatActivity.startGroupChat(getActivity(), session.getPeer());
                }
                else {
                    //否则跳转到C2C单聊界面
                    ChatActivity.startC2CChat(getActivity(), session.getPeer());

                }
            }
        });


    }


}
