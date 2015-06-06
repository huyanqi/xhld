package com.xhld.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 用于异步加载图片
 * 
 */
public class SYNCImageView extends ImageView {

	private String imageURL = null;
	private ImageLoader imgLoader = ImageLoader.getInstance();
	private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();

	public SYNCImageView(Context context) {
		super(context);
	}

	public SYNCImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SYNCImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * 设置图片url，获得从网络返回的图片
	 * 
	 * @param url
	 */
	public void getImageFromURL(String url, ImageLoadingListener listener) {
		getImageFromurl(url, listener);
	}

	public void getImageFromURL(String url, ImageLoadingListener listener,int defaultResourceId) {
		getImageFromurl(url, listener, defaultResourceId);
	}

	private void getImageFromurl(String url, ImageLoadingListener listener) {
		this.imageURL = url;
		if (imageURL == null || imageURL.equals("")) {
			return;
		}
		if(!imgLoader.isInited())
			imgLoader.init(ImageLoaderConfiguration.createDefault(getContext()));
		imgLoader.displayImage(this.imageURL, this, options, listener);
	}

	private void getImageFromurl(String url, ImageLoadingListener listener, int defaultResourceId) {
		this.imageURL = url;
		if (imageURL == null || imageURL.equals("")) {
			return;
		}

		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true)
		        .showImageOnLoading(defaultResourceId).showImageOnFail(defaultResourceId).build();
		imgLoader.displayImage(this.imageURL, this, options, listener);
	}

	public void getRoundedImageFromUrl(String url, ImageLoadingListener listener,int round) {
		this.imageURL = url;
		if (imageURL == null || imageURL.equals("")) {
			return;
		}
		DisplayImageOptions roundedOption = new DisplayImageOptions.Builder()
        .cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(round)).build();
		imgLoader.displayImage(this.imageURL, this, roundedOption, listener);
	}

}
