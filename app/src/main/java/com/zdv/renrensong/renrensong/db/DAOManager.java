package com.zdv.renrensong.renrensong.db;

import android.content.Context;

import com.zdv.renrensong.renrensong.dao.DaoMaster;
import com.zdv.renrensong.renrensong.dao.DaoSession;


/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/8 10:23
  * 实现功能
 * 1.创建数据库
 * 2.创建数据库的表
 * 3.对数据库的升级
 * 4.对数据库的增删查改
 */

public class DAOManager {
    //TAG
    public static final String TAG = DAOManager.class.getSimpleName();
    //数据库名称
    private static final String DB_NAME = "baibau.db";
    //多线程访问
    private volatile static DAOManager manager;

    //操作类
    private static DaoMaster.DevOpenHelper helper;
    //上下文
    private Context mContext;
    //核心类
    private static DaoMaster daoMaster;
    private DaoSession daoSession;

    //单例模式
    public static DAOManager getInstance() {
        DAOManager instance = null;
        if (manager == null) {
            synchronized (DAOManager.class) {
                if (instance == null) {
                    instance = new DAOManager();
                    manager = instance;
                }
            }
        }else{
            instance=manager;
        }
        return instance;
    }

    //传递上下文
    public void  initManager(Context context){
        this.mContext = context;
    }

    /**
     * 判断是否存在数据库，如果没有则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            helper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 完成对数据库的操作，只是个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 打开输出日志，默认关闭
     */
    public void setDebug() {
  //      QueryBuilder.LOG_SQL = true;
  //      QueryBuilder.LOG_VALUES = true;
    }

    /**
     * 关闭DaoSession
     */
    public void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    /**
     * 关闭Helper
     */
    public void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

    /**
     * 关闭所有的操作
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }


}
