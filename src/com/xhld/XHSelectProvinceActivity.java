package com.xhld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xhld.adapter.CityItemAdapter;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHProvinceModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;

public class XHSelectProvinceActivity extends BaseActivity
  implements AdapterView.OnItemClickListener
{
  private CityItemAdapter adapter;
  private List<String> datas;
  private ListView listview;
  private List<XHProvinceModel> resultDatas;

  private void getProvinces()
  {
    NetTools.getXHDataFromNetwork2(this, "provinceList", new HashMap(), new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHSelectProvinceActivity.this.datas.removeAll(XHSelectProvinceActivity.this.datas);
        XHSelectProvinceActivity.this.resultDatas.removeAll(XHSelectProvinceActivity.this.resultDatas);
        try
        {
          XHSelectProvinceActivity.this.resultDatas.addAll(Tools.jAry2List(paramAnonymousJSONObject.getJSONArray("resultProvices"), XHProvinceModel.class));
          Iterator localIterator = XHSelectProvinceActivity.this.resultDatas.iterator();
          while (true)
          {
            if (!localIterator.hasNext())
            {
             adapter.notifyDataSetChanged();
              return;
            }
            XHProvinceModel localXHProvinceModel = (XHProvinceModel)localIterator.next();
            XHSelectProvinceActivity.this.datas.add(localXHProvinceModel.name);
          }
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHSelectProvinceActivity.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(true));
  }

  private void initViews()
  {
    this.listview = ((ListView)findViewById(R.id.listview));
    this.listview.setOnItemClickListener(this);
    this.resultDatas = new ArrayList();
    ListView localListView = this.listview;
    ArrayList localArrayList = new ArrayList();
    this.datas = localArrayList;
    CityItemAdapter localCityItemAdapter = new CityItemAdapter(this, localArrayList);
    this.adapter = localCityItemAdapter;
    localListView.setAdapter(localCityItemAdapter);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == -1)
    {
      setResult(-1, paramIntent);
      finish();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_listview);
    initActionBar("选择省份");
    initViews();
    getProvinces();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Intent localIntent = new Intent(this, XHSelectCityActivity.class);
    localIntent.putExtra("province", (Serializable)this.resultDatas.get(paramInt));
    startActivityForResult(localIntent, 1024);
  }
}