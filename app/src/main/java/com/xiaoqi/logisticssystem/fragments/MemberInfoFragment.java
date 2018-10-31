package com.xiaoqi.logisticssystem.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Customer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;

public class MemberInfoFragment extends Fragment {
    @BindView(R.id.lv_info)
    ListView lvInfo;
    Unbinder unbinder;
    private List<Customer> customers;
    private MyAdapter myAdapter;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_member, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    /**
     * item点击事件
     *
     * @param position
     */
    @OnItemClick(R.id.lv_info)
    void OnItemClick(int position) {
        final Customer customer = customers.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.item_dialog_update, null);
        builder.setView(view);
        final EditText etPhone;
        final EditText etChname;
        final EditText etAddress;
        Button btnFinash;
        Button btnQuxiao;
        etPhone = view.findViewById(R.id.et_phone);
        etChname = view.findViewById(R.id.et_chname);
        etAddress = view.findViewById(R.id.et_address);
        btnFinash = view.findViewById(R.id.btn_finash);
        btnQuxiao = view.findViewById(R.id.btn_quxiao);

        etPhone.setText(customer.getPhone());
        etAddress.setText(customer.getAddress());
        etChname.setText(customer.getChName());

        btnFinash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                String chName = etChname.getText().toString();

                if (TextUtils.isEmpty(chName) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)) {
                    AppClient.showToast("人员信息不能为空");
                    return;
                }
                etChname.setText(chName);
                etAddress.setText(address);
                etPhone.setText(phone);
                customer.setChName(chName);
                customer.setAddress(address);
                customer.setPhone(phone);
                AppClient.getDaoSession().getCustomerDao().update(customer);
                AppClient.showToast("更新成功");
                myAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

        btnQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 长按删除事件
     *
     * @param position
     * @return
     */
    @OnItemLongClick(R.id.lv_info)
    boolean OnItemLongClick(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("删除")
                .setMessage("确认删除" + customers.get(position).getChName() + "吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppClient.getDaoSession().getCustomerDao().delete(customers.get(position));
                        AppClient.showToast("成功删除");
                        customers.remove(customers.get(position));
                        myAdapter.notifyDataSetChanged();//跟新数据
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        return true;
    }


    private void initData() {
        customers = AppClient.getDaoSession().getCustomerDao().loadAll();
        myAdapter = new MyAdapter();
        lvInfo.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return customers.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_lv_member, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Customer customer = customers.get(position);
            holder.tv1.setText(customer.getChName());
            holder.tv2.setText(customer.getPhone());
            holder.tv3.setText(customer.getAddress());
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv1)
            TextView tv1;
            @BindView(R.id.tv2)
            TextView tv2;
            @BindView(R.id.tv3)
            TextView tv3;


            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
