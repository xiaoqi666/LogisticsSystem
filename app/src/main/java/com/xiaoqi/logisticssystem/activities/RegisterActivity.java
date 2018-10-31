package com.xiaoqi.logisticssystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ping.greendao.gen.CustomerDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Customer;
import com.xiaoqi.logisticssystem.db.CustomerDBUtils;
import com.xiaoqi.logisticssystem.utils.RegexUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_chname)
    EditText etChname;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.rb_putong)
    RadioButton rbPutong;
    @BindView(R.id.rb_manager)
    RadioButton rbManager;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.et_address)
    EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    /**
     * 进行注册的业务逻辑
     */
    @OnClick(R.id.btn_register)
    void onclick() {
        String phone = etPhone.getText().toString();
        String pwd1 = etPwd.getText().toString();
        String pwd2 = etPwd2.getText().toString();
        String chName = etChname.getText().toString();
        String username = etUsername.getText().toString();
        String address = etAddress.getText().toString();

        //进行非空校验
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(username) || TextUtils.isEmpty(chName) || TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2) || TextUtils.isEmpty(phone)) {
            AppClient.showToast("注册信息不能为空");
            return;
        }

        if (!pwd1.equals(pwd2)) {
            AppClient.showToast("两次密码不一致");
            return;
        }


        if (!RegexUtils.checkMobile(phone)) {
            AppClient.showToast("手机号码不符合规范");
            return;
        }
        List<Customer> usernames = CustomerDBUtils.query(CustomerDao.Properties.Username.eq(username));
        if (usernames.size() > 0) {
            AppClient.showToast("用户已存在");
            return;
        }

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPhone(phone);
        customer.setPassword(pwd1);
        customer.setChName(chName);
        customer.setAddress(address);
        if (rbManager.isChecked())
            customer.setLevel(1);
        if (rbPutong.isChecked())
            customer.setLevel(2);
        CustomerDBUtils.insert(customer);
        Intent intent = new Intent();
        intent.putExtra("username", username);
        intent.putExtra("password", pwd1);
        setResult(10, intent);

        finish();//结束当前activity，在登录页面回填用户名和密码
    }
}
