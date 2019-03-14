package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.myproject.bean.UserBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChangePasswordActivity extends AppCompatActivity {


    @InjectView(R.id.new_password)
    EditText newPassword;
    @InjectView(R.id.old_password)
    EditText oldPassword;
    String str_newPassword;
    String str_oldPassword;
    String url;
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.true_change)
    Button trueChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        ButterKnife.inject(this);
        url = "http://120.79.87.68:5000/updataUser";
    }


    @OnClick({R.id.back, R.id.true_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent intent = new Intent(this,SetActivity.class);
                startActivity(intent);
                break;
            case R.id.true_change:
                str_newPassword = newPassword.getText().toString().trim();
                str_oldPassword = oldPassword.getText().toString().trim();
                    OkGo.<String>post(url)
                            .params("phone_number", SPUtils.getInstance().getString("str_login_number"))
                            .params("old_password",str_oldPassword)
                            .params("password", str_newPassword)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    UserBean userBean = new Gson().fromJson(response.body(), UserBean.class);
                                    if (userBean.code.equals("1")) {
                                        ToastUtils.showShort("密码修改成功");
                                        Intent intent = new Intent(ChangePasswordActivity.this, SetActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ToastUtils.showShort("密码修改失败");
                                        return;
                                    }
                                }
                            });
                break;
        }
    }
}
