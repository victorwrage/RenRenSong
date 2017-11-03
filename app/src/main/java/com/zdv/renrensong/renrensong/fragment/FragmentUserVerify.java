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

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class FragmentUserVerify extends BaseFragment implements IUserView {

    IUserVerifyListtener listtener;
    @Bind(R.id.header_btn)
    ImageView header_btn;

    @Bind(R.id.verify_name_et)
    EditText verify_name_et;
    @Bind(R.id.verify_num_et)
    EditText verify_num_et;
    @Bind(R.id.header_title)
    TextView header_title;
    @Bind(R.id.verify_commit_tv)
    TextView verify_commit_tv;


    @Bind(R.id.verify_rotate)
    ImageView verify_rotate;
    @Bind(R.id.verify_commit_iv)
    ImageView verify_commit_iv;

    @Bind(R.id.verify_do_lay)
    LinearLayout verify_do_lay;

    QueryPresent present;
    Utils util;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_lay, container, false);
        ButterKnife.bind(FragmentUserVerify.this, view);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentUserVerify.this);

        header_title.setText("实名认证");
        RxView.clicks(header_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listtener.finishVerify());
        RxView.clicks(verify_do_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> doVerify());


        if (!Constant.user_info.get(Constant.USER_INFO_NAME).equals("")) {
            verify_name_et.setText(util.getNameEncrypt(Constant.user_info.get(Constant.USER_INFO_NAME)));
            verify_num_et.setText(util.getIDCardEncrypt(Constant.user_info.get(Constant.USER_INFO_IDCARD)));
        }
        if (Constant.user_info.get(Constant.USER_INFO_ISAUTH).equals("1")) {
            verify_name_et.setEnabled(false);
            verify_num_et.setEnabled(false);
            verify_do_lay.setEnabled(false);
            verify_commit_tv.setText("已验证");
        }
        return view;
    }

    private void doVerify() {
        if (verify_name_et.getText().toString().trim().equals("")) {
            VToast.toast(getContext(), "请输入姓名");
            return;
        }

        String card_err;
        try {
            card_err = util.IDCardValidate(verify_num_et.getText().toString().trim());
        }catch (ParseException e){
            card_err = e.getMessage();
        }

        if (!card_err.equals("")) {
            VToast.toast(getContext(), card_err);
            return;
        }

        verify_commit_iv.setVisibility(View.GONE);
        startLoading(verify_rotate);
        verify_do_lay.setEnabled(false);


        present.initRetrofit(Constant.URL_RENRENSONG, false);
        KLog.v(Constant.user_info.get(Constant.USER_INFO_ID) + "--" + Constant.user_info.get(Constant.USER_INFO_TOKEN));
        present.QueryVerify(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN), verify_name_et.getText().toString().trim()
                , verify_num_et.getText().toString().trim());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listtener = (IUserVerifyListtener) context;
        } catch (Exception e) {
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

    }

    @Override
    public void ResolveVerifyInfo(RenRenSongCodeResponse info) {

        verify_commit_iv.setVisibility(View.VISIBLE);
        stopLoading(verify_rotate);
        verify_do_lay.setEnabled(true);
        if (info.getStatus() != null) {
            VToast.toast(getContext(), info.getInfo());
            if (info.getStatus().equals(SUCCESS)) {
                verify_name_et.setText("");
                verify_num_et.setText("");
            }

        } else {
            VToast.toast(getContext(), "网络错误，请重试!");
        }
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
    public interface IUserVerifyListtener {
        /**
         */
        void finishVerify();

    }
}
