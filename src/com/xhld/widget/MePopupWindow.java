package com.xhld.widget;

import com.xhld.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MePopupWindow extends PopupWindow implements OnClickListener{

	private int[] icons;
	private String [] titles;
	private Context context;
	private View view;
	
	private ImageView iv1,iv2,iv3,iv4;
	private TextView tv1,tv2,tv3,tv4;
	
	private ClickeCallBack callback;
	
	public MePopupWindow(Context context) {
		super(context);
	}
	
	public MePopupWindow(Context context,int[] icons,String[] titles,ClickeCallBack callback){
		super(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		this.context = context;
		this.icons = icons;
		this.titles = titles;
		this.callback = callback;
		init();
	}
	
	private void init(){
		view = LayoutInflater.from(context).inflate(R.layout.pop, null);
		initWidget();
		setContentView(view);
	}

	private void initWidget(){
		iv1 = (ImageView) view.findViewById(R.id.tab1img);
		iv2 = (ImageView) view.findViewById(R.id.tab2img);
		iv3 = (ImageView) view.findViewById(R.id.tab3img);
		iv4 = (ImageView) view.findViewById(R.id.tab4img);
		
		tv1 = (TextView) view.findViewById(R.id.tab1tv);
		tv2 = (TextView) view.findViewById(R.id.tab2tv);
		tv3 = (TextView) view.findViewById(R.id.tab3tv);
		tv4 = (TextView) view.findViewById(R.id.tab4tv);
		
		iv1.setImageResource(icons[0]);
		iv2.setImageResource(icons[1]);
		iv3.setImageResource(icons[2]);
		iv4.setImageResource(icons[3]);
		
		tv1.setText(titles[0]);
		tv2.setText(titles[1]);
		tv3.setText(titles[2]);
		tv4.setText(titles[3]);
		
		view.findViewById(R.id.tab1).setOnClickListener(this);
		view.findViewById(R.id.tab2).setOnClickListener(this);;
		view.findViewById(R.id.tab3).setOnClickListener(this);;
		view.findViewById(R.id.tab4).setOnClickListener(this);;
		
	}
	
	public interface ClickeCallBack {
		public void tab1Clicked();
		public void tab2Clicked();
		public void tab3Clicked();
		public void tab4Clicked();
	}
	
	private Animation getAnimation() {
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.home_btn_animation);
		return animation;
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		view.findViewById(R.id.tab1).setAnimation(getAnimation());
		view.findViewById(R.id.tab2).setAnimation(getAnimation());
		view.findViewById(R.id.tab3).setAnimation(getAnimation());
		view.findViewById(R.id.tab4).setAnimation(getAnimation());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tab1:
			callback.tab1Clicked();
			break;
		case R.id.tab2:
			callback.tab2Clicked();
			break;
		case R.id.tab3:
			callback.tab3Clicked();
			break;
		case R.id.tab4:
			callback.tab4Clicked();
			break;
		}
	}
	
}
