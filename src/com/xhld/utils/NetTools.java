package com.xhld.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.xhld.bean.XHUserInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * @author Frankie
 * 
 */
public class NetTools {

	public static final String HOST = "http://202.98.133.37/";
	public static final String XHHOST = "http://www.xiaohelidai.com/app/";
	public static final String XHHOST2 = "http://www.xiaohelidai.com/page/";
	//public static final String HOST = "http://192.168.199.220:8080/WanLianKuaiBo/";
	public static final String CUSTOMER = "xhld";

	public static void getDataFromNetwork(Context context,final String action,final Map<String, Object> param, final NetToolCallBack callback) {
		getDataFromNetwork(context,action,param,callback,false);
	}
	
	public static void getDataFromNetwork(final Context context,final String action,final Map<String, Object> param, final NetToolCallBack callback,final Boolean readCache) {
		if(readCache){
			getDataFromCache(context, action, param, callback);
		}
		
		final Handler dataHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(callback == null || msg == null || msg.obj == null) return;
				if (msg.what == 0) {
					callback.callBack((JSONObject) msg.obj);
				} else {
					callback.error(msg.obj.toString());
				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					HttpPost httpPost = new HttpPost(HOST + action);
					HttpEntity entity;
					List<NameValuePair> datas = new ArrayList<NameValuePair>();
					if(param != null){
						for(String set : param.keySet()){
							BasicNameValuePair bnvp = new BasicNameValuePair(set, String.valueOf(param.get(set)));
							datas.add(bnvp);
						}
					}
					datas.add(new BasicNameValuePair("customer", CUSTOMER));
					entity = new UrlEncodedFormEntity(datas, HTTP.UTF_8);
					httpPost.setEntity(entity);
					HttpClient httpClient = new DefaultHttpClient();
					
					//连接超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
					//请求超时
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
					
					HttpResponse httpResp = httpClient.execute(httpPost);
					if (httpResp.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
						Message msg = new Message();
						msg.what = 0;
						msg.obj = new JSONObject(result);
						dataHandler.sendMessage(msg);
						
						if(readCache){
							writeToCache(context, action, param, result);
						}
					}else{
						Message msg = new Message();
						msg.what = 1;
						msg.obj = httpResp.getStatusLine().getStatusCode() + ":" + action +" 请求失败，请检查网络设置是否正常。";
						dataHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 1;
					msg.obj = e.getMessage();
					dataHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	public static void getXHDataFromNetwork(final Context context,final String action,final Map<String, Object> param, final NetToolCallBack callback,final Boolean readCache) {
		if(readCache){
			getXHDataFromCache(context, action, param, callback);
		}
		
		final Handler dataHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(callback == null || msg == null || msg.obj == null) return;
				if (msg.what == 0) {
					callback.callBack((JSONObject) msg.obj);
				} else {
					callback.error(msg.obj.toString());
				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					HttpPost httpPost = new HttpPost(XHHOST + action);
					HttpEntity entity;
					List<NameValuePair> datas = new ArrayList<NameValuePair>();
					if(param != null){
						for(String set : param.keySet()){
							BasicNameValuePair bnvp = new BasicNameValuePair(set, String.valueOf(param.get(set)));
							datas.add(bnvp);
						}
					}
					datas.add(new BasicNameValuePair("customer", CUSTOMER));
					
					XHUserInfo user = Tools.getXHUser(context);
					datas.add(new BasicNameValuePair("uname", user.mobile));
					datas.add(new BasicNameValuePair("upwd", user.password));
					
					entity = new UrlEncodedFormEntity(datas, HTTP.UTF_8);
					httpPost.setEntity(entity);
					HttpClient httpClient = new DefaultHttpClient();
					
					//连接超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
					//请求超时
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
					
					HttpResponse httpResp = httpClient.execute(httpPost);
					if (httpResp.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
						Message msg = new Message();
						msg.what = 0;
						msg.obj = new JSONObject(result);
						dataHandler.sendMessage(msg);
						
						if(readCache){
							writeToXHCache(context, action, param, result);
						}
					}else{
						Message msg = new Message();
						msg.what = 1;
						msg.obj = httpResp.getStatusLine().getStatusCode() + ":" + action +" 请求失败，请检查网络设置是否正常。";
						dataHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 1;
					msg.obj = e.getMessage();
					dataHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	public static void getXHDataFromNetwork2(final Context context,final String action,final Map<String, Object> param, final NetToolCallBack callback,final Boolean readCache) {
		if(readCache){
			getXHDataFromCache(context, action, param, callback);
		}
		
		final Handler dataHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(callback == null || msg == null || msg.obj == null) return;
				if (msg.what == 0) {
					callback.callBack((JSONObject) msg.obj);
				} else {
					callback.error(msg.obj.toString());
				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					HttpPost httpPost = new HttpPost(XHHOST2 + action);
					HttpEntity entity;
					List<NameValuePair> datas = new ArrayList<NameValuePair>();
					if(param != null){
						for(String set : param.keySet()){
							BasicNameValuePair bnvp = new BasicNameValuePair(set, String.valueOf(param.get(set)));
							datas.add(bnvp);
						}
					}
					datas.add(new BasicNameValuePair("customer", CUSTOMER));
					
					XHUserInfo user = Tools.getXHUser(context);
					datas.add(new BasicNameValuePair("uname", user.mobile));
					datas.add(new BasicNameValuePair("upwd", user.password));
					
					entity = new UrlEncodedFormEntity(datas, HTTP.UTF_8);
					httpPost.setEntity(entity);
					HttpClient httpClient = new DefaultHttpClient();
					
					//连接超时
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
					//请求超时
					httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
					
					HttpResponse httpResp = httpClient.execute(httpPost);
					if (httpResp.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
						Message msg = new Message();
						msg.what = 0;
						msg.obj = new JSONObject(result);
						dataHandler.sendMessage(msg);
						
						if(readCache){
							writeToXHCache(context, action, param, result);
						}
					}else{
						Message msg = new Message();
						msg.what = 1;
						msg.obj = httpResp.getStatusLine().getStatusCode() + ":" + action +" 请求失败，请检查网络设置是否正常。";
						dataHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 1;
					msg.obj = e.getMessage();
					dataHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	private static final String CACHE = "cache";
	private static final String XHCACHE = "xhcache";
	
	private static void getXHDataFromCache(Context context,String action,Map<String, Object> param,NetToolCallBack callback){
		String key = action + "?";
		if(param != null){
			for(String set : param.keySet()){
				key += set + "=" + param.get(set);
			}
		}
		String result = context.getSharedPreferences(XHCACHE, 0).getString(key, "");
		if(!"".equals(result)){
			try {
				callback.callBack(new JSONObject(result));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void getDataFromCache(Context context,String action,Map<String, Object> param,NetToolCallBack callback){
		String key = action + "?";
		if(param != null){
			for(String set : param.keySet()){
				key += set + "=" + param.get(set);
			}
		}
		String result = context.getSharedPreferences(CACHE, 0).getString(key, "");
		if(!"".equals(result)){
			try {
				callback.callBack(new JSONObject(result));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void writeToXHCache(Context context,String action,Map<String, Object> param,String content){
		String key = action + "?";
		if(param != null){
			for(String set : param.keySet()){
				key += set + "=" + param.get(set);
			}
		}
		context.getSharedPreferences(XHCACHE, 0).edit().putString(key, content).commit();
	}
	
	private static void writeToCache(Context context,String action,Map<String, Object> param,String content){
		String key = action + "?";
		if(param != null){
			for(String set : param.keySet()){
				key += set + "=" + param.get(set);
			}
		}
		context.getSharedPreferences(CACHE, 0).edit().putString(key, content).commit();
	}

	public interface NetToolCallBack {
		public void callBack(JSONObject result) ;
		public void error(String error) ;
	};

}
