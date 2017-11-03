
package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @ClassName: DialogFragmentAddTag
 * @Description:TODO(二维码界面)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentQcode extends DialogFragment {
    private IQcodeListener callBack;


    @Bind(R.id.my_qcode_shut_tv)
    TextView my_qcode_shut_tv;
    @Bind(R.id.my_qcode_later_tv)
    TextView my_qcode_later_tv;


    @Bind(R.id.my_qcode_qcode_iv)
    ImageView my_qcode_qcode_iv;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    Bitmap qcode;
    private Map<String, String> temp_info;

    public DialogFragmentQcode() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IQcodeListener) activity;
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
        View view = inflater.inflate(R.layout.my_qcode_layout, container,
                false);
        ButterKnife.bind(DialogFragmentQcode.this, view);
        util = Utils.getInstance();

        RxView.clicks(my_qcode_shut_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> shut());
        RxView.clicks(my_qcode_later_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> later());
        return view;
    }

    private void later() {
        callBack.laterRate();
        dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (qcode != null) {
            my_qcode_qcode_iv.setImageBitmap(qcode);
        }
    }

    private void shut() {
        dismiss();
    }

    public void setQcode(Bitmap bitmap) {
        qcode = bitmap;
    }


    /**
     *
     */
    public interface IQcodeListener {

        void laterRate();
    }
}
