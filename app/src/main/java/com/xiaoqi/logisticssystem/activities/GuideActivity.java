package com.xiaoqi.logisticssystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.ping.greendao.gen.CustomerDao;
import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Customer;
import com.xiaoqi.logisticssystem.bean.Express;
import com.xiaoqi.logisticssystem.db.CustomerDBUtils;
import com.xiaoqi.logisticssystem.db.ExpressDBUtils;

import java.util.List;
import java.util.NavigableMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 欢迎页面
 */
public class GuideActivity extends AppCompatActivity {
    private static final String[] types = new String[]{"圆通", "申通", "韵达", "中通", "EMS", "天天", "汇通", "邮政"};
    private static final String[] payType = new String[]{"在线支付", "到付", "面对面"};

    @BindView(R.id.cl_main)
    RelativeLayout clMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.guide);
        clMain.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                List<Customer> customers = CustomerDBUtils.queryAll();

                //如果没有用户的话，进行默认创建
                if (customers.size() < 1) {
                    //初始化一个普通用户和一个管理员用户
                    Customer customer = new Customer();
                    customer.setChName("席嘉婧");
                    customer.setLevel(2);
                    customer.setPassword("123456");
                    customer.setPhone("15255556666");
                    customer.setUsername("customer");
                    customer.setAddress("上海建桥学院");
                    customer.setUserId(UUID.randomUUID().toString());


                    CustomerDBUtils.insert(customer);

                    Customer manager = new Customer();
                    manager.setChName("管理");
                    manager.setLevel(1);
                    manager.setPassword("123456");
                    manager.setPhone("15355556666");
                    manager.setUsername("manager");
                    manager.setUserId(UUID.randomUUID().toString());
                    manager.setAddress("法国浪漫街道101号");
                    CustomerDBUtils.insert(manager);
                }


                //如果没有快递的话,进行默认创建
                int size = ExpressDBUtils.queryAll().size();
                if (size < 1) {
                    List<Customer> datas = CustomerDBUtils.query(CustomerDao.Properties.Username.eq("customer"));
                    Customer customer = datas.get(0);
                    String userId = customer.getUserId();

                    for (int i = 0; i < 10; i++) {
                        Express express = new Express();
                        express.setUserId(userId);
                        express.setType(types[i % types.length]);
                        express.setSendPhone("15566667777");
                        express.setChName("席嘉婧");
                        express.setTrackNumber("2018121212" + i);
                        express.setSendAddress("江苏南京");
                        express.setStatus(0);
                        express.setPayType(payType[i % payType.length]);
                        express.setName("小米手机" + i);
                        express.setReceName("张三");
                        express.setReceAddress("上海建桥学院");
                        express.setRecePhone("15577776666");
                        ExpressDBUtils.insert(express);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //跳转到登录界面
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
