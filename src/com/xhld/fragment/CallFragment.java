package com.xhld.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xhld.ContactActivity;
import com.xhld.MainActivity;
import com.xhld.PayActivity;
import com.xhld.R;
import com.xhld.adapter.CallRecordsItemAdapter;
import com.xhld.bean.CallRecordModel;
import com.xhld.bean.UserModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;

/**
 * 
 * @author Frankie
 *
 * 拨号
 */
public class CallFragment extends Fragment implements OnClickListener,OnItemClickListener,OnLongClickListener{

	private MainActivity mActivity;
	private int[] typesName = new int[]{-1,0,1,2};
	private View view = null;
	private List<CallRecordModel> callRecordsList;
	private List<CallRecordModel> cacheModel;
	public static String keyword = "";
	
	private TextView balanceTv;
	private EditText numberEt;
	
	public LinearLayout keyboardLayout;
	private ListView callRecordsListView;
	private CallRecordsItemAdapter callRecordsItemAdapter;
	
	public CallFragment(MainActivity activity){
		this.mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null){
			view = inflater.inflate(R.layout.fragment_call, container, false);
			initWidget();
			refreshBalance();
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initWidget(){
		balanceTv = (TextView) view.findViewById(R.id.balance_tv);
		numberEt = (EditText) view.findViewById(R.id.number_et);
		keyboardLayout = (LinearLayout) view.findViewById(R.id.keyboard_ly);
		callRecordsListView = (ListView) view.findViewById(R.id.call_records_listview);
		callRecordsList = new ArrayList<CallRecordModel>();
		cacheModel = new ArrayList<CallRecordModel>();
		callRecordsItemAdapter = new CallRecordsItemAdapter(mActivity, callRecordsList);
		callRecordsListView.setAdapter(callRecordsItemAdapter);
		callRecordsListView.setOnItemClickListener(this);
		
		//view.findViewById(R.id.close_keyboard).setOnClickListener(this);
		view.findViewById(R.id.number_del).setOnClickListener(this);
		view.findViewById(R.id.call_btn).setOnClickListener(this);
		view.findViewById(R.id.balance_ly).setOnClickListener(this);
		view.findViewById(R.id.to_contact).setOnClickListener(this);
		view.findViewById(R.id.to_pay).setOnClickListener(this);
		
		numberEt.setOnClickListener(this);
		numberEt.addTextChangedListener(watcher);
		view.findViewById(R.id.number_del).setOnLongClickListener(this);
		
	}
	
	private void refreshBalance(){
		UserModel user = Tools.getUser(mActivity);
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("mobile", user.mobile);
		NetTools.getDataFromNetwork(mActivity, "balance", param, new NetToolCallBack() {
			@Override
			public void error(String error) {
				Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void callBack(JSONObject result) {
				if(Tools.isOk(result)) {
					try {
						balanceTv.setText(String.format("%.2f", result.getDouble("msg"))+"元");
					} catch (JSONException e) {
						balanceTv.setText("0元");
						e.printStackTrace();
					}
				}
			}
		},true);
	}
	
	public void getCallRecords(){
		final Handler callRecordsHandler = new Handler(){
			public void handleMessage(Message msg) {
				List<CallRecordModel> callList = (List<CallRecordModel>) msg.obj;
				callList = filterName(callList);
				cacheModel.removeAll(cacheModel);
				cacheModel.addAll(callList);
				callRecordsList.removeAll(callRecordsList);
				callRecordsList.addAll(callList);
				callRecordsItemAdapter.notifyDataSetChanged();
			};
		};
		
		new Thread(){
			public void run() {
				ContentResolver cr = mActivity.getContentResolver();
				PrettyTime pt = new PrettyTime(new Date());
				pt.setLocale(new Locale("ZH_CN"));
				final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, new String[] {CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,CallLog.Calls.TYPE, CallLog.Calls.DATE }, null, null,CallLog.Calls.DEFAULT_SORT_ORDER);
				List<CallRecordModel> list = new ArrayList<CallRecordModel>();
				CallRecordModel crmModel;
				while(cursor.moveToNext()){
					crmModel = new CallRecordModel();
					crmModel.num = cursor.getString(0).replace("-", "").replace("+86", "");
					if(cursor.getString(1)!=null){
						crmModel.name = cursor.getString(1);
					}else{
						crmModel.name = cursor.getString(0);
					}
					if(Integer.parseInt(cursor.getString(2))<=3){
						crmModel.type = typesName[Integer.parseInt(cursor.getString(2))]; 
					}else{
						crmModel.type = typesName[1];
					}
					crmModel.time = pt.format(new Date(Long.parseLong(cursor.getString(3))));
					list.add(crmModel);
				}
				cursor.close();
				if(list.size()>0){
					Message msg = new Message();
					msg.obj = list;
					callRecordsHandler.sendMessage(msg);
				}
			};
		}.start();
	}
	
	/**
	 * 过滤重复名字
	 * @param callList
	 * @return
	 */
	private List<CallRecordModel> filterName(List<CallRecordModel> callList){
		tempData.removeAll(tempData);
		for(CallRecordModel cRecordModel : callList){
			CallRecordModel tempModel = isExist(cRecordModel);
			if(tempModel != null){
				tempModel.callTimes ++;
			}else{
				tempData.add(cRecordModel);
			}
		}
		return tempData;
	}
	
	List<CallRecordModel> tempData = new ArrayList<CallRecordModel>();
	private CallRecordModel isExist(CallRecordModel callRecordModel){
		for(CallRecordModel model : tempData){
			if(model.num.equals(callRecordModel.num)){
				return model;
			}
		}
		return null;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		/*case R.id.close_keyboard:
			mActivity.showOrHideKeyboard();
			break;*/
		case R.id.number_del:
			String text = numberEt.getText().toString();
			if(text.length() > 0){
				numberEt.setText(text.substring(0, text.length()-1));
			}
			break;
		case R.id.call_btn:
			if(numberEt.getText().toString().length() > 0){
				Tools.toCall(mActivity, numberEt.getText().toString(), null);
				numberEt.setText("");
			}
			break;
		case R.id.to_contact:
			startActivityForResult(new Intent(mActivity,ContactActivity.class), 1024);
			break;
		case R.id.balance_ly:
			refreshBalance();
			break;
		case R.id.to_pay:
			startActivity(new Intent(mActivity,PayActivity.class));
			break;
		}
	}

