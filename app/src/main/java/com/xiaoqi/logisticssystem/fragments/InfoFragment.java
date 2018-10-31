package com.xiaoqi.logisticssystem.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Express;
import com.xiaoqi.logisticssystem.db.ExpressDBUtils;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InfoFragment extends Fragment {
    @BindView(R.id.lv_info)
    ListView lvInfo;
    Unbinder unbinder;
    private List<Express> expresses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_info, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
//1540109614364

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        expresses = ExpressDBUtils.query(ExpressDao.Properties.UserId.eq(SpUtils.getString("userId", "")));
        lvInfo.setAdapter(new MyAdapter());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return expresses.size();
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
                convertView = View.inflate(getActivity(), R.layout.item_expressinfo, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Express express = expresses.get(position);
            holder.tvExpressnumber.setText(express.getTrackNumber());
            holder.tvGoodname.setText(express.getName());
            holder.tvGoodtype.setText(express.getType());
            holder.tvPaytype.setText(express.getPayType());

            holder.tvSendaddr.setText(express.getSendAddress());
            holder.tvSendname.setText(express.getChName());
            holder.tvSenphone.setText(express.getSendPhone());

            holder.tvReceaddr.setText(express.getReceAddress());
            holder.tvRecename.setText(express.getReceName());
            holder.tvRecephone.setText(express.getRecePhone());
            int status = express.getStatus();
            if (status == 1)
                holder.tv_status.setText("寄送中");
            if (status == 0)
                holder.tv_status.setText("下单完成待寄送");
            if (status == 2)
                holder.tv_status.setText("到达目的地");
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_expressnumber)
            TextView tvExpressnumber;
            @BindView(R.id.tv_goodtype)
            TextView tvGoodtype;
            @BindView(R.id.tv_paytype)
            TextView tvPaytype;
            @BindView(R.id.tv_goodname)
            TextView tvGoodname;
            @BindView(R.id.tv_sendname)
            TextView tvSendname;
            @BindView(R.id.tv_sendaddr)
            TextView tvSendaddr;
            @BindView(R.id.tv_senphone)
            TextView tvSenphone;
            @BindView(R.id.tv_recename)
            TextView tvRecename;
            @BindView(R.id.tv_recephone)
            TextView tvRecephone;
            @BindView(R.id.tv_receaddr)
            TextView tvReceaddr;
            @BindView(R.id.tv_status)
            TextView tv_status;

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
