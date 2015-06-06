package com.xhld.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.xhld.utils.Config;
import com.xhld.widget.BaseProcessDialog;

public class BaseActivity extends Activity{

	public Context context;
	public ActionBar actionBar = null;
	public BaseProcessDialog processDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Config.started.add(this);
		initDialog();
	}
	
	private void initDialog() {
		processDialog = new BaseProcessDialog(this);
		processDialog.setCanceledOnTouchOutside(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void initActionBar(String title){
		initactionbar();
		actionBar.setTitle(title);
	}
	
	public void showToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void initactionbar(){
		actionBar = getActionBar();
		this.actionBar.setHomeButtonEnabled(true);
		this.actionBar.setDisplayHomeAsUpEnabled(true);
		this.actionBar.setDisplayShowHomeEnabled(false);
		this.actionBar.setDisplayShowTitleEnabled(true);
	}
}
