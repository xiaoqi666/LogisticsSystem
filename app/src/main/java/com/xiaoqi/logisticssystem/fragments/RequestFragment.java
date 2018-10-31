package com.xiaoqi.logisticssystem.fragments;

import android.content.Intent;
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

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.activities.AdviceDetailActivity;
import com.xiaoqi.logisticssystem.bean.AdviceBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;

/**
 * 请求建议页面,可查看用户请求
 */
public class RequestFragment extends Fragment {
    @BindView(R.id.lv_advice)
    ListView lvAdvice;
    Unbinder unbinder;

    private List<AdviceBean> adviceBeans;
    private MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_request, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @OnItemClick(R.id.lv_advice)
    void onItemClick(int position) {
        AdviceBean adviceBean = adviceBeans.get(position);
        Intent intent = new Intent(getActivity(), AdviceDetailActivity.class);
        intent.putExtra("adviceBean", adviceBean);
        startActivity(intent);
    }

    private void initData() {
        adviceBeans = AppClient.getDaoSession().getAdviceBeanDao().loadAll();

        lvAdvice.setVisibility(View.VISIBLE);
        myAdapter = new MyAdapter();
        lvAdvice.setAdapter(myAdapter);


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return adviceBeans.size();
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
                convertView = View.inflate(getActivity(), R.layout.item_lv_advice, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AdviceBean adviceBean = adviceBeans.get(position);
            holder.tvName.setText(adviceBean.getTitle());
            holder.tvCanshu.setText(adviceBean.getContent());
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_canshu)
            TextView tvCanshu;

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
