package com.zdv.renrensong.renrensong.model;


import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongEvaluateResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongSendResponse;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * Info:接口实现类
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:42
 */

public class RequestModelImpl implements IRequestMode {
    IRequestMode iRequestMode;


    @Override
    public Flowable<RenRenSongCodeResponse> QueryRegister(@Query("mobile") String mobile, @Query("code") String code) {
        return iRequestMode.QueryRegister(mobile,code);
    }

    @Override
    public Flowable<ResponseBody> QueryQcode(@Query("mobile") String mobile) {
        return iRequestMode.QueryQcode(mobile);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryCode(@Query("mobile") String mobile) {
        return iRequestMode.QueryCode(mobile);
    }

    @Override
    public Flowable<ResponseBody> QueryLogin(@Field("mobile") String mobile, @Field("user_pass") String user_pass
    , @Field("code") String code, @Field("session_id") String session_id) {
        return iRequestMode.QueryLogin(mobile,user_pass,code,session_id);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryForget(@Field("mobile") String mobile, @Field("code") String code, @Field("new_password") String user_pass) {
        return iRequestMode.QueryForget(mobile, code, user_pass);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryAlter(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("old_password") String old_password,@Field("new_password") String new_password) {
        return iRequestMode.QueryAlter(userID, TOKEN, old_password,new_password);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryEmail(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("email") String email) {
        return iRequestMode.QueryEmail(userID,TOKEN,email);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryVerify(@Field("userID") String userID, @Field("TOKEN") String TOKEN, @Field("real_name") String real_name, @Field("id_card_num") String id_card_num) {
        return iRequestMode.QueryVerify(userID, TOKEN, real_name, id_card_num);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryCodeVerify(@Query("mobile") String mobile, @Query("code") String code) {
        return iRequestMode.QueryCodeVerify(mobile, code);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryChangePhone(@Query("mobile") String mobile, @Query("code") String code, @Query("useID") String useID, @Query("TOKEN") String TOKEN) {
        return iRequestMode.QueryChangePhone(mobile, code, useID, TOKEN);
    }

    @Override
    public Flowable<RenRenSongContentResponse> QueryFetchData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("receiver_id") String receiver_id, @Query("start") int start, @Query("count") int  count) {
        return iRequestMode.QueryFetchData(userID, TOKEN, receiver_id, start, count);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryFetchOrderOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id, @Query("deliverer") String deliverer) {
        return iRequestMode.QueryFetchOrderOrder(userID, TOKEN, order_id, deliverer);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryFetchCancelOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id, @Query("deliverer") String deliverer) {
        return iRequestMode.QueryFetchCancelOrder(userID, TOKEN, order_id, deliverer);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryFetchFetchedOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id, @Query("order_num") String order_num, @Query("receiver_id") String receiver_id) {
        return iRequestMode.QueryFetchFetchedOrder(userID, TOKEN, order_id, order_num, receiver_id);
    }

    @Override
    public Flowable<RenRenSongContentResponse> QuerySendData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("sender_id") String sender_id) {
        return iRequestMode.QuerySendData(userID, TOKEN, sender_id);
    }

    @Override
    public Flowable<RenRenSongContentResponse> QueryApointData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("receiver_id") String receiver_id) {
        return iRequestMode.QueryApointData(userID, TOKEN, receiver_id);
    }

    @Override
    public Flowable<RenRenSongSendResponse> QueryReceiveData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id, @Query("order_num") String order_num, @Query("sender_id") String sender_id) {
        return iRequestMode.QueryReceiveData(userID, TOKEN, order_id, order_num, sender_id);
    }

    @Override
    public Flowable<RenRenSongCodeResponse> QueryPoiData(@Query("userID") String userID, @Query("TOKEN") String TOKEN,@Query("location") String location, @Query("mobile") String mobile, @Query("deliverer_id") String deliverer_id) {
        return iRequestMode.QueryPoiData(userID,TOKEN,location, mobile, deliverer_id);
    }

    @Override
    public Flowable<RenRenSongEvaluateResponse> QueryEvaluateData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("deliverer_id") String deliverer_id) {
        return iRequestMode.QueryEvaluateData(userID, TOKEN, deliverer_id);
    }
}
