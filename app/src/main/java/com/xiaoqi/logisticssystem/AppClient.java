package com.xiaoqi.logisticssystem;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.ping.greendao.gen.DaoMaster;
import com.ping.greendao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

public class AppClient extends Application {
    public static final boolean ENCRYPTED = true;

    private static DaoSession daoSession;
    private static SharedPreferences sp;
    private static Toast toast;


    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("sp_name", 0);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        SDKInitializer.initialize(getApplicationContext());

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "users-db-encrypted" : "users-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * 弹吐司
     *
     * @param content
     */
    public static void showToast(String content) {
        toast.setText(content);
        toast.show();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    public static SharedPreferences getSp() {
        return sp;
    }
}
