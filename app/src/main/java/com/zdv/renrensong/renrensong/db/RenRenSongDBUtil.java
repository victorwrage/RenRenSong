package com.zdv.renrensong.renrensong.db;

import android.content.Context;

import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/8 10:34
 */

public class RenRenSongDBUtil {

    //TAG
    private static final String TAG = RenRenSongDBUtil.class.getSimpleName();

    private DAOManager daoManager;

    //构造方法
    public RenRenSongDBUtil(Context context) {
        daoManager = DAOManager.getInstance();
        daoManager.initManager(context);
    }

    /**
     * 对数据库中student表的插入操作
     *
     * @param item
     * @return
     */
    public boolean insertEvaluate(RenRenSongEvaluateInfo item) {
        boolean flag = false;

        flag = daoManager.getDaoSession().insert(item) != -1 ? true : false;
        return flag;
    }
    /**
     * 对数据库中student表的插入操作
     *
     * @param item
     * @return
     */
    public boolean insertContent(RenRenSongContentInfo item) {
        boolean flag = false;

        flag = daoManager.getDaoSession().insert(item) != -1 ? true : false;
        return flag;
    }

    public boolean insertOrReplaceEvaluate(RenRenSongEvaluateInfo item) {

        boolean flag = false;
        try {
            flag = daoManager.getDaoSession().insertOrReplace(item) != -1 ? true : false;

        }catch(Exception e){
            e.fillInStackTrace();
        }
        return flag;
    }
    public boolean insertOrReplaceContent(RenRenSongContentInfo item) {

        boolean flag = false;
        try {
            flag = daoManager.getDaoSession().insertOrReplace(item) != -1 ? true : false;

        }catch(Exception e){
            e.fillInStackTrace();
        }
        return flag;
    }
    /**
     * 修改
     * @param
     * @return
     */
    public boolean updateEvaluateItem(RenRenSongEvaluateInfo item) {

        boolean flag = false;
        try {
            daoManager.getDaoSession().update(item);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改
     * @param
     * @return
     */
    public boolean updateContentItem(RenRenSongContentInfo item) {

        boolean flag = false;
        try {
            daoManager.getDaoSession().update(item);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteEvaluate(RenRenSongEvaluateInfo item) {

        boolean flag = false;
        try {
            //删除指定ID
            daoManager.getDaoSession().delete(item);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //daoManager.getDaoSession().deleteAll(); //删除所有记录
        return flag;
    }
    public boolean deleteContent(RenRenSongContentInfo item) {

        boolean flag = false;
        try {
            //删除指定ID
            daoManager.getDaoSession().delete(item);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //daoManager.getDaoSession().deleteAll(); //删除所有记录
        return flag;
    }

    /**
     * 查询单条
     *
     * @param key
     * @return
     */
    public RenRenSongEvaluateInfo listOneEvaluateItem(long key) {
        return daoManager.getDaoSession().load(RenRenSongEvaluateInfo.class, key);
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<RenRenSongEvaluateInfo> listAllEvaluate() {
        return daoManager.getDaoSession().loadAll(RenRenSongEvaluateInfo.class);
    }
    /**
     * 全部查询
     *
     * @return
     */
    public List<RenRenSongContentInfo> listAllContent() {
        return daoManager.getDaoSession().loadAll(RenRenSongContentInfo.class);
    }

    /**
     * 原生查询
     */
    public void queryEvaluateNative() {
        //查询条件
        String where = "where name like ? and _id > ?";
        //使用sql进行查询
        List<RenRenSongEvaluateInfo> list = daoManager.getDaoSession().queryRaw(RenRenSongEvaluateInfo.class, where,
                new String[]{"%l%", "6"});
        KLog.i(TAG, list + "");
    }

    /**
     * 原生查询
     */
    public void queryContentNative() {
        //查询条件
        String where = "where name like ? and _id > ?";
        //使用sql进行查询
        List<RenRenSongContentInfo> list = daoManager.getDaoSession().queryRaw(RenRenSongContentInfo.class, where,
                new String[]{"%l%", "6"});
        KLog.i(TAG, list + "");
    }

    /**
     * QueryBuilder查询大于
     */
    public void queryBuilder() {
        //查询构建器
        QueryBuilder<RenRenSongEvaluateInfo> queryBuilder = daoManager.getDaoSession().queryBuilder(RenRenSongEvaluateInfo.class);
        //查询年龄大于19的北京
      //  List<SynergyOrderItem> list = queryBuilder.where(SynergyOrderItemDao.Properties.Age.ge(19)).where(SynergyOrderItemDao.Properties.Address.like("北京")).list();
    //    KLog.i(TAG, list + "");
    }
}
