
package com.zdv.renrensong.renrensong;

import android.app.Activity;
import android.app.Application;

import com.socks.library.KLog;

import java.util.LinkedList;
import java.util.List;

/** 
 * @ClassName VApplication 
 * @Description TODO  Application基类
 * @Version 1.0
 * @Creation 2013-8-10 上午10:09:20 
 * @Mender xiaoyl
 * @Modification 2013-8-10 上午10:09:20 
 **/
public class VApplication extends Application {
	protected static VApplication instance;
	private List<Activity> activityList = new LinkedList<Activity>();
	private String myState;
	public static boolean isExit = false;
	
	public VApplication() {

	}

	
	public static VApplication getInstance() {
		if (null == instance) {
			instance = new VApplication();
		}
		return instance;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		try 
	    {
			UncaughtExceptionCatcher.getInstance(getApplicationContext());
	    }
	    catch (Exception localException)
	    {
	       KLog.e(localException.getMessage());
	    }
	}
	
	public String getState() {
		return myState;
	}

	public void setState(String s) {
		myState = s;
	}
	
	/**
	 * 
	 * @param activity
	 */
	public void addActivitys(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 退出应用程序
	 */
	public void exitApplication() {
		isExit = true;
		for (Activity a : activityList) {
			a.finish();
		}
		System.exit(0);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
