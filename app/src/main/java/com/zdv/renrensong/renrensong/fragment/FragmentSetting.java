package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.bmob.v3.update.BmobUpdateAgent;

public class FragmentSetting extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	ISettingListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;

	@Bind(R.id.setting_add_lay)
	RelativeLayout setting_add_lay;
	@Bind(R.id.setting_road_lay)
	RelativeLayout setting_road_lay;
	@Bind(R.id.setting_sound_lay)
	RelativeLayout setting_sound_lay;
	@Bind(R.id.setting_user_update_lay)
	RelativeLayout setting_user_update_lay;
	@Bind(R.id.setting_update_lay)
	RelativeLayout setting_update_lay;
	@Bind(R.id.setting_law_lay)
	RelativeLayout setting_law_lay;
	@Bind(R.id.setting_about_lay)
	RelativeLayout setting_about_lay;

	@Bind(R.id.setting_version_tv)
	TextView setting_version_tv;


	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_lay, container, false);
		ButterKnife.bind(FragmentSetting.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishSetting());
		RxView.clicks(setting_add_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> address());

		RxView.clicks(setting_user_update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> userUpdate());
		RxView.clicks(setting_update_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> versionUpdate());
		RxView.clicks(setting_law_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> law());
		RxView.clicks(setting_about_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> about());
		header_title.setText("设置");
		setting_version_tv.setText(util.getAppVersionName(getContext()));
		return view;
	}

	private void about() {

	}

	private void law() {

	}

	private void versionUpdate() {
		BmobUpdateAgent.forceUpdate(getContext());
	}


	private void userUpdate() {

	}


	private void address() {
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (ISettingListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}


	/**
	 *
	 */
	public  interface ISettingListtener {
		/**
		 */
       void finishSetting();

	}
}
