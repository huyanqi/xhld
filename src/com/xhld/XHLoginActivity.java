package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xhld.base.BaseActivity;
import com.xhld.utils.NetTools;
import com.xhld.utils.Tools;

public class XHLoginActivity extends BaseActivity
  implements View.OnClickListener
{
  private EditText password;
  private EditText username;

  private void login()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("username", this.username.getText().toString());
    localHashMap.put("password", this.password.getText().toString());
    NetTools.getXHDataFromNetwork(this, "appuserlogin", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        try
        {
          XHLoginActivity.this.processDialog.dismiss();
          if ("200".equals(paramAnonymousJSONObject.getJSONObject("result").getString("statusCode")))
          {
            Tools.saveXHUser(XHLoginActivity.this, XHLoginActivity.this.username.getText().toString(), XHLoginActivity.this.password.getText().toString());
            XHLoginActivity.this.showToast("成功登录");
            XHLoginActivity.this.setResult(-1);
            XHLoginActivity.this.finish();
            return;
          }
          XHLoginActivity.this.showToast(paramAnonymousJSONObject.getJSONObject("result").getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHLoginActivity.this.processDialog.dismiss();
        XHLoginActivity.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1024)
    {
      this.username.setText(paramIntent.getStringExtra("username"));
      this.password.setText(paramIntent.getStringExtra("password"));
      login();
    }
  }

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		default:
			return;
		case R.id.submit:
			login();
			return;
		case R.id.reg:
			startActivityForResult(new Intent(this, XHRegActivity.class), 1024);
			break;
		}
	}

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_xh_login);
    this.username = ((EditText)findViewById(R.id.username));
    this.password = ((EditText)findViewById(R.id.password));
    findViewById(R.id.submit).setOnClickListener(this);
  }
}