
package com.zdv.renrensong.renrensong.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(忘记密码界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentForget extends DialogFragment implements IUserView {
    private static final String COOKIE_KEY = "cookie";
    private IForgotListtener callBack;
    ProgressDialog progressDialog;
    SharedPreferences sp;
    private static final int STEP_PHONE_NO = 0;
    private static final int STEP_CODE = STEP_PHONE_NO + 1;
    private static final int STEP_PASSWORD = STEP_CODE + 1;
    private static final int STEP_COMPLETE = STEP_PASSWORD + 1;

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAIL = "failed";

    private static final int TIMER_CODE = 0;
    private static final int TIMER_QCODE = TIMER_CODE + 1;


    private static final int TYPE_FORGET = 0;


    private Map<String, String> temp_info;

    private Disposable disposable;

    @Bind(R.id.login_shut_iv)
    ImageView login_shut_iv;
    @Bind(R.id.forget_time_tv)
    TextView forget_time_tv;

    @Bind(R.id.forget_pw_old_et)
    EditText forget_pw_old_et;
    @Bind(R.id.forget_pw_new_et)
    EditText forget_pw_new_et;
    @Bind(R.id.forget_code_et)
    EditText forget_code_et;


  /*  @Bind(R.id.forget_code_again_btn)
    Button forget_code_again_btn;*/
    @Bind(R.id.forget_next_btn)
    Button forget_next_btn;

    @Bind(R.id.forget_code_lay)
    LinearLayout forget_code_lay;

    int cur_step = STEP_PHONE_NO;
    QueryPresent present;
    Utils util;

    public DialogFragmentForget() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IForgotListtener) activity;
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
        View view = inflater.inflate(R.layout.forget_layout, container,
                false);
        ButterKnife.bind(DialogFragmentForget.this, view);

        temp_info = new HashMap<>();
        util = Utils.getInstance();
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(getActivity());
        present.setView(DialogFragmentForget.this);

        String user_phone = sp.getString(Constant.USER_INFO_PHONE, "");
        forget_pw_old_et.setHint("请输入手机号码");
        forget_pw_old_et.setInputType(InputType.TYPE_CLASS_PHONE);
        forget_pw_old_et.setText(user_phone);
        forget_pw_old_et.selectAll();
        RxView.clicks(forget_next_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> nextStep());
        RxView.clicks(login_shut_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> {
            callBack.onForgotDissmiss();
            dismiss();
        });
        RxView.clicks(forget_time_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchCode());

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forget_pw_old_et.setFocusable(true);
        forget_pw_old_et.setFocusableInTouchMode(true);
        forget_pw_old_et.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) forget_pw_old_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(forget_pw_old_et, 0);
                           }
                       },
                500);
    }

    private void fetchCode() {
        //showWaitDialog("正在获取验证码...");

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryCode(temp_info.get("phone_num"));
        forget_next_btn.setEnabled(false);
        forget_next_btn.setText("正在获取验证码");
        forget_time_tv.setEnabled(false);

    }

    private void nextStep() {
        switch (cur_step) {
            case STEP_PHONE_NO:
                if (!util.verifyPhone(forget_pw_old_et.getText().toString().trim())) {
                    VToast.toast(getActivity(), "请输入正确的手机号码!");
                } else {
                    forget_next_btn.setText("正在获取验证码");
                    forget_next_btn.setEnabled(false);
                    temp_info.put("phone_num", forget_pw_old_et.getText().toString().trim());
                    //showWaitDialog("正在获取验证码...");
                    present.initRetrofit(Constant.URL_RENRENSONG, false);
                    present.QueryCode(temp_info.get("phone_num"));
                    break;
                }
                break;
            case STEP_CODE:
                if (forget_code_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入短信验证码!");
                } else if (forget_pw_new_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请设置新密码!");
                } else {
                    forget_next_btn.setText("正在提交");
                    forget_next_btn.setEnabled(false);
                    present.initRetrofit(Constant.URL_RENRENSONG, false);

                    String encryed_pw = util.Base64(util.MD5(forget_pw_new_et.getText().toString().trim()));
                    present.QueryForget(temp_info.get("phone_num"), forget_code_et.getText().toString().trim(),
                            encryed_pw);
                }
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
        hideWaitDialog();
        forget_next_btn.setText("提交");
        forget_next_btn.setEnabled(true);
        KLog.v(info.toString());
        if (info == null || info.getStatus() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            if (info.getStatus().equals(STATUS_SUCCESS)) {

                forget_pw_new_et.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                forget_time_tv.setEnabled(false);
                forget_time_tv.setVisibility(View.VISIBLE);
                timer(TIMER_CODE);
                forget_pw_old_et.setVisibility(View.GONE);
                forget_pw_new_et.setVisibility(View.VISIBLE);
                forget_code_lay.setVisibility(View.VISIBLE);
                VToast.toast(getActivity(), info.getInfo());
                cur_step = STEP_CODE;
                forget_code_et.setText("");
                forget_code_et.setFocusable(true);
                forget_code_et.setFocusableInTouchMode(true);
                forget_code_et.requestFocus();

            }
        }
    }


    @Override
    public void ResolveForgetInfo(RenRenSongCodeResponse info) {
        KLog.v(info.toString());
        forget_next_btn.setText("提交");
        forget_next_btn.setEnabled(true);
        if (info == null || info.getStatus() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            if(info.getStatus().equals(STATUS_SUCCESS)) {
                callBack.onForgotDissmiss();
                dismiss();
            }
        }
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
        forget_time_tv.setEnabled(true);
        forget_time_tv.setText("重新获取");
        forget_next_btn.setEnabled(false);
    }

    private void next(int type, int s) {
        // KLog.v(s + "=type=" + type);
        if (s >= 10) {
            forget_time_tv.setText(s + "秒后重发");
        } else {
            forget_time_tv.setText("0" + s + "秒后重发");
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


    /**
     *
     */
    public  interface IForgotListtener {
        /**
         */
      void onForgotDissmiss();

    }
}
