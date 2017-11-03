package com.zdv.renrensong.renrensong.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.adapter.ChoiceSpinnerAdapter;
import com.zdv.renrensong.renrensong.adapter.FetchItemAdapter;
import com.zdv.renrensong.renrensong.bean.IAdapterItemClick;
import com.zdv.renrensong.renrensong.bean.IAdapterLoadDate;
import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.bean.RenRenSongContentResponse;
import com.zdv.renrensong.renrensong.customView.CustomSinnper;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
import com.zdv.renrensong.renrensong.present.DbPresent;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.IDBContentView;
import com.zdv.renrensong.renrensong.view.IDataView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.gmariotti.recyclerview.itemanimator.SlideScaleInOutRightItemAnimator;

public class BaseFetchFragment extends BaseFragment implements IDataView, IAdapterItemClick, IDBContentView, IAdapterLoadDate {
    RecyclerViewWithEmpty mRecyclerView;
    SwipeRefreshLayout swipe_lay;
    CustomSinnper spinner_order, spinner_scope;
    ChoiceSpinnerAdapter adapter_scope, adapter_order;
    RelativeLayout empty_lay;
    TextView header_count, empty_tv;
    ImageView empty_iv;

    IFragmentListener listener;
    QueryPresent present;
    Utils util;
    Disposable temp_dis;

    public boolean isInit = false;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_ADAPTER_SUCCESS:
                    adapter.notifyDataSetChanged();
                    header_count.setText(data.size() + "件货物");
                    VToast.toast(getContext(), msg.obj + "");
                    setEmptyStatus(false);
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
    private boolean isLoading;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 5;
    private boolean isRefresh = true;

    @Override
    public void onAttach(Context context) {
        try {
            listener = (IFragmentListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        data = new ArrayList<>();
        delete_temp = new ArrayList<>();

        mRecyclerView = (RecyclerViewWithEmpty) view.findViewById(R.id.recycle_view);
        swipe_lay = (SwipeRefreshLayout) view.findViewById(R.id.swipe_lay);
        adapter = new FetchItemAdapter(data, getActivity(), BaseFetchFragment.this, mRecyclerView);
        header_count = (TextView) view.findViewById(R.id.header_count);

        empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
        empty_tv = (TextView) view.findViewById(R.id.empty_tv);
        empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);

        spinner_scope = (CustomSinnper) view.findViewById(R.id.spinner_scope);
        spinner_order = (CustomSinnper) view.findViewById(R.id.spinner_order);
        adapter_scope = new ChoiceSpinnerAdapter(getActivity(), scopes);

        spinner_scope.setAdapter(adapter_scope);
        spinner_scope.setOnItemSeletedListener((parent, v, position, id) -> changeScope(position));

        adapter_order = new ChoiceSpinnerAdapter(getActivity(), orders);

        //  AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter,mRecyclerView);
        spinner_order.setAdapter(adapter_order);
        spinner_order.setOnItemSeletedListener((parent, v, position, id) -> changeOrder(position));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setItemAnimator(new SlideScaleInOutRightItemAnimator(mRecyclerView));

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRecyclerView.setEmptyView(empty_lay);
        mRecyclerView.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                KLog.v("test", "totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                    //此时是刷新状态
                    data.add(null);
                    view.post(() -> {
                        // Notify adapter with appropriate notify methods
                        adapter.notifyItemRangeInserted(data.size()-1, 1);
                    });
//                    adapter.notifyDataSetChanged();
                    fetchFromNetWork(start_position,load_count);
                    isLoading = true;
                }
            }
        });
        swipe_lay.setColorSchemeColors(Color.parseColor("#1484c6"));
        swipe_lay.setOnRefreshListener(() -> {
            Refresh();
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KLog.v("onViewCreated");
        initDate();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isInit) {
            showWaitDialog("请稍等...");
        }
    }

    protected void setEmptyStatus(boolean isOffLine) {
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

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork(start_position, load_count);
    }

    @Override
    public void ResolveFetchInfo(RenRenSongContentResponse info) {
        isLoading = false;
        listener.onDataLoaded();
        if(isRefresh){
            swipe_lay.setRefreshing(false);
            data.clear();
            isRefresh = false;
        }
        hideWaitDialog();
        if (info.getStatus() == null) {
            setEmptyStatus(true);
            KLog.v("网络错误");
            VToast.toast(getContext(), "网络错误");
            return;
        }
        if (info.getStatus().equals(SUCCESS)) {
            if (start_position == 0) {
                isInit = true;
            } else {
                data.remove(data.size() -1);
            }
            for (RenRenSongContentInfo i_d : info.getContent()) {
                i_d.setActivity_page(0);
                i_d.setIsOperating(false);
                i_d.setIsShow(true);
                i_d.setOrder_owner(Constant.user_info.get(Constant.USER_INFO_ID));
            }
            data.addAll(info.getContent());
            changeScope(spinner_scope.getSelectedPosition() == -1 ? 0 : spinner_scope.getSelectedPosition());//显示全部
            changeOrder(spinner_order.getSelectedPosition() == -1 ? 0 : spinner_order.getSelectedPosition());
            start_position += info.getContent().size();
            header_count.setText(data.size() + "件货物");
            adapter.notifyDataSetChanged();
            ((FetchItemAdapter) adapter).setMoreStatus(true);
        } else {
            data.add(new RenRenSongContentInfo());//显示已经到底了
            ((FetchItemAdapter) adapter).setMoreStatus(false);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void ResolveOrderInfo(RenRenSongCodeResponse info) {
        KLog.v(info.toString());
        synchronized (lock) {
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
                            fetchFromNetWork(start_position, load_count);//多端操作数据同步
                            Message msg = new Message();
                            msg.what = REFRESH_ADAPTER_FAIL;
                            msg.obj = info.getInfo();
                            handler.sendMessage(msg);
                            return;
                        }
                        data.remove(renRenSongContentInfo);
                        Message msg = new Message();
                        msg.what = REFRESH_ADAPTER_SUCCESS;
                        msg.obj = info.getInfo();
                        handler.sendMessage(msg);
                        listener.onDataChanged();
                        KLog.v(info.toString());
                        return;
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .subscribe();

        }
    }

    public void initDate() {
        if (Constant.user_info == null) return;
        KLog.v("initDate");
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(BaseFetchFragment.this);

        dbPresent = DbPresent.getInstance(getActivity());
        dbPresent.setView(BaseFetchFragment.this);
        //  dbPresent.QueryContent();


        fetchFromNetWork(start_position, load_count);
    }

    /**
     * 1分钟刷一次列表
     */
    private void intervalRefresh() {
        //  int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        if (temp_dis != null) return;
        temp_dis = Observable.interval(0, 1, TimeUnit.MINUTES)
                //  .take(limit + 1)
                //  .map(s -> limit - s.intValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                //  .doOnComplete(() -> complete(type))
                .subscribe(s -> fetchFromNetWork(start_position, load_count));
    }

    public  void Refresh(){
        start_position = 0;
        isRefresh = true;
        fetchFromNetWork(start_position,load_count);
    }
    /**
     * 网络获取
     */
    public void fetchFromNetWork(int start, int count) {
        if (Constant.user_info == null) return;
        KLog.v("start:" + start);
        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryFetch(Constant.user_info.get(Constant.USER_INFO_ID),
                Constant.user_info.get(Constant.USER_INFO_TOKEN), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID), start, count);
    }

    @Override
    public void onItemDateClick(RenRenSongContentInfo i_t) {
        showWaitDialog("正在操作...");
        delete_temp.add(i_t);
    }

    @Override
    public void ResolveDBContentData(List<RenRenSongContentInfo> info) {
//        KLog.v("获取缓存" + info.size());
        hideWaitDialog();
        for (RenRenSongContentInfo cache_item : info) {

            if (cache_item.getActivity_page() == 0) {
                //     data.add(cache_item);
            }
        }
        adapter.notifyDataSetChanged();
        header_count.setText(data.size() + "件货物");

        if (data.size() == 0) {
            showWaitDialog("正在从服务器读取数据|...");
        }
        // intervalRefresh();

    }

    @Override
    public void ResolveDBContentInsertData(boolean isInsert) {

    }

    @Override
    public void ResolveDBContentDeleteData(boolean isDelete) {

    }

    @Override
    public void onLoadMore() {

          fetchFromNetWork(start_position, load_count);
    }


    public interface IFragmentListener {

        void onDataLoaded();

        void onDataChanged();

    }
}
