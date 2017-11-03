package com.zdv.renrensong.renrensong.fragment;


import android.content.Context;
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

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RenRenSongContentInfo;
import com.zdv.renrensong.renrensong.adapter.ApointItemAdapter;
import com.zdv.renrensong.renrensong.adapter.ChoiceSpinnerAdapter;
import com.zdv.renrensong.renrensong.bean.IAppointAdapterItemClick;
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
import com.zdv.renrensong.renrensong.view.IDataOrderView;
import com.zdv.renrensong.renrensong.view.IFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**

 * @author xiaoyl
 * @date 2013-07-20
 */
public class Fragment4 extends BaseFragment implements IFragment ,IDataOrderView,IDBContentView, IAppointAdapterItemClick {
	private String title = "已预约";
	RecyclerViewWithEmpty mRecyclerView;

	CustomSinnper spinner_order,spinner_scope;
	ChoiceSpinnerAdapter adapter_scope,adapter_order;
	RelativeLayout empty_lay;
	TextView header_count,empty_tv;
	ImageView empty_iv;
	IFragment4Listtener listtener;
	QueryPresent present;
	Utils util;
	public boolean isInit = false;
	protected Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case REFRESH_ADAPTER_SUCCESS:
					adapter.notifyDataSetChanged();
					header_count.setText(data.size() + "件货物");
					VToast.toast(getContext(), msg.obj+"");
					setEmptyStatus(false);
					break;
				case REFRESH_ADAPTER_FAIL:

					VToast.toast(getContext(), msg.obj+"");
					break;
				case REFRESH_ADAPTER_FAIL_NETWORK:
					setEmptyStatus(true);
					VToast.toast(getContext(), msg.obj+"");
					break;
			}
		}
	};
	@Override
	public void onAttach(Context context) {
		try {
			listtener = (IFragment4Listtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
		super.onAttach(context);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment4_layout, container,false);
		data = new ArrayList<>();
		delete_temp = new ArrayList<>();
		adapter = new ApointItemAdapter(data,getActivity(),Fragment4.this);
		//AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(adapter,mRecyclerView);
		mRecyclerView = (RecyclerViewWithEmpty) view.findViewById(R.id.recycle_view);
		header_count = (TextView) view.findViewById(R.id.header_count);

		empty_iv = (ImageView) view.findViewById(R.id.empty_iv);
		empty_tv = (TextView) view.findViewById(R.id.empty_tv);
		empty_lay = (RelativeLayout) view.findViewById(R.id.empty_lay);

		spinner_scope = (CustomSinnper) view.findViewById(R.id.spinner_scope);
		spinner_order = (CustomSinnper) view.findViewById(R.id.spinner_order);
		adapter_scope = new ChoiceSpinnerAdapter(getActivity(),scopes);
		spinner_scope.setAdapter(adapter_scope);
		spinner_scope.setOnItemSeletedListener((parent,v, position,  id)->changeScope(position));

		adapter_order = new ChoiceSpinnerAdapter(getActivity(),orders);
		spinner_order.setAdapter(adapter_order);
		spinner_order.setOnItemSeletedListener((parent,v, position,  id)->changeOrder(position));

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		//mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
		//mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
		mRecyclerView.setEmptyView(empty_lay);
		mRecyclerView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		KLog.v("onViewCreated");
		initDate();
	}

	public void initDate() {

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		present.setView(Fragment4.this);

		dbPresent = DbPresent.getInstance(getActivity());
		dbPresent.setView(Fragment4.this);

		dbPresent.QueryContent();
	}

	public void fetchFromNetWork() {
		present.initRetrofit(Constant.URL_RENRENSONG, false);
		present.QueryApointData(Constant.user_info.get(Constant.USER_INFO_ID),
				Constant.user_info.get(Constant.USER_INFO_TOKEN), Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));
	}

	private void setEmptyStatus(boolean isOffLine){
		if(isOffLine) {
			empty_iv.setImageResource(R.drawable.netword_error);
			empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
			empty_lay.setEnabled(true);
			RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
		}else{
			empty_lay.setEnabled(false);
			empty_iv.setImageResource(R.drawable.null_ico);
			empty_tv.setText("没有东东哦");
		}
	}

	private void emptyClick(){
		showWaitDialog("努力加载中...");
		fetchFromNetWork();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void ResolveApointDateInfo(RenRenSongContentResponse info) {
        hideWaitDialog();
		if(info.getStatus() ==null){
			setEmptyStatus(true);
			VToast.toast(getContext(),"网络错误");
			return;
		}

		setEmptyStatus(false);
		duplicatedData(info,3);
		listtener.onDataLoaded4();
		changeScope(spinner_scope.getSelectedPosition()==-1?0:spinner_scope.getSelectedPosition());//显示全部
		changeOrder(spinner_order.getSelectedPosition()==-1?0:spinner_order.getSelectedPosition());

		adapter.notifyDataSetChanged();
		header_count.setText(data.size() + "件货物");
		isInit = true;

	}

	@Override
	public void ResolveCancelOrderInfo(RenRenSongCodeResponse info) {
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
					data.remove(renRenSongContentInfo);
					Message msg = new Message();
					msg.what = REFRESH_ADAPTER_SUCCESS;
					msg.obj = info.getInfo();
					handler.sendMessage(msg);
					listtener.onDataCancelChanged4();
					KLog.v(info.toString());
					return;
				}).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe();


	}

	@Override
	public void ResolveOrderOrderInfo(RenRenSongCodeResponse info) {
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
					data.remove(renRenSongContentInfo);
					Message msg = new Message();
					msg.what = REFRESH_ADAPTER_SUCCESS;
					msg.obj = info.getInfo();
					handler.sendMessage(msg);
					listtener.onDataFetchChanged4();
					KLog.v(info.toString());
					return;
				}).observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.computation())
				.subscribe();

	}


	@Override
	public void onItemCancelClick(RenRenSongContentInfo i_t) {
		showWaitDialog("正在取消预约...");
		delete_temp.add(i_t);
	}

	@Override
	public void onItemFetchClick(RenRenSongContentInfo i_t) {
		showWaitDialog("正在取件...");
		delete_temp.add(i_t);
	}

	@Override
	public void ResolveDBContentData(List<RenRenSongContentInfo> info) {
		hideWaitDialog();
		for(RenRenSongContentInfo cache_item:info){
			if(cache_item.getActivity_page()==3){
				data.add(cache_item);
			}
		}
		adapter.notifyDataSetChanged();
		header_count.setText(data.size() + "件货物");

		if(data.size()==0){
			showWaitDialog("正在从服务器读取数据|...");
		}

		present.initRetrofit(Constant.URL_RENRENSONG,false);
		present.QueryApointData(Constant.user_info.get(Constant.USER_INFO_ID),
				Constant.user_info.get(Constant.USER_INFO_TOKEN),Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));
	}

	@Override
	public void ResolveDBContentInsertData(boolean isInsert) {

	}

	@Override
	public void ResolveDBContentDeleteData(boolean isDelete) {

	}


	/**
	 *
	 */
	public  interface IFragment4Listtener {
		/**
		 */
		void onDataLoaded4();
		void onDataFetchChanged4();
		void onDataCancelChanged4();
	}

}
