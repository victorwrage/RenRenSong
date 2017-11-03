
package com.zdv.renrensong.renrensong.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * @Description:TODO(更换手机界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentChangePhone extends DialogFragment implements IUserView {
    private static final String COOKIE_KEY = "cookie";
    private IChangeListtener callBack;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    private static final int STEP_0 = 0;
    private static final int STEP_1 = STEP_0+1;
    private static final int STEP_2 = STEP_1 + 1;


    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "failed";

    private static final int TIMER_CODE = 0;


    private Map<String, String> temp_info;

    private Disposable disposable;

    @Bind(R.id.change_shut_iv)
    ImageView change_shut_iv;


    @Bind(R.id.change_title_tv)
    TextView change_title_tv;
    @Bind(R.id.change_time_tv)
    TextView change_time_tv;

    @Bind(R.id.change_phone_et)
    EditText change_phone_et;
    @Bind(R.id.change_code_et)
    EditText change_code_et;

    @Bind(R.id.change_change_btn)
    Button change_change_btn;

    int cur_step = STEP_0;
    QueryPresent present;
    Utils util;

    public DialogFragmentChangePhone() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IChangeListtener) activity;
        } catch (ClassCastException e) {

        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_phone_layout, container,
                false);
        ButterKnife.bind(DialogFragmentChangePhone.this, view);

        temp_info = new HashMap<>();
        util = Utils.getInstance();
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(getActivity());
        present.setView(DialogFragmentChangePhone.this);

        RxView.clicks(change_change_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> nextStep());
        RxView.clicks(change_shut_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dissMissChange(0,""));
        RxView.clicks(change_time_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchCode());

        return view;
    }

    private void dissMissChange(int status,String phone_num){
        callBack.onChangeDissmiss(status,"");
        dismiss();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryCode(Constant.user_info.get(Constant.USER_INFO_PHONE));

        change_phone_et.setText(Constant.user_info.get(Constant.USER_INFO_PHONE));
        change_phone_et.setEnabled(false);

        change_change_btn.setText("正在获取验证码");
        change_change_btn.setEnabled(false);
        change_time_tv.setVisibility(View.GONE);
        change_time_tv.setEnabled(false);
        change_title_tv.setText("验证手机");
        setCancelable(false);
    }

    private void fetchCode() {
        if(!util.verifyPhone(change_phone_et.getText().toString().trim())){
            VToast.toast(getActivity(), "请输入验证的手机号码!");
            return;
        }
        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryCode(change_phone_et.getText().toString().trim());

        change_change_btn.setText("正在获取验证码");
        change_change_btn.setEnabled(false);
        change_time_tv.setEnabled(false);

    }

    private void nextStep() {
        switch (cur_step) {
            case STEP_0:
                fetchCode();
                break;
            case STEP_1:
                if (change_code_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入验证码!");
                } else {
                    present.initRetrofit(Constant.URL_RENRENSONG, false);
                    present.QueryCodeVerify(change_phone_et.getText().toString().trim(), change_code_et.getText().toString().trim());
                    change_change_btn.setText("正在验证");
                    change_change_btn.setEnabled(false);
                    change_time_tv.setEnabled(false);
                }
                break;
            case STEP_2:
                if (change_phone_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入新的手机号码!");
                    return;
                }
                if (change_code_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入验证码!");
                    return;
                }

                change_change_btn.setText("正在提交");
                change_change_btn.setEnabled(false);

                present.initRetrofit(Constant.URL_RENRENSONG, false);
                present.QueryPhoneChange(change_phone_et.getText().toString().trim(), change_code_et.getText().toString().trim()
                        , Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN));
                break;

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
        change_change_btn.setText("验证");
        change_change_btn.setEnabled(true);


        KLog.v(info.toString());
        if (info == null || info.getStatus() == null) {
            switch (cur_step){
                case STEP_0:
                    change_time_tv.setEnabled(false);
                    change_time_tv.setText("获取验证码");
                    break;
                case STEP_1:
                    break;
                case STEP_2:
                    change_time_tv.setEnabled(false);
                    change_time_tv.setText("获取验证码");
                    break;
            }
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            if (info.getStatus().equals(STATUS_SUCCESS)) {

                switch (cur_step) {
                    case STEP_0:
                        showSoftInput(change_code_et);
                        change_change_btn.setEnabled(true);
                        change_time_tv.setEnabled(false);
                        change_time_tv.setVisibility(View.VISIBLE);
                        timer(TIMER_CODE);
                        cur_step = STEP_1;
                        break;
                    case STEP_1:

                        break;
                    case STEP_2:
                        change_time_tv.setEnabled(false);
                        change_time_tv.setVisibility(View.VISIBLE);
                        change_change_btn.setEnabled(true);
                        showSoftInput(change_code_et);
                        timer(TIMER_CODE);
                        break;
                }
            }else{
                switch (cur_step){
                    case STEP_0:
                        change_time_tv.setEnabled(false);
                        change_time_tv.setText("获取验证码");
                        break;
                    case STEP_1:
                        break;
                    case STEP_2:
                        change_time_tv.setEnabled(false);
                        change_time_tv.setText("获取验证码");
                        break;
                }
            }
        }
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
        change_change_btn.setEnabled(true);
        change_change_btn.setText("验证");

        if (info.getStatus() == null) {

            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            if (info.getStatus().equals(STATUS_SUCCESS)) {
                cur_step = STEP_2;
                change_title_tv.setText("验证新手机");
                change_phone_et.setText("");
                change_phone_et.setHint("请输入新手机号码");
                change_phone_et.setEnabled(true);

                change_code_et.setText("");
                showSoftInput(change_phone_et);
                disposable.dispose();
                change_time_tv.setText("获取验证码");
                change_time_tv.setEnabled(true);
                change_change_btn.setText("提交");
                change_change_btn.setEnabled(true);

            }
        }
    }

    private void showSoftInput(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(editText, 0);
                           }
                       },
                500);
    }

    @Override
    public void ResolvePhoneChangeInfo(RenRenSongCodeResponse info) {
        change_change_btn.setEnabled(true);
        change_change_btn.setText("提交");
        if (info.getStatus() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            dissMissChange(1,change_phone_et.getText().toString().trim());
        }
    }

    /**
     * 计时器
     **/
    private void timer(int type) {
        int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .take(limit + 1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete())
                .subscribe(s -> next(type, s));

    }

    private void complete() {
        change_time_tv.setEnabled(true);
        change_time_tv.setText("重新获取");
        change_change_btn.setEnabled(false);
    }

    private void next(int type, int s) {
        // KLog.v(s + "=type=" + type);
        if (s >= 10) {
            change_time_tv.setText(s + "秒后重发");
        } else {
            change_time_tv.setText("0" + s + "秒后重发");
        }
    }


    private void hideWaitDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void showWaitDialog(String tip) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(tip);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setOnDismissListener((dia) -> onProgressDissmiss());
        progressDialog.show();

    }

    private void onProgressDissmiss() {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    public interface IChangeListtener {

        void onChangeDissmiss(int status ,String phone_num);

    }
}
