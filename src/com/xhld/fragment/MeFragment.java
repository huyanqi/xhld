package com.xhld.fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xhld.ChangePwdActivity;
import com.xhld.LoginActivity;
import com.xhld.MainActivity;
import com.xhld.R;
import com.xhld.XHLoginActivity;
import com.xhld.bean.AdModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;
import com.xhld.widget.MePopupWindow;
import com.xhld.widget.MePopupWindow.ClickeCallBack;
import com.xhld.widget.SYNCImageView;

public class MeFragment extends Fragment implements View.OnClickListener
{
  private PopupWindow jiekuanPop;
  private MainActivity mActivity;
  private SYNCImageView meTopAd;
  private View view = null;
  private PopupWindow zhanghuPop;
  private PopupWindow ziJinPop;

  public MeFragment(MainActivity activity){
		this.mActivity = activity;
}

  private void initWidget()
  {
    this.meTopAd = ((SYNCImageView)this.view.findViewById(R.id.top_ad));
    this.view.findViewById(R.id.zijin_ly).setOnClickListener(this);
    this.view.findViewById(R.id.touzi_ly).setOnClickListener(this);
    this.view.findViewById(R.id.jiekuan_ly).setOnClickListener(this);
    this.view.findViewById(R.id.zhanghu_ly).setOnClickListener(this);
    this.view.findViewById(R.id.change_pwd).setOnClickListener(this);
    this.view.findViewById(R.id.log_out).setOnClickListener(this);
  }

  private void loadTopAd()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("code", Integer.valueOf(1030));
    NetTools.getDataFromNetwork(this.mActivity, "getAd", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          if (Tools.isOk(paramAnonymousJSONObject))
          {
            JSONArray localJSONArray = paramAnonymousJSONObject.getJSONArray("model");
            if (Tools.jAry2List(localJSONArray, AdModel.class).size() == 0)
              return;
            AdModel localAdModel = (AdModel)Tools.jAry2List(localJSONArray, AdModel.class).get(0);
            MeFragment.this.meTopAd.getImageFromURL(localAdModel.url, null);
            return;
          }
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
      }
    }
    , Boolean.valueOf(true));
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId()) {
    case R.id.change_pwd:
      this.mActivity.startActivity(new Intent(this.mActivity, ChangePwdActivity.class));
      break;
    case R.id.log_out:
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.mActivity);
      localBuilder.setMessage("确定退出当前用户吗？");
      localBuilder.setTitle("提示");
      localBuilder.setPositiveButton("退出", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Tools.logOut(MeFragment.this.mActivity);
          MeFragment.this.mActivity.startActivity(new Intent(MeFragment.this.mActivity, LoginActivity.class));
          MeFragment.this.mActivity.finish();
        }
      });
      localBuilder.setNegativeButton("取消", null);
      localBuilder.create().show();
      break;
    case R.id.zijin_ly:
      if (!Tools.XHUserIsExist(this.mActivity))
      {
    	  this.mActivity.showToast("请先登录");
        this.mActivity.startActivityForResult(new Intent(this.mActivity, XHLoginActivity.class), 1024);
        break;
      }
		if (ziJinPop == null)
			ziJinPop = new MePopupWindow(this.mActivity, new int[] {R.drawable.icon_woyaochongzhi,R.drawable.icon_woyaotixian, R.drawable.icon_zijinjilu,R.drawable.icon_shenqingchongzhi }, new String[] {"我要充值", "我要提现", "资金记录", "充值申请记录" },new ClickeCallBack() {
				@Override
				public void tab4Clicked() {
					
				}
				
				@Override
				public void tab3Clicked() {
					
				}
				
				@Override
				public void tab2Clicked() {
					
				}
				
				@Override
				public void tab1Clicked() {
					
				}
			});
		ziJinPop.showAtLocation(view, Gravity.TOP, 0, 0);
      break;
    case R.id.touzi_ly:
      if (!Tools.XHUserIsExist(this.mActivity))
      {
        this.mActivity.showToast("请先登录");
        this.mActivity.startActivityForResult(new Intent(this.mActivity, XHLoginActivity.class), 1024);
        break;
      }
      //startActivity(new Intent(this.mActivity, XHTouZiGuanLiActivity.class));
      break;
    case R.id.jiekuan_ly:
      if (!Tools.XHUserIsExist(this.mActivity))
      {
        this.mActivity.showToast("请先登录");
        this.mActivity.startActivityForResult(new Intent(this.mActivity, XHLoginActivity.class), 1024);
        break;
      }
      if (this.jiekuanPop == null)
    	  this.jiekuanPop = new MePopupWindow(this.mActivity, new int[] {R.drawable.icon_jiekuanshenqing,R.drawable.icon_jiekuanshenqingjilu, R.drawable.icon_zijinjilu,R.drawable.icon_back }, new String[] {"借款申请", "借款申请记录", "借款记录", "" },new ClickeCallBack() {
				@Override
				public void tab4Clicked() {
					jiekuanPop.dismiss();
					jiekuanPop = null;
				}
				
				@Override
				public void tab3Clicked() {
					
				}
				
				@Override
				public void tab2Clicked() {
					
				}
				
				@Override
				public void tab1Clicked() {
					
				}
			});
      this.jiekuanPop.showAtLocation(this.view, Gravity.TOP, 0, 0);
      break;
    case R.id.zhanghu_ly:
    	if (!Tools.XHUserIsExist(this.mActivity))
        {
          this.mActivity.showToast("请先登录");
          this.mActivity.startActivityForResult(new Intent(this.mActivity, XHLoginActivity.class), 1024);
          return;
        }
        if (this.zhanghuPop == null)
        	this.zhanghuPop = new MePopupWindow(this.mActivity, new int[] {R.drawable.icon_jibenxinxi,R.drawable.icon_xiangxixinxi, R.drawable.icon_lianxirenxinxi,R.drawable.icon_back }, new String[] {"基本信息", "详细信息", "联系人信息", "" },new ClickeCallBack() {
				@Override
				public void tab4Clicked() {
					zhanghuPop.dismiss();
					zhanghuPop = null;
				}
				
				@Override
				public void tab3Clicked() {
					
				}
				
				@Override
				public void tab2Clicked() {
					
				}
				
				@Override
				public void tab1Clicked() {
					
				}
			});
        this.zhanghuPop.showAtLocation(this.view, Gravity.TOP, 0, 0);
        break;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (this.view == null)
    {
      this.view = paramLayoutInflater.inflate(R.layout.fragment_me, paramViewGroup, false);
      initWidget();
      loadTopAd();
    } 
    
    	// 缓存的rootView需要判断是否已经被加过parent，
  		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
  		ViewGroup parent = (ViewGroup) view.getParent();
  		if (parent != null) {
  			parent.removeView(view);
  		}
    return this.view;
  }

  public boolean removePop()
  {
    if (this.ziJinPop != null)
    {
      this.ziJinPop.dismiss();
      this.ziJinPop = null;
      return true;
    }
    if (this.jiekuanPop != null)
    {
      this.jiekuanPop.dismiss();
      this.jiekuanPop = null;
      return true;
    }
    if (this.zhanghuPop != null)
    {
      this.zhanghuPop.dismiss();
      this.zhanghuPop = null;
      return true;
    }
    return false;
  }
}