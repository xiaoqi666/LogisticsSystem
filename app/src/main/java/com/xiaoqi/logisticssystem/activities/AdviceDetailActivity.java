package com.xiaoqi.logisticssystem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ping.greendao.gen.CustomerDao;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.AdviceBean;
import com.xiaoqi.logisticssystem.bean.Customer;
import com.xiaoqi.logisticssystem.db.CustomerDBUtils;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdviceDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_detail);
        ButterKnife.bind(this);
        AdviceBean adviceBean = getIntent().getParcelableExtra("adviceBean");
        tvContent.setText(adviceBean.getContent());
        tvName.setText(adviceBean.getTitle());
        tvTime.setText(adviceBean.getDateStr());

        List<Customer> userId = CustomerDBUtils.query(CustomerDao.Properties.UserId.eq(adviceBean.getUserId()));
        tvUser.setText(userId.get(0).getChName());
        tvPhone.setText(userId.get(0).getPhone());
    }
}
