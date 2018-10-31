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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Express;
import com.xiaoqi.logisticssystem.utils.RegexUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.Unbinder;

public class ExpressSelectFragment extends Fragment {
    private static final String[] payType = new String[]{"在线支付", "到付", "面对面"};


    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.lv_express)
    ListView lvExpress;
    Unbinder unbinder;
    private static final String[] types = new String[]{"全部", "圆通", "申通", "韵达", "中通", "EMS", "天天", "汇通", "邮政"};
    @BindView(R.id.btn_search)
    Button btnSearch;
    private ExpressDao expressDao;
    private List<Express> datas;
    private MyAdapter myAdapter;
    private List<Express> data;
    private AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_express, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        spType.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_tv_sp, types));
        expressDao = AppClient.getDaoSession().getExpressDao();
        datas = expressDao.loadAll();
        data = new ArrayList<>();
        data.addAll(datas);
        myAdapter = new MyAdapter();
        lvExpress.setAdapter(myAdapter);
    }

    /**
     * item点击事件
     *
     * @param position
     */
    @OnItemClick(R.id.lv_express)
    void OnItemClick(int position) {
        final Express express = data.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.item_send, null);
        builder.setView(view);
        final Spinner spType;
        final Spinner spPay;
        final EditText etGoodname;
        final EditText etSendname;
        final EditText etSendaddr;
        final EditText etSenphone;
        final EditText etRecename;
        final EditText etRecephone;
        final EditText etReceaddr;
        TextView tvTicknumber;
        Button btnCommit;
        Button btn_cancel;

        spType = view.findViewById(R.id.sp_type);
        spPay = view.findViewById(R.id.sp_pay);

        spType.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_tv_sp, types));
        spPay.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_tv_sp, payType));

        String payTypeStr = express.getPayType();
        for (int i = 0; i < payType.length; i++) {
            if (payType[i].equals(payTypeStr)) {
                spPay.setSelection(i);
                break;
            }
        }

        String type = express.getType();
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(type)) {
                spType.setSelection(i);
                break;
            }
        }

        etGoodname = view.findViewById(R.id.et_goodname);
        etSendname = view.findViewById(R.id.et_sendname);
        etSendaddr = view.findViewById(R.id.et_sendaddr);
        etSenphone = view.findViewById(R.id.et_senphone);
        etRecename = view.findViewById(R.id.et_recename);
        etRecephone = view.findViewById(R.id.et_recephone);
        etReceaddr = view.findViewById(R.id.et_receaddr);
        tvTicknumber = view.findViewById(R.id.tv_ticknumber);

        etGoodname.setText(express.getName());
        etSendname.setText(express.getChName());
        etSendaddr.setText(express.getSendAddress());
        etSenphone.setText(express.getSendPhone());
        etRecename.setText(express.getReceName());
        etRecephone.setText(express.getRecePhone());
        etReceaddr.setText(express.getReceAddress());
        tvTicknumber.setText("订单编号:" + express.getTrackNumber());

        btnCommit = view.findViewById(R.id.btn_commit);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodName = etGoodname.getText().toString();
                String receAddr = etReceaddr.getText().toString();
                String recename = etRecename.getText().toString();
                String recephone = etRecephone.getText().toString();
                String sendname = etSendname.getText().toString();
                String sendAddre = etSendaddr.getText().toString();
                String senPhone = etSenphone.getText().toString();


                int selectedItemPosition = spType.getSelectedItemPosition();//获取选中的条目
                String type = types[selectedItemPosition];

                if (TextUtils.isEmpty(goodName) ||
                        TextUtils.isEmpty(receAddr) ||
                        TextUtils.isEmpty(recename) ||
                        TextUtils.isEmpty(recephone) ||
                        TextUtils.isEmpty(sendname) ||
                        TextUtils.isEmpty(sendAddre) ||
                        TextUtils.isEmpty(senPhone)) {
                    AppClient.showToast("寄件信息不能为空");
                    return;
                }

                if (!RegexUtils.checkMobile(recephone) || !RegexUtils.checkMobile(senPhone)) {
                    AppClient.showToast("手机号码不符合规范");
                    return;
                }

                int position = spPay.getSelectedItemPosition();

                express.setName(goodName);
                express.setType(type);
                express.setChName(sendname);
                express.setSendAddress(sendAddre);
                express.setSendPhone(senPhone);
                express.setReceAddress(receAddr);
                express.setRecePhone(recephone);
                express.setReceName(recename);
                express.setPayType(ExpressSelectFragment.payType[position]);

                AppClient.getDaoSession().getExpressDao().update(express);
                AppClient.showToast("修改成功");
                myAdapter.notifyDataSetChanged();
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
    @OnItemLongClick(R.id.lv_express)
    boolean OnItemLongClick(final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("删除")
                .setMessage("确认删除" + data.get(position).getChName() + "吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppClient.getDaoSession().getExpressDao().delete(data.get(position));
                        AppClient.showToast("成功删除");
                        Express express = data.get(position);
                        data.remove(express);
                        datas.remove(express);
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


    /**
     * 查询按钮的点击事件
     */
    @OnClick(R.id.btn_search)
    void onclick() {
        data.clear();
        int position = spType.getSelectedItemPosition();
        if (position != 0) {
            for (int i = 0; i < datas.size(); i++) {
                Express express = datas.get(i);
                if (express.getType().equals(types[position]))
                    data.add(express);
            }
        } else {
            data.addAll(datas);
        }

        String s = etSearch.getText().toString();
        if (s.matches("\\d+")) {
            for (int i = 0; i < data.size(); i++) {
                Express express = data.get(i);
                if (!express.getTrackNumber().contains(s)) {
                    data.remove(express);
                    i--;
                }
            }
        } else {
            for (int i = 0; i < data.size(); i++) {
                Express express = data.get(i);
                if (!express.getChName().contains(s)) {
                    data.remove(express);
                    i--;
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
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
            Express express = data.get(position);
            holder.tv1.setText(express.getTrackNumber());
            holder.tv2.setText(express.getChName());
            holder.tv3.setText(express.getReceName());
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
