package com.xhld.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.xhld.utils.Config;
import com.xhld.widget.BaseProcessDialog;

public class BaseFragmentActivity extends FragmentActivity{
	
	public BaseProcessDialog processDialog = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Config.started.add(this);
		initDialog();
	}
	
	private void initDialog() {
		processDialog = new BaseProcessDialog(this);
		processDialog.setCanceledOnTouchOutside(false);
	}
	
	public void showToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
