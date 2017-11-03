package com.zdv.renrensong.renrensong;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.activity.BaseActivity;
import com.zdv.renrensong.renrensong.activity.EvaluateActivity;
import com.zdv.renrensong.renrensong.bean.RenRenSongCodeResponse;
import com.zdv.renrensong.renrensong.customView.ProgressBarItem;
import com.zdv.renrensong.renrensong.fragment.BaseFetchFragment;
import com.zdv.renrensong.renrensong.fragment.DialogFragmentForget;
import com.zdv.renrensong.renrensong.fragment.DialogFragmentQcode;
import com.zdv.renrensong.renrensong.fragment.DialogFragmentRegisterLogin;
import com.zdv.renrensong.renrensong.fragment.Fragment1;
import com.zdv.renrensong.renrensong.fragment.Fragment4;
import com.zdv.renrensong.renrensong.fragment.FragmentFetch;
import com.zdv.renrensong.renrensong.fragment.FragmentLogin;
import com.zdv.renrensong.renrensong.fragment.FragmentSend;
import com.zdv.renrensong.renrensong.present.QueryPresent;
import com.zdv.renrensong.renrensong.util.Constant;
import com.zdv.renrensong.renrensong.util.Utils;
import com.zdv.renrensong.renrensong.util.VToast;
import com.zdv.renrensong.renrensong.view.ILocationView;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity implements DialogFragmentRegisterLogin.ILoginListtener, FragmentLogin.ILoginListener, FragmentFetch.IFetchListener, FragmentSend.IFragmentSendListener, Fragment1.IFragment1Listener, BaseFetchFragment.IFragmentListener, Fragment4.IFragment4Listtener, DialogFragmentQcode.IQcodeListener, ILocationView,DialogFragmentForget.IForgotListtener {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private static final String PAGE_0 = "page_0";
    private static final String PAGE_1 = "page_1";
    private static final String PAGE_2 = "page_2";

    private FragmentLogin fragment0;
    private FragmentFetch fragment1;
    private FragmentSend fragment2;


    @Bind(R.id.main_fetch_tv)
    TextView main_fetch_tv;
    @Bind(R.id.main_send_tv)
    TextView main_send_tv;
    @Bind(R.id.main_location_tv)
    TextView main_location_tv;
    @Bind(R.id.main_home_iv)
    ImageView main_home_iv;
    @Bind(R.id.main_location_lay)
    LinearLayout main_location_lay;
    /*   @Bind(R.id.progress_bar)
       ProgressBarItem progress_bar;*/
    @Bind(R.id.main_content_body)
    LinearLayout main_content_body;
 /*   @Bind(R.id.progress_bar_mask)
    LinearLayout progress_bar_mask;*/

    @Bind(R.id.main_tab_lay)
    LinearLayout main_tab_lay;
    @Bind(R.id.main_header_lay)
    RelativeLayout main_header_lay;
    private boolean isInit;
    private boolean isForeground = false;
    boolean gotoEvaluate = false;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    private static final String ALIAS_KEY = "alias";
    private String SetAlias = "setAlias";
    private String FLAG_GOEVALUATE = "goEvaluate";
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    KLog.v("Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    KLog.i("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    KLog.i(logs);
                    sp.edit().putBoolean(SetAlias, true).commit();
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    KLog.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    KLog.e(logs);
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fore_lay);
        ButterKnife.bind(MainActivity.this);
        initDate();
        initView();
        KLog.v("onCreate");
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        KLog.v("onNewIntent");
        super.onNewIntent(intent);
        gotoEvaluate = intent.getBooleanExtra(FLAG_GOEVALUATE, false);
        KLog.v(gotoEvaluate+"");
    }

    private void initDate() {
        sp = getSharedPreferences(ALIAS_KEY, MODE_PRIVATE);
        util = Utils.getInstance();
        present = QueryPresent.getInstance(MainActivity.this);
        present.setView(MainActivity.this);


        CustomPushNotificationBuilder builder = new
                CustomPushNotificationBuilder(MainActivity.this,
                R.layout.customer_notitfication_layout,
                R.id.icon,
                R.id.title,
                R.id.text);
        // 指定定制的 Notification Layout
        builder.statusBarDrawable = R.mipmap.icon;
        // 指定最顶层状态栏小图标
        builder.layoutIconDrawable = R.mipmap.icon;
        // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(8, builder);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground = false;
    }

    private void loadData() {
        if (!isForeground) {
            KLog.v("app is not foreground");
            return;
        }
        KLog.v("app is foreground");
        isInit = true;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment1 = new FragmentFetch();
        ft.add(R.id.fragment_container, fragment1, PAGE_1);

        ft.hide(fragment0);
        ft.show(fragment1);
        ft.commit();
        BmobUpdateAgent.update(this);
    }

    /**
     * 当fragment1创建完成后显示tab
     */
    private void showTab() {

        RxView.clicks(main_fetch_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showFetch());
        RxView.clicks(main_send_tv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showSend());
        RxView.clicks(main_home_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> toggle());
        RxView.clicks(main_location_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> location());

        main_tab_lay.setVisibility(View.VISIBLE);
        main_header_lay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }

    @Override
    protected void onResume() {
        KLog.v("onResume");
        isForeground = true;
        super.onResume();
        if (Constant.user_info == null && fragment1 != null) {//登录界面从后台回到前台时调用
            refreshSliding();
            fragment1.setClearFlag();
            if(fragment2!=null) {
                fragment2.fetchFromNetWork();
            }
            showLogin();
        }
        if (!gotoEvaluate && !isInit && Constant.user_info != null) {
            KLog.v("isInit"+isInit);
            loadData();
            refreshSliding();
        }
    }

    private void showLogin() {
        DialogFragmentRegisterLogin fragment = new DialogFragmentRegisterLogin();
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void clearDateFlag() {
        super.clearDateFlag();
        fragment1.setClearFlag();
        if(fragment2!=null) {
            fragment2.isInit = false;
        }
    }

    private void initView() {
        main_fetch_tv.setSelected(true);
        main_send_tv.setSelected(false);
        main_send_tv.setTextColor(0xFF4682B4);
        main_fetch_tv.setTextColor(0xFFFFFFFF);
        main_tab_lay.setVisibility(View.GONE);
        main_header_lay.setVisibility(View.GONE);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentLogin();
        ft.add(R.id.fragment_container, fragment0, PAGE_0);

        ft.show(fragment0);
        ft.commit();
        Bmob.initialize(this, Constant.PUBLIC_BMOB_KEY);
       // BmobUpdateAgent.initAppVersion(context);
        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新

            } else if (updateStatus == UpdateStatus.No) {
                KLog.v("版本无更新");
            } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                KLog.v("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
            } else if (updateStatus == UpdateStatus.IGNORED) {
                KLog.v("该版本已被忽略更新");
            } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                KLog.v("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
            } else if (updateStatus == UpdateStatus.TimeOut) {
                KLog.v("查询出错或查询超时");
            }
        });
    }

    /**
     * 定位
     */
    private void location() {
        main_location_tv.setText(mLocationClient.getLastKnownLocation().getDistrict());
    }

    private void showFetch() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        main_fetch_tv.setSelected(true);
        main_send_tv.setSelected(false);
        main_send_tv.setTextColor(0xFF4682B4);
        main_fetch_tv.setTextColor(0xFFFFFFFF);
        ft.hide(fragment2).show(fragment1).commit();
    }

    private void showSend() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        main_fetch_tv.setTextColor(0xFF4682B4);
        main_send_tv.setTextColor(0xFFFFFFFF);
        main_fetch_tv.setSelected(false);
        main_send_tv.setSelected(true);
        if (fragment2 == null) {
            fragment2 = new FragmentSend();
            ft.add(R.id.fragment_container, fragment2, PAGE_2);
        }
        ft.hide(fragment1).show(fragment2).commit();
    }


    @Override
    public void fragment1Created() {
        showTab();
    }

    @Override
    public void onDataChanged() {
        fragment1.setClearFlag();
    }

    @Override
    public void onDataLoaded() {
        ProgressBarItem.hideProgress();
    }

    @Override
    public void onDataLoaded4() {
        ProgressBarItem.hideProgress();
    }

    @Override
    public void onDataFetchChanged4() {
        if (fragment2 != null) {
            fragment2.isInit = false;
        }
        fragment1.setClearFlag();
    }

    @Override
    public void onDataCancelChanged4() {
        fragment1.setClearFlag();
    }

    @Override
    public void AutoLoginFinished() {
        initSliding();
        startLocation();
    }

    private void startLocation() {
        if (!sp.getBoolean(SetAlias, false)) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Constant.user_info.get(Constant.USER_INFO_ID)));//设置推送别名
        }

        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    @Override
    public void onSendDataLoaded() {
        ProgressBarItem.hideProgress();
    }

    @Override
    public void OnLoginSuccess() {
        if (fragment1 == null) {
            startLocation();
            //loadData();// 已取消->在同步位置后进入首页
        } else {
            if (fragment1.isVisible()) {
                fragment1.refreshFirst();
            } else {
                if (fragment2 != null && fragment2.isVisible()) {
                    fragment2.fetchFromNetWork();
                }
            }
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Constant.user_info.get(Constant.USER_INFO_ID)));//设置推送别名
        initSliding();
        refreshSliding();
    }

    @Override
    public void OnLoginCancel() {
        if (Constant.user_info == null) {
            finish();
        }
    }

    @Override
    public void laterRate() {
        if (fragment2 != null && fragment2.getUserVisibleHint()) {
            fragment2.laterRate();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(300000);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void ResolveSendPoi(RenRenSongCodeResponse info) {
        KLog.v("ResolveSendPoi");
        KLog.v(info == null ? "" : info.toString());
        if (info == null) {
            KLog.v("位置同步失败，不能使用排序功能");
        }
        if (info != null &&( info.getStatus() == null || info.getStatus().equals("failed"))) {
            VToast.toast(context, "位置同步失败，不能使用排序功能");
            KLog.v("位置同步失败，不能使用排序功能");
        }
        if (!gotoEvaluate) {
            loadData();
            refreshSliding();
        }
        if (gotoEvaluate) {//通知栏点击直接到评价栏
            KLog.v("jump to evaluate");
            gotoEvaluate = false;
            startActivity(new Intent(MainActivity.this, EvaluateActivity.class));
            return;
        }
    }

    @Override
    public void onForgotDissmiss() {
        showLogin();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            KLog.v("onReceiveLocation");
            main_location_lay.setEnabled(true);

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                //  KLog.v(location.getAddrStr());
                if (main_location_tv != null)
                    main_location_tv.post(() -> main_location_tv.setText(location.getDistrict()));
                KLog.v("位置更新" + location.getAddrStr());
                if (fragment1 != null) return;
                present.initRetrofit(Constant.URL_RENRENSONG, false);
                present.QueryPoi(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN), location.getLongitude() + "," + location.getLatitude(), Constant.user_info.get(Constant.USER_INFO_PHONE),
                        Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                //KLog.v(location.getAddrStr());
                if (main_location_tv != null)
                    main_location_tv.post(() -> main_location_tv.setText(location.getDistrict()));
                KLog.v("位置更新" + location.getAddrStr());
                if (fragment1 != null) return;
                present.initRetrofit(Constant.URL_RENRENSONG, false);
                present.QueryPoi(Constant.user_info.get(Constant.USER_INFO_ID), Constant.user_info.get(Constant.USER_INFO_TOKEN), location.getLongitude() + "," + location.getLatitude(), Constant.user_info.get(Constant.USER_INFO_PHONE),
                        Constant.user_info.get(Constant.USER_INFO_SHOPPER_ID));

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                KLog.v("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                if (fragment1 != null) return;
                main_fetch_tv.post(() -> ResolveSendPoi(null));
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                if (fragment1 != null) return;
                main_fetch_tv.post(() -> ResolveSendPoi(null));
                KLog.v("定位失败，不能使用排序功能");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                if (fragment1 != null) return;
                main_fetch_tv.post(() -> ResolveSendPoi(null));

                KLog.v("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }else{
                if (fragment1 != null) return;
                main_fetch_tv.post(() -> ResolveSendPoi(null));
                KLog.v("无法获取有效定位依据导致定位失败");
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            KLog.v(s);
        }
    }
}