	public void keyBoardClick(View view){
		String text = numberEt.getText().toString() + view.getTag();
		numberEt.setText(text);
	}
	
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			keyword = s.toString();
			if(!s.equals("")){
				search(s);
			}else{
				callRecordsList.removeAll(callRecordsList);
				callRecordsList.addAll(cacheModel);
				callRecordsItemAdapter.notifyDataSetChanged();
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	private void search(CharSequence number){
		List<CallRecordModel> temp = new ArrayList<CallRecordModel>();
		for(CallRecordModel crm : cacheModel){
			if(crm.num.contains(number)){
				temp.add(crm);
			}
			callRecordsList.removeAll(callRecordsList);
			callRecordsList.addAll(temp);
			callRecordsItemAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		CallRecordModel model = callRecordsList.get((int) arg3);
		String name = model.name;
		if(model.name.contains("(")){
			name = model.name.substring(0,model.name.indexOf("("));
		}
		Tools.toCall(mActivity, model.num, name);
	}

	@Override
	public boolean onLongClick(View arg0) {
		numberEt.setText("");
		return false;
	}
	
	public boolean keyboardIsShown(){
		return keyboardLayout.isShown();
	}
	
	public void hideKeyboard(){
		keyboardLayout.setVisibility(View.GONE);
	}
	
	public void showKeyboard(){
		keyboardLayout.setVisibility(View.VISIBLE);
	}
	
}
