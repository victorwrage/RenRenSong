package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentUserManager extends BaseFragment {
    private static final String COOKIE_KEY = "cookie";
	private static final String ALIAS_KEY = "alias";
	IUserListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;

	@Bind(R.id.header_title)
	TextView header_title;

	@Bind(R.id.user_phone_tv)
	TextView user_phone_tv;
	@Bind(R.id.user_author_tv)
	TextView user_author_tv;
	@Bind(R.id.user_email_tv)
	TextView user_email_tv;

	@Bind(R.id.user_phone_lay)
	RelativeLayout user_phone_lay;
	@Bind(R.id.user_author_lay)
	RelativeLayout user_author_lay;
	@Bind(R.id.user_password_lay)
	RelativeLayout user_password_lay;
	@Bind(R.id.user_email_lay)
	RelativeLayout user_email_lay;
    @Bind(R.id.user_logout_lay)
    RelativeLayout user_logout_lay;

	QueryPresent present;
	Utils util;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_lay, container, false);
		ButterKnife.bind(FragmentUserManager.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getActivity());

		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishThis());
		RxView.clicks(user_phone_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> phone());
		RxView.clicks(user_author_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> verify());
		RxView.clicks(user_email_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> email());
		RxView.clicks(user_password_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> password());
		RxView.clicks(user_logout_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> logout());

		user_phone_tv.setText(util.getPhoneEncrypt(Constant.user_info.get(Constant.USER_INFO_PHONE)));
		header_title.setText("账号与安全");
		if(Constant.user_info.get(Constant.USER_INFO_ISAUTH).equals("0")) {
			user_author_tv.setText("未认证");
		}else{
			user_author_tv.setText("已认证");
			user_author_tv.setTextColor(getResources().getColor(R.color.green));
		}
		if(Constant.user_info.get(Constant.USER_INFO_EMAIL_STATUS).equals("0")) {
			user_email_tv.setText("未绑定");
		}else{
			user_email_tv.setText(Constant.user_info.get(Constant.USER_INFO_EMAIL));
		}
		return view;
	}

    private void logout() {
        SharedPreferences sp  = getContext().getSharedPreferences(COOKIE_KEY, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

		SharedPreferences sp2  = getContext().getSharedPreferences(ALIAS_KEY, getContext().MODE_PRIVATE);
		SharedPreferences.Editor editor2 = sp2.edit();
		editor2.clear();
		editor2.commit();

        Constant.user_info = null;
        listtener.setFragClear();
        listtener.finishThis();
    }

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IUserListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}

	private void password() {
		listtener.changePassword();
	}

	private void email() {
		listtener.changeEmail();
	}

	private void verify() {
		listtener.verify();
	}

	private void phone() {
		listtener.changePhone();
	}


	/**
	 *
	 */
	public  interface IUserListtener {
		/**
		 */
       void finishThis();
       void setFragClear();
       void changePhone();
       void verify();
       void changeEmail();
       void changePassword();

	}
}
