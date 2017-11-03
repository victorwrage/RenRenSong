package com.zdv.renrensong.renrensong.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zdv.renrensong.renrensong.R;
import com.zdv.renrensong.renrensong.RRSApplication;
import com.zdv.renrensong.renrensong.fragment.ListFragmentMenuSliding;
import com.zdv.renrensong.renrensong.util.DoubleConfirm;


/** 
 * @ClassName:	BaseActivity 
 * @Description:TODO(界面的基类) 
 * @author:	xiaoyl
 * @date:	2013-7-10 下午2:30:06 
 *  
 */
public class BaseActivity extends SlidingFragmentActivity implements ListFragmentMenuSliding.IFragmentSlindingListtener {
	private DoubleConfirm double_c;
	ListFragmentMenuSliding mFrag;
	SlidingMenu slidingMenu;
	protected Context context;


	/** 双击事件 */
	private DoubleConfirm.DoubleConfirmEvent doubleConfirmEvent = new DoubleConfirm.DoubleConfirmEvent() {
		public void doSecondConfirmEvent() {
			RRSApplication.getInstance().exitApplication();
		}

		public int getFirstConfirmTipsId() {
			return R.string.msg_exit;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.activity_behind_lay);

		RRSApplication.getInstance().addActivitys(this);
		context = getApplicationContext();

		this.double_c = new DoubleConfirm();
		this.double_c.setEvent(this.doubleConfirmEvent);

	}

	public void initSliding(){
		if (mFrag == null) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			mFrag = new ListFragmentMenuSliding();
			ft.replace(R.id.container_behind, mFrag);
			ft.commit();
		} else {
			mFrag = (ListFragmentMenuSliding) getSupportFragmentManager()
					.findFragmentById(R.id.container_behind);
		}
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		int width = 2 * localDisplayMetrics.widthPixels / 3;

		this.slidingMenu = getSlidingMenu();
		this.slidingMenu.setMode(SlidingMenu.LEFT);
		//this.slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		this.slidingMenu.setShadowDrawable(R.drawable.shadow);
		//this.slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		//this.slidingMenu.setFadeDegree(0.35F);
		this.slidingMenu.setTouchModeAbove(SlidingMenu.LEFT);
		/*this.slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}
		});*/
		this.slidingMenu.setBehindWidth(width);
		invalidateOptionsMenu();

		/*getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.bg_tab));*/
	}

	protected void refreshSliding(){
        if(mFrag!=null) {
            mFrag.initView();
        }
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if(slidingMenu!=null && slidingMenu.isMenuShowing()){
			return super.onKeyDown(paramInt, paramKeyEvent);
		}
		this.double_c.onKeyPressed(paramKeyEvent, this);
		return false;
	}

	@Override
	public void clearDateFlag() {

	}
}
