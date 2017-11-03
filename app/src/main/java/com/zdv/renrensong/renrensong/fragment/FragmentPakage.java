package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.adapter.CardItemAdapter;
import com.zdv.renrensong.renrensong.customView.RecyclerViewWithEmpty;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentPakage extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	IPakageListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;
	@Bind(R.id.header_add)
	ImageView header_add;

	@Bind(R.id.empty_lay)
	RelativeLayout empty_lay;
	@Bind(R.id.empty_iv)
	ImageView empty_iv;
	@Bind(R.id.empty_tv)
	TextView empty_tv;

	@Bind(R.id.pakage_cards)
	RecyclerViewWithEmpty pakage_cards;
    CardItemAdapter adapter;
    ArrayList<CardItemAdapter.Card> cards;
	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pakage_lay, container, false);
		ButterKnife.bind(FragmentPakage.this,view);
		cards = new ArrayList<>();
		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishPakage());
		RxView.clicks(header_add).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> add());

		adapter = new CardItemAdapter(cards,getContext(),FragmentPakage.this);
		header_add.setVisibility(View.VISIBLE);

		pakage_cards.setLayoutManager(new LinearLayoutManager(getActivity()));
		pakage_cards.setAdapter(adapter);
		pakage_cards.setEmptyView(empty_lay);
		header_title.setText("我的钱包");

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initData();
	}

	private void add() {
        listtener.pakageAdd();
	}

	private void setEmptyStatus(boolean isOffLine){
		if(isOffLine) {
			empty_iv.setImageResource(R.drawable.netword_error);
			empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
			empty_lay.setEnabled(true);
			RxView.clicks(empty_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
		}else{
			empty_lay.setEnabled(false);
			empty_iv.setImageResource(R.drawable.null_ico);
			empty_tv.setText("暂时没有绑定银行卡");
		}
	}

	private void emptyClick(){
		showWaitDialog("努力加载中...");
		initData();
	}


	private void initData(){
		for(int s=0;s<5;s++) {
			CardItemAdapter.Card card = adapter.new Card();
			card.setCard_bg(R.drawable.bank_contruct_bg);
			card.setCard_icon(R.drawable.bank_construction);
			card.setCard_type("construct");
			card.setCard_num("6226 **** **** **** 732");
		//	cards.add(card);
		}
		adapter.notifyDataSetChanged();
		setEmptyStatus(false);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IPakageListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}


	/**
	 *
	 */
	public  interface IPakageListtener {
		/**
		 */
       void finishPakage();
       void pakageAdd();

	}
}
