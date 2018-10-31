package com.xiaoqi.logisticssystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.fragments.DingDanFragment;
import com.xiaoqi.logisticssystem.fragments.ExpressSelectFragment;
import com.xiaoqi.logisticssystem.fragments.MemberInfoFragment;
import com.xiaoqi.logisticssystem.fragments.RequestFragment;
import com.xiaoqi.logisticssystem.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends AppCompatActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.ib_finash)
    ImageButton ibFinash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
        //显示默认页面
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new RequestFragment()).commit();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new RequestFragment()).commit();
                        break;
                    case R.id.rb2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new MemberInfoFragment()).commit();
                        break;
                    case R.id.rb3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ExpressSelectFragment()).commit();
                        break;
                    case R.id.rb4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new DingDanFragment()).commit();
                        break;
                }
            }
        });
    }


    @OnClick(R.id.ib_finash)
    public void onClick() {
        startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
        finish();
        SpUtils.putBoolean("isAuto", false);
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            AppClient.showToast("再按一次退出程序");
            firstTime = secondTime;
        } else {
            finish();
        }
    }


}
