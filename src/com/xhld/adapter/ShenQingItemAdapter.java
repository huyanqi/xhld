package com.xhld.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xhld.R;
import com.xhld.bean.XHLoanSummaryVos;
import com.xhld.utils.Tools;

@SuppressLint({ "SimpleDateFormat" })
public class ShenQingItemAdapter extends BaseAdapter {
	private Context context;
	private List<XHLoanSummaryVos> data;
	private LayoutInflater mInflater;

	public ShenQingItemAdapter(Context paramContext,
			List<XHLoanSummaryVos> paramList) {
		this.context = paramContext;
		this.mInflater = LayoutInflater.from(paramContext);
		this.data = paramList;
	}

	public int getCount() {
		return this.data.size();
	}

	public Object getItem(int paramInt) {
		return this.data.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		Holder localHolder;
		if (paramView == null) {
			localHolder = new Holder();
			paramView = this.mInflater.inflate(R.layout.item_jiekuanjilu, null);
			localHolder.biaoti = ((TextView) paramView.findViewById(R.id.name));
			localHolder.jine = ((TextView) paramView.findViewById(R.id.jine));
			localHolder.shijian = ((TextView) paramView.findViewById(R.id.shijian));
			localHolder.leixing = ((TextView) paramView.findViewById(R.id.leixing));
			localHolder.qixian = ((TextView) paramView.findViewById(R.id.qixian));
			localHolder.fangshi = ((TextView) paramView.findViewById(R.id.fangshi));
			localHolder.jieguo = ((TextView) paramView.findViewById(R.id.jieguo));
			paramView.setTag(localHolder);
		} else {
			localHolder = (Holder) paramView.getTag();
		}
		XHLoanSummaryVos localXHLoanSummaryVos = (XHLoanSummaryVos) this.data
				.get(paramInt);
		localHolder.biaoti.setText("借款标题:" + localXHLoanSummaryVos.name);
		localHolder.jine.setText("借款金额:"
				+ localXHLoanSummaryVos.loanApply.amount);
		localHolder.shijian.setText("申请时间:"
				+ localXHLoanSummaryVos.createTimeStr);
		localHolder.leixing.setText("申请类型:" + localXHLoanSummaryVos.type);
		localHolder.qixian.setText("还款期限:"
				+ localXHLoanSummaryVos.loanApply.month + "个月");
		localHolder.fangshi.setText("还款方式:"
				+ localXHLoanSummaryVos.repaymentType);
		localHolder.jieguo.setText("处理结果:" + localXHLoanSummaryVos.state);
		Tools.setItemBackGround(paramView, paramInt);
		return paramView;
	}

	private class Holder {
		TextView biaoti;
		TextView fangshi;
		TextView jieguo;
		TextView jine;
		TextView leixing;
		TextView qixian;
		TextView shijian;

	}
}