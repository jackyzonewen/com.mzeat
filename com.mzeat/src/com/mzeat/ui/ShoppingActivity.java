package com.mzeat.ui;

import com.baidu.location.LocationClient;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.location.BaiduLocationOption;
import com.mzeat.ui.FoodActivity.NetworkChange;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.ShowToast;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class ShoppingActivity extends TabActivity {

	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;

	private TextView tv_title;
	private LocationClient mLocClient = null;

	// private NetworkChange networkChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);

		tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("food").setIndicator("food")
				.setContent(new Intent(this, FoodActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("buy").setIndicator("buy")
				.setContent(new Intent(this, BuyActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("hotel").setIndicator("hotel")
				.setContent(new Intent(this, HotelActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("happy").setIndicator("happy")
				.setContent(new Intent(this, HappyActivity.class)));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub

			}
		});

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);

	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_food:
				tabHost.setCurrentTabByTag("food");

				break;
			case R.id.radio_buy:
				tabHost.setCurrentTabByTag("buy");

				break;

			case R.id.radio_hotal:
				tabHost.setCurrentTabByTag("hotel");

				break;
			case R.id.radio_happy:
				tabHost.setCurrentTabByTag("happy");

				break;

			default:
				break;
			}
		}
	};





}
