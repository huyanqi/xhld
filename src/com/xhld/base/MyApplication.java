package com.xhld.base;

import im.fir.sdk.FirSdk;
import android.app.Application;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		FirSdk.init(this);
		super.onCreate();
	}
}
