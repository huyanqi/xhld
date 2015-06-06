package com.xhld;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;

import com.xhld.base.BaseActivity;
import com.xhld.bean.AdModel;
import com.xhld.utils.NetTools;
import com.xhld.utils.NetTools.NetToolCallBack;
import com.xhld.utils.Tools;
import com.xhld.widget.SYNCImageView;

public class Welcome extends BaseActivity {

	private SYNCImageView welcomeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		welcomeImg = new SYNCImageView(this);
		welcomeImg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		welcomeImg.setScaleType(ScaleType.FIT_XY);
		setContentView(welcomeImg);
		loadWelcome();
	}

	private void loadWelcome() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("code", 1028);
		NetTools.getDataFromNetwork(this, "getAd", param,
				new NetToolCallBack() {
					@Override
					public void error(String error) {
						showToast(error);
					}

					@Override
					public void callBack(JSONObject result) {
						try {
							if (Tools.isOk(result)) {
								JSONArray jAry = result.getJSONArray("model");
								List<AdModel> list = Tools.jAry2List(jAry,AdModel.class);
								if (list.size() == 0) {
									setTimer();
									flag = 1;
									return;
								}
								AdModel model = Tools.jAry2List(jAry,
										AdModel.class).get(0);
								welcomeImg.getImageFromURL(model.url, null);
								setTimer();
								flag = 1;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, true);
	}

	int flag = 0;
	private void setTimer() {
		if(flag == 1) return;
		final Handler timerHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				startActivity(new Intent(Welcome.this,LoginActivity.class));
				finish();
			}
		};

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				timerHandler.sendEmptyMessage(1);
			}
		}, 3 * 1000);
	}

}
