package com.xiaoqi.logisticssystem.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.fragments.AdviceFragment;
import com.xiaoqi.logisticssystem.fragments.ChangeInfoFragment;
import com.xiaoqi.logisticssystem.fragments.InfoFragment;
import com.xiaoqi.logisticssystem.fragments.SendFragment;
import com.xiaoqi.logisticssystem.fragments.WelcomeFragment;
import com.xiaoqi.logisticssystem.utils.SpUtils;
import com.xiaoqi.logisticssystem.view.MyImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 普通用户登录后的主界面
 */
public class CustomerActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.left_menu)
    ListView leftMenu;
    @BindView(R.id.dl_left)
    DrawerLayout dlLeft;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.iv_touxiang)
    MyImageView ivTouxiang;
    @BindView(R.id.tv_name)
    TextView tvName;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean isFirst = true;

    @Override
    protected void onStart() {
        super.onStart();
        if (isFirst) {
            isFirst = false;
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessage(String name) {//此处不用public,打包后正式版本会报错
        tvName.setText(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);
        initView();
        initData();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WelcomeFragment()).commit();
    }

    /**
     * 初始化侧边栏数据
     */
    private void initData() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.touxiang);
        ivTouxiang.setBitmap(bitmap);
        //  ivTouxiang.setmOuterRing(56);
        //  ivTouxiang.setColor(Color.RED);
        //  ivTouxiang.setOuterRingAlpha(50);
        tvName.setText(SpUtils.getString("chName", ""));
        //设置适配器
        leftMenu.setAdapter(new MyAdapter());

    }

    /**
     * 初始化侧边栏
     */
    private void initView() {
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, dlLeft, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        dlLeft.setDrawerListener(mDrawerToggle);

    }

    @OnItemClick(R.id.left_menu)
    void onItemClick(int position) {
        switch (items[position]) {
            case "快递查询":
                startActivity(new Intent(CustomerActivity.this, EmsActivity.class));
                dlLeft.closeDrawers();
                break;
            case "急速寄件":
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new SendFragment()).commit();
                dlLeft.closeDrawers();
                break;
            case "寄件信息":
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new InfoFragment()).commit();
                dlLeft.closeDrawers();
                break;
            case "留言建议":
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new AdviceFragment()).commit();
                dlLeft.closeDrawers();
                break;
            case "信息修改":
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ChangeInfoFragment()).commit();
                dlLeft.closeDrawers();
                break;
            case "用户退出":
                startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
                finish();
                SpUtils.putBoolean("isAuto", false);
                break;
        }
    }


    /**
     * 侧边栏数据
     */
    private static final String[] items = new String[]{"快递查询", "急速寄件", "寄件信息", "留言建议", "信息修改", "用户退出"};

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
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
                convertView = View.inflate(CustomerActivity.this, R.layout.item_tv, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItem.setText(items[position]);
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_item)
            TextView tvItem;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
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
