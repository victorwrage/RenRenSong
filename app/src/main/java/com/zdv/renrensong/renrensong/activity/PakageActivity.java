package com.zdv.renrensong.renrensong.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.fragment.FragmentPakage;
import com.zdv.renrensong.renrensong.fragment.FragmentPakageAdd;
import com.zdv.renrensong.renrensong.util.Utils;

import butterknife.ButterKnife;

public class PakageActivity extends FragmentActivity implements FragmentPakage.IPakageListtener,FragmentPakageAdd.IPakageAddListtener{

    private static final String PAGE_1 = "page_1";
    private static final String PAGE_2 = "page_2";

    Utils util;

    FragmentPakage fragment0;
    FragmentPakageAdd fragment1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_lay);
        ButterKnife.bind(PakageActivity.this);
        initDate();
        initView();

    }

    private void initDate() {
        util = Utils.getInstance();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 =new FragmentPakage();
        fragment1 =new FragmentPakageAdd();
        ft.add(R.id.fragment_container, fragment0, PAGE_1);
        ft.add(R.id.fragment_container, fragment1, PAGE_2);
        ft.show(fragment0);
        ft.hide(fragment1);
        ft.commit();
    }


    @Override
    public void finishPakage() {
        finish();
    }

    @Override
    public void pakageAdd() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment1);
        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public void finishPakageAdd() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment0);
        ft.hide(fragment1);
        ft.commit();
    }
}
