package com.zdv.renrensong.renrensong.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.fragment.DialogFragmentChangePhone;
import com.zdv.renrensong.renrensong.fragment.FragmentUserEmail;
import com.zdv.renrensong.renrensong.fragment.FragmentUserManager;
import com.zdv.renrensong.renrensong.fragment.FragmentUserPassword;
import com.zdv.renrensong.renrensong.fragment.FragmentUserPhone;
import com.zdv.renrensong.renrensong.fragment.FragmentUserVerify;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;

import butterknife.ButterKnife;

public class UserManagerActivity extends FragmentActivity implements FragmentUserManager.IUserListtener,FragmentUserVerify.IUserVerifyListtener,FragmentUserPhone.IUserPhoneListtener
,FragmentUserPassword.IUserPasswordListtener,FragmentUserEmail.IUserEmailListtener,DialogFragmentChangePhone.IChangeListtener{

    private static final String PAGE_1 = "page_1";
    private static final String PAGE_2 = "page_2";
    private static final String PAGE_3 = "page_3";
    private static final String PAGE_4 = "page_4";
    private static final String PAGE_5 = "page_5";


    private FragmentUserManager fragment0;
    private FragmentUserPhone fragment1;
    private FragmentUserVerify fragment2;
    private FragmentUserPassword fragment3;
    private FragmentUserEmail fragment4;

    Utils util;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_lay);
        ButterKnife.bind(UserManagerActivity.this);
        initDate();
        initView();

    }

    private void initDate() {
        util = Utils.getInstance();
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentUserManager();
        fragment1 = new FragmentUserPhone();
        fragment2 =new FragmentUserVerify();
        fragment3 =new FragmentUserPassword();
        fragment4 =new FragmentUserEmail();

        ft.add(R.id.fragment_container, fragment0, PAGE_1);
        ft.add(R.id.fragment_container, fragment1, PAGE_2);
        ft.add(R.id.fragment_container, fragment2, PAGE_3);
        ft.add(R.id.fragment_container, fragment3, PAGE_4);
        ft.add(R.id.fragment_container, fragment4, PAGE_5);
        ft.show(fragment0);
        ft.hide(fragment1);
        ft.hide(fragment2);
        ft.hide(fragment3);
        ft.hide(fragment4);
        ft.commit();

    }


    @Override
    public void finishThis() {
        finish();
    }

    @Override
    public void setFragClear() {
        setResult(Constant.FRAGMENT_CLEAR_DATE);
    }

    @Override
    public void changePhone() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.show(fragment1);
        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if(!fragment0.isVisible()){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.show(fragment0);
                    ft.hide(fragment1);
                    ft.hide(fragment2);
                    ft.hide(fragment3);
                    ft.hide(fragment4);
                    ft.commit();
                    return true;
                }else{
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return false;
    }

    @Override
    public void verify() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment2);
        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public void changeEmail() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment4);
        ft.hide(fragment0);
        ft.commit();
    }

    @Override
    public void changePassword() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment3);
        ft.hide(fragment0);
        ft.commit();
    }

    private void showMain(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment0);
        ft.hide(fragment1);
        ft.hide(fragment2);
        ft.hide(fragment3);
        ft.hide(fragment4);
        ft.commit();
    }
    @Override
    public void finishVerify() {
        showMain();
    }

    @Override
    public void finishPhone() {
        showMain();
    }

    @Override
    public void finishPassword() {
        showMain();
    }

    @Override
    public void finishEmail() {
        showMain();
    }

    @Override
    public void onChangeDissmiss(int status, String phone_num) {
        if(fragment1.isVisible()){
            fragment1.onDialogBack(status,phone_num);
        }
    }
}
