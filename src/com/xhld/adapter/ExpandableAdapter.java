package com.xhld.adapter;

import java.util.List;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.xhld.ContactActivity;
import com.xhld.R;
import com.xhld.bean.UserModel;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	
	private Activity activity;
	private List<String> groupArray;
	private List<List<UserModel>> childArray;

	public ExpandableAdapter(Activity a,List<String> groupArray,List<List<UserModel>> childArray) {
		this.activity = a;
		this.childArray = childArray;
		this.groupArray = groupArray;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return childArray.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return childArray.get(groupPosition).size();
	}

	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if(convertView == null){
			holder = new ChildHolder();
			convertView = activity.getLayoutInflater().from(activity).inflate(R.layout.item_child, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			convertView.setTag(holder);
		}else{
			holder = (ChildHolder) convertView.getTag();
		}
		UserModel userModel = childArray.get(groupPosition).get(childPosition);
		String name = userModel.name;
		if(!"".equals(ContactActivity.keyword)){
			name = name.replace(ContactActivity.keyword, "<font color='red'>"+ContactActivity.keyword+"</font>");
		}
		String number = userModel.number;
		if(!"".equals(ContactActivity.keyword)){
			number = number.replace(ContactActivity.keyword, "<font color='red'>"+ContactActivity.keyword+"</font>");
		}
		holder.name.setText(Html.fromHtml(name));
		holder.number.setText(Html.fromHtml(number));
		return convertView;
	}
	
	private class ChildHolder {
		private TextView name;
		private TextView number;
	}

	// group method stub
	public Object getGroup(int groupPosition) {
		return groupArray.get(groupPosition);
	}

	public int getGroupCount() {
		return groupArray.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		GroupHolder holder = null;
		if(convertView == null){
			holder = new GroupHolder();
			convertView = activity.getLayoutInflater().from(activity).inflate(R.layout.item_group, null);
			holder.name = (TextView) convertView.findViewById(R.id.group_sort);
			convertView.setTag(holder);
		}else{
			holder = (GroupHolder) convertView.getTag();
		}
		holder.name.setText(groupArray.get(groupPosition));
		return convertView;
	}
	
	private class GroupHolder {
		private TextView name;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}