package com.zdv.renrensong.renrensong.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongEvaluateResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongSendResponse;
import com.zdv.renrensong.renrensong.model.IRequestMode;
import com.zdv.renrensong.renrensong.model.converter.CustomGsonConverter;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.view.IDataEvaluateView;
import com.zdv.renrensong.renrensong.view.IDataOrderView;
import com.zdv.renrensong.renrensong.view.IDataSendView;
import com.zdv.renrensong.renrensong.view.IDataView;
import com.zdv.renrensong.renrensong.view.ILocationView;
import com.zdv.renrensong.renrensong.view.IUserView;
import com.zdv.renrensong.renrensong.view.IView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017/4/6.
 */

public class QueryPresent implements IRequestPresent {
    private IView iView;
    private Context context;
    private IRequestMode iRequestMode;

    private static QueryPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private QueryPresent(Context context_) {
        context = context_;
    }

    public static synchronized QueryPresent getInstance(Context context) {
        if (instance == null) {
            return new QueryPresent(context);
        }
        return instance;
    }

    public void initRetrofit(String url, boolean isXml) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        try {
            if (isXml) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .client(client)
                       // .addConverterFactory(Xm.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            }

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofitOrigin(String url) {
        try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);


        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofit2(String url, boolean isXml) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(CustomGsonConverter.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public void QueryRegister(String mobile_no, String code) {
        iRequestMode.QueryRegister(mobile_no,code)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveRegisterInfo(s));
    }

    @Override
    public void QueryCode(String mobile_no) {
        iRequestMode.QueryCode(mobile_no)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveCodeInfo(s));
    }

    @Override
    public void QueryQcode(String mobile) {
        iRequestMode.QueryQcode(mobile)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveQcodeInfo(s));
    }

    @Override
    public void QueryLogin(String mobile_no, String pw,String code,String session) {
        iRequestMode.QueryLogin(mobile_no,pw,code,session)
                .onErrorReturn(s -> new ResponseBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveLoginInfo(s));
    }

    @Override
    public void QueryForget(String mobile, String code, String user_pass) {
        iRequestMode.QueryForget(mobile,code,user_pass)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveForgetInfo(s));
    }

    @Override
    public void QueryAlter(String useID, String TOKEN, String old_password, String new_password) {
        iRequestMode.QueryAlter(useID,TOKEN,old_password,new_password)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveAlterInfo(s));
    }

    @Override
    public void QueryEmail(String useID, String TOKEN, String email) {
        iRequestMode.QueryEmail(useID,TOKEN,email)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveEmailInfo(s));
    }

    @Override
    public void QueryVerify(String useID, String TOKEN, String real_name, String id_card_num) {
        iRequestMode.QueryVerify(useID,TOKEN,real_name,id_card_num)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveVerifyInfo(s));
    }

    @Override
    public void QueryCodeVerify(String mobile, String code) {
        iRequestMode.QueryCodeVerify(mobile, code)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolveCodeVerifyInfo(s));
    }

    @Override
    public void QueryPhoneChange(String mobile, String code, String useID, String TOKEN) {
        iRequestMode.QueryChangePhone(mobile, code,useID,TOKEN)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IUserView) iView).ResolvePhoneChangeInfo(s));
    }

    @Override
    public void QueryFetch(String userID, String TOKEN, String sender_id, int start,  int  count) {
        iRequestMode.QueryFetchData(userID, TOKEN, sender_id, start, count)
                .onErrorReturn(s -> new RenRenSongContentResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataView) iView).ResolveFetchInfo(s));
    }

    @Override
    public void QueryFetchOrderOrder(String userID, String TOKEN, String order_id, String deliverer ) {
        iRequestMode.QueryFetchOrderOrder(userID,TOKEN,order_id,deliverer)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataView) iView).ResolveOrderInfo(s));
    }

    @Override
    public void QueryFetchCancelOrder(String userID, String TOKEN, String order_id, String deliverer) {
        iRequestMode.QueryFetchCancelOrder(userID,TOKEN,order_id,deliverer)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataOrderView) iView).ResolveCancelOrderInfo(s));
    }

    @Override
    public void QueryFetchFetchOrder(String userID, String TOKEN, String order_id, String order_num, String receiver_id) {
        iRequestMode.QueryFetchFetchedOrder( userID,  TOKEN,  order_id,  order_num,  receiver_id)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataOrderView) iView).ResolveOrderOrderInfo(s));
    }

    @Override
    public void QueryApointData(String userID, String TOKEN, String deliverer) {
        iRequestMode.QueryApointData( userID,  TOKEN,  deliverer)
                .onErrorReturn(s -> new RenRenSongContentResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataOrderView) iView).ResolveApointDateInfo(s));
    }

    @Override
    public void QueryReceiveOrder(String userID, String TOKEN, String order_id, String order_num, String sender_id) {
        iRequestMode.QueryReceiveData( userID,  TOKEN,  order_id,  order_num,  sender_id)
                .onErrorReturn(s -> new RenRenSongSendResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataSendView) iView).ResolveReceiveOrderInfo(s));
    }

    @Override
    public void QuerySend(String userID, String TOKEN, String sender_id) {
        iRequestMode.QuerySendData(userID,TOKEN,sender_id)
                .onErrorReturn(s -> new RenRenSongContentResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataSendView) iView).ResolveSendDateInfo(s));
    }

    @Override
    public void QueryPoi(String userID,String TOKEN,String location, String mobile, String deliverer_id) {
        iRequestMode.QueryPoiData(userID,TOKEN, location,  mobile, deliverer_id)
                .onErrorReturn(s -> new RenRenSongCodeResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((ILocationView) iView).ResolveSendPoi(s));
    }

    @Override
    public void QueryEvaluate(String userID, String TOKEN, String deliverer_id) {
        iRequestMode.QueryEvaluateData(userID, TOKEN, deliverer_id)
                .onErrorReturn(s -> new RenRenSongEvaluateResponse())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDataEvaluateView) iView).ResolveEvaluateDateInfo(s));
    }
}
