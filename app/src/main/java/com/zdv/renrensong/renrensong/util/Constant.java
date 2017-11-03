package com.zdv.renrensong.renrensong.util;

import java.util.Map;

/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/5/13 14:43
 */

public class Constant {

    //public static final String URL_RENRENSONG = "http://192.168.0.188/QhwCMFX/";//前海微
    public static final String URL_RENRENSONG = "https://www.qianhaiwei.com/rrs/";//前海微


    public static final int DEFAULT_TIMEOUT = 10*60;//超时时间(S)

    public static final int DEFAULT_MESSAGE_TIMEOUT = 120;//短信验证码超时时间(S)
    public static final String PUBLIC_BMOB_KEY = "a75286ead52da3a100a80e9ee2b4616e";


    public static Map<String,String> user_info;
    public static final String USER_INFO_ID = "user_id";//缓存
    public static final String USER_INFO_PHONE = "user_phone";//缓存
    public static final String USER_INFO_TOKEN = "token";//缓存
    public static final String USER_INFO_EMAIL = "user_email";//缓存
    public static final String USER_INFO_IDCARD = "id_card_num";//缓存
    public static final String USER_INFO_NAME = "real_name";//缓存
    public static final String USER_INFO_EMAIL_STATUS = "email_status";//缓存
    public static final String USER_INFO_ISAUTH = "is_auth";//缓存
    public static final String USER_INFO_USER_NAME = "user_name";//缓存
    public static final String USER_INFO_SHOPPER_ID = "shopper_id";//缓存
    public static final String USER_INFO_SESSION_ID = "user_session";//缓存
    public static final String USER_INFO_PW = "user_pw";//缓存

    public static final int FRAGMENT_CLEAR_DATE = 1000;//重新加载数据


    public static boolean willShowLogin = false;
}
