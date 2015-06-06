package com.xhld.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.CallLog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xhld.CallActivity;
import com.xhld.R;
import com.xhld.bean.UserModel;
import com.xhld.bean.XHUserInfo;

public class Tools {
	
	private static final String WL_USER_KEY = "wl_user_key";
	private static final String XH_USER_KEY = "xh_user_key";

	public static boolean isOk(JSONObject result) {
		try {
			if ("ok".equals(result.getString("result"))) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static <T> List<T> jAry2List(JSONArray jAry, Class<T> c) {
		Gson gson = new Gson();
		List<T> list = new ArrayList<T>();
		try {
			for (int i = 0; i < jAry.length(); i++) {
				list.add(gson.fromJson(jAry.get(i).toString(), c));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	public static void setGridViewHeightBasedOnChildren(Context context,GridView gridView) {
		ListAdapter listAdapter = gridView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		
		if(listAdapter.getCount() == 0) return;
		View listItem = listAdapter.getView(0, null, gridView);
		listItem.measure(0, 0);
		int itemHeight = listItem.getMeasuredHeight();
		
		int line = (listAdapter.getCount() % 3) > 0 ? (listAdapter.getCount() / 3) + 1 : listAdapter.getCount() / 3;

		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		params.height = line * (Tools.dip2px(context, 5) + itemHeight);
		gridView.setLayoutParams(params);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}
	
	public static UserModel getUser(Context context){
		String userJson = context.getSharedPreferences(WL_USER_KEY, 0).getString(WL_USER_KEY, "");
		Gson gson = new Gson();
		return gson.fromJson(userJson, UserModel.class);
	}
	
	public static void saveUser(Context context,JSONObject obj){
		context.getSharedPreferences(WL_USER_KEY, 0).edit().putString(WL_USER_KEY, obj.optString("msg")).commit();
	}
	
	public static void logOut(Context context){
		context.getSharedPreferences(WL_USER_KEY, 0).edit().clear().commit();
	}
	
	public static void insertCallLog(Context context,String name,String number){
		ContentValues values = new ContentValues();
	    values.put(CallLog.Calls.CACHED_NAME, name);
	    values.put(CallLog.Calls.NUMBER, number);
	    values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
	    values.put(CallLog.Calls.DATE, System.currentTimeMillis());
	    values.put(CallLog.Calls.NEW, 0);//0已看 1未看
	    context.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
	}  
	
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static void toCall(Context context,String num,String name){
		Intent intent = new Intent(context,CallActivity.class);
		intent.putExtra("num", num);
		if(name != null){
			intent.putExtra("name", name);
		}
		context.startActivity(intent);
	}
	
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	/**    
     * 去除特殊字符或将所有中文标号替换为英文标号    
     *     
     * @param str    
     * @return    
     */     
    public static String stringFilter(String str) {      
        str = str.replaceAll("【", "[").replaceAll("】", "]")      
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号      
        String regEx = "[『』]"; // 清除掉特殊字符      
        Pattern p = Pattern.compile(regEx);      
        Matcher m = p.matcher(str);      
        return m.replaceAll("").trim();      
    }
    
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
    	// 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			return packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
    }
    
    public static void setItemBackGround(View view,int position){
    	if(position % 2 == 0){
    		view.setBackgroundResource(android.R.color.white);
    	}else{
    		view.setBackgroundResource(R.color.line);
    	}
    }
    
    public static boolean XHUserIsExist(Activity context){
    	String username = context.getSharedPreferences(XH_USER_KEY, 0).getString("username", "");
    	if(!"".equals(username))
    		return true;
    	return false;
    }
	
    public static void saveXHUser(Activity activity,String username,String password){
    	activity.getSharedPreferences(XH_USER_KEY, 0).edit().putString("username", username).commit();
    	activity.getSharedPreferences(XH_USER_KEY, 0).edit().putString("password", password).commit();
    }
    
    public static XHUserInfo getXHUser(Context context){
    	XHUserInfo user = new XHUserInfo();
    	user.mobile = context.getSharedPreferences(XH_USER_KEY, 0).getString("username", "");
    	user.password = context.getSharedPreferences(XH_USER_KEY, 0).getString("password", "");
    	return user;
    }
    
    public static int getAge(String birthday){
    	
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = new Date();
		java.util.Date mydate;
		try {
			mydate = myFormatter.parse(birthday);
			long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000)
					+ 1;
			String year = new java.text.DecimalFormat("#").format(day / 365f);
			return Integer.parseInt(year);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return 0;
    }
    
    public static String long2Str(Long paramLong){
    	Date localDate = new Date(paramLong.longValue());
    	return new SimpleDateFormat("yyyy-MM-dd").format(localDate);
    }
    
    public static String getMarryType(int type){
    	String[] types = new String[]{"","未婚","已婚","丧偶","离婚"};
    	return types[type];
    }
    
    public static String getDegree(int degree){
    	String[] degrees = new String[]{"初中及以下","高中或中专","大学专科","大学本科(含双学历)","研究生或以上"};
    	return degrees[degree];
    }
    
    public static String getEmployeeType(int type){
    	String[] types = new String[]{"受薪人士","自雇人士"};
    	if(type >= types.length)
    		return "";
    	return types[type];
    }
    
}
