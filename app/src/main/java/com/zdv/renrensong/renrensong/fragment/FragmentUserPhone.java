package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentUserPhone extends BaseFragment{

	IUserPhoneListtener listtener;
	@Bind(R.id.header_btn)
	ImageView header_btn;

	@Bind(R.id.phone_title_tv)
	TextView phone_title_tv;
	@Bind(R.id.header_title)
	TextView header_title;


	@Bind(R.id.phone_change_lay)
	LinearLayout phone_change_lay;

	QueryPresent present;
	Utils util;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.phone_lay, container, false);
		ButterKnife.bind(FragmentUserPhone.this,view);

		util = Utils.getInstance();

		header_title.setText("修改手机号");
		RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishPhone());
		RxView.clicks(phone_change_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> changePhone());
		phone_title_tv.setText("您当前的手机号为"+Constant.user_info.get(Constant.USER_INFO_PHONE));
		return view;
	}

    private void changePhone() {
         DialogFragmentChangePhone changePhone = new DialogFragmentChangePhone();
		 changePhone.show(getFragmentManager(),"");
    }

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listtener = (IUserPhoneListtener) context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}



	public void onDialogBack(int status,String phone_num){
		switch (status){
			case 0:

				break;
			case 1:
				phone_title_tv.setText("您已经绑定新的手机号:"+phone_num);
				break;
		}
	}



	/**
	 *
	 */
	public  interface IUserPhoneListtener {
		/**
		 */
       void finishPhone();

	}
}
