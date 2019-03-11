package com.example.myproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;

public class ChatActivity extends AppCompatActivity {

    private C2CChatPanel chatPanel;
    private String chatId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chatInfo");
        initView();

    }

    private void initView() {
        //从布局文件中获取聊天面板组件
        chatPanel = findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        /*
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
        chatPanel.setBaseChatId(chatId);
    }


    public static void startC2CChat(Context context, String chatInfo) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatInfo", chatInfo);
        context.startActivity(intent);
    }
}
