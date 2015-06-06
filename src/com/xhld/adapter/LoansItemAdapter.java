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
import com.xhld.bean.XHLoansModel;
import com.xhld.utils.Tools;

@SuppressLint({"SimpleDateFormat"})
public class LoansItemAdapter extends BaseAdapter
{
  private Context context;
  private List<XHLoansModel> data;
  private LayoutInflater mInflater;

  public LoansItemAdapter(Context paramContext, List<XHLoansModel> paramList)
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
    Holder localHolder = null;
    if (paramView == null)
    {
      localHolder = new Holder();
      paramView = this.mInflater.inflate(R.layout.item_loan, null);
      localHolder.nameTextView = ((TextView)paramView.findViewById(R.id.name));
      localHolder.interestTextView = ((TextView)paramView.findViewById(R.id.interest));
      localHolder.amountTextView = ((TextView)paramView.findViewById(R.id.amount));
      localHolder.progressTextView = ((TextView)paramView.findViewById(R.id.process));
      localHolder.monthTextView = ((TextView)paramView.findViewById(R.id.month));
      localHolder.stateTextView = ((TextView)paramView.findViewById(R.id.state));
      paramView.setTag(localHolder);
    }else{
    	localHolder = (Holder)paramView.getTag();
    }
      XHLoansModel localXHLoansModel = (XHLoansModel)this.data.get(paramInt);
      localHolder.nameTextView.setText(localXHLoansModel.name);
      localHolder.interestTextView.setText("年利率:" + localXHLoansModel.interest + "%");
      localHolder.amountTextView.setText("投资金额:" + localXHLoansModel.amount + "元");
      localHolder.progressTextView.setText("进度:" + 100 * (localXHLoansModel.progress / localXHLoansModel.amount) + "%");
      localHolder.monthTextView.setText("期限:" + localXHLoansModel.month + "月");
      localHolder.stateTextView.setText(localXHLoansModel.state);
      Tools.setItemBackGround(paramView, paramInt);
      return paramView;
      
  }

  private class Holder{
    TextView amountTextView;
    TextView interestTextView;
    TextView monthTextView;
    TextView nameTextView;
    TextView progressTextView;
    TextView stateTextView;
  }
}