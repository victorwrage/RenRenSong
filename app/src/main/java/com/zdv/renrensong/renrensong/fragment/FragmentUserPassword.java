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
import com.socks.library.KLog;
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

public class FragmentUserPassword extends BaseFragment implements IUserView{

	IUserPasswordListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;
	@Bind(R.id.header_title)
	TextView header_title;


	@Bind(R.id.password_rotate)
	ImageView password_rotate;
	@Bind(R.id.password_commit_icon)
	ImageView password_commit_icon;

	@Bind(R.id.password_p1_et)
	EditText password_p1_et;
	@Bind(R.id.password_p2_et)
	EditText password_p2_et;
	@Bind(R.id.password_p3_et)
	EditText password_p3_et;


	@Bind(R.id.password_code_commit_lay)
	LinearLayout password_code_commit_lay;

	QueryPresent present;
	Utils util;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.password_lay, container, false);
		ButterKnife.bind(FragmentUserPassword.this,view);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getContext());
		present.setView(FragmentUserPassword.this);
		header_title.setText("修改密码");
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishPassword());
		RxView.clicks(password_code_commit_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> commit());

		return view;
	}

	private void commit() {
		if(password_p1_et.getText().toString().trim().equals("")){
			VToast.toast(getContext(),"请输入旧密码");
			return;
		}
		if(password_p2_et.getText().toString().trim().equals("")){
			VToast.toast(getContext(),"请输入新密码");
			return;
		}
		if(password_p3_et.getText().toString().trim().equals("")){
			VToast.toast(getContext(),"请确认新密码");
			return;
		}
		if(!password_p2_et.getText().toString().trim().equals(password_p3_et.getText().toString().trim())){
			VToast.toast(getContext(),"两次密码不一致");
			return;
		}

		password_commit_icon.setVisibility(View.GONE);
		startLoading(password_rotate);
		password_code_commit_lay.setEnabled(false);

		String encryed_pw_old = util.Base64(util.MD5(password_p1_et.getText().toString().trim()));
		String encryed_pw_new = util.Base64(util.MD5(password_p3_et.getText().toString().trim()));

		present.initRetrofit(Constant.URL_RENRENSONG,false);
		KLog.v(Constant.user_info.get(Constant.USER_INFO_ID)+"--"+Constant.user_info.get(Constant.USER_INFO_TOKEN));
		present.QueryAlter(Constant.user_info.get(Constant.USER_INFO_ID),Constant.user_info.get(Constant.USER_INFO_TOKEN),encryed_pw_old
		,encryed_pw_new);
	}



	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IUserPasswordListtener) context;
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
		password_commit_icon.setVisibility(View.VISIBLE);
		stopLoading(password_rotate);
		password_code_commit_lay.setEnabled(true);
		if (info.getStatus() != null) {
			VToast.toast(getContext(),info.getInfo());
			if(info.getStatus().equals(SUCCESS)){
				password_p1_et.setText("");
				password_p2_et.setText("");
				password_p3_et.setText("");
			}

		}else{
			VToast.toast(getContext(),"网络错误，请重试!");
		}
	}

	@Override
	public void ResolveEmailInfo(RenRenSongCodeResponse info) {

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
	public  interface IUserPasswordListtener {
		/**
		 */
       void finishPassword();

	}
}
