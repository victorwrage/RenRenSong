package com.zdv.renrensong.renrensong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.adapter.ChoiceSpinnerAdapter;
import com.zdv.renrensong.renrensong.adapter.SendItemAdapter;
import com.zdv.renrensong.renrensong.bean.ISendAdapterItemClick;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongSendResponse;
import com.zdv.renrensong.renrensong.customView.CustomSinnper;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
import com.zdv.renrensong.renrensong.present.DbPresent;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.service.PrintBillService;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.EncodingHandler;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.IDBContentView;
import com.zdv.renrensong.renrensong.view.IDataSendView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xyl
 * @info 送件界面
 * @date 2017-05-10
 */
public class FragmentSend extends BaseFragment implements IDataSendView, ISendAdapterItemClick, IDBContentView {

    RecyclerViewWithEmpty mRecyclerView;

    CustomSinnper spinner_order, spinner_scope;
    ChoiceSpinnerAdapter adapter_scope, adapter_order;
    ImageView qcode_scan, qcode_code;
    RelativeLayout empty_lay;
    TextView header_count, empty_tv;
    ImageView empty_iv;
    IFragmentSendListener listener;
    QueryPresent present;
    Utils util;
    DialogFragmentQcode fragmentQcode;
    MyBroadcastReceiver myReiceiver;
    RenRenSongContentInfo cur_item;
    String qcode;
    public boolean isInit = false;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_ADAPTER_SUCCESS:
                    adapter.notifyDataSetChanged();
                    header_count.setText(data.size() + "件货物");
                    VToast.toast(getContext(), ((RenRenSongSendResponse) msg.obj).getInfo());
                    setEmptyStatus(false);
                    qcode = ((RenRenSongSendResponse) msg.obj).getUrl();
                    code();
                    break;
                case REFRESH_ADAPTER_FAIL:

                    VToast.toast(getContext(), msg.obj + "");
                    break;
                case REFRESH_ADAPTER_FAIL_NETWORK:
                    setEmptyStatus(true);
                    VToast.toast(getContext(), msg.obj + "");
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        try {
            listener = (IFragmentSendListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_lay, container, false);

        data = new ArrayList<>();
        delete_temp = new ArrayList<>();
        adapter = new SendItemAdapter(data, getActivity(), FragmentSend.this);
        mRecyclerView = (RecyclerViewWithEmpty) view.findViewById(R.id.recycle_view);
        header_count = (TextView) view.findViewById(R.id.header_count);
        qcode_scan = (ImageView) view.findViewById(R.id.qcode_scan);
        qcode_code = (ImageView) view.findViewById(R.id.qcode_code);

        empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        empty_tv = (TextView) view.findViewById(R.id.empty_tv);
        empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);

        spinner_scope = (CustomSinnper) view.findViewById(R.id.spinner_scope);
        spinner_order = (CustomSinnper) view.findViewById(R.id.spinner_order);
        adapter_scope = new ChoiceSpinnerAdapter(getActivity(), scopes);
        spinner_scope.setAdapter(adapter_scope);
        spinner_scope.setOnItemSeletedListener((parent, v, position, id) -> changeScope(position));

