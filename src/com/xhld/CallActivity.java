package com.xhld;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Html;
import android.widget.TextView;

import com.xhld.base.BaseActivity;
import com.xhld.bean.AdModel;
import com.xhld.bean.UserModel;
import com.xhld.utils.Config;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;
import com.xhld.widget.SYNCImageView;

public class CallActivity extends BaseActivity {

	private TextView nameTv;
	private TextView numTv;
	private TextView statusTv;
	private SYNCImageView adIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		initWidget();
		init();
		getAd();
	}

	private void initWidget() {
		nameTv = (TextView) findViewById(R.id.call_name);
		numTv = (TextView) findViewById(R.id.call_num);
		statusTv = (TextView) findViewById(R.id.status_tv);
		adIv = (SYNCImageView) findViewById(R.id.img);
	}

	private void init() {
		if (!getIntent().hasExtra("num")) {
			showToast("没有传入num");
			finish();
			return;
		}
		String num = getIntent().getStringExtra("num");
		String name = getIntent().hasExtra("name") ?  getIntent().getStringExtra("name") : queryNameByNum(num);
		Tools.insertCallLog(this,name,num);
		nameTv.setText(name != null ? name : num);
		numTv.setText(num);
		toCall(num);
	}
	
	private void getAd(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("code", 1026);
		NetTools.getDataFromNetwork(this,"getAd", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
			}

			@Override
			public void callBack(JSONObject result) {
				try {
					if (Tools.isOk(result)) {
						JSONArray jAry = result.getJSONArray("model");
						AdModel model = Tools.jAry2List(jAry,AdModel.class).get(0);
						adIv.getImageFromURL(model.url, null);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},true);
	}
	
	private void toCall(final String num){
		final UserModel user = Tools.getUser(this);
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("fromCaller", user.mobile);
		param.put("toCaller", num);
		param.put("accId", user.accCodeId);
		//param.put("accPwd", user.accCodePwd);
		param.put("callerDisplayNumber", num);
		NetTools.getDataFromNetwork(context, "call", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				statusTv.setText(error);
				//开始5秒自动关闭
				setTimer();
			}
			
			@Override
			public void callBack(JSONObject result) {
				if(Tools.isOk(result)){
					statusTv.setText(Html.fromHtml("呼叫请求提交成功，系统正在回呼你的手机\r\n"+user.mobile+"请注意接听。"));
					Config.callingNum = num;
				}else{
					statusTv.setText("呼叫请求不成功，请稍后重试。");
				}
				
				//开始5秒自动关闭
				setTimer();
			}
		});
	}
	
	private void setTimer(){
		
		final Handler timerHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				finish();
			}
		};
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				timerHandler.sendEmptyMessage(1);
			}
		}, 5 * 1000);
	}

	private String queryNameByNum(String num) {
		Cursor cursorOriginal = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + num + "'", null, null);
		if (null != cursorOriginal) {
			if (cursorOriginal.getCount() > 1) {
				return null;
			} else {
				if (cursorOriginal.moveToFirst()) {
					return cursorOriginal
							.getString(cursorOriginal
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}
	
	@Override
	public void finish() {
		Config.callingNum = null;
		super.finish();
	}
	
}
