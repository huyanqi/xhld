package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xhld.base.BaseActivity;
import com.xhld.utils.NetTools;

public class XHRegActivity extends BaseActivity
  implements View.OnClickListener
{
  private boolean isSend = false;
  private EditText reg_code;
  private EditText reg_mail;
  private EditText reg_mobile;
  private EditText reg_password;
  private TextView reg_send_code;
  private EditText reg_username;

  private void init()
  {
    this.reg_username = ((EditText)findViewById(2131296409));
    this.reg_password = ((EditText)findViewById(2131296386));
    this.reg_mobile = ((EditText)findViewById(2131296385));
    this.reg_mail = ((EditText)findViewById(2131296411));
    this.reg_code = ((EditText)findViewById(2131296412));
    findViewById(2131296410).setOnClickListener(this);
  }

  private void sendCode()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("mobile", this.reg_mobile.getText().toString());
    localHashMap.put("mobileCode", Integer.valueOf((int)(100000.0D * (1.0D + 9.0D * Math.random()))));
    NetTools.getXHDataFromNetwork(this, "appSendMessage", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHRegActivity.this.processDialog.dismiss();
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("200".equals(localJSONObject.getString("statusCode")))
          {
            XHRegActivity.this.isSend = true;
            XHRegActivity.this.reg_mobile.setEnabled(false);
            XHRegActivity.this.showToast("短信发送成功");
            return;
          }
          XHRegActivity.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHRegActivity.this.processDialog.dismiss();
        XHRegActivity.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void toReg()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("username", this.reg_username.getText().toString());
    localHashMap.put("password", this.reg_password.getText().toString());
    localHashMap.put("email", this.reg_mail.getText().toString());
    localHashMap.put("tel", this.reg_mobile.getText().toString());
    NetTools.getXHDataFromNetwork(this, "appregister", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHRegActivity.this.processDialog.dismiss();
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("200".equals(localJSONObject.getString("statusCode")))
          {
            Intent localIntent = new Intent();
            localIntent.putExtra("username", XHRegActivity.this.reg_username.getText().toString());
            localIntent.putExtra("password", XHRegActivity.this.reg_password.getText().toString());
            XHRegActivity.this.setResult(-1, localIntent);
            XHRegActivity.this.finish();
            return;
          }
          XHRegActivity.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHRegActivity.this.processDialog.dismiss();
        XHRegActivity.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void toVeri()
  {
    if (this.reg_code.getText().length() != 6)
    {
      showToast("请输入手机获取到的验证码");
      return;
    }
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("mobileCode", this.reg_code.getText().toString());
    NetTools.getXHDataFromNetwork(this, "appCheckMobileCode", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHRegActivity.this.processDialog.dismiss();
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("1".equals(localJSONObject.getString("statusCode")))
          {
            XHRegActivity.this.toReg();
            return;
          }
          XHRegActivity.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHRegActivity.this.processDialog.dismiss();
        XHRegActivity.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131296410:
      if (this.reg_mobile.getText().length() == 11)
      {
        sendCode();
        return;
      }
      showToast("请输入正确的11位手机号");
      return;
    case 2131296389:
    }
    if (!this.isSend)
    {
      showToast("请先获取手机验证码");
      return;
    }
    toVeri();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("快速注册");
    setContentView(2130903077);
    init();
  }
}