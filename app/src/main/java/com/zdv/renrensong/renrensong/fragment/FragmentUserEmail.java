package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.IUserView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentUserEmail extends BaseFragment implements IUserView{

	IUserEmailListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;

	@Bind(R.id.email_title_tv)
	TextView email_title_tv;
	@Bind(R.id.header_title)
	TextView header_title;

	@Bind(R.id.email_rotate)
	ImageView email_rotate;
	@Bind(R.id.email_change_iv)
	ImageView email_change_iv;

	@Bind(R.id.email_email_et)
	EditText email_email_et;
	@Bind(R.id.email_change_lay)
	LinearLayout email_change_lay;

	QueryPresent present;
	Utils util;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.email_lay, container, false);
		ButterKnife.bind(FragmentUserEmail.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getContext());
		present.setView(FragmentUserEmail.this);

		header_title.setText("绑定(修改)邮箱");
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishEmail());
		RxView.clicks(email_change_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> changeEmail());
		if(Constant.user_info.get(Constant.USER_INFO_EMAIL_STATUS).equals("0")) {
			if(Constant.user_info.get(Constant.USER_INFO_EMAIL).equals("")){
				email_title_tv.setText("您当前没有绑定邮箱");
			}else {
				email_title_tv.setTextColor(getResources().getColor(R.color.word1));
				email_title_tv.setText("您的邮箱"+Constant.user_info.get(Constant.USER_INFO_EMAIL)+"未绑定,请登录邮箱确认验证!");
			}
		}else{
			email_title_tv.setTextColor(getResources().getColor(R.color.green));
			email_title_tv.setText("您的绑定邮箱为" + Constant.user_info.get(Constant.USER_INFO_EMAIL));
		}
		return view;
	}

    private void changeEmail() {
		if(!util.isEmail(email_email_et.getText().toString().trim())){
			VToast.toast(getContext(),"请输入正确的邮箱");
			return;
		}
		email_change_iv.setVisibility(View.GONE);
		startLoading(email_rotate);
		email_change_lay.setEnabled(false);
		present.initRetrofit2(Constant.URL_RENRENSONG,false);
		present.QueryEmail(Constant.user_info.get(Constant.USER_INFO_ID),Constant.user_info.get(Constant.USER_INFO_TOKEN),email_email_et.getText().toString().trim());
    }

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IUserEmailListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}

	@Override
	public void ResolveLoginInfo(ResponseBody info) {

	}

	@Override
	public void ResolveRegisterInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolveQcodeInfo(ResponseBody info) {

	}

	@Override
	public void ResolveCodeInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolveForgetInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolveAlterInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolveEmailInfo(RenRenSongCodeResponse info) {
		email_change_iv.setVisibility(View.VISIBLE);
		stopLoading(email_rotate);
		email_change_lay.setEnabled(true);
		if (info.getStatus() != null) {
			VToast.toast(getContext(),info.getInfo());
			if(info.getStatus().equals(SUCCESS)){
				email_email_et.setText("");
			}
		}else{
			VToast.toast(getContext(),"网络错误，请重试!");
		}
	}

	@Override
	public void ResolveVerifyInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolveCodeVerifyInfo(RenRenSongCodeResponse info) {

	}

	@Override
	public void ResolvePhoneChangeInfo(RenRenSongCodeResponse info) {

	}


	/**
	 *
	 */
	public  interface IUserEmailListtener {
		/**
		 */
       void finishEmail();

	}
}
