package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.renrensong.renrensong.activity.CreditActivity;
import com.zdv.renrensong.renrensong.activity.EvaluateActivity;
import com.zdv.renrensong.renrensong.activity.FeedBackActivity;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.activity.PakageActivity;
import com.zdv.renrensong.renrensong.activity.SettingActivity;
import com.zdv.renrensong.renrensong.activity.UserManagerActivity;
import com.zdv.renrensong.renrensong.util.Constant;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 侧滑栏界面
 *
 * @author xiaoyl
 * @date 2013-07-16
 */
public class ListFragmentMenuSliding extends ListFragment implements OnItemClickListener {

    @Bind(R.id.setting_header_lay)
    LinearLayout setting_header_lay;
    @Bind(R.id.setting_phone_tv)
    TextView setting_phone_tv;
    IFragmentSlindingListtener listtener;
    private String menus[] = {"信用分", "钱包", "反馈", "设置","我的评价"};
    private int icons[] = {R.drawable.credit, R.drawable.cash_pakage,
            R.drawable.feedback, R.drawable.setting, R.drawable.evaluate};

    @Override
    public void onAttach(Context context) {
        try {
            listtener = (IFragmentSlindingListtener) context;
        }catch(Exception e){
            e.fillInStackTrace();
        }
        super.onAttach(context);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MenuAdapter adapter = new MenuAdapter(getActivity());
        adapter.add(new SlidingMenuItem(menus[0], icons[0]));
        adapter.add(new SlidingMenuItem(menus[1], icons[1]));
        adapter.add(new SlidingMenuItem(menus[2], icons[2]));
        adapter.add(new SlidingMenuItem(menus[3], icons[3]));
        adapter.add(new SlidingMenuItem(menus[4], icons[4]));
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.container_lay, null);
        ButterKnife.bind(ListFragmentMenuSliding.this, view);

        RxView.clicks(setting_header_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> userManager());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.user_info == null) {
            setting_phone_tv.setText("点击登录");
        } else {
            setting_phone_tv.setText(Constant.user_info.get(Constant.USER_INFO_USER_NAME));
        }
    }

    protected void userManager() {
        if (Constant.user_info == null) {
            DialogFragmentRegisterLogin fragment = new DialogFragmentRegisterLogin();
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "");

        } else {
            startActivityForResult(new Intent(getContext(), UserManagerActivity.class),0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.FRAGMENT_CLEAR_DATE){//更换用户后清除数据
            KLog.v("切换用户");
            listtener.clearDateFlag();
        }
    }

    public void initView() {
        if(setting_phone_tv !=null && Constant.user_info!=null) {
            setting_phone_tv.setText(Constant.user_info.get(Constant.USER_INFO_USER_NAME));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg2) {
            case 0:
                startCredit();
                break;

            case 1:
                startPakage();
                break;
            case 2:
                startFeedBack();
                break;
            case 3:
                startSetting();
                break;
            case 4:
                startEvaluate();
                break;
        }

    }

    private void startEvaluate() {
        startActivity(new Intent(getContext(), EvaluateActivity.class));
    }

    private void startPakage() {
        startActivity(new Intent(getContext(), PakageActivity.class));
    }

    private void startCredit() {
        startActivity(new Intent(getContext(), CreditActivity.class));
    }

    private void startSetting() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }


    private void startPreference() {


    }

    private void startFeedBack() {
        startActivity(new Intent(getContext(), FeedBackActivity.class));
    }


    private class MenuAdapter extends ArrayAdapter<SlidingMenuItem> {

        public MenuAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.behind_row_lay, null);
            }
            ImageView img = (ImageView) convertView.findViewById(R.id.row_icon);
            img.setImageResource(getItem(position).getIcon());
            TextView txv = (TextView) convertView.findViewById(R.id.row_title);
            txv.setText(getItem(position).getName());
            return convertView;
        }
    }

    private class SlidingMenuItem {
        private String name;
        private int icon;

        public SlidingMenuItem(String name, int icon_res) {
            this.name = name;
            this.icon = icon_res;
        }

        public String getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

    }

    public  interface IFragmentSlindingListtener {

        void clearDateFlag();
    }
}
