package com.mzeat.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

public class CustomViewpage extends ViewPager {

	

	public CustomViewpage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
