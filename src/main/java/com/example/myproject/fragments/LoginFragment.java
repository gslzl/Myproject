package com.example.myproject.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.myproject.HomepageActivity;
import com.example.myproject.LoginActivity;
import com.example.myproject.R;
import com.example.myproject.bean.UserBean;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    View v;
    Activity activity;
    CheckBox cb_rm_pass;
    Button btn_login;
    EditText et_login_number;
    EditText et_login_pass;
    TextView tv_forget_pass;
    private String str_login_number;
    String   str_login_pass;

    Boolean flag = false;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false);

        activity = getActivity();

        cb_rm_pass = v.findViewById(R.id.cb_rm_pass);
        cb_rm_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });
        et_login_number = v.findViewById(R.id.et_login_number);
        et_login_pass = v.findViewById(R.id.et_login_pass);
        btn_login = v.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postUrl = "http://120.79.87.68:5000/login";
                str_login_number = et_login_number.getText().toString().trim();
                str_login_pass = et_login_pass.getText().toString().trim();
                if(str_login_pass.isEmpty()){
                    ToastUtils.showShort("请输入密码！！！");
                }
                else {
                    if (flag) {
                        //选中状态我们需要把et里面的内容存入sp
                        SPUtils.getInstance().put("str_login_number", str_login_number);
                        SPUtils.getInstance().put("str_login_pass", str_login_pass);
                    } else {
                        //未选中需要清除
                        SPUtils.getInstance().remove("str_login_number");
                        SPUtils.getInstance().remove("str_login_pass");
                    }
                    OkGo.<String>post(postUrl)
                            .params("phone_number",str_login_number)
                            .params("password",str_login_pass)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    UserBean userBean = new Gson().fromJson(response.body(), UserBean.class);
                                    if (userBean.code.equals("1")){
                                        SPUtils.getInstance().put("str_nick_name",userBean.data.name);
                                        SPUtils.getInstance().put("str_avatar_url",userBean.data.avatar);
                                        SPUtils.getInstance().put("str_login_number", str_login_number);
                                        SPUtils.getInstance().put("str_login_pass", str_login_pass);
                                        SPUtils.getInstance().put("str_nim_token",userBean.data.userSig);
                                        LogUtils.i("tokern"+userBean.data.userSig);
                                        loginNim(str_login_number,userBean.data.userSig);
                                    }

                                }
                            });
                }
            }
        });

        et_login_number.setText(SPUtils.getInstance().getString("str_login_number"));
        et_login_pass.setText(SPUtils.getInstance().getString("str_login_pass"));

        tv_forget_pass = v.findViewById(R.id.tv_forget_pass);
        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).replaceFragment(new ForgetPassFragment());

            }
        });
        return v;
    }

    public void loginNim(String account,String token){
        LoginInfo info = new LoginInfo(account,token);
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        startActivity(new Intent(getActivity(),HomepageActivity.class));
                    }

                    @Override
                    public void onFailed(int code) {
                        ToastUtils.showShort("登录失败");
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                };
        NimUIKitImpl.login(info,callback);

    }




}

