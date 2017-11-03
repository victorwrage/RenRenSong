package com.zdv.renrensong.renrensong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.zdv.renrensong.renrensong.R;

import java.util.ArrayList;

public class FragmentFetch extends BaseFragment {

	Fragment1 f1;
	Fragment2 f2 ;
	Fragment3 f3 ;
	Fragment4 f4 ;
	ViewPager viewPager;
	IFetchListener listener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.viewpage_lay, container, false);
		 f1 = new Fragment1();
		 f2 = new Fragment2();
		 f3 = new Fragment3();
		 f4 = new Fragment4();
		ArrayList<Fragment> frams = new ArrayList<>();
		frams.add(f1);
		frams.add(f2);
		frams.add(f3);
		frams.add(f4);
		FragmentViewPage adapter = new FragmentViewPage(getFragmentManager(), frams);
       
		viewPager = (ViewPager) view.findViewById(R.id.pager);
		TabPageIndicator indicator = (TabPageIndicator) view
				.findViewById(R.id.indicator);
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		indicator.setViewPager(viewPager, 0);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
				switch(i){
					case 0:
						if(f1.isInit){
							return;
						}
						/*showWaitDialog("正在刷新...");
						f1.fetchFromNetWork(start_position ,load_count);*/
						break;
					case 1:
						if(f2.isInit){
							return;
						}
						/*showWaitDialog("正在刷新...");
						f2.fetchFromNetWork(start_position ,load_count);*/
						break;
					case 2:
						if(f3.isInit){
							return;
						}
						/*showWaitDialog("正在刷新...");
						f3.fetchFromNetWork(start_position ,load_count);*/
						break;
					case 3:
						if(f4.isInit){
							return;
						}
						showWaitDialog("正在刷新...");
						f4.fetchFromNetWork();
						break;
				}
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
		return view;
	}

	public void setClearFlag(){
		f1.isInit = false;
		f2.isInit = false;
		f3.isInit = false;
		f4.isInit = false;
	}

	public void refreshFirst(){
		if(f1.getUserVisibleHint()) {
			f1.fetchFromNetWork(  start_position ,load_count);
			return;
		}
		if(f2.getUserVisibleHint()) {
			f2.fetchFromNetWork(start_position ,load_count);
			return;
		}
		if(f3.getUserVisibleHint()) {
			f3.fetchFromNetWork(start_position ,load_count);
			return;
		}
		if(f4.getUserVisibleHint()) {
			f4.fetchFromNetWork();
			return;
		}
    }

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			listener = (IFetchListener)context;
		}catch(Exception e){
			e.fillInStackTrace();
		}
	}

	public  interface IFetchListener {

	}
}
