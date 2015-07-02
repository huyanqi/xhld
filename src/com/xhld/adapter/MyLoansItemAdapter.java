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
import com.xhld.bean.XHUserLoanRepayVos;
import com.xhld.utils.Tools;

@SuppressLint({"SimpleDateFormat"})
public class MyLoansItemAdapter extends BaseAdapter
{
  private Context context;
  private List<XHUserLoanRepayVos> data;
  private LayoutInflater mInflater;

  public MyLoansItemAdapter(Context paramContext, List<XHUserLoanRepayVos> paramList)
  {
    this.context = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.data = paramList;
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
    if (paramView == null)
    {
      localHolder = new Holder();
      paramView = this.mInflater.inflate(R.layout.item_myloan, null);
      localHolder.name = ((TextView)paramView.findViewById(R.id.name));
      localHolder.interest = ((TextView)paramView.findViewById(R.id.interest));
      localHolder.loanAmount = ((TextView)paramView.findViewById(R.id.loanAmount));
      localHolder.amount = ((TextView)paramView.findViewById(R.id.amount));
      localHolder.received = ((TextView)paramView.findViewById(R.id.received));
      localHolder.receiving = ((TextView)paramView.findViewById(R.id.receiving));
      localHolder.nextReceivedDate = ((TextView)paramView.findViewById(R.id.nextReceivedDate));
      localHolder.stateRemark = ((TextView)paramView.findViewById(R.id.stateRemark));
      localHolder.indexmonth = ((TextView)paramView.findViewById(R.id.indexmonth));
      paramView.setTag(localHolder);
    }else{
    	localHolder = (Holder)paramView.getTag();
    }
      XHUserLoanRepayVos localXHUserLoanRepayVos = (XHUserLoanRepayVos)this.data.get(paramInt);
      localHolder.name.setText(localXHUserLoanRepayVos.name);
      localHolder.interest.setText("利率:" + localXHUserLoanRepayVos.interest + "%");
      localHolder.loanAmount.setText("借款金额:" + localXHUserLoanRepayVos.loanAmount);
      localHolder.amount.setText("我的投资:" + localXHUserLoanRepayVos.amount);
      localHolder.received.setText("已收金额:" + localXHUserLoanRepayVos.received);
      localHolder.receiving.setText("待收本金:" + localXHUserLoanRepayVos.receiving);
      localHolder.nextReceivedDate.setText("还款日:" + localXHUserLoanRepayVos.nextTime);
      localHolder.stateRemark.setText("状态:" + localXHUserLoanRepayVos.stateRemark);
      localHolder.indexmonth.setText("当前期数/还款期数:" + localXHUserLoanRepayVos.currentIndex + "/" + localXHUserLoanRepayVos.month);
      Tools.setItemBackGround(paramView, paramInt);
      return paramView;
  }

  private class Holder
  {
    TextView amount;
    TextView indexmonth;
    TextView interest;
    TextView loanAmount;
    TextView name;
    TextView nextReceivedDate;
    TextView received;
    TextView receiving;
    TextView stateRemark;

  }
}