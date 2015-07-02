package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHUserBaseInfo;
import com.xhld.utils.NetTools;

public class XHJiBenXinXi extends BaseActivity
  implements View.OnClickListener
{
  private Gson gson;
  private RadioGroup guyong_tv;
  private RadioGroup hunyin_gv;
  private RadioGroup renshou_gv;
  private RadioGroup yanglao_gv;
  private RadioGroup zinv_gv;

  private boolean formValid()
  {
    if (this.hunyin_gv.getCheckedRadioButtonId() == -1)
    {
      showToast("请选择婚姻状况");
      return false;
    }
    if (this.zinv_gv.getCheckedRadioButtonId() == -1)
    {
      showToast("请选择是否有子女");
      return false;
    }
    if (this.yanglao_gv.getCheckedRadioButtonId() == -1)
    {
      showToast("请选择是否有养老保险");
      return false;
    }
    if (this.renshou_gv.getCheckedRadioButtonId() == -1)
    {
      showToast("请选择是否有人寿保险");
      return false;
    }
    if (this.guyong_tv.getCheckedRadioButtonId() == -1)
    {
      showToast("请选择雇佣类型");
      return false;
    }
    return true;
  }

  private void getBaseInfo()
  {
    this.processDialog.show();
    NetTools.getXHDataFromNetwork(this, "getBasicinfo", new HashMap(), new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHJiBenXinXi.this.processDialog.dismiss();
        try
        {
          XHUserBaseInfo localXHUserBaseInfo = (XHUserBaseInfo)XHJiBenXinXi.this.gson.fromJson(paramAnonymousJSONObject.getString("basicinfoResult"), XHUserBaseInfo.class);
          if (localXHUserBaseInfo == null)
            return;
          XHJiBenXinXi.this.selectRadio(localXHUserBaseInfo.marriageType, XHJiBenXinXi.this.hunyin_gv);
          XHJiBenXinXi.this.selectRadio(localXHUserBaseInfo.hasChildren, XHJiBenXinXi.this.zinv_gv);
          XHJiBenXinXi.this.selectRadio(localXHUserBaseInfo.endowmentInsurance, XHJiBenXinXi.this.yanglao_gv);
          XHJiBenXinXi.this.selectRadio(localXHUserBaseInfo.lifeInsurance, XHJiBenXinXi.this.renshou_gv);
          XHJiBenXinXi.this.selectRadio(localXHUserBaseInfo.employmentType, XHJiBenXinXi.this.guyong_tv);
          return;
        }
        catch (JsonSyntaxException localJsonSyntaxException)
        {
          localJsonSyntaxException.printStackTrace();
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHJiBenXinXi.this.processDialog.dismiss();
        XHJiBenXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private String getTagValue(RadioGroup paramRadioGroup)
  {
    return (String) findViewById(paramRadioGroup.getCheckedRadioButtonId()).getTag();
  }

  private void init()
  {
    this.gson = new Gson();
    this.hunyin_gv = ((RadioGroup)findViewById(R.id.hunyin_gv));
    this.zinv_gv = ((RadioGroup)findViewById(R.id.zinv_gv));
    this.yanglao_gv = ((RadioGroup)findViewById(R.id.yanglao_gv));
    this.renshou_gv = ((RadioGroup)findViewById(R.id.renshou_gv));
    this.guyong_tv = ((RadioGroup)findViewById(R.id.guyong_tv));
  }

  private void saveBaseInfo()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("marriageType", getTagValue(this.hunyin_gv));
    localHashMap.put("hasChildren", getTagValue(this.zinv_gv));
    localHashMap.put("endowmentInsurance", getTagValue(this.yanglao_gv));
    localHashMap.put("lifeInsurance", getTagValue(this.renshou_gv));
    localHashMap.put("employmentType", getTagValue(this.guyong_tv));
    NetTools.getXHDataFromNetwork(this, "addBasicinfo", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHJiBenXinXi.this.processDialog.dismiss();
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("200".equals(localJSONObject.getString("statusCode")))
          {
            XHJiBenXinXi.this.showToast(localJSONObject.getString("message"));
            XHJiBenXinXi.this.finish();
            return;
          }
          XHJiBenXinXi.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHJiBenXinXi.this.processDialog.dismiss();
        XHJiBenXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void selectRadio(int paramInt, RadioGroup paramRadioGroup)
  {
	  for(int i=0;i<paramRadioGroup.getChildCount();i++){
		  RadioButton localRadioButton = (RadioButton)paramRadioGroup.getChildAt(i);
		  if (localRadioButton.getTag().equals(paramInt+""))
		        localRadioButton.setChecked(true);
	  }
	  
  }

  public void onClick(View paramView)
  {
    if (!formValid())
      return;
    saveBaseInfo();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("基本信息");
    setContentView(R.layout.activity_xh_jibenxinxi);
    init();
    getBaseInfo();
  }
}