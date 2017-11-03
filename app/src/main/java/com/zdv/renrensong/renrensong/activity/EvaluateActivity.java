package com.zdv.renrensong.renrensong.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.zdv.renrensong.renrensong.MainActivity;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.fragment.FragmentEvaluate;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import butterknife.ButterKnife;

public class EvaluateActivity extends FragmentActivity implements FragmentEvaluate.IEvaluateListener {

    private static final String PAGE_1 = "page_1";
    private String FLAG_GOEVALUATE = "goEvaluate";
    Utils util;

    FragmentEvaluate fragment0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_lay);
        ButterKnife.bind(EvaluateActivity.this);
        if (Constant.user_info == null) {
            Intent loginIntent = new Intent(EvaluateActivity.this, MainActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginIntent.putExtra(FLAG_GOEVALUATE,true);
            startActivity(loginIntent);
            finish();
            return;
        }
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentEvaluate();
        ft.add(R.id.fragment_container, fragment0, PAGE_1);
        ft.show(fragment0);
        ft.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(102);
    }

    @Override
    public void finishEvaluate() {
        finish();
    }
}
