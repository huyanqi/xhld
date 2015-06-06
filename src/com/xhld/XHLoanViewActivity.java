package com.xhld;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xhld.base.BaseActivity;
import com.xhld.bean.XHContactInfo;
import com.xhld.bean.XHFileInfo;
import com.xhld.bean.XHLoan;
import com.xhld.bean.XHLoanInfo;
import com.xhld.bean.XHLoanResultModel;
import com.xhld.bean.XHUserInfo;
import com.xhld.bean.XHWorkInfo;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;

public class XHLoanViewActivity extends BaseActivity implements OnClickListener{
	
	private TextView address;
	private TextView age;
	private TextView amount;
	private LinearLayout bottom_danweizhengming;
	private LinearLayout bottom_jiaoyiliushui;
	private LinearLayout bottom_jingyingzhengming;
	private LinearLayout bottom_juzhuzhengming;
	private LinearLayout bottom_shenfenzheng;
	private LinearLayout bottom_shouruzhengming;
	private LinearLayout bottom_xinyong;
	private TextView degree;
	private Dialog dialog;
	private TextView employmentType;
	private TextView gender;
	private Gson gson;
	private TextView haschildren;
	private TextView interest;
	private TextView lastmoney;
	private TextView marryType;
	private TextView month;
	private TextView name;
	private TextView person;
	private TextView progress;
	private TextView realname;
	private TextView repaymentType;
	private XHLoanResultModel resultModel;
	private TextView state;
	private TextView totalAmount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loan_view);
		initActionBar("债权详情");
		init();
		loadDetail();
	}

	private void init() {
		gson = new Gson();
		name = ((TextView) findViewById(R.id.name));
		amount = ((TextView) findViewById(R.id.amount));
		interest = ((TextView) findViewById(R.id.interest));
		month = ((TextView) findViewById(R.id.month));
		repaymentType = ((TextView) findViewById(R.id.repaymentType));
		progress = ((TextView) findViewById(R.id.progress));
		lastmoney = ((TextView) findViewById(R.id.lastmoney));
		person = ((TextView) findViewById(R.id.person));
		totalAmount = ((TextView) findViewById(R.id.totalAmount));
		state = ((TextView) findViewById(R.id.state));
		realname = ((TextView) findViewById(R.id.realname));
		marryType = ((TextView) findViewById(R.id.marryType));
		gender = ((TextView) findViewById(R.id.gender));
		haschildren = ((TextView) findViewById(R.id.haschildren));
		age = ((TextView) findViewById(R.id.age));
		degree = ((TextView) findViewById(R.id.degree));
		address = ((TextView) findViewById(R.id.address));
		employmentType = ((TextView) findViewById(R.id.employmentType));
		bottom_shenfenzheng = ((LinearLayout) findViewById(R.id.bottom_shenfenzheng));
		bottom_xinyong = ((LinearLayout) findViewById(R.id.bottom_xinyong));
		bottom_danweizhengming = ((LinearLayout) findViewById(R.id.bottom_danweizhengming));
		bottom_shouruzhengming = ((LinearLayout) findViewById(R.id.bottom_shouruzhengming));
		bottom_juzhuzhengming = ((LinearLayout) findViewById(R.id.bottom_juzhuzhengming));
		bottom_jingyingzhengming = ((LinearLayout) findViewById(R.id.bottom_jingyingzhengming));
		bottom_jiaoyiliushui = ((LinearLayout) findViewById(R.id.bottom_jiaoyiliushui));
	}
	
	private void loadDetail(){
	    HashMap localHashMap = new HashMap();
	    localHashMap.put("loanuuid", getIntent().getStringExtra("uuid"));
	    NetTools.getXHDataFromNetwork(this,"detail",localHashMap,new NetToolCallBack() {
			
			@Override
			public void error(String error) {
				showToast(error);
			}
			
			@Override
			public void callBack(JSONObject result) {
				System.out.println(result);
				try {
					resultModel = gson.fromJson(result.getString("loanResult"), XHLoanResultModel.class);
					XHLoan localXHLoan = resultModel.loan;
					XHLoanInfo localXHLoanInfo = resultModel.loanInfo;
		            XHUserInfo localXHUserInfo = resultModel.userInfo;
		            XHContactInfo localXHContactInfo = resultModel.contactInfo;
		            XHWorkInfo localXHWorkInfo = resultModel.workInfo;
		            
		            name.setText(localXHLoan.name);
		            amount.setText(localXHLoan.amount + "元");
		            interest.setText(localXHLoan.interest + "%");
		            month.setText(localXHLoan.month + "个月");
		            repaymentType.setText("还款方式:" + localXHLoan.repaymentType);
		            progress.setText("投标进度:" + 100 * (localXHLoan.progress / localXHLoan.amount) + "%");
		            lastmoney.setText("剩余可投金额:" + (localXHLoan.amount - localXHLoan.progress) + "元");
		            person.setText("投标人数:" + localXHLoan.person + "人");
		            totalAmount.setText("逾期到息本和利:" + localXHLoan.totalAmount + "元");
		            state.setText(localXHLoan.state);
		            
		            if (localXHLoan.stateCode == 1)
		                state.setOnClickListener(XHLoanViewActivity.this);
		            
		            realname.setText("姓名:" + localXHUserInfo.realName);
		            marryType.setText("婚姻状况:" + Tools.getMarryType(localXHContactInfo.marrigeType));
		            
		            if (localXHUserInfo.gender == 0) 
		            	gender.setText("性别:女");
		            else 
		            	gender.setText("性别:男");
		            
		            if (localXHContactInfo.haschildren == 0)
		            	haschildren.setText("有无子女:有");
		            else
		            	haschildren.setText("有无子女:无");
		            
		            age.setText("年龄:" + Tools.getAge(Tools.long2Str(localXHUserInfo.birthday.time)));
		            degree.setText("文化程度:" + Tools.getDegree(localXHUserInfo.degree));
		            address.setText("户口所在地:" + localXHLoanInfo.province.name + "-" + localXHLoanInfo.city.name);
		            employmentType.setText("雇佣类型:"+Tools.getEmployeeType(localXHWorkInfo.employeeType));
		            
		            showPassed(resultModel.fileInfo);
		            
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		},false);
	}

	private void showPassed(XHFileInfo paramXHFileInfo){
		if (("".equals(paramXHFileInfo.IDCardFront)) || ("".equals(paramXHFileInfo.IDCardCon))){
			bottom_shenfenzheng.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.report)){
			bottom_xinyong.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.companyCertify)){
			bottom_danweizhengming.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.incomeCertify)){
			bottom_shouruzhengming.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.locationCertify)){
			bottom_juzhuzhengming.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.enterpriseCertify)){
			bottom_jingyingzhengming.setVisibility(View.GONE);
		}
		if ("".equals(paramXHFileInfo.businessCertify)){
			bottom_jiaoyiliushui.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		if (this.dialog == null) {
			final EditText localEditText = new EditText(this);
			localEditText.setKeyListener(new DigitsKeyListener(false, true));
			this.dialog = new AlertDialog.Builder(this)
					.setTitle("输入投资金额")
					.setIcon(R.drawable.ic_launcher)
					.setView(localEditText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramAnonymousDialogInterface,
										int paramAnonymousInt) {
									invest(localEditText.getText().toString());
								}
							}).setNegativeButton("取消", null).create();
		}
		this.dialog.show();
	}
	
	private void invest(String paramString){
		if (!"".equals(paramString)) {
			int i = Integer.parseInt(paramString);
			if ((i < 50) || (20000 < i)) {
				showToast("投资金额在50-20000之间");
				return;
			}
		}
		this.processDialog.show();
		HashMap<String,Object> localHashMap = new HashMap<String,Object>();
		localHashMap.put("amount", paramString);
		localHashMap.put("loanuuid", this.resultModel.loan.uuid);
		NetTools.getXHDataFromNetwork(this, "user/invest/addInvestAction",
				localHashMap, new NetTools.NetToolCallBack() {
					public void callBack(JSONObject paramAnonymousJSONObject) {
						processDialog.dismiss();
						try {
							JSONObject localJSONObject = paramAnonymousJSONObject.getJSONObject("result");
							if ("200".equals(localJSONObject.getString("statusCode"))) {
								showToast(localJSONObject.getString("message"));
								loadDetail();
								return;
							}
							showToast(localJSONObject.getString("message"));
							return;
						} catch (JSONException localJSONException) {
							localJSONException.printStackTrace();
						}
					}

					public void error(String paramAnonymousString) {
						showToast(paramAnonymousString);
						processDialog.dismiss();
					}
				}, false);
	}

}
