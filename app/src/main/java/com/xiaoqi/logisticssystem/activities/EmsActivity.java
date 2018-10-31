package com.xiaoqi.logisticssystem.activities;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.R;
import com.xiaoqi.logisticssystem.bean.Express;
import com.xiaoqi.logisticssystem.db.ExpressDBUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地图显示页面
 */
public class EmsActivity extends AppCompatActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_search)
    Button btnSearch;
    //第一步，初始化LocationClient类
    private LocationClient mLocationClient = null;
    @BindView(R.id.mmap)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private MyLocationData locData;
    private LocationManager locationManager;
    private Location location;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ems);
        ButterKnife.bind(this);
        initMap();//初始化百度地图定位
    }


    /**
     * 搜索按钮的点击事件
     */
    @OnClick(R.id.btn_search)
    void onclick() {
        //获取搜索内容
        String search_content = etContent.getText().toString();
        if (TextUtils.isEmpty(search_content)) {
            AppClient.showToast("搜索内容不能为空");
            return;
        }

        List<Express> expressList = ExpressDBUtils.query(ExpressDao.Properties.TrackNumber.eq(search_content));
        if (expressList.size() < 1) {
            AppClient.showToast("没有查询到快递内容");
            return;
        }

        //进行查询到快递内容的处理
        Express express = expressList.get(0);
        //弹出对话框进行显示
        showDialog(express);
    }

    /**
     * 查询到快递信息，进行对话框显示
     *
     * @param express
     */
    private void showDialog(Express express) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.item_lv_express, null);
        builder.setView(view);
        ListView lv;
        Button btnQuding;
        lv = view.findViewById(R.id.lv);
        btnQuding = view.findViewById(R.id.btn_quding);
        lv.setAdapter(new MyAdapter(express));
        btnQuding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    /*
      private String name;//寄送物品名称
    private String type;//寄送类型

    private String chName;//寄送人
    private String sendAddress;//发件地址
    private String sendPhone;//发件人电话

    private String receAddress;//收件地址
    private String receName;//收件人
    private String recePhone;//收件人电话

    private String trackNumber;//快递单号
     */

    private static final String[] items = new String[]{"单号:", "状态:", "名称:", "类型:", "寄件人:", "寄件地址:"
            , "寄件人电话:", "收件人:", "收件人电话:", "收件地址:"};

    class MyAdapter extends BaseAdapter {
        private Express express;

        public MyAdapter(Express express) {
            this.express = express;
        }

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
                convertView = View.inflate(EmsActivity.this, R.layout.item_lv_wether, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(items[position]);

            switch (items[position]) {
                case "单号:":
                    holder.tvCanshu.setText(express.getTrackNumber());
                    break;
                case "名称:":
                    holder.tvCanshu.setText(express.getName());
                    break;
                case "状态:":
                    int status = express.getStatus();
                    if (status == 1)
                        holder.tvCanshu.setText("寄送中");
                    if (status == 0)
                        holder.tvCanshu.setText("下单完成待寄送");
                    if (status == 2)
                        holder.tvCanshu.setText("到达目的地");
                    break;
                case "类型:":
                    holder.tvCanshu.setText(express.getType());
                    break;
                case "寄件人:":
                    holder.tvCanshu.setText(express.getChName());
                    break;
                case "寄件地址:":
                    holder.tvCanshu.setText(express.getSendAddress());
                    break;
                case "寄件人电话:":
                    holder.tvCanshu.setText(express.getSendPhone());
                    break;
                case "收件人:":
                    holder.tvCanshu.setText(express.getReceName());
                    break;
                case "收件人电话:":
                    holder.tvCanshu.setText(express.getRecePhone());
                    break;
                case "收件地址:":
                    holder.tvCanshu.setText(express.getReceAddress());
                    break;

            }
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


    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mmap);
        mBaiduMap = mMapView.getMap();
        //设置地图类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //设置缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));

        // MapStatusUpdateFactory.zoomTo(20);//设置缩放级别
        //定位罗盘态
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        BDLocationListener listener = new MyLocationListener();
        //此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
        //Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
        mLocationClient = new LocationClient(getApplicationContext());
        //注册位置监听器
        mLocationClient.registerLocationListener(listener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3000; //5秒发送一次
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setNeedDeviceDirect(true); //返回的定位结果包含手机机头方向
        mLocationClient.setLocOption(option);
        mLocationClient.start(); //启动位置请求
        mLocationClient.requestLocation();//发送请求
        // 当不需要定位图层时关闭定位图层
        // mBaiduMap.setMyLocationEnabled(false);
    }


    /**
     * 位置监听器
     * <p/>
     * BDLocationListener接口有1个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
     */
    class MyLocationListener implements BDLocationListener {

        /**
         * 接收位置的信息回调方法
         *
         * @param location
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.d("BaiduLocationApiDem", sb.toString());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            //创建一个图层选项
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            OverlayOptions options = new MarkerOptions().position(latlng).icon(bitmapDescriptor);
            mBaiduMap.addOverlay(options);
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(latlng)
                    .zoom(12)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


}
