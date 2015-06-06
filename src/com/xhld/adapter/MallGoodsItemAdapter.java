package com.xhld.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xhld.R;
import com.xhld.bean.GoodsModel;
import com.xhld.widget.SYNCImageView;

@SuppressLint("SimpleDateFormat")
public class MallGoodsItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<GoodsModel> data;

	public MallGoodsItemAdapter(Context context, List<GoodsModel> data) {
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater.inflate(R.layout.item_mall_goods, null);
			
			holder.thum = (SYNCImageView) convertView.findViewById(R.id.goods_img);
			holder.name = (TextView) convertView.findViewById(R.id.goods_name);
			holder.price = (TextView) convertView.findViewById(R.id.goods_price);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		GoodsModel goodsModel = data.get(position);
		loadThum(goodsModel.thum, holder.thum);
		holder.name.setText(goodsModel.name);
		holder.price.setText(goodsModel.price+"");

		return convertView;

	}
	
	private void loadThum(final String url,final SYNCImageView view){
		view.setTag(url);
		view.getImageFromURL(url, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String arg0, View arg1) {}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				if(arg1.getTag().equals(url)){
					view.setImageBitmap(arg2);
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {}
		});
	}
	
	private class Holder {
		SYNCImageView thum;
		TextView name;
		TextView price;
	}

}
