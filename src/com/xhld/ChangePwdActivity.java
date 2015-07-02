package com.xhld;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xhld.base.BaseActivity;
import com.xhld.bean.UserModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;

public class ChangePwdActivity extends BaseActivity implements OnClickListener{

	private EditText oldPwd,newPwd,newPwdR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		initWidget();
	}
	
	private void initWidget(){
		oldPwd = (EditText) findViewById(R.id.old_pwd);
		newPwd = (EditText) findViewById(R.id.new_pwd);
		newPwdR = (EditText) findViewById(R.id.new_pwd_replace);
		
		findViewById(R.id.submit_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String oldPwdString = oldPwd.getText().toString();
		final String newPwdString = newPwd.getText().toString();
		final String newPwdRString = newPwdR.getText().toString();
		if("".equals(oldPwdString) || "".equals(newPwdString) || "".equals(newPwdRString)){
			showToast("每项必填");
			return;
		}
		if(!newPwdRString.equals(newPwdString)){
			showToast("2次新密码填写不一致");
			return;
		}
		if(newPwdString.length() < 6){
			showToast("密码必须大于6位数");
			return;
		}
		final UserModel user = Tools.getUser(this);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("oldPwd", oldPwdString);
		param.put("newPwd", newPwdString);
		processDialog.show();
		NetTools.getXHDataFromNetwork(this,"updateUserPwd", param, new NetToolCallBack() {
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
						Gson gson = new Gson();
						Tools.saveXHUser(ChangePwdActivity.this, user.mobile, newPwdString);
						UserModel model = Tools.getUser(ChangePwdActivity.this);
						model.password = newPwdRString;
						Tools.saveUser(ChangePwdActivity.this, new JSONObject(gson.toJson(model)));
						showToast("密码修改成功");
						finish();
					}else{
						showToast(result.getString("message"));
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		},false);
	}
	
}
