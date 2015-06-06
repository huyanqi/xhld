package com.xhld.utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;


public class Config {
	
	public static List<Activity> started = new ArrayList<Activity>();//�Ѵ򿪹��activity
	
	public static Map<String, SoftReference<Bitmap>> imageCaches;
	
	public static String callingNum;
	
}
