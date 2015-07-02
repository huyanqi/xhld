package com.xhld;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHUserLianXiRen;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.widget.BaseProcessDialog;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class XHLianXiRenXinXi extends BaseActivity
  implements View.OnClickListener
{
  private EditText family1Mobi;
  private EditText family1Name;
  private Spinner family1Sp;
  private EditText family2Mobi;
  private EditText family2Name;
  private Spinner family2Sp;
  private EditText family3Mobi;
  private EditText family3Name;
  private Spinner family3Sp;
  private EditText family4Mobi;
  private EditText family4Name;
  private Spinner family4Sp;
  private Gson gson;

  private void getData()
  {
    this.processDialog.show();
    NetTools.getXHDataFromNetwork(this, "getContactinfo", new HashMap(), new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHLianXiRenXinXi.this.processDialog.dismiss();
        try
        {
          XHUserLianXiRen localXHUserLianXiRen = (XHUserLianXiRen)XHLianXiRenXinXi.this.gson.fromJson(paramAnonymousJSONObject.getString("contactResult"), XHUserLianXiRen.class);
          XHLianXiRenXinXi.this.family1Name.setText(localXHUserLianXiRen.family1);
          XHLianXiRenXinXi.this.family2Name.setText(localXHUserLianXiRen.family2);
          XHLianXiRenXinXi.this.family3Name.setText(localXHUserLianXiRen.family3);
          XHLianXiRenXinXi.this.family4Name.setText(localXHUserLianXiRen.family4);
          XHLianXiRenXinXi.this.family1Sp.setSelection(localXHUserLianXiRen.relation1);
          XHLianXiRenXinXi.this.family2Sp.setSelection(localXHUserLianXiRen.relation2);
          XHLianXiRenXinXi.this.family3Sp.setSelection(localXHUserLianXiRen.relation3);
          XHLianXiRenXinXi.this.family4Sp.setSelection(localXHUserLianXiRen.relation4);
          XHLianXiRenXinXi.this.family1Mobi.setText(localXHUserLianXiRen.rfmobile1);
          XHLianXiRenXinXi.this.family2Mobi.setText(localXHUserLianXiRen.rfmobile2);
          XHLianXiRenXinXi.this.family3Mobi.setText(localXHUserLianXiRen.rfmobile3);
          XHLianXiRenXinXi.this.family4Mobi.setText(localXHUserLianXiRen.rfmobile4);
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
        XHLianXiRenXinXi.this.processDialog.dismiss();
        XHLianXiRenXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  private void init()
  {
    this.gson = new Gson();
    this.family1Name = ((EditText)findViewById(R.id.family1Name));
    this.family1Sp = ((Spinner)findViewById(R.id.family1Sp));
    this.family1Mobi = ((EditText)findViewById(R.id.family1Mobi));
    this.family2Name = ((EditText)findViewById(R.id.family2Name));
    this.family2Sp = ((Spinner)findViewById(R.id.family2Sp));
    this.family2Mobi = ((EditText)findViewById(R.id.family2Mobi));
    this.family3Name = ((EditText)findViewById(R.id.family3Name));
    this.family3Sp = ((Spinner)findViewById(R.id.family3Sp));
    this.family3Mobi = ((EditText)findViewById(R.id.family3Mobi));
    this.family4Name = ((EditText)findViewById(R.id.family4Name));
    this.family4Sp = ((Spinner)findViewById(R.id.family4Sp));
    this.family4Mobi = ((EditText)findViewById(R.id.family4Mobi));
  }

  private void saveLianXiRen()
  {
    this.processDialog.show();
    HashMap localHashMap = new HashMap();
    localHashMap.put("family1", this.family1Name.getText().toString());
    localHashMap.put("relation1", Integer.valueOf(1 + this.family1Sp.getSelectedItemPosition()));
    localHashMap.put("rfmobile1", this.family1Mobi.getText().toString());
    localHashMap.put("family2", this.family2Name.getText().toString());
    localHashMap.put("relation2", Integer.valueOf(1 + this.family2Sp.getSelectedItemPosition()));
    localHashMap.put("rfmobile2", this.family2Mobi.getText().toString());
    localHashMap.put("family3", this.family3Name.getText().toString());
    localHashMap.put("relation3", Integer.valueOf(1 + this.family3Sp.getSelectedItemPosition()));
    localHashMap.put("rfmobile3", this.family3Mobi.getText().toString());
    localHashMap.put("family4", this.family4Name.getText().toString());
    localHashMap.put("relation4", Integer.valueOf(1 + this.family4Sp.getSelectedItemPosition()));
    localHashMap.put("rfmobile4", this.family4Mobi.getText().toString());
    NetTools.getXHDataFromNetwork(this, "updatecontactInfo", localHashMap, new NetTools.NetToolCallBack()
    {
      public void callBack(JSONObject paramAnonymousJSONObject)
      {
        XHLianXiRenXinXi.this.processDialog.dismiss();
        try
        {
          JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
          if ("200".equals(localJSONObject.getString("statusCode")))
          {
            XHLianXiRenXinXi.this.showToast(localJSONObject.getString("message"));
            XHLianXiRenXinXi.this.finish();
            return;
          }
          XHLianXiRenXinXi.this.showToast(localJSONObject.getString("message"));
          return;
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
        }
      }

      public void error(String paramAnonymousString)
      {
        XHLianXiRenXinXi.this.processDialog.dismiss();
        XHLianXiRenXinXi.this.showToast(paramAnonymousString);
      }
    }
    , Boolean.valueOf(false));
  }

  public void onClick(View paramView)
  {
    saveLianXiRen();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    initActionBar("联系人信息");
    setContentView(R.layout.activity_xh_lianxirenxinxi);
    init();
    getData();
  }
}