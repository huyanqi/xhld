package com.xhld.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhld.R;
import com.xhld.bean.CallRecordModel;
import com.xhld.fragment.CallFragment;

@SuppressLint("SimpleDateFormat")
public class CallRecordsItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CallRecordModel> data;
	private Context context;

	public CallRecordsItemAdapter(Context context,
			List<CallRecordModel> data) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_call_records, null);
			holder.statusImageView = (ImageView) convertView
					.findViewById(R.id.status);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.name);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.time);
			holder.numTextView = (TextView) convertView.findViewById(R.id.num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		CallRecordModel crModel = data.get(position);
		holder.timeTextView.setText(crModel.time);
		if(crModel.callTimes == 0){
			holder.nameTextView.setText(crModel.name);
		}else{
			holder.nameTextView.setText(crModel.name + "("+ (crModel.callTimes+1)+")");
		}
		String num = crModel.num;
		if(!"".equals(CallFragment.keyword)){
			num = num.replace(CallFragment.keyword, "<font color='red'>"+CallFragment.keyword+"</font>");
		}
		holder.numTextView.setText(Html.fromHtml(num));
		setStatusIcon(holder.statusImageView, crModel.type);

		return convertView;

	}

	private void setStatusIcon(ImageView statucIcon, int status) {
		switch (status) {
		case 1:
			statucIcon.setImageResource(R.drawable.call_log_type_incoming);
			break;
		case 2:
			statucIcon.setImageResource(R.drawable.call_log_type_outgoing);
			break;
		case 3:
			statucIcon.setImageResource(R.drawable.call_log_type_missed);
			break;
		default:
			statucIcon.setImageResource(R.drawable.call_log_type_block);
			break;
		}
	}

	private class Holder {
		ImageView statusImageView;
		TextView nameTextView;
		TextView timeTextView;
		TextView numTextView;
	}

}
