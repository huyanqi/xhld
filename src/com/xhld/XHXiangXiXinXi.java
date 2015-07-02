package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHUserDetailInfo;
import com.xhld.utils.NetTools;

public class XHXiangXiXinXi extends BaseActivity
  implements View.OnClickListener
{
  private Spinner degree;
  private Gson gson;
  private EditText juzhu_et;
  private EditText zhengjian_et;

  private boolean formValid()
  {
    if (this.zhengjian_et.getText().length() == 0)
    {
      showToast("请填写证件地址");
      return false;
    }
    if (this.juzhu_et.getText().length() == 0)
    {
      showToast("请填写居住地址");
      return false;
    }
    return true;
  }

  private void getDetailInfo()
  {
    this.processDialog.show();
    NetTools.getXHDataFromNetwork(this, "getDetailInfo", new HashMap(), new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHXiangXiXinXi.this.processDialog.dismiss();
        try
        {
          XHUserDetailInfo localXHUserDetailInfo = (XHUserDetailInfo)XHXiangXiXinXi.this.gson.fromJson(paramAnonymousJSONObject.getString("detailInfoResult"), XHUserDetailInfo.class);
          XHXiangXiXinXi.this.degree.setSelection(localXHUserDetailInfo.highDegree);
          XHXiangXiXinXi.this.zhengjian_et.setText(localXHUserDetailInfo.documentsAddress);
          XHXiangXiXinXi.this.juzhu_et.setText(localXHUserDetailInfo.resideAddress);
          XHXiangXiXinXi.this.zhengjian_et.setSelection(XHXiangXiXinXi.this.zhengjian_et.getText().length());
          XHXiangXiXinXi.this.juzhu_et.setSelection(XHXiangXiXinXi.this.juzhu_et.getText().length());
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHXiangXiXinXi.this.processDialog.dismiss();
        XHXiangXiXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
    this.gson = new Gson();
    this.degree = ((Spinner)findViewById(R.id.degree));
    this.zhengjian_et = ((EditText)findViewById(R.id.zhengjian_et));
    this.juzhu_et = ((EditText)findViewById(R.id.juzhu_et));
  }

  private void saveDetailInfo()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("highDegree", Integer.valueOf(this.degree.getSelectedItemPosition()));
    localHashMap.put("documentsAddress", this.zhengjian_et.getText().toString());
    localHashMap.put("resideAddress", this.juzhu_et.getText().toString());
    NetTools.getXHDataFromNetwork(this, "addDetailinfo", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHXiangXiXinXi.this.processDialog.dismiss();
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("200".equals(localJSONObject.getString("statusCode")))
          {
            XHXiangXiXinXi.this.showToast(localJSONObject.getString("message"));
            XHXiangXiXinXi.this.finish();
            return;
          }
          XHXiangXiXinXi.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHXiangXiXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  public void onClick(View paramView)
  {
    if (!formValid())
      return;
    saveDetailInfo();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("详细信息");
    setContentView(R.layout.activity_xh_xiangxixinxi);
    init();
    getDetailInfo();
  }
}