package com.example.myproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.myproject.fragments.LoginFragment;
import com.example.myproject.fragments.RegisterFragment;
import com.example.myproject.utils.PhotoPickUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.example.myproject.utils.PhotoPickUtil.REQUEST_PERMISSION;

public class LoginActivity extends AppCompatActivity {

    TextView tv_sl_login;
    TextView tv_sl_register;
    @InjectView(R.id.left)
    TextView left;
    @InjectView(R.id.right)
    TextView right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        replaceFragment(new LoginFragment());

        //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PhotoPickUtil.REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.finish();
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //      登录注册
    public void selector(View view) {
        tv_sl_login = findViewById(R.id.tv_sl_login);
        tv_sl_register = findViewById(R.id.tv_sl_register);

        switch (view.getId()) {
            case R.id.tv_sl_login:
                right.setVisibility(View.INVISIBLE);
                left.setVisibility(View.VISIBLE);
                replaceFragment(new LoginFragment());
                break;
            case R.id.tv_sl_register:
                left.setVisibility(View.INVISIBLE);
                right.setVisibility(View.VISIBLE);
                replaceFragment(new RegisterFragment());
        }
    }

    //    切换碎片
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment, fragment);
        fragmentTransaction.commit();
    }


}

