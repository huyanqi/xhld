package com.xhld;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xhld.adapter.MyFragmentPagerAdapter;
import com.xhld.base.BaseFragmentActivity;
import com.xhld.bean.UserModel;
import com.xhld.fragment.CallFragment;
import com.xhld.fragment.MeFragment;
import com.xhld.fragment.XHFragment;
import com.xhld.utils.Config;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;
import com.xhld.widget.MyViewPager;

public class MainActivity extends BaseFragmentActivity implements OnClickListener {

	private LinearLayout tab1, tab2, tab3, tab4;
	private ArrayList<LinearLayout> tabsList = null;
	private MyViewPager viewpager;
	private ArrayList<Fragment> fragments = null;
	private CallFragment callFragment; 
	private XHFragment xhFragment;
	private MeFragment meFragment;
	private int currentFragment = -1;
	private int[] tabImgs = new int[]{R.drawable.icon_tab1_p,R.drawable.icon_tab2_p,R.drawable.icon_tab4_p};
	private int[] tabImgsP = new int[]{R.drawable.tab1_press,R.drawable.tab2_press,R.drawable.tab4_press};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		initWidget();
		loadConfig();
		checkVersion();
	}
	
	private void initWidget(){
		loadFragment();
		viewpager = (MyViewPager) findViewById(R.id.viewpager);
		viewpager.setCanScroll(false);
		viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		showFragment(0);
	}
	
	private void initUI() {
		if(getIntent().hasExtra("flag"))
			reLogin();
		initTab();
	}
	
	private void checkVersion(){
		NetTools.getDataFromNetwork(this,"download", null, new NetToolCallBack() {
			@Override
			public void error(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void callBack(JSONObject result) {
				if(Tools.isOk(result)){
					try {
						final JSONObject resultMsg = result.getJSONObject("msg");
						int lastVersion = resultMsg.getInt("androidCode");
						if(Tools.getVersionCode(MainActivity.this) != lastVersion){
							//有新版本，需要更新
							AlertDialog.Builder builder = new Builder(MainActivity.this);
							builder.setMessage("应用有新版本，需要立即更新吗?");
							builder.setTitle("提示");
							builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										String url = resultMsg.getString("androidUrl");
										Intent intent = new Intent();        
								        intent.setAction("android.intent.action.VIEW");    
								        Uri content_url = Uri.parse(url);   
								        intent.setData(content_url);
								        startActivity(intent);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
							builder.setNegativeButton("取消",null);
							builder.create().show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * 自动登录
	 */
	private void reLogin(){
		UserModel user = Tools.getUser(this);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("mobile", user.mobile);
		param.put("password", user.password);
		NetTools.getDataFromNetwork(this,"login", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
			}
			@Override
			public void callBack(JSONObject result) {
				if(!Tools.isOk(result)){
					Tools.logOut(MainActivity.this);
					Toast.makeText(MainActivity.this, "登录失败，请检查手机号/密码。",Toast.LENGTH_SHORT).show();
					startActivity(new Intent(MainActivity.this,LoginActivity.class));
				}else{
					Tools.saveUser(MainActivity.this, result);
				}
			}
		});
	}
	
	private void loadFragment(){
		fragments = new ArrayList<Fragment>();
		
		callFragment = new CallFragment(this);
		xhFragment = new XHFragment();
		meFragment = new MeFragment(this);
		
		fragments.add(callFragment);
		fragments.add(xhFragment);
		fragments.add(meFragment);
		
	}
	
	private void initTab() {
		tab1 = (LinearLayout) findViewById(R.id.show_tab1);
		tab2 = (LinearLayout) findViewById(R.id.show_tab2);
		tab3 = (LinearLayout) findViewById(R.id.show_tab3);

		tabsList = new ArrayList<LinearLayout>();
		tabsList.add(tab1);
		tabsList.add(tab2);
		tabsList.add(tab3);

		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.show_tab1:
			showOrHideKeyboard();
			showFragment(0);
			break;
		case R.id.show_tab2:
			showFragment(1);
			break;
		case R.id.show_tab3:
			showFragment(2);
			break;
		}
	}
	
	private void selectTab(int index){
		for(int i=0;i<tabsList.size();i++){
			if(i==index){
				View view = tabsList.get(i);
				((ImageView)view.findViewById(R.id.tab_iv)).setImageResource(tabImgs[i]);
				((TextView)view.findViewById(R.id.tab_tv)).setTextColor(getResources().getColor(R.color.orange));
			}else{
				View view = tabsList.get(i);
				((ImageView)view.findViewById(R.id.tab_iv)).setImageResource(tabImgsP[i]);
				((TextView)view.findViewById(R.id.tab_tv)).setTextColor(R.drawable.tab_tvcolor_press);
			}
		}
		if(index != 0){
			((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setText("电话");
			((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setTextColor(getResources().getColor(R.drawable.tab_tvcolor_press));
		}
	}
	
	/**
	 * 跳转fragment
	 * @param index fragment的索引
	 */
	public void showFragment(int index){
		if(currentFragment == index)
			return;
		
		currentFragment = index;
		viewpager.setCurrentItem(index);
		selectTab(index);
		if(index != 0){
			leaveTab0();
		}else{
			//showOrHideKeyboard();
		}
	}
	
	private void leaveTab0(){
		View view = tabsList.get(0);
		((ImageView)view.findViewById(R.id.tab_iv)).setImageResource(tabImgsP[0]);
		((TextView)view.findViewById(R.id.tab_tv)).setTextColor(R.drawable.tab_tvcolor_press);
	}
	
	public void showOrHideKeyboard(){
		((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setTextColor(getResources().getColor(R.color.blue));
		if(currentFragment == 0){
			if(callFragment.keyboardIsShown()){
				callFragment.hideKeyboard();
				((ImageView)tabsList.get(0).findViewById(R.id.tab_iv)).setImageResource(R.drawable.tab_dial_ic_open);
				((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setText("展开键盘");
			}else{
				callFragment.showKeyboard();
				((ImageView)tabsList.get(0).findViewById(R.id.tab_iv)).setImageResource(R.drawable.tab_dial_ic_close);
				((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setText("隐藏键盘");
			}
		}else{
			callFragment.showKeyboard();
			((ImageView)tabsList.get(0).findViewById(R.id.tab_iv)).setImageResource(R.drawable.tab_dial_ic_close);
			((TextView)tabsList.get(0).findViewById(R.id.tab_tv)).setText("隐藏键盘");
		}
	}
	
	private long firstTime = 0;
	@Override
	public void onBackPressed() {
		if(meFragment.removePop()) return;
		
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 1200) { // 如果两次按键时间间隔大于2秒，则不退出
			Toast.makeText(this, "再按一次返回键返回到桌面", Toast.LENGTH_SHORT).show();
			firstTime = secondTime;// 更新firstTime
			return;
		} else { // 两次按键小于2秒时，退出应用
			for(Activity activity:Config.started){
				activity.finish();
			}
			System.exit(0);
		}
		
	}
	
	public void keyBoardClick(View view){
		callFragment.keyBoardClick(view);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		callFragment.getCallRecords();
	}
	
	/**
	 * 加载服务器配置文件
	 */
	private void loadConfig(){
		AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>(){
			@Override
			protected String doInBackground(String... params) {
				try {
					String read;
					String readStr = "";
					URL url = new URL(params[0]);
					HttpURLConnection urlCon   = (HttpURLConnection)url.openConnection();
					BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(),"UTF-8"));
					while ((read = br.readLine()) != null) {
						readStr = readStr + read;
					}
					br.close();
					return readStr;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(result != null){
					try {
						final JSONObject jObj = new JSONObject(result);
						PackageInfo pi= getPackageManager().getPackageInfo(getPackageName(), 0);
						if(jObj.getInt("VERSION") > pi.versionCode){
							//服务器版本号大于当前版本号，应该更新了
							new AlertDialog.Builder(MainActivity.this). 
			                 setTitle("发现有更新").
			                 setIcon(R.drawable.ic_launcher).
			                 setPositiveButton("立即更新", new DialogInterface.OnClickListener() { 
			                     @Override 
			                     public void onClick(DialogInterface dialog, int which) { 
										try {
											Intent intent= new Intent();        
											intent.setAction("android.intent.action.VIEW");    
											Uri content_url = Uri.parse(jObj.getString("DOWNLOAD_URL"));
											intent.setData(content_url);
											startActivity(intent);
										} catch (JSONException e) {
											e.printStackTrace();
										}   
			                     }
			                 }). 
			                 setNegativeButton("暂不更新", null).create().show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		};
		task.execute(NetTools.HOST + "app_config.txt");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK) return;
		if(requestCode == 1024){
			xhFragment.getUserAccount();
		}
	}
	
}
