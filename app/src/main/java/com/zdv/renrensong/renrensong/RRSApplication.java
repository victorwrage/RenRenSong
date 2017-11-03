package com.zdv.renrensong.renrensong;


import com.baidu.location.LocationClient;

import cn.jpush.android.api.JPushInterface;

/**
 * @ClassName:	NFCApplication 
 * @Description:TODO(Application) 
 * @author:	xiaoyl
 * @date:	2013-7-10 下午4:01:27 
 *  
 */
public class RRSApplication extends VApplication {
	private static RRSApplication instance;
	public static boolean isExit = false;
	public LocationClient mLocationClient = null;
	public RRSApplication() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);

	}

	public static RRSApplication getInstance() {
		if (null == instance) {
			instance = new RRSApplication();
		}
		return instance;
	}
}
