package com.xhld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.xhld.R;

public class CityItemAdapter extends BaseAdapter
{
  private List<String> citys;
  private Context context;

  public CityItemAdapter(Context paramContext, List<String> paramList)
  {
    this.citys = paramList;
    this.context = paramContext;
  }

  public int getCount()
  {
    return this.citys.size();
  }

  public Object getItem(int paramInt)
  {
    return this.citys.get(paramInt);
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
      paramView = LayoutInflater.from(this.context).inflate(R.layout.item_city, null);
      localHolder = new Holder();
      localHolder.name = ((TextView)paramView.findViewById(R.id.name));
      paramView.setTag(localHolder);
    }else{
    	localHolder = (Holder)paramView.getTag();
    }
      localHolder.name.setText((CharSequence)this.citys.get(paramInt));
      return paramView;
  }

  private class Holder
  {
    TextView name;
  }
}