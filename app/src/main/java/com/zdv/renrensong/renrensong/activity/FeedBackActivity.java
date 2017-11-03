package com.zdv.renrensong.renrensong.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.fragment.FragmentFeedBack;
import com.zdv.renrensong.renrensong.util.Utils;

import butterknife.ButterKnife;

public class FeedBackActivity extends FragmentActivity implements FragmentFeedBack.IFeedBackListtener{

    private static final String PAGE_1 = "page_1";

    Utils util;

    FragmentFeedBack fragment0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_lay);
        ButterKnife.bind(FeedBackActivity.this);
        initDate();
        initView();

    }

    private void initDate() {
        util = Utils.getInstance();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 =new FragmentFeedBack();
        ft.add(R.id.fragment_container, fragment0, PAGE_1);
        ft.show(fragment0);
        ft.commit();
    }


    @Override
    public void finishFeedBack() {
        finish();
    }
}
