package com.xiaoqi.logisticssystem.db;

import com.ping.greendao.gen.CustomerDao;
import com.ping.greendao.gen.DaoSession;
import com.xiaoqi.logisticssystem.AppClient;
import com.xiaoqi.logisticssystem.bean.Customer;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * Customer 类的对象操作
 */
public class CustomerDBUtils {
          /*
          insert(User entity)：插入一条记录
          delete(User entity) ：根据实体类删除一条记录
          update(User entity)：更新一条记录

         loadAll()：查询所有记录

        ：返回：List
     */


    private static CustomerDao customerDao;
    private static DaoSession daoSession;

    static {
        daoSession = AppClient.getDaoSession();
        customerDao = daoSession.getCustomerDao();
    }


    public static long insert(Customer cus) {
        return customerDao.insert(cus);
    }


    public static void delete(Customer cus) {
        customerDao.delete(cus);
    }

    public static void update(Customer cus) {
        customerDao.update(cus);
    }

    public static List<Customer> queryAll() {
        return customerDao.loadAll();
    }

    /**
     * CustomerDao.Properties.Name.eq("")
     *
     * @param condition
     * @return
     */
    public static List<Customer> query(WhereCondition condition) {
        return customerDao.queryBuilder().where(condition).list();
    }


}
