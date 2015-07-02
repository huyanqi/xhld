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
import com.xhld.bean.XHLoanApply;
import com.xhld.utils.Tools;

@SuppressLint({"SimpleDateFormat"})
public class ShenQingJiLuItemAdapter extends BaseAdapter
{
  private Context context;
  private List<XHLoanApply> data;
  private LayoutInflater mInflater;

  public ShenQingJiLuItemAdapter(Context paramContext, List<XHLoanApply> paramList)
  {
    this.context = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.data = paramList;
  }

  private String getState(int paramInt)
  {
    if(paramInt < 0){
    	return "被拒绝";
    }else if(paramInt < 11){
    	return "完善资料";
    }else if(paramInt == 11){
    	return "等待审核";
    }else if(paramInt == 12){
    	return "请到小合利贷官网提交证件";
    }else if(paramInt == 13){
    	return "等待审核证件";
    }else if(paramInt != 14){
    	return "审核通过,发布贷款";
    }
    
    return "";

  }

  public int getCount()
  {
    return this.data.size();
  }

  public Object getItem(int paramInt)
  {
    return this.data.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
	  Holder localHolder;
	  if (paramView == null){
		  localHolder = new Holder();
		  paramView = this.mInflater.inflate(R.layout.item_shenqingjilu, null);
		  localHolder.biaoti = ((TextView)paramView.findViewById(R.id.name));
	      localHolder.jine = ((TextView)paramView.findViewById(R.id.jine));
	      localHolder.shijian = ((TextView)paramView.findViewById(R.id.shijian));
	      localHolder.leixing = ((TextView)paramView.findViewById(R.id.leixing));
	      localHolder.qixian = ((TextView)paramView.findViewById(R.id.qixian));
	      localHolder.fangshi = ((TextView)paramView.findViewById(R.id.fangshi));
	      localHolder.jieguo = ((TextView)paramView.findViewById(R.id.jieguo));
	      paramView.setTag(localHolder);
	  }else{
		  localHolder = (Holder)paramView.getTag();
	  }
	  
	  XHLoanApply localXHLoanApply = data.get(paramInt);
	  localHolder.biaoti.setText("借款标题:" + localXHLoanApply.loanApplyInfo.name);
	  localHolder.fangshi.setText("还款方式:"+ (localXHLoanApply.loanApplyInfo.repaymentType == 1 ? "等额本息":"一次付息，到期还本"));
	  localHolder.jieguo.setText("处理结果:" + getState(localXHLoanApply.state));
      localHolder.jine.setText("借款金额:" + localXHLoanApply.loanApplyInfo.amount);
      localHolder.leixing.setText("申请类型:"+ (localXHLoanApply.type == 2 ? "个人":"企业"));
      localHolder.qixian.setText("还款期限:"+localXHLoanApply.loanApplyInfo.month);
      localHolder.shijian.setText("申请时间:" + localXHLoanApply.createTimeStr);
      
      Tools.setItemBackGround(paramView, paramInt);
      
      return paramView;
  }

  private class Holder
  {
    TextView biaoti;
    TextView fangshi;
    TextView jieguo;
    TextView jine;
    TextView leixing;
    TextView qixian;
    TextView shijian;

    private Holder()
    {
    }
  }
}