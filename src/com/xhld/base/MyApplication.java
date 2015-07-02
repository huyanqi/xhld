package com.xhld.base;

import im.fir.sdk.FIR;
import android.app.Application;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		FIR.init(this);
		super.onCreate();
	}
}
