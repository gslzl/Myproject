package com.example.myproject.utils;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.myproject.MyApp;
import com.example.myproject.bean.ResultMessage;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import java.io.File;

public class MyUtils {

    public static void uploadAvatar(File file,String account){

        OkGo.<String>post(MyApp.UPLOAD_AVATAR_URL)
                .isMultipart(true)
                .params("phone_number",account)
                .params("file",file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        ResultMessage resultMessage = gson.fromJson(response.body(),ResultMessage.class);
                        if(resultMessage.getCode()==ResultMessage.SUCCESS){
                            ToastUtils.showShort("头像上传成功");
                        }
                    }
                });
    }
}

