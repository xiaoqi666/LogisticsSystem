package com.xiaoqi.logisticssystem.db;

import com.ping.greendao.gen.DaoSession;
import com.ping.greendao.gen.ExpressDao;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.bean.Express;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class ExpressDBUtils {

    private static DaoSession daoSession;
    private static ExpressDao expressDao;

    static {
        daoSession = AppClient.getDaoSession();
        expressDao = daoSession.getExpressDao();
    }


    public static long insert(Express cus) {
        return expressDao.insert(cus);
    }


    public static void delete(Express cus) {
        expressDao.delete(cus);
    }

    public static void update(Express cus) {
        expressDao.update(cus);
    }

    public static List<Express> queryAll() {
        return expressDao.loadAll();
    }


    public static List<Express> query(WhereCondition condition) {
        return expressDao.queryBuilder().where(condition).list();
    }

}
