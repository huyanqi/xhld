package com.xhld;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.xhld.base.BaseActivity;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;

public class LoginActivity extends BaseActivity implements OnClickListener{

	private static final String USER_KEY = "user_key";
	private EditText mobileEt,pwdEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init(){
		mobileEt = (EditText) findViewById(R.id.login_mobile);
		pwdEt = (EditText) findViewById(R.id.login_password);
		if(Tools.getUser(this) != null){
			Intent intent = new Intent(this,MainActivity.class);
			intent.putExtra("flag", true);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.to_reg:
			Intent intent = new Intent(this,RegActivity.class);
			startActivityForResult(intent, 1024);
			break;
		case R.id.to_login:
			login();
			break;
		}
	}
	
	private void login(){
		String mobile = mobileEt.getText().toString().trim();
		String password = pwdEt.getText().toString().trim();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mobile", mobile);
		param.put("password", password);
		processDialog.show();
		NetTools.getDataFromNetwork(this,"login", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				showToast(error);
			}
			@Override
			public void callBack(JSONObject result) {
				if(Tools.isOk(result)){
					Tools.saveUser(context, result);
					toXHLogin();
				}else{
					processDialog.dismiss();
					showToast("登录失败，请检查手机号/密码。");
				}
			}
		});
	}
	
	private void toXHLogin(){
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("username", mobileEt.getText().toString().trim());
		param.put("password", pwdEt.getText().toString().trim());
		NetTools.getXHDataFromNetwork(this, "appuserlogin", param, new NetToolCallBack() {
			
			@Override
			public void error(String error) {
				showToast(error);
				processDialog.dismiss();
			}
			
			@Override
			public void callBack(JSONObject result) {
				processDialog.dismiss();
				System.out.println(result);
				try {
					result = result.getJSONObject("result");
					if(result.getInt("statusCode") != 200){
						showToast("小合利贷:"+result.getString("message"));
					}else{
						Tools.saveXHUser(LoginActivity.this, mobileEt.getText().toString().trim(), pwdEt.getText().toString().trim());
					}
					startActivity(new Intent(LoginActivity.this,MainActivity.class));
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 1024){
			//注册成功返回
			mobileEt.setText(data.getStringExtra("mobile"));
			pwdEt.setText(data.getStringExtra("password"));
			login();
		}
	}
	
	
	
}
