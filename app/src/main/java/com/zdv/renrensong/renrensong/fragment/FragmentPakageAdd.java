package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentPakageAdd extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	IPakageAddListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;
	@Bind(R.id.header_add)
	ImageView header_add;

	@Bind(R.id.pakage_name_et)
	EditText pakage_name_et;
	@Bind(R.id.pakage_num_et)
	EditText pakage_num_et;

	@Bind(R.id.pakage_commit_lay)
	RelativeLayout pakage_commit_lay;

	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pakage_add_lay, container, false);
		ButterKnife.bind(FragmentPakageAdd.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishPakageAdd());
		RxView.clicks(pakage_commit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> addCard());

		header_title.setText("添加银行卡");

		return view;
	}

	private void addCard() {

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IPakageAddListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}


	/**
	 *
	 */
	public  interface IPakageAddListtener {
		/**
		 */
       void finishPakageAdd();

	}
}
