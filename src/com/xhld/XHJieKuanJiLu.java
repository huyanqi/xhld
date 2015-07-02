package com.xhld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.xhld.adapter.ShenQingItemAdapter;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHLoanSummaryVos;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;
import com.xhld.widget.PullToRefreshView;

public class XHJieKuanJiLu extends BaseActivity
{
  private ShenQingItemAdapter adapter;
  private List<XHLoanSummaryVos> datas;
  private ListView listview;
  private int pageNum = 1;
  private PullToRefreshView pull;

  private void getDatas()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("no", Integer.valueOf(this.pageNum));
    NetTools.getXHDataFromNetwork(this, "getLoanList", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHJieKuanJiLu.this.processDialog.dismiss();
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if (XHJieKuanJiLu.this.pageNum == 1)
            XHJieKuanJiLu.this.datas.removeAll(XHJieKuanJiLu.this.datas);
          List<XHLoanSummaryVos> localList = Tools.jAry2List(localJSONObject.getJSONArray("loanSummaryVos"), XHLoanSummaryVos.class);
          for(XHLoanSummaryVos vos:localList){
        	  vos.createTimeStr = Tools.long2Str(vos.loanApply.createTime.time);
          }
          datas.addAll(localList);
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
        XHJieKuanJiLu.this.processDialog.dismiss();
        XHJieKuanJiLu.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
	  listview = ((ListView)findViewById(R.id.listview));
    listview.setAdapter(adapter = new ShenQingItemAdapter(this, datas = new ArrayList()));
    initPull();
  }

  private void initPull()
  {
    this.pull = ((PullToRefreshView)findViewById(R.id.pull));
    this.pull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener()
    {
      public void onHeaderRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHJieKuanJiLu.this.pageNum = 1;
        XHJieKuanJiLu.this.getDatas();
      }
    });
    this.pull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener()
    {
      public void onFooterRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHJieKuanJiLu localXHJieKuanJiLu = XHJieKuanJiLu.this;
        localXHJieKuanJiLu.pageNum = (1 + localXHJieKuanJiLu.pageNum);
        XHJieKuanJiLu.this.getDatas();
      }
    });
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("借款记录");
    setContentView(R.layout.activity_listview);
    init();
    this.processDialog.show();
    getDatas();
  }
}