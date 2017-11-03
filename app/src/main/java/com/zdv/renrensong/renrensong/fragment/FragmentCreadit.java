package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentCreadit extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	ICreditListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;

	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.creadit_lay, container, false);
		ButterKnife.bind(FragmentCreadit.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishCredit());
		header_title.setText("积分");

		return view;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (ICreditListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}


	public  interface ICreditListtener {

       void finishCredit();

	}
}
