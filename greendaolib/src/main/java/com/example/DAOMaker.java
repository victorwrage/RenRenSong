package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/8 10:11
 */

public class DAOMaker {

    public static void main(String[] args) {
        //生成数据库的实体类,还有版本号
        Schema schema = new Schema(2, "com.zdv.renrensong.renrensong");
        addEvaluateItem(schema);
        addOrderItem(schema);
        //指定dao
        schema.setDefaultJavaPackageDao("com.zdv.renrensong.renrensong.dao");
        try {
            //指定路径
            new DaoGenerator().generateAll(schema, "D:\\work\\RenRenSong\\app\\src\\main\\java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建评价数据库的表
     *
     * @param schema
     */
    public static void addEvaluateItem(Schema schema) {
        //创建数据库的表
        Entity entity = schema.addEntity("RenRenSongEvaluateInfo");
        //主键 是int类型
        entity.addIdProperty();
        //名称
        entity.addStringProperty("order_id");
        entity.addStringProperty("rating_content");
        entity.addStringProperty("level");
        entity.addStringProperty("r_time");
        entity.addStringProperty("user_id");
        entity.addStringProperty("key_word");
        entity.addStringProperty("rating_person");
        entity.addStringProperty("is_end");
        entity.addBooleanProperty("is_read");
        entity.addStringProperty("evaluate_owner");

    }

    /**
     * 创建评价数据库的表
     *
     * @param schema
     */
    public static void addOrderItem(Schema schema) {
        //创建数据库的表
        Entity entity = schema.addEntity("RenRenSongContentInfo");
        //主键 是int类型
        entity.addIdProperty();
        //名称
        entity.addStringProperty("order_num");
        entity.addStringProperty("order_id");
        entity.addStringProperty("sender_id");
        entity.addStringProperty("organ_id");
        entity.addStringProperty("g_time");
        entity.addStringProperty("end_time");
        entity.addStringProperty("status");
        entity.addStringProperty("item_name");
        entity.addStringProperty("item_type");
        entity.addStringProperty("item_total");
        entity.addStringProperty("item_weight");
        entity.addStringProperty("item_volume");
        entity.addStringProperty("y_address");
        entity.addStringProperty("y_tel");
        entity.addStringProperty("y_name");
        entity.addStringProperty("h_address");
        entity.addStringProperty("h_tel");
        entity.addStringProperty("h_name");
        entity.addStringProperty("create_time");
        entity.addStringProperty("finish_time");
        entity.addStringProperty("r_status");
        entity.addStringProperty("s_status");
        entity.addStringProperty("is_close");
        entity.addStringProperty("is_back");
        entity.addStringProperty("is_inventory");
        entity.addStringProperty("receiver_id");
        entity.addStringProperty("money");
        entity.addStringProperty("pay_status");
        entity.addStringProperty("c_type");
        entity.addStringProperty("gathering_num");
        entity.addStringProperty("pay_type");
        entity.addStringProperty("order_type");
        entity.addStringProperty("remark");
        entity.addStringProperty("s_id");
        entity.addStringProperty("url");
        entity.addStringProperty("distance");
        entity.addBooleanProperty("isShow");
        entity.addBooleanProperty("isOperating");
        entity.addIntProperty("activity_page");
        entity.addStringProperty("order_owner");


    }

}
