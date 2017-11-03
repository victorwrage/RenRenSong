package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.IUserView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;

public class FragmentLogin extends Fragment implements IUserView{
	private static final String COOKIE_KEY = "cookie";
	SharedPreferences sp;
	QueryPresent present;
	Utils util;
	ILoginListener listtener;
	DialogFragmentRegisterLogin fragment;
	private static final String STATUS_SUCCESS = "success";
	private static final String STATUS_FAIL = "failed";
    private boolean shouldShowLogin = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.splash_lay, container, false);

		util = Utils.getInstance();
		present = QueryPresent.getInstance(getContext());
		present.setView(FragmentLogin.this);
		sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
		autoLogin();

		return view;
	}

	private void autoLogin() {

		String user_phone = sp.getString(Constant.USER_INFO_PHONE, "");
		String user_pw = sp.getString(Constant.USER_INFO_PW, "");
		String user_session = sp.getString(Constant.USER_INFO_SESSION_ID, "");
		KLog.v(user_phone+"--"+user_pw+"--"+user_session);
		if(user_phone.equals("")||user_pw.equals("")){
			showLogin();
			return;
		}
		present.initRetrofitOrigin(Constant.URL_RENRENSONG);
		present.QueryLogin(user_phone,user_pw,"",user_session);

	}

	@Override
	public void onStop() {
		super.onStop();
		setUserVisibleHint(false);
		KLog.v("onStop");
	}

	@Override
	public void onResume() {
		super.onResume();
		setUserVisibleHint(true);
		if( Constant.willShowLogin ){
			Constant.willShowLogin = false;
			showLogin();
			return;
		}
		if(shouldShowLogin && Constant.user_info == null && fragment==null){
			KLog.v("showLogin");
			showLogin();
		}
		KLog.v("onResume");
	}

	private void showLogin() {
		shouldShowLogin = true;//在自动登陆失败切换了界面再回来继续弹出
		if(getUserVisibleHint()) {//防止在登陆时切换了界面，致使登录窗口弹出出错
			fragment = new DialogFragmentRegisterLogin();
			fragment.setCancelable(false);
			fragment.show(getFragmentManager(), "");
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (ILoginListener)context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}

	@Override
	public void ResolveLoginInfo(ResponseBody info_) {

		if(info_.source()==null){
			showLogin();
			return;
		}
		JSONObject info=null;
		try {
			//KLog.v(info_.string());
			info = new JSONObject(info_.string());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		if (info == null || info.opt("status") == null) {
			showLogin();
			VToast.toast(getContext(), "网络错误，请重试!");
		} else {
			KLog.v(info.toString());

			JSONObject content = null;
			try {
				content = info.getJSONObject("content");
			} catch (JSONException e) {

			}
			VToast.toast(getContext(), info.opt("info").toString());
			if ( info.opt("status").toString().equals(STATUS_SUCCESS)) {

				if(content==null){
					showLogin();
					KLog.v("解析错误");
					return;
				}
				Constant.user_info = new HashMap<>();
				Constant.user_info.put(Constant.USER_INFO_PHONE,sp.getString("user_phone", ""));
				Constant.user_info.put(Constant.USER_INFO_ID,content.optString("userID"));
				Constant.user_info.put(Constant.USER_INFO_TOKEN,content.optString("TOKEN"));
				Constant.user_info.put(Constant.USER_INFO_EMAIL,content.optString("user_email"));
				Constant.user_info.put(Constant.USER_INFO_IDCARD,content.optString("id_card_num"));
				Constant.user_info.put(Constant.USER_INFO_NAME,content.optString("real_name"));
				Constant.user_info.put(Constant.USER_INFO_EMAIL_STATUS,content.optString("email_status"));
				Constant.user_info.put(Constant.USER_INFO_ISAUTH,content.optString("is_auth"));
				Constant.user_info.put(Constant.USER_INFO_SHOPPER_ID,content.optString("deliverer_id"));
				Constant.user_info.put(Constant.USER_INFO_USER_NAME,content.optString("user_name"));
				listtener.AutoLoginFinished();
			} else {
				showLogin();
			}
		}
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


	public  interface ILoginListener {
       void AutoLoginFinished();
	}
}
