package com.mzeat.ui;

import java.util.ArrayList;

import com.mzeat.R;
import com.mzeat.image.BitmapManager;
import com.mzeat.image.BmpManager;
import com.mzeat.ui.widget.MulitPointTouchListener;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SharePhoto extends BaseActivity {
	private ImageView imgView;
	private TextView total_num;
	private TextView current_num;
	private BmpManager bmpManager;
	private ViewPager mPager;
	private ArrayList<View> pageViews = new ArrayList<View>();
	private ArrayList<String> url_imgs = new ArrayList<String>();
	DisplayMetrics dm;
	
	
	ProgressBar pb;
	int fromwhere;
	int position = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photodetail);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);// 获取分辨率
		this.bmpManager = new BmpManager(dm,BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));

		total_num = (TextView) findViewById(R.id.total_num);
		current_num = (TextView) findViewById(R.id.current_num);
		
		
		fromwhere = getIntent().getIntExtra("fromShareList", 0);
		position = getIntent().getIntExtra("position", 0);
		
		if (fromwhere == 1) {
			url_imgs.add(getIntent().getStringExtra("img_url"));
		}else {
			url_imgs = getIntent().getStringArrayListExtra("img_url");
		}
		
		total_num.setText(String.valueOf(url_imgs.size()));
		mPager = (ViewPager) findViewById(R.id.photo_page);
		for (int i = 0; i <url_imgs.size()  ; i++) {

			RelativeLayout ll  = (RelativeLayout) LayoutInflater.from(
					SharePhoto.this).inflate(R.layout.activity_photo,
					null);
			pb = (ProgressBar)  ll.findViewById(R.id.imagezoomdialog_progress)	;
			imgView =  (ImageView) ll.findViewById(R.id.sharephoto);
			imgView.setOnTouchListener(new  MulitPointTouchListener());// 设置触屏监听
			bmpManager.loadBitmap(url_imgs.get(i), imgView,pb);
			pageViews.add(ll);

		}
		mPager.setAdapter(new myPagerView());
		mPager.setCurrentItem(position);
		current_num.setText(String.valueOf(position+1));
		mPager.setOnPageChangeListener(new  OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				current_num.setText(String.valueOf(arg0+1));
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/***
	 * viewpager 的数据源
	 * 
	 * @author zhangjia
	 * 
	 */
	class myPagerView extends PagerAdapter {
		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		/***
		 * 获取每一个item， 类于listview中的getview
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			
			
			return pageViews.get(arg1);
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			finish();
		}
		

		return true;
	}
}
