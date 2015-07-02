package com.xhld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.xhld.adapter.ShenQingJiLuItemAdapter;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHLoanApply;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;
import com.xhld.widget.PullToRefreshView;

public class XHShenQingJiLu extends BaseActivity
{
  private ShenQingJiLuItemAdapter adapter;
  private List<XHLoanApply> datas;
  private ListView listview;
  private int pageNum = 1;
  private PullToRefreshView pull;

  private void getDatas()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("no", Integer.valueOf(this.pageNum));
    NetTools.getXHDataFromNetwork(this, "getApplyLoans", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHShenQingJiLu.this.processDialog.dismiss();
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if (XHShenQingJiLu.this.pageNum == 1)
            XHShenQingJiLu.this.datas.removeAll(XHShenQingJiLu.this.datas);
          List<XHLoanApply> localList = Tools.jAry2List(localJSONObject.getJSONArray("loanApplys"), XHLoanApply.class);
          for(XHLoanApply apply : localList){
        	  apply.createTimeStr = Tools.long2Str(apply.createTime.time);
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
        XHShenQingJiLu.this.processDialog.dismiss();
        XHShenQingJiLu.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
    listview = ((ListView)findViewById(R.id.listview));
    listview.setAdapter(adapter = new ShenQingJiLuItemAdapter(this, datas = new ArrayList()));
    initPull();
  }

  private void initPull()
  {
    this.pull = ((PullToRefreshView)findViewById(R.id.pull));
    this.pull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener()
    {
      public void onHeaderRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHShenQingJiLu.this.pageNum = 1;
        XHShenQingJiLu.this.getDatas();
      }
    });
    this.pull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener()
    {
      public void onFooterRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHShenQingJiLu localXHShenQingJiLu = XHShenQingJiLu.this;
        localXHShenQingJiLu.pageNum = (1 + localXHShenQingJiLu.pageNum);
        XHShenQingJiLu.this.getDatas();
      }
    });
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("借款申请记录");
    setContentView(R.layout.activity_listview);
    init();
    this.processDialog.show();
    getDatas();
  }
}