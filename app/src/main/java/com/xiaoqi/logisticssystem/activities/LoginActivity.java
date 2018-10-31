package com.xiaoqi.logisticssystem.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ping.greendao.gen.CustomerDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Customer;
import com.xiaoqi.logisticssystem.db.CustomerDBUtils;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_remember)
    CheckBox cbRemember;
    @BindView(R.id.cb_autologin)
    CheckBox cbAutologin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        /**
         * 动态权限申请，申请完毕，进行数据的初始化以及事件
         */
        initPermission();
//        initData();
//        initEvent();
    }


    /**
     * 申请权限
     */
    private void initPermission() {
        //所要申请的权限
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (EasyPermissions.hasPermissions(this, perms)) {//检查是否获取该权限
            initData();
            initEvent();
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", 0, perms);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (perms.size() > 0) {
            AppClient.showToast("权限被拒绝");
            System.exit(0);
        }

    }


    /**
     * 初始化事件，checkbox的勾选事件
     */
    private void initEvent() {
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cbAutologin.setChecked(false);
                }
            }
        });


        cbAutologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbRemember.setChecked(true);
                }
            }
        });
    }

    /**
     * 初始化数据：用户名和密码的回填，自动登录等逻辑
     */
    private void initData() {
        boolean isAuto = SpUtils.getBoolean("isAuto", false);
        boolean isRemember = SpUtils.getBoolean("isRemember", false);
        String username = SpUtils.getString("username", "");
        String password = SpUtils.getString("password", "");
        cbAutologin.setChecked(isAuto);
        cbRemember.setChecked(isRemember);
        if (isAuto) {
            etPwd.setText(password);
            etUsername.setText(username);
            login();//直接进行登录的业务逻辑
        } else if (isRemember) {
            //用户名和密码回填
            etPwd.setText(password);
            etUsername.setText(username);
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_register})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登录页面
                login();
                break;
            case R.id.btn_register://注册页面
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 10);
                break;
        }
    }


    /**
     * 实现登录的业务逻辑
     */
    private void login() {
        String pwdStr = etPwd.getText().toString();
        String usernameStr = etUsername.getText().toString();
        if (TextUtils.isEmpty(pwdStr) || TextUtils.isEmpty(usernameStr)) {
            AppClient.showToast("用户名或密码不能为空");
            return;
        }

        List<Customer> customerList = CustomerDBUtils.query(CustomerDao.Properties.Username.eq(usernameStr));
        if (customerList.size() > 0) {
            Customer customer = customerList.get(0);
            String password = customer.getPassword();
            if (password.equals(pwdStr)) { //用户名和密码都正确
                int level = customer.getLevel();//判断用户等级，是管理员还是普通用户
                if (level == 1) {//管理员等级
                    startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
                }
                finish();
                //记录登录用户的信息
                SpUtils.putString("username", usernameStr);
                SpUtils.putString("userId", customer.getUserId());
                SpUtils.putString("password", pwdStr);
                SpUtils.putString("chName", customer.getChName());
                SpUtils.putBoolean("isAuto", cbAutologin.isChecked());
                SpUtils.putBoolean("isRemember", cbRemember.isChecked());
            } else {
                AppClient.showToast("用户名或密码有误！");
            }
        } else {
            AppClient.showToast("用户不存在");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == 10) {
            etUsername.setText(data.getStringExtra("username"));
            etPwd.setText(data.getStringExtra("password"));
        }

    }

}
