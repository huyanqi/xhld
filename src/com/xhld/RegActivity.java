package com.xhld;

import java.util.HashMap;

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

public class RegActivity extends BaseActivity implements OnClickListener{

	private EditText usernameEt,passwordEt,mobileEt,mailEt,codeEt;
	private boolean isSend = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		initWidget();
	}
	
	private void initWidget(){
		usernameEt = (EditText) findViewById(R.id.reg_username);
		passwordEt = (EditText) findViewById(R.id.reg_password);
		mobileEt = (EditText) findViewById(R.id.reg_mobile);
		mailEt = (EditText) findViewById(R.id.reg_mail);
		codeEt = (EditText) findViewById(R.id.reg_code);
		
		findViewById(R.id.reg_sendMobileCode).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.reg_submit:
			toReg();
			break;
		case R.id.reg_sendMobileCode:
			sendMobileCode();
			break;
		}
	}
	
	private void sendMobileCode(){
		if(mobileEt.getText().toString().trim().length() != 11) {
			showToast("请输入正确的手机号");
			return;
		}
		int code = (int) ((Math.random()*9+1)*100000);
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("mobile", mobileEt.getText().toString().trim());
		param.put("mobileCode", code);
		processDialog.show();
		NetTools.getXHDataFromNetwork(this, "appSendMessage", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				isSend = true;
				mobileEt.setEnabled(false);
				//showToast(error);
				showToast("短信已发送");
			}
			
			@Override
			public void callBack(JSONObject result) {
				processDialog.dismiss();
				isSend = true;
				showToast("短信已发送");
				mobileEt.setEnabled(false);
				/*try {
					result = result.getJSONObject("result");
					if("".equals(result.getString("message"))){
						showToast("短信已发送");
					}else{
						showToast(result.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}*/
			}
		}, false);
	}
	
	private void toReg(){
		if(!isSend){
			showToast("请先获取验证码");
			return;
		}
		final String username = usernameEt.getText().toString();
		final String password = passwordEt.getText().toString();
		final String email = mailEt.getText().toString();
		final String mobile = mobileEt.getText().toString();
		final String code = codeEt.getText().toString();
		if(username.length() == 0){
			showToast("请填写用户名");
			return;
		}
		if(password.length() == 0){
			showToast("请填写密码");
			return;
		}
		if(code.length() != 6){
			showToast("请填写手机短信获取到的验证码");
			return;
		}
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("mobile", mobile);
		map.put("password", password);
		processDialog.show();
		NetTools.getDataFromNetwork(this,"reg", map, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				showToast(error);
			}
			@Override
			public void callBack(JSONObject result) {
				processDialog.dismiss();
				if(Tools.isOk(result)){
					//注册到小合
					regToXH(code, username, password, email, mobile);
				}else{
					showToast("注册失败，请更换手机号重试。");
					regToXH(code, username, password, email, mobile);
				}
			}
		});
	}
	
	private void regToXH(String code,String username,final String password,String email,final String tel){
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("mobileCode", code);
		NetTools.getXHDataFromNetwork(this, "appCheckMobileCode", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				showToast(error);
			}
			
			@Override
			public void callBack(JSONObject result) {
				processDialog.dismiss();
				try {
					result = result.getJSONObject("result");
					if(result.getInt("statusCode") == 200){
						//注册成功
						Intent intent = new Intent();
						intent.putExtra("mobile", tel);
						intent.putExtra("password", password);
						setResult(RESULT_OK,intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, false);
	}
	
}