        adapter_order = new ChoiceSpinnerAdapter(getActivity(), orders);
        spinner_order.setAdapter(adapter_order);
        spinner_order.setOnItemSeletedListener((parent, v, position, id) -> changeOrder(position));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter,mRecyclerView);
        // mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setEmptyView(empty_lay);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    private void setEmptyStatus(boolean isOffLine) {
        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.null_ico);
            empty_tv.setText("没有东东哦");
        }
    }

    private void emptyClick() {
        showWaitDialog("努力加载中...");
        fetchFromNetWork();
    }

    private void code() {
        try {
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(qcode,
                    420);
            fragmentQcode = new DialogFragmentQcode();
            fragmentQcode.setQcode(qrCodeBitmap);
            fragmentQcode.setCancelable(false);
            fragmentQcode.show(getFragmentManager(), "");

        } catch (WriterException e) {

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // showWaitDialog("正在加载...");
        KLog.v("onViewCreated" + getUserVisibleHint() + "---" + isInit);
        initDate();
    }


    public void initDate() {
        if (Constant.user_info == null) {
            return;
        }
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(FragmentSend.this);

        dbPresent = DbPresent.getInstance(getActivity());
        dbPresent.setView(FragmentSend.this);

        dbPresent.QueryContent();
        IntentFilter filter = new IntentFilter("android.intent.action.EvaluateBroadcastReceiver");
        myReiceiver = new MyBroadcastReceiver();
        getContext().registerReceiver(myReiceiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myReiceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        setUserVisibleHint(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        KLog.v("onResume" + hidden + "---" + isInit);
        if (!hidden) {
            if (!isInit) {
                fetchFromNetWork();
            }
        }
        setUserVisibleHint(hidden);
    }

    @Override
    public void ResolveSendDateInfo(RenRenSongContentResponse info) {
         hideWaitDialog();
        if (info.getStatus() == null) {
            setEmptyStatus(true);
            VToast.toast(getContext(), "网络错误");
            return;
        }

        setEmptyStatus(false);
        duplicatedData(info, 4);

        changeScope(spinner_scope.getSelectedPosition() == -1 ? 0 : spinner_scope.getSelectedPosition());//显示全部
        changeOrder(spinner_order.getSelectedPosition() == -1 ? 0 : spinner_order.getSelectedPosition());

        adapter.notifyDataSetChanged();
        header_count.setText(data.size() + "件货物");
        isInit = true;
        listener.onSendDataLoaded();
    }

    @Override
    public void ResolveReceiveOrderInfo(RenRenSongSendResponse info) {
        hideWaitDialog();
        String delete_item_id = delete_temp.remove(0).getOrder_id();

        Disposable doAct = Flowable.fromIterable(data).filter(d_i -> d_i.getOrder_id().equals(delete_item_id))
                .take(1).doOnNext(renRenSongContentInfo -> {
                    if (info.getStatus() == null) {
                        renRenSongContentInfo.setIsOperating(false);
                        Message msg = new Message();
                        msg.what = REFRESH_ADAPTER_FAIL_NETWORK;
                        msg.obj = "网络错误";
                        handler.sendMessage(msg);
                        return;
                    }
                    if (!info.getStatus().equals(SUCCESS)) {
                        renRenSongContentInfo.setIsOperating(false);

                        KLog.v(info.getInfo());
                        fetchFromNetWork();//多端操作数据同步
                        Message msg = new Message();
                        msg.what = REFRESH_ADAPTER_FAIL;
                        msg.obj = info.getInfo();
                        handler.sendMessage(msg);
                        return;
                    }
                    cur_item = renRenSongContentInfo;
                    dbPresent.DeleteContent(renRenSongContentInfo);//删除缓存
                    data.remove(renRenSongContentInfo);
                    Message msg = new Message();
                    msg.what = REFRESH_ADAPTER_SUCCESS;

                    msg.obj = info;
                    handler.sendMessage(msg);
                    KLog.v(info.toString());
                    return;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe();


    }


    private void print() {
        String model = android.os.Build.MODEL;
        KLog.v("model" + model);
        if (model.equals("SQ27C")) {//I9000
            printI9000(qcode);
        } else {

        }
    }

    public void fetchFromNetWork() {
        if(Constant.user_info==null){
            return;
        }
        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QuerySend(Constant.user_info.get(Constant.USER_INFO_ID),
                Constant.user_info.get(Constant.USER_INFO_TOKEN), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));
    }

    private void printI9000(String qcode) {

        Intent intentService = new Intent(getActivity(), PrintBillService.class);
        intentService.putExtra("order_num", cur_item.getOrder_num() == null ? "" : cur_item.getOrder_num());
        intentService.putExtra("order_id", cur_item.getOrder_id() == null ? "" : cur_item.getOrder_id());
        intentService.putExtra("receiver_id", cur_item.getReceiver_id() == null ? "" : cur_item.getReceiver_id());
        intentService.putExtra("g_time", cur_item.getG_time() == null ? "" : cur_item.getG_time());
        intentService.putExtra("item_name", cur_item.getItem_name() == null ? "" : cur_item.getItem_name());
        KLog.v("print start");
        intentService.putExtra("QCODE", qcode);
        getActivity().startService(intentService);
    }

    @Override
    public void onItemReceiveClick(RenRenSongContentInfo i_t) {
        showWaitDialog("签收中...");
        delete_temp.add(i_t);
    }


    public void laterRate() {
        print();
    }

    @Override
    public void ResolveDBContentData(List<RenRenSongContentInfo> info) {
        hideWaitDialog();
        for (RenRenSongContentInfo cache_item : info) {
            if (cache_item.getActivity_page() == 4) {
                data.add(cache_item);
            }
        }
        header_count.setText(data.size() + "件货物");
        adapter.notifyDataSetChanged();
        if(data.size()==0){
            showWaitDialog("正在从服务器读取数据|...");
        }
        fetchFromNetWork();
    }

    @Override
    public void ResolveDBContentInsertData(boolean isInsert) {

    }

    @Override
    public void ResolveDBContentDeleteData(boolean isDelete) {

    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String order_id = intent.getStringExtra("order_id");
            KLog.v(order_id);
            if(order_id!=null && !order_id.equals(cur_item.getOrder_id()))return;
            if (intent.getAction().equals("android.intent.action.EvaluateBroadcastReceiver")) {
                if (fragmentQcode != null && fragmentQcode.isVisible()) {
                    fragmentQcode.dismiss();
                    print();
                }
            }
        }
    }

    public interface IFragmentSendListener {
        void onSendDataLoaded();
    }

}
