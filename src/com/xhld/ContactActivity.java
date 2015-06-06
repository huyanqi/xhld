package com.xhld;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;

import com.xhld.adapter.ExpandableAdapter;
import com.xhld.base.BaseActivity;
import com.xhld.bean.UserModel;
import com.xhld.utils.PinyinToolkit;
import com.xhld.utils.Tools;

public class ContactActivity extends BaseActivity implements OnClickListener,OnLongClickListener,OnChildClickListener,OnTouchListener{

	private EditText contactEt;
	private ImageView numberDelIv;
	private ArrayList<UserModel> user;
	private ArrayList<UserModel> tempDatas;//临时存放所有用户的，方便检索
	private ExpandableListView listview;
	private ExpandableAdapter adapter;
	public static String keyword = "";

	private List<String> groupArray;
	private List<List<UserModel>> childArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		initWidget();
		getContact();
	}

	private void initWidget() {
		tempDatas = new ArrayList<UserModel>();
		user = new ArrayList<UserModel>();
		groupArray = new ArrayList<String>();
		childArray = new ArrayList<List<UserModel>>();
		adapter = new ExpandableAdapter(this, groupArray, childArray);
		contactEt = (EditText) findViewById(R.id.contact_et);
		numberDelIv = (ImageView) findViewById(R.id.number_del);
		listview = (ExpandableListView) findViewById(R.id.listview);
		listview.setOnChildClickListener(this);
		listview.setGroupIndicator(null);
		listview.setAdapter(adapter);
		listview.setOnTouchListener(this);
		numberDelIv.setOnClickListener(this);
		numberDelIv.setOnLongClickListener(this);
		contactEt.addTextChangedListener(watcher);
	}

	public void getContact() {
		
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				ArrayList<UserModel> user = (ArrayList<UserModel>) msg.obj;
				filterNumber(user);
				updateDatas(user);
			}
		};
		
		new Thread(){
			public void run() {
				Cursor cursor = null;
				try {

					Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
					// 这里是获取联系人表的电话里的信息 包括：名字，名字拼音，联系人id,电话号码；
					// 然后在根据"sort-key"排序
					cursor = getContentResolver().query(uri,new String[] { "display_name", "sort_key", "contact_id","data1" }, null, null, "sort_key");
					if (cursor.moveToFirst()) {
						do {
							UserModel contact = new UserModel();
							String contact_phone = cursor
									.getString(cursor
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							String name = cursor.getString(0);
							String sortKey = getSortKey(cursor.getString(1));
							int contact_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
							contact.name = name;
							contact.sortKey = sortKey;
							contact.number = contact_phone.replace("-", "");
							contact.id = contact_id;
							if (name != null)
								user.add(contact);
						} while (cursor.moveToNext());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.obj = user;
				handler.sendMessage(msg);
			};
		}.start();
	}
	
	//电话号码去重
	private void filterNumber(ArrayList<UserModel> users){
		List<String> tempStr = new ArrayList<String>();
		ArrayList<UserModel> filterUsers = new ArrayList<UserModel>();
		for(UserModel user:users){
			if(!tempStr.contains(user.number)){
				filterUsers.add(user);
				tempStr.add(user.number);
			}
		}
		users.removeAll(users);
		users.addAll(filterUsers);
	}

	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if(!key.matches("[A-Z]") && !"#".equals(sortKeyString)){
			key = PinyinToolkit.cn2FirstSpell(sortKeyString); 
			return key.substring(0, 1).toUpperCase();
		}
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	/**
	 * 重新组装数据
	 */
	private void updateDatas(List<UserModel> users) {
		groupArray.removeAll(groupArray);
		childArray.removeAll(childArray);
		for (UserModel model : users) { 
			if (!groupExist(model.sortKey))
				groupArray.add(model.sortKey);
		}
		// 组装完group后，组装child
		ArrayList<UserModel> tempDatas = new ArrayList<UserModel>();
		tempDatas.addAll(users);
		for (String group : groupArray) {
			List<UserModel> child = new ArrayList<UserModel>();
			for (UserModel user : tempDatas) {
				if (user.sortKey.equals(group)) {
					child.add(user);
				}
			}
			childArray.add(child);
		}

		for (int i = 0; i < adapter.getGroupCount(); i++) {
			listview.expandGroup(i);
		}
		adapter.notifyDataSetChanged();
	}

	private boolean groupExist(String keySort) {
		for (String sortString : groupArray) {
			if (sortString.equals(keySort))
				return true;
		}
		return false;
	}

	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {
			keyword = s.toString();
			if(!s.equals("")){
				search(s);
			}else{
				user.removeAll(user);
				user.addAll(tempDatas);
				adapter.notifyDataSetChanged();
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	private void search(CharSequence keyword){
		List<UserModel> searchDatas = new ArrayList<UserModel>();
		for(UserModel umModel : user){
			if(umModel.name.contains(keyword) || umModel.number.contains(keyword)){
				searchDatas.add(umModel);
			}
		}
		updateDatas(searchDatas);
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.number_del){
			String text = contactEt.getText().toString();
			if(text.length() > 0){
				contactEt.setText(text.substring(0, text.length()-1));
				contactEt.setSelection(contactEt.getText().toString().length());
			}
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		contactEt.setText("");
		return false;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
		UserModel model = childArray.get(groupPosition).get(childPosition);
		Tools.toCall(this, model.number,model.name);
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(inputMethodManager.isActive())
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		return false;
	}

}
