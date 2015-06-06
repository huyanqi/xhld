package com.xhld;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.xhld.base.BaseActivity;
import com.xhld.bean.UserModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;

public class PayActivity extends BaseActivity implements OnClickListener{

	private TextView bindNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initWidget();
	}
	
	private void initWidget(){
		UserModel user = Tools.getUser(this);
		bindNum = (TextView) findViewById(R.id.bind_num);
		
		bindNum.setText("绑定手机号:"+user.mobile);
	}

	@Override
	public void onClick(View arg0) {
		String cardId = ((EditText)findViewById(R.id.card_id)).getText().toString();
		String cardPwd = ((EditText)findViewById(R.id.card_pwd)).getText().toString();
		if(cardId.trim().length() == 0 || cardPwd.trim().length() == 0){
			showToast("请填写卡号/卡密");
			return;
		}
		HashMap<String, Object> param = new HashMap<String,Object>();
		param.put("mobile", Tools.getUser(context).mobile);
		param.put("cardId", cardId);
		param.put("cardPwd", cardPwd);
		NetTools.getDataFromNetwork(context, "pay", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				showToast("充值异常");
			}
			
			@Override
			public void callBack(JSONObject result) {
				if(Tools.isOk(result)){
					showToast("充值成功.");
					finish();
				}else{
					showToast(result.optString("msg"));
				}
			}
		});
	}
	
}
