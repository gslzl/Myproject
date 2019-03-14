package com.example.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.example.myproject.bean.UserBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetActivity extends AppCompatActivity {

    @InjectView(R.id.return_1)
    ImageView return1;
    @InjectView(R.id.personal)
    TextView personal;
    @InjectView(R.id.changePassword)
    TextView changePassword;
    @InjectView(R.id.exit)
    Button exit;
    String infor_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.inject(this);
        infor_user = "http://120.79.87.68:5000/getUser";
    }

    @OnClick({R.id.return_1, R.id.personal, R.id.changePassword, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.return_1:
                final Intent intent =new Intent(this,HomepageActivity.class);
                startActivity(intent);
                break;
            case R.id.personal:
                OkGo.<String>post(infor_user)
                        .params("phone_number", SPUtils.getInstance().getString("str_login_number"))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                UserBean userBean = new Gson().fromJson(response.body(),UserBean.class);
                                Intent intent_personal =new Intent(SetActivity.this,PersonalActivity.class);
                                intent_personal.putExtra("user_bean",userBean);
                                startActivity(intent_personal);
                            }
                        });

                break;
            case R.id.changePassword:
                Intent intent_change =new Intent(this,ChangePasswordActivity.class);
                startActivity(intent_change);
                break;
            case R.id.exit:
                Intent intent_exit =new Intent(this,LoginActivity.class);
                startActivity(intent_exit);
                break;
        }
    }
}
