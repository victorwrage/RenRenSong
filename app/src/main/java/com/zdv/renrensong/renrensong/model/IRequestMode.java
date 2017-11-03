package com.zdv.renrensong.renrensong.model;

import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongEvaluateResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongSendResponse;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xyl on 2017/4/6.
 */

public interface IRequestMode {

    @GET("index.php?g=Api&m=Api&a=register")
    Flowable<RenRenSongCodeResponse> QueryRegister(@Query("mobile") String mobile, @Query("code") String code);

    @GET("index.php?g=Api&m=Api&a=getImgCode")
    Flowable<ResponseBody> QueryQcode(@Query("mobile") String mobile);

    @GET("index.php?g=Api&m=Api&a=mobileCode")
    Flowable<RenRenSongCodeResponse> QueryCode(@Query("mobile") String mobile);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=login")
    Flowable<ResponseBody> QueryLogin(@Field("mobile") String mobile, @Field("user_pass") String user_pass,
                                              @Field("code") String code, @Field("session_id") String session_id);
    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=newPass")
    Flowable<RenRenSongCodeResponse> QueryForget(@Field("mobile") String mobile, @Field("code") String code,@Field("new_password") String user_pass);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=newPass")
    Flowable<RenRenSongCodeResponse> QueryAlter(@Field("userID") String userID, @Field("TOKEN") String TOKEN,@Field("old_password") String old_password,@Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=bindEmail")
    Flowable<RenRenSongCodeResponse> QueryEmail(@Field("userID") String userID, @Field("TOKEN") String TOKEN,@Field("email") String email);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Api&a=realAuth")
    Flowable<RenRenSongCodeResponse> QueryVerify(@Field("userID") String userID, @Field("TOKEN") String TOKEN,@Field("real_name") String real_name,@Field("id_card_num") String id_card_num);

    @GET("index.php?g=Api&m=Api&a=isRCode")
    Flowable<RenRenSongCodeResponse> QueryCodeVerify(@Query("mobile") String mobile, @Query("code") String code);

    @GET("index.php?g=Api&m=Api&a=changeMobile")
    Flowable<RenRenSongCodeResponse> QueryChangePhone(@Query("mobile") String mobile, @Query("code") String code, @Query("useID") String useID,@Query("TOKEN") String TOKEN);

    @GET("index.php?g=Api&m=Api&a=waitReceive")
    Flowable<RenRenSongContentResponse> QueryFetchData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("receiver_id") String receiver_id, @Query("start") int start, @Query("count") int  count);

    @GET("index.php?g=Api&m=Api&a=bookOrder")
    Flowable<RenRenSongCodeResponse> QueryFetchOrderOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id
            , @Query("deliverer") String deliverer);

    @GET("index.php?g=Api&m=Api&a=cancelBookOrder")
    Flowable<RenRenSongCodeResponse> QueryFetchCancelOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id
            , @Query("deliverer") String deliverer);

    @GET("index.php?g=Api&m=Api&a=receiveOrder")
    Flowable<RenRenSongCodeResponse> QueryFetchFetchedOrder(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id
            , @Query("order_num") String order_num , @Query("receiver_id") String receiver_id);

    @GET("index.php?g=Api&m=Api&a=waitSend")
    Flowable<RenRenSongContentResponse> QuerySendData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("sender_id") String sender_id);

    @GET("index.php?g=Api&m=Api&a=getBookOrder")
    Flowable<RenRenSongContentResponse> QueryApointData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("receiver_id") String receiver_id);

    @GET("index.php?g=Api&m=Api&a=signIn")
    Flowable<RenRenSongSendResponse> QueryReceiveData(@Query("userID") String userID, @Query("TOKEN") String TOKEN, @Query("order_id") String order_id
            , @Query("order_num") String order_num, @Query("sender_id") String sender_id );

    @GET("index.php?g=Api&m=Api&a=getLocation")
    Flowable<RenRenSongCodeResponse> QueryPoiData( @Query("userID") String userID,@Query("TOKEN") String TOKEN,@Query("location") String location ,@Query("mobile") String mobile,@Query("deliverer_id") String deliverer_id);

    @GET("index.php?g=Api&m=Api&a=getEvaluate")
    Flowable<RenRenSongEvaluateResponse> QueryEvaluateData(@Query("userID") String userID, @Query("TOKEN") String TOKEN,@Query("deliverer_id") String deliverer_id);


}
