package com.xhld.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @author Frankie
 *
 * 拦截掉touch事件的viewpager(禁止手滑)
 */
public class WLViewPager extends ViewPager
{

	private boolean isCanScroll = true;  
	public WLViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public WLViewPager(Context context)
	{
		super(context);
	}
    public void setCanScroll(boolean isCanScroll){  
        this.isCanScroll = isCanScroll;  
    }  
  
  

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }
}
