
package com.zdv.renrensong.renrensong.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
 * @Description:TODO(登录界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentRegisterLogin extends DialogFragment implements IUserView {
    private ILoginListtener callBack;
    ProgressDialog progressDialog;
    private final int STEP_PHONE_NO = 0;
    private final int STEP_CODE = STEP_PHONE_NO + 1;
    private final int STEP_PASSWORD = STEP_CODE + 1;
    private final int STEP_COMPLETE = STEP_PASSWORD + 1;

    private final String STR_PHONE = "phone_num";
    private final String COOKIE_KEY = "cookie";

    private final String STATUS_SUCCESS = "success";
    private final String STATUS_FAIL = "failed";

    private final int TIMER_CODE = 0;
    private final int TIMER_QCODE = TIMER_CODE + 1;

    private final int TYPE_REGISTER = 0;
    private final int TYPE_LOGIN = TYPE_REGISTER + 1;

    private int CUR_TYPE = TYPE_LOGIN;
    boolean isRegisterOrForgot = false;
    private Disposable disposable_q;
    private Disposable disposable;
    @Bind(R.id.login_phone_et)
    EditText login_phone_et;
    @Bind(R.id.login_code_et)
    EditText login_code_et;
    @Bind(R.id.login_login_btn)
    Button login_login_btn;
    @Bind(R.id.login_register_tv)
    TextView login_register_tv;

/*    @Bind(R.id.login_code_again_btn)
    Button login_code_again_btn;*/

    @Bind(R.id.login_shut_iv)
    ImageView login_shut_iv;
    @Bind(R.id.login_time_tv)
    TextView login_time_tv;
    @Bind(R.id.login_title_tv)
    TextView login_title_tv;
    @Bind(R.id.login_prefix_tv)
    TextView login_prefix_tv;
    @Bind(R.id.login_forget_tv)
    TextView login_forget_tv;

    @Bind(R.id.login_qcode_et)
    EditText login_qcode_et;
    @Bind(R.id.login_qcode_iv)
    ImageView login_qcode_iv;
    @Bind(R.id.login_qcode_lay)
    LinearLayout login_qcode_lay;

    int cur_step = STEP_PHONE_NO;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    private Map<String, String> temp_info;

    public DialogFragmentRegisterLogin() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (ILoginListtener) activity;
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
        View view = inflater.inflate(R.layout.login_layout, container,
                false);
        ButterKnife.bind(DialogFragmentRegisterLogin.this, view);

        util = Utils.getInstance();
        present = QueryPresent.getInstance(getActivity());
        present.setView(DialogFragmentRegisterLogin.this);
        sp = getActivity().getSharedPreferences(COOKIE_KEY, 0);
        temp_info = new HashMap<>();
        login_phone_et.setHint("请输入手机号码");
        login_phone_et.setInputType(InputType.TYPE_CLASS_PHONE);
        RxView.clicks(login_login_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> nextStep());
        RxView.clicks(login_shut_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        RxView.clicks(login_register_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> register());
        RxView.clicks(login_time_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchCode());//
        RxView.clicks(login_qcode_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> fetchQCode());
        RxView.clicks(login_forget_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> forget());
        return view;
    }

    private void forget() {
        isRegisterOrForgot=true;
        dismiss();
        DialogFragmentForget fragment = new DialogFragmentForget();
        fragment.setCancelable(false);
        fragment.show(getFragmentManager(), "");

    }

    private void fetchQCode() {
        present.initRetrofitOrigin(Constant.URL_RENRENSONG);
        present.QueryQcode(temp_info.get(STR_PHONE));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login_phone_et.setFocusable(true);
        login_phone_et.setFocusableInTouchMode(true);
        login_phone_et.requestFocus();
        String user_phone = sp.getString(Constant.USER_INFO_PHONE, "");
        login_phone_et.setText(user_phone);
        login_phone_et.selectAll();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) login_phone_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(login_phone_et, 0);
                           }
                       },
                500);
    }

    private void fetchCode() {
        login_login_btn.setEnabled(false);
        login_login_btn.setText("正在拉取验证码");

        present.initRetrofit(Constant.URL_RENRENSONG, false);
        present.QueryCode(temp_info.get(STR_PHONE));
        login_time_tv.setEnabled(false);//
        login_login_btn.setEnabled(false);
    }

    private void register() {
        CUR_TYPE = TYPE_REGISTER;
        login_title_tv.setText("注册");
        login_register_tv.setVisibility(View.GONE);
    }

    private void nextStep() {
        switch (cur_step) {
            case STEP_PHONE_NO:
                if (!util.verifyPhone(login_phone_et.getText().toString())) {
                    VToast.toast(getActivity(), "请输入正确的手机号码!");
                } else {
                    login_prefix_tv.setVisibility(View.GONE);
                    temp_info.put(STR_PHONE, login_phone_et.getText().toString());
                    switch (CUR_TYPE) {
                        case TYPE_REGISTER:

                            login_login_btn.setEnabled(false);
                            login_login_btn.setText("正在拉取验证码");
                            present.initRetrofit(Constant.URL_RENRENSONG, false);
                            present.QueryCode(login_phone_et.getText().toString());
                            break;
                        case TYPE_LOGIN:

                            login_forget_tv.setVisibility(View.VISIBLE);
                            login_phone_et.setText("");
                            login_phone_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            login_register_tv.setVisibility(View.GONE);
                            login_login_btn.setText("登录");
                            login_phone_et.setHint("请输入密码");
                            cur_step = STEP_PASSWORD;
                            break;
                    }
                }
                break;
            case STEP_CODE:
                if (login_code_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入短信验证码!");
                } else {
                    login_login_btn.setEnabled(false);
                    login_login_btn.setText("正在注册");
                    present.initRetrofit(Constant.URL_RENRENSONG, false);
                    present.QueryRegister(temp_info.get(STR_PHONE), login_code_et.getText().toString());
                }
                break;
            case STEP_PASSWORD:
                if (login_phone_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入密码!");
                    return;
                }
                if (login_qcode_lay.getVisibility() == View.VISIBLE && login_qcode_et.getText().toString().trim().equals("")) {
                    VToast.toast(getActivity(), "请输入验证码!");
                    return;
                }
                login_login_btn.setEnabled(false);
                login_login_btn.setText("正在登录");
                present.initRetrofit(Constant.URL_RENRENSONG, false);
                String encryed_pw = util.Base64(util.MD5(login_phone_et.getText().toString()));
//                KLog.v("encryed_pw" + encryed_pw+"-----"+util.MD5(login_phone_et.getText().toString()));
                temp_info.put("password", encryed_pw);
                present.QueryLogin(temp_info.get(STR_PHONE), encryed_pw, login_qcode_et.getText().toString(), "");

                InputMethodManager inputManager =
                        (InputMethodManager) login_phone_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(login_phone_et.getWindowToken(), 0);

                break;
        }
    }

    @Override
    public void ResolveLoginInfo(ResponseBody info_) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("登录");
        if(getContext()==null|| isDetached() || isRemoving()){
            return;
        }
        JSONObject info = null;
        if(info_.source()==null){
            VToast.toast(getContext(), "网络错误，请重试!");
            return;
        }
        try {
            info = new JSONObject(info_.string());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (info == null || info.opt("status") == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            KLog.v(info.toString());
            JSONObject content = null;
            try {
                content = info.getJSONObject("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VToast.toast(getActivity(), info.opt("info").toString());
            //KLog.v(info.opt("status").toString() +"\r\n"+content.opt("img").toString());
            if (info.opt("status").toString().equals(STATUS_SUCCESS)) {

                if (content == null) {
                    VToast.toast(getActivity(), "解析错误");
                    return;
                }
                login_phone_et.setText("");
                cur_step = STEP_COMPLETE;
                Constant.user_info = new HashMap<>();
                Constant.user_info.put(Constant.USER_INFO_PHONE, temp_info.get(STR_PHONE));
                Constant.user_info.put(Constant.USER_INFO_ID, content.optString("userID"));
                Constant.user_info.put(Constant.USER_INFO_TOKEN, content.optString("TOKEN"));
                Constant.user_info.put(Constant.USER_INFO_EMAIL,content.optString("user_email"));
                Constant.user_info.put(Constant.USER_INFO_IDCARD,content.optString("id_card_num"));
                Constant.user_info.put(Constant.USER_INFO_NAME,content.optString("real_name"));
                Constant.user_info.put(Constant.USER_INFO_EMAIL_STATUS,content.optString("email_status"));
                Constant.user_info.put(Constant.USER_INFO_ISAUTH,content.optString("is_auth"));
                Constant.user_info.put(Constant.USER_INFO_SHOPPER_ID, content.optString("deliverer_id"));
                Constant.user_info.put(Constant.USER_INFO_USER_NAME, content.optString("user_name"));

                SharedPreferences.Editor editor = sp.edit();

                editor.putString(Constant.USER_INFO_PHONE, temp_info.get(STR_PHONE));
                editor.putString(Constant.USER_INFO_USER_NAME, content.optString("user_name"));
                editor.putString(Constant.USER_INFO_SHOPPER_ID, content.optString("deliverer_id"));
                editor.putString(Constant.USER_INFO_TOKEN, content.optString("TOKEN"));
                editor.putString(Constant.USER_INFO_IDCARD, content.optString("id_card_num"));
                editor.putString(Constant.USER_INFO_NAME, content.optString("real_name"));
                editor.putString(Constant.USER_INFO_ID, content.optString("userID"));

                editor.putString(Constant.USER_INFO_EMAIL,content.optString("user_email"));
                editor.putString(Constant.USER_INFO_EMAIL_STATUS,content.optString("email_status"));
                editor.putString(Constant.USER_INFO_ISAUTH,content.optString("is_auth"));

                editor.putString(Constant.USER_INFO_PW, temp_info.get("password"));
                editor.putString(Constant.USER_INFO_SESSION_ID, content.optString("session_id"));
                editor.commit();
                callBack.OnLoginSuccess();
                dismiss();
            } else if (info.opt("status").toString().equals(STATUS_FAIL) && (content!=null && content.opt("img") != null)) {

                login_qcode_lay.setVisibility(View.VISIBLE);
                String img_str = content.opt("img").toString().substring(22);
                Bitmap bitmap = util.stringtoBitmap(img_str);
                login_qcode_iv.setImageBitmap(bitmap);
                timer(TIMER_QCODE);
            }
        }
    }

    @Override
    public void ResolveRegisterInfo(RenRenSongCodeResponse info) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("注册");
        if (info == null || info.getStatus() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            KLog.v(info.toString());

            VToast.toast(getActivity(), info.getInfo());
            if (info.getStatus().equals(STATUS_SUCCESS)) {
                login_phone_et.setText("");
                VToast.toast(getActivity(), "密码会在审核后以短信方式发送到您的手机！");
                cur_step = STEP_COMPLETE;
                isRegisterOrForgot = true;
                Constant.willShowLogin = true;
                dismiss();

            }
        }
    }

    @Override
    public void ResolveQcodeInfo(ResponseBody info) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("登录");
        String res;
        try {
            res = info.string().substring(24).trim();
            res = res.replaceAll("\\\\r\\\\n", "");
        } catch (IOException e) {
            e.printStackTrace();
            VToast.toast(getActivity(), "验证码图片解析错误");
            return;
        }
        if (res != null) {
            login_qcode_lay.setVisibility(View.VISIBLE);
            Bitmap bitmap = util.stringtoBitmap(res);
            login_qcode_iv.setImageBitmap(bitmap);
            timer(TIMER_QCODE);
        } else {
            VToast.toast(getActivity(), "验证码图片解析错误");
        }
    }

    @Override
    public void ResolveCodeInfo(RenRenSongCodeResponse info) {
        login_login_btn.setEnabled(true);
        login_login_btn.setText("下一步");
        login_time_tv.setEnabled(true);
        KLog.v(info.toString());
        if (info == null || info.getStatus() == null) {
            VToast.toast(getActivity(), "网络错误，请重试!");
        } else {
            VToast.toast(getActivity(), info.getInfo());
            if (info.getStatus().equals(STATUS_SUCCESS)) {

                login_login_btn.setText("注册");

                login_time_tv.setEnabled(false);//
                login_time_tv.setVisibility(View.VISIBLE);

                timer(TIMER_CODE);
                login_code_et.setVisibility(View.VISIBLE);
                login_phone_et.setVisibility(View.GONE);
                login_register_tv.setVisibility(View.GONE);
                VToast.toast(getActivity(), info.getInfo());
                if (CUR_TYPE == TYPE_REGISTER) {
                    cur_step = STEP_CODE;
                    login_code_et.setHint("请输入短信验证码");
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

    }

    @Override
    public void ResolvePhoneChangeInfo(RenRenSongCodeResponse info) {

    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable_q != null) {
            disposable_q.dispose();
        }
        if(callBack!=null && !isRegisterOrForgot) {
            callBack.OnLoginCancel();
        }
    }


    /**
     * 计时器
     **/
    private void timer(int type) {
        int limit = Constant.DEFAULT_MESSAGE_TIMEOUT;
        Disposable temp_dis;
        temp_dis = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(limit + 1)
                .map(s -> limit - s.intValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> complete(type))
                .subscribe(s -> next(type, s));
        switch (type) {
            case TIMER_CODE:
                if (disposable != null) {
                    disposable.dispose();
                }
                disposable = temp_dis;
                break;
            case TIMER_QCODE:
                if (disposable_q != null) {
                    disposable_q.dispose();
                }
                disposable_q = temp_dis;
                break;
        }


    }

    private void complete(int type) {
        switch (type) {
            case TIMER_CODE:
                login_time_tv.setEnabled(true);
                login_time_tv.setText("重新获取");
                login_login_btn.setEnabled(false);

                break;
            case TIMER_QCODE:
                fetchQCode();
                break;
        }

    }

    private void next(int type, int s) {
        // KLog.v(s + "=type=" + type);
        if (s >= 10) {
            login_time_tv.setText(s + "秒后重发");
        } else {
            login_time_tv.setText("0" + s + "秒后重发");
        }
    }


    public  interface ILoginListtener {

          void OnLoginSuccess();
          void OnLoginCancel();

    }
}
