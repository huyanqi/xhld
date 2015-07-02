package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.xhld.base.BaseActivity;
import com.xhld.bean.XHCityModel;
import com.xhld.bean.XHProvinceModel;
import com.xhld.utils.NetTools;

public class XHJieKuanShenQing extends BaseActivity
  implements View.OnClickListener
{
  private XHCityModel city;
  private TextView city_tv;
  private TextView jiekuanbiaoti_tv;
  private TextView jiekuanjine_tv;
  private TextView jiekuanlilv_tv;
  private TextView jiekuanmiaoshu_tv;
  private String[] monthDatas = { "12", "18", "24", "36" };
  private Spinner monthSpinner;
  private String[] paymentDatas = { "1", "2" };
  private Spinner paymentSpinner;
  private XHProvinceModel province;
  private String[] typeDatas = { "2", "1" };
  private Spinner typeSpinner;
  private String[] useDatas = { "1", "2", "3", "4", "5" };
  private Spinner useSpinner;

  private boolean formVali()
  {
    if ((this.province == null) || (this.city == null))
    {
      showToast("请选择居住城市");
      return false;
    }
    if ("".equals(this.jiekuanbiaoti_tv.getText().toString()))
    {
      showToast("请输入借款标题");
      return false;
    }
    if ("".equals(this.jiekuanjine_tv.getText().toString()))
    {
      showToast("请输入借款金额");
      return false;
    }
    if ("".equals(this.jiekuanlilv_tv.getText().toString()))
    {
      showToast("请输入借款利率");
      return false;
    }
    if ("".equals(this.jiekuanmiaoshu_tv.getText().toString()))
    {
      showToast("请输入借款描述");
      return false;
    }
    return true;
  }

  private void init()
  {
    setSpinner();
    this.city_tv = ((TextView)findViewById(R.id.city));
    this.jiekuanbiaoti_tv = ((TextView)findViewById(R.id.jiekuanbiaoti_tv));
    this.jiekuanjine_tv = ((TextView)findViewById(R.id.jiekuanjine_tv));
    this.jiekuanlilv_tv = ((TextView)findViewById(R.id.jiekuanlilv_tv));
    this.jiekuanmiaoshu_tv = ((TextView)findViewById(R.id.jiekuanmiaoshu_tv));
    findViewById(R.id.city_ly).setOnClickListener(this);
    findViewById(R.id.link).setOnClickListener(this);
    findViewById(R.id.submit).setOnClickListener(this);
  }

  private void setSpinner()
  {
    this.monthSpinner = ((Spinner)findViewById(R.id.month));
    this.typeSpinner = ((Spinner)findViewById(R.id.typeSpinner));
    this.useSpinner = ((Spinner)findViewById(R.id.useSpinner));
    this.paymentSpinner = ((Spinner)findViewById(R.id.paymentSpinner));
  }

  private void submit()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("type", this.typeDatas[this.typeSpinner.getSelectedItemPosition()]);
    localHashMap.put("amount", this.jiekuanjine_tv.getText().toString());
    localHashMap.put("month", this.monthDatas[this.monthSpinner.getSelectedItemPosition()]);
    localHashMap.put("name", this.jiekuanbiaoti_tv.getText().toString());
    localHashMap.put("purpose", this.useDatas[this.useSpinner.getSelectedItemPosition()]);
    localHashMap.put("repaymentType", this.paymentDatas[this.paymentSpinner.getSelectedItemPosition()]);
    localHashMap.put("interest", this.jiekuanlilv_tv.getText().toString());
    localHashMap.put("description", this.jiekuanmiaoshu_tv.getText().toString());
    localHashMap.put("province", Integer.valueOf(this.province.id));
    localHashMap.put("city", Integer.valueOf(this.city.id));
    NetTools.getXHDataFromNetwork(this, "user/loan/addLoanApplyInfo", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        System.out.println(paramAnonymousJSONObject);
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if (!localJSONObject.getString("statusCode").equals("200"))
          {
            XHJieKuanShenQing.this.showToast(localJSONObject.getString("message"));
            return;
          }
          XHJieKuanShenQing.this.showToast("申请提交成功");
          XHJieKuanShenQing.this.finish();
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHJieKuanShenQing.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 != RESULT_OK);
    if(paramInt1 == 1024){
    	this.province = ((XHProvinceModel)paramIntent.getSerializableExtra("province"));
    	this.city = ((XHCityModel)paramIntent.getSerializableExtra("city"));
    	this.city_tv.setText(this.province.name + " " + this.city.name);
    }
  }

  public void onClick(View paramView)
  {
	  switch (paramView.getId()) {
	case R.id.city_ly:
		startActivityForResult(new Intent(this, XHSelectProvinceActivity.class), 1024);
		break;
	case R.id.link:
		Intent localIntent = new Intent();
		localIntent.setAction("android.intent.action.VIEW");
		localIntent.setData(Uri.parse("http://xiaohelidai.com/page/loanOnAgreement"));
		startActivity(localIntent);
		break;
	case R.id.submit:
		submit();
		break;
	}
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("借款申请");
    setContentView(R.layout.activity_xh_jiekuanshenqing);
    init();
  }
}