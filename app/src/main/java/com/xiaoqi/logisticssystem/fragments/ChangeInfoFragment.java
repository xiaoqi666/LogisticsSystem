package com.xiaoqi.logisticssystem.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ping.greendao.gen.CustomerDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Customer;
import com.xiaoqi.logisticssystem.db.CustomerDBUtils;
import com.xiaoqi.logisticssystem.utils.RegexUtils;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChangeInfoFragment extends Fragment {
    @BindView(R.id.et_chname)
    EditText etChname;
    @BindView(R.id.et_phone)
    EditText etPhone;
    Unbinder unbinder;
    @BindView(R.id.btn_queren)
    Button btnQueren;
    @BindView(R.id.et_address)
    EditText etAddress;
    private List<Customer> username;
    private Customer customer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_chageinfo, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username = CustomerDBUtils.query(CustomerDao.Properties.Username.eq(SpUtils.getString("username", "")));
        if (username.size() > 0) {
            customer = username.get(0);
            etChname.setText(customer.getChName());
            etPhone
                    .setText(customer.getPhone());
            etAddress.setText(customer.getAddress());
        }

    }

    @OnClick(R.id.btn_queren)
    void onClick() {
        if (customer != null) {
            String chname = etChname.getText().toString();
            String phone = etPhone.getText().toString();
            String address = etAddress.getText().toString();

            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(chname) || TextUtils.isEmpty(phone)) {
                AppClient.showToast("信息不能为空");
                return;
            }

            if (!RegexUtils.checkMobile(phone)) {
                AppClient.showToast("手机号码不符合规范");
                return;
            }
            customer.setPhone(phone);
            customer.setChName(chname);
            customer.setAddress(address);
            CustomerDBUtils.update(customer);
            AppClient.showToast("更新成功");
            EventBus.getDefault().post(chname);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
