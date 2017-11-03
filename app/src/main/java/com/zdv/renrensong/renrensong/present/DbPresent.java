package com.zdv.renrensong.renrensong.present;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;

import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.RenRenSongEvaluateInfo;
import com.zdv.renrensong.renrensong.db.RenRenSongDBUtil;
import com.zdv.renrensong.renrensong.view.IDBContentView;
import com.zdv.renrensong.renrensong.view.IDBEvaluateView;
import com.zdv.renrensong.renrensong.view.IView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/4/6.
 */

public class DbPresent implements IDbPresent {
    private IView iView;
    private Context context;
    private IDbPresent iRequestMode;
    private static RenRenSongDBUtil dbUtil;
    private static DbPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private DbPresent(Context context_) {
        context = context_;
    }

    public static synchronized DbPresent getInstance(Context context) {
        if (instance == null) {
            dbUtil = new RenRenSongDBUtil(context);
            return new DbPresent(context);
        }
        return instance;
    }


    @Override
    public void QueryEvaluate() {
        Observable.just(dbUtil.listAllEvaluate())
                .onErrorReturn(s -> new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBEvaluateView) iView).ResolveDBEvaluateData(s));
    }

    @Override
    public void InsertReplaceEvaluate(RenRenSongEvaluateInfo item) {
        Observable.just(dbUtil.insertOrReplaceEvaluate(item))
                .onErrorReturn(s -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBEvaluateView) iView).ResolveDBEvaluateInsertData(s));
    }

    @Override
    public void DeleteEvaluate(RenRenSongEvaluateInfo item) {
        Observable.just(dbUtil.deleteEvaluate(item))
                .onErrorReturn(s -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBEvaluateView) iView).ResolveDBEvaluateDeleteData(s));
    }


    @Override
    public void QueryContent() {
        Observable.just(dbUtil.listAllContent())
                .onErrorReturn(s -> new ArrayList<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBContentView) iView).ResolveDBContentData(s));
    }

    @Override
    public void InsertReplaceContent(RenRenSongContentInfo item) {
        Observable.just(dbUtil.insertOrReplaceContent(item))
                .onErrorReturn(s -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBContentView) iView).ResolveDBContentInsertData(s));
    }

    @Override
    public void DeleteContent(RenRenSongContentInfo item) {
        Observable.just(dbUtil.deleteContent(item))
                .onErrorReturn(s -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IDBContentView) iView).ResolveDBContentDeleteData(s));
    }
}
