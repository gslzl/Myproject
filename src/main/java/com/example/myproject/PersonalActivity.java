package com.example.myproject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.example.myproject.bean.UserBean;
import com.example.myproject.fragments.LoginFragment;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends AppCompatActivity {


    @InjectView(R.id.head_image)
    CircleImageView headImage;
    @InjectView(R.id.name)
    EditText name;
    @InjectView(R.id.address)
    EditText address;
    @InjectView(R.id.phonenumber)
    EditText phonenumber;
    String str_name;
    String str_addresss;
    String str_phonenumber;
    String url;
    LoginFragment str_login_number;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.inject(this);
        File file = new File(Environment.getExternalStorageDirectory(), MyApp.AVATAR_FILE_NAME);
        if (file.exists())
            headImage.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + MyApp.AVATAR_FILE_NAME));
        url = "http://120.79.87.68:5000/updataUser";
    }

    @OnClick({R.id.back_personal, R.id.personal_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_personal:
                Intent intent_set = new Intent(this, SetActivity.class);
                startActivity(intent_set);
                break;
            case R.id.personal_change:
                str_addresss = address.getText().toString().trim();
                str_name = name.getText().toString().trim();
                str_phonenumber = phonenumber.getText().toString().trim();

                OkGo.<String>post(url)
                        .params("phone_number", String.valueOf(str_login_number))
                        .params("address", str_addresss)
                        .params("name", str_name)
                        .params("address_phone", str_phonenumber)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                UserBean userBean = new Gson().fromJson(response.body(), UserBean.class);
                                if (userBean.code.equals("1")) {
                                    ToastUtils.showShort("修改个人资料成功");
                                    Intent intent_set = new Intent(PersonalActivity.this, SetActivity.class);
                                    startActivity(intent_set);
                                } else {
                                    return;
                                }
                            }
                        });
                break;
        }
    }
}
