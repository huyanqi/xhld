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
import com.xhld.bean.XHCityModel;
import com.xhld.bean.XHProvinceModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;

public class XHSelectCityActivity extends BaseActivity
  implements AdapterView.OnItemClickListener
{
  private CityItemAdapter adapter;
  private List<String> datas;
  private ListView listview;
  private XHProvinceModel provinceModel;
  private List<XHCityModel> resultDatas;

  private void getCitys(String paramString)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("provice", paramString);
    NetTools.getXHDataFromNetwork2(this, "cityList", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHSelectCityActivity.this.datas.removeAll(XHSelectCityActivity.this.datas);
        XHSelectCityActivity.this.resultDatas.removeAll(XHSelectCityActivity.this.resultDatas);
        try
        {
          XHSelectCityActivity.this.resultDatas.addAll(Tools.jAry2List(paramAnonymousJSONObject.getJSONArray("resultCitys"), XHCityModel.class));
          Iterator localIterator = XHSelectCityActivity.this.resultDatas.iterator();
          while (true)
          {
            if (!localIterator.hasNext())
            {
              XHSelectCityActivity.this.adapter.notifyDataSetChanged();
              return;
            }
            XHCityModel localXHCityModel = (XHCityModel)localIterator.next();
            XHSelectCityActivity.this.datas.add(localXHCityModel.name);
          }
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHSelectCityActivity.this.showToast(paramAnonymousString);
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

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_listview);
    initActionBar("选择城市");
    initViews();
    this.provinceModel = ((XHProvinceModel)getIntent().getSerializableExtra("province"));
    if (this.provinceModel == null)
    {
      showToast("没有传入省份");
      finish();
      return;
    }
    getCitys(this.provinceModel.id+"");
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("province", this.provinceModel);
    localIntent.putExtra("city", (Serializable)this.resultDatas.get(paramInt));
    setResult(-1, localIntent);
    finish();
  }
}