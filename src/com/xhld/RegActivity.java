package com.xhld;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xhld.base.BaseActivity;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;

public class RegActivity extends BaseActivity implements OnClickListener{

	private EditText usernameEt,passwordEt,reg_password2,mobileEt,mailEt,codeEt;
	private Button sendMobileCode;
	private int time = 100;//100s
	
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
		reg_password2 = (EditText) findViewById(R.id.reg_password2);
		mailEt = (EditText) findViewById(R.id.reg_mail);
		codeEt = (EditText) findViewById(R.id.reg_code);
		sendMobileCode = (Button) findViewById(R.id.reg_sendMobileCode);
		
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
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("mobile", mobileEt.getText().toString().trim());
		param.put("mobileCode", (int)((Math.random()*9+1)*100000));
		processDialog.show();
		NetTools.getXHDataFromNetwork(this, "appSendMessage", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				showToast("error:"+error);
			}
			
			@Override
			public void callBack(JSONObject result) {
				processDialog.dismiss();
				showToast("验证码已发送至该手机，请查收");
				sendMobileCode.setEnabled(false);
				try {
					result = result.getJSONObject("result");
					if(result.getInt("statusCode") == 200){
						showToast("验证码已发送至该手机，请查收");
						setTimer();
					}else{
						showToast(result.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, false);
	}
	
	private void setTimer(){
		time = 100;
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				sendMobileCode.setText("下次发送:"+time--);
				if(time == 0){
					sendMobileCode.setText("获取验证码");
					sendMobileCode.setEnabled(true);
				}
			}
		};
		TimerTask task = new TimerTask(){  
		      public void run() {  
		      Message message = new Message();      
		      message.what = 1;      
		      handler.sendMessage(message);    
		   }  
		};
		Timer timer = new Timer(true);;
		timer.schedule(task, 0, 1000);
	}
	
	private void toReg(){
		/*if(!isSend){
			showToast("请先获取验证码");
			return;
		}*/
		final String username = usernameEt.getText().toString();
		final String password = passwordEt.getText().toString();
		final String email = mailEt.getText().toString();
		final String mobile = mobileEt.getText().toString();
		final String code = codeEt.getText().toString();

		if(!password.equals(reg_password2.getText().toString())){
			showToast("两次密码不一致");
			return;
		}
		
		if(username.length() == 0){
			showToast("请填写用户名");
			return;
		}
		if(password.length() == 0){
			showToast("请填写密码");
			return;
		}
		
		if(code.length() == 0){
			showToast("请填写手机获取到的验证码");
			return;
		}
		regToXH(username, password, email, mobile, code);
		
		/*HashMap<String,Object> map = new HashMap<String,Object>();
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
		});*/
	}
	
	private void regToXH(final String username,final String password,final String email,final String tel,String mobileCode){
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("username", username);
		param.put("password", password);
		param.put("email", email);
		param.put("tel", tel);
		param.put("mobileCode", mobileCode);
		System.out.println(param);
		processDialog.show();
		NetTools.getXHDataFromNetwork(RegActivity.this, "appregister", param, new NetTools.NetToolCallBack() {
			
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
					if(result.getInt("statusCode") != 200){
						showToast(result.getString("message"));
						return;
					}
					Intent intent = new Intent();
					intent.putExtra("mobile", tel);
					intent.putExtra("password", password);
					setResult(RESULT_OK,intent);
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, false);
		
		/*HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("mobileCode", code);
		NetTools.getXHDataFromNetwork(this, "appCheckMobileCode", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				processDialog.dismiss();
				showToast(error);
			}
			
			@Override
			public void callBack(JSONObject result) {
				try {
					result = result.getJSONObject("result");
					if(result.getInt("statusCode") == 0){
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, false);*/
	}
	
}
