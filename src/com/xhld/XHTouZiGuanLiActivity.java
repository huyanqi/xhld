package com.xhld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.xhld.adapter.MyLoansItemAdapter;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHUserLoanRepayVos;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;
import com.xhld.widget.PullToRefreshView;

public class XHTouZiGuanLiActivity extends BaseActivity
{
  private MyLoansItemAdapter adapter;
  private List<XHUserLoanRepayVos> datas;
  private ListView listview;
  private int no = 1;
  private PullToRefreshView pull;

  private void getData()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("no", Integer.valueOf(this.no));
    NetTools.getXHDataFromNetwork(this, "recordlist", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("investmentRecordResult");
          if ("1".equals(localJSONObject.getString("no")))
            XHTouZiGuanLiActivity.this.datas.removeAll(XHTouZiGuanLiActivity.this.datas);
          List<XHUserLoanRepayVos> list = Tools.jAry2List(localJSONObject.getJSONArray("userLoanRepayVos"), XHUserLoanRepayVos.class);
          for(XHUserLoanRepayVos vos:list){
        	  vos.nextTime = Tools.long2Str(vos.nextReceivedDate.time);
          }
          datas.addAll(list);
          adapter.notifyDataSetChanged();
          pull.onComplete();
        }
        catch (JSONException localJSONException)
        {
            localJSONException.printStackTrace();
            pull.onComplete();
      }
        }

      public void error(String paramAnonymousString)
      {
        XHTouZiGuanLiActivity.this.showToast(paramAnonymousString);
        XHTouZiGuanLiActivity.this.pull.onComplete();
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
    this.listview = ((ListView)findViewById(R.id.listview));
    listview.setAdapter(adapter = new MyLoansItemAdapter(this, datas = new ArrayList()));
    initPull();
  }

  private void initPull()
  {
    this.pull = ((PullToRefreshView)findViewById(R.id.pull));
    this.pull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener()
    {
      public void onHeaderRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHTouZiGuanLiActivity.this.no = 1;
        XHTouZiGuanLiActivity.this.getData();
      }
    });
    this.pull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener()
    {
      public void onFooterRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHTouZiGuanLiActivity localXHTouZiGuanLiActivity = XHTouZiGuanLiActivity.this;
        localXHTouZiGuanLiActivity.no = (1 + localXHTouZiGuanLiActivity.no);
        XHTouZiGuanLiActivity.this.getData();
      }
    });
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("我的债权");
    setContentView(R.layout.activity_listview);
    init();
    getData();
  }
}