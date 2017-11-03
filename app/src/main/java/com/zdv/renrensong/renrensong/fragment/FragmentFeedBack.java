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

public class FragmentFeedBack extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	IFeedBackListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;


	@Bind(R.id.feedback_content)
	EditText feedback_content;
	@Bind(R.id.feedback_contact_et)
	EditText feedback_contact_et;

	@Bind(R.id.feedback_upload_iv)
	ImageView feedback_upload_iv;

	@Bind(R.id.feedback_commit_lay)
	RelativeLayout feedback_commit_lay;


	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feedback_lay, container, false);
		ButterKnife.bind(FragmentFeedBack.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishFeedBack());

		RxView.clicks(feedback_upload_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> updateImg());
		RxView.clicks(feedback_commit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> commit());

		header_title.setText("反馈");

		return view;
	}

	private void commit() {

	}

	private void updateImg() {

	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IFeedBackListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}


	/**
	 *
	 */
	public  interface IFeedBackListtener {
		/**
		 */
       void finishFeedBack();

	}
}
