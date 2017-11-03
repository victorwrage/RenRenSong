package com.zdv.renrensong.renrensong.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zdv.renrensong.renrensong.view.IFragment;

import java.util.ArrayList;

/**
 * 主界面FragmentPagerAdapter适配器
 * @author xiaoyl
 * @date 2013-07-16
 */
public class FragmentViewPage extends FragmentPagerAdapter {
	ArrayList<Fragment> fragments;
	public FragmentViewPage(FragmentManager fm, ArrayList<Fragment> fragments) {
		
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}
    
	@Override
	public CharSequence getPageTitle(int position) {
		IFragment d =  (IFragment)fragments.get(position);
		return d.getTitle();
	}
	@Override
	public int getCount() {

		return fragments.size();
	}
}
