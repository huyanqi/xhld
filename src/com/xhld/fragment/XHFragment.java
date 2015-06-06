package com.xhld.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xhld.MainActivity;
import com.xhld.R;
import com.xhld.XHLoanViewActivity;
import com.xhld.XHLoginActivity;
import com.xhld.adapter.LoansItemAdapter;
import com.xhld.bean.XHLoansModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;
import com.xhld.widget.PullToRefreshView;

public class XHFragment extends Fragment
  implements View.OnClickListener, AdapterView.OnItemClickListener
{
  private LoansItemAdapter adapter;
  private TextView balanceTv;
  private List<XHLoansModel> datas;
  private TextView freezeTv;
  private LinearLayout headerLy;
  private TextView incomeTv;
  private ListView listview;
  private TextView loginLy;
  private MainActivity mActivity;
  private int pageNum = 1;
  private PullToRefreshView pull;
  private View view = null;

  private void getLoans() {
    HashMap<String,Object> localHashMap = new HashMap<String,Object>();
    localHashMap.put("no", Integer.valueOf(this.pageNum));
    NetTools.getXHDataFromNetwork(this.mActivity, "getLoans", localHashMap, new NetTools.NetToolCallBack(){
      public void callBack(JSONObject paramAnonymousJSONObject) {
        try {
          if (XHFragment.this.pageNum == 1)
            XHFragment.this.datas.removeAll(XHFragment.this.datas);
          XHFragment.this.datas.addAll(Tools.jAry2List(paramAnonymousJSONObject.getJSONObject("loansResult").getJSONArray("loansVos"), XHLoansModel.class));
          adapter.notifyDataSetChanged();
          XHFragment.this.pull.onComplete();
          return;
        }
        catch (JSONException localJSONException)
        {
        	localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHFragment.this.pull.onComplete();
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
    this.balanceTv = ((TextView)this.view.findViewById(R.id.balance));
    this.freezeTv = ((TextView)this.view.findViewById(R.id.freeze));
    this.incomeTv = ((TextView)this.view.findViewById(R.id.income));
    this.listview = ((ListView)this.view.findViewById(R.id.listview));
    this.headerLy = ((LinearLayout)this.view.findViewById(R.id.header_ly));
    this.loginLy = ((TextView)this.view.findViewById(R.id.login_ly));
    this.loginLy.setOnClickListener(this);
    ListView localListView = this.listview;
    MainActivity localMainActivity = this.mActivity;
    localListView.setAdapter(adapter = new LoansItemAdapter(localMainActivity, datas = new ArrayList()));
    this.listview.setOnItemClickListener(this);
    initPull();
  }

  private void initPull()
  {
    this.pull = ((PullToRefreshView)this.view.findViewById(R.id.pull));
    this.pull.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener()
    {
      public void onHeaderRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHFragment.this.pageNum = 1;
        XHFragment.this.getLoans();
      }
    });
    this.pull.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener()
    {
      public void onFooterRefresh(PullToRefreshView paramAnonymousPullToRefreshView)
      {
        XHFragment localXHFragment = XHFragment.this;
        localXHFragment.pageNum = (1 + localXHFragment.pageNum);
        XHFragment.this.getLoans();
      }
    });
  }

  public void getUserAccount()
  {
    if (!Tools.XHUserIsExist(this.mActivity))
    {
      this.loginLy.setVisibility(0);
      this.headerLy.setVisibility(8);
      return;
    }
    HashMap localHashMap = new HashMap();
    NetTools.getXHDataFromNetwork(this.mActivity, "getUserAccount", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHFragment.this.loginLy.setVisibility(8);
          XHFragment.this.headerLy.setVisibility(0);
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("userAccountResult");
          XHFragment.this.balanceTv.setText(localJSONObject.getInt("balance") + "元");
          XHFragment.this.freezeTv.setText(localJSONObject.getInt("freeze") + "元");
          XHFragment.this.incomeTv.setText(localJSONObject.getInt("income") + "元");
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHFragment.this.loginLy.setVisibility(0);
        XHFragment.this.headerLy.setVisibility(8);
      }
    }
    , Boolean.valueOf(false));
  }

  public void onClick(View paramView)
  {
    this.mActivity.startActivityForResult(new Intent(this.mActivity, XHLoginActivity.class), 1024);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (this.view == null)
    {
    	mActivity = (MainActivity) getActivity();
      this.view = paramLayoutInflater.inflate(R.layout.activity_xh_main, paramViewGroup, false);
      init();
      getLoans();
      getUserAccount();
    }
    	// 缓存的rootView需要判断是否已经被加过parent，
 		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
 		ViewGroup parent = (ViewGroup) view.getParent();
 		if (parent != null) {
 			parent.removeView(view);
 		}
    return this.view;
  }

	public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
			int paramInt, long paramLong) {
		String str = ((XHLoansModel) this.datas.get(paramInt)).loanuuid;
		Intent localIntent = new Intent(this.mActivity,XHLoanViewActivity.class);
		localIntent.putExtra("uuid", str);
		localIntent.putExtra("model", (Serializable) this.datas.get(paramInt));
		startActivity(localIntent);
  }
}