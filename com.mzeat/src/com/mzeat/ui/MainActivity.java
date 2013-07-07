package com.mzeat.ui;

import java.util.Timer;

import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.model.User;
import com.mzeat.util.LogUtil;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity {

	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	public final static String SER_KEY = "main";

	public static TextView tv_tips;
	int count;
	private PreferencesConfig mConfig = MzeatApplication.getInstance()
			.getpPreferencesConfig();
	Intent mycount;
	RadioButton index;
	RadioButton rb_myorder;
	RadioButton rb_mycount;
	RadioButton rb_message;

	MyReceiver receiver;


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mycount = new Intent(MainActivity.this, MycountActivity.class);
		setContentView(R.layout.activity_main);
		tv_tips = (TextView) findViewById(R.id.main_tab_new_message);

		if (count != 0) {
			tv_tips.setText(String.valueOf(count));
			tv_tips.setVisibility(View.VISIBLE);

		}
		
		receiver = new MyReceiver();

		IntentFilter filter = new IntentFilter();

		filter.addAction("android.intent.action.setTextView");

		registerReceiver(receiver, filter);
		
		tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("index").setIndicator("index")
				.setContent(new Intent(this, IndexActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("shopcart").setIndicator("shopcart")
				.setContent(new Intent(this, ShopCartActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("message").setIndicator("message")
				.setContent(new Intent(this, MessageActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("mycount").setIndicator("mycount")
				.setContent(mycount));
		tabHost.addTab(tabHost.newTabSpec("Login").setIndicator("Login")
				.setContent(new Intent(MainActivity.this, LoginActivity.class)));

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				if (tabId.equals("message")) {
					tv_tips.setVisibility(View.GONE);


				} else {
					if (MzeatApplication.getInstance().getpPreferencesConfig().getInt("count", 0 )!= 0) {
						count = MzeatApplication.getInstance().getpPreferencesConfig().getInt("count", 0);
						tv_tips.setText(String.valueOf(count));
						tv_tips.setVisibility(View.VISIBLE);

					}
				}
			}
		});

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(checkedChangeListener);
		index = (RadioButton) findViewById(R.id.radio_index);
		rb_mycount = (RadioButton) findViewById(R.id.radio_mycount);
		rb_myorder = (RadioButton) findViewById(R.id.radio_cart);
		rb_message = (RadioButton) findViewById(R.id.radio_message);

	}

	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_index:
				tabHost.setCurrentTabByTag("index");

				break;
			case R.id.radio_cart:
				tabHost.setCurrentTabByTag("shopcart");

				break;

			case R.id.radio_message:
				tabHost.setCurrentTabByTag("message");

				break;
			case R.id.radio_mycount:
				if (mConfig.getInt("loginstate", 0) == 1) {
					tabHost.setCurrentTabByTag("mycount");

				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivityForResult(intent, 1);
				}

				break;

			default:
				break;
			}
		}
	};
	String TAG = "MainActivity";

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.getLogOnStart(TAG);
		
		int fromnotice = MzeatApplication.getInstance().getpPreferencesConfig().getInt("fromnotice",0);
		Log.e("formnotice", String.valueOf(fromnotice));
		if (fromnotice == 1) {
			rb_message.setChecked(true);
			tabHost.setCurrentTabByTag("message");
			MzeatApplication.getInstance().getpPreferencesConfig()
			.setInt("fromnotice", 0);
		}
		
		int logout = MzeatApplication.getInstance().getpPreferencesConfig()
				.getInt("logout", 0);
		// 注销之后跳到注册页面再返回后跳到主界面
		if (logout == 1) {
			index.setChecked(true);
			tabHost.setCurrentTabByTag("index");
			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("logout", 0);
		}
		// 注册成功后跳到我的账号
		else if (logout == 2) {
			rb_mycount.setChecked(true);
			tabHost.setCurrentTabByTag("mycount");
			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("logout", 0);
		}
		// 从好优惠详细跳转到购物车
		if (MzeatApplication.getInstance().getpPreferencesConfig()
				.getInt("tomyorder", 0) == 1) {
			rb_myorder.setChecked(true);
			tabHost.setCurrentTabByTag("shopcart");
			MzeatApplication.getInstance().getpPreferencesConfig()
			.setInt("tomyorder", 0);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.getLogOnPause(TAG);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.getLogOnStop(TAG);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.getLogOnDestroy(TAG);
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public class MyReceiver extends BroadcastReceiver {

		// 自定义一个广播接收器

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub

			System.out.println("OnReceiver");

			Bundle bundle = intent.getExtras();

			count = bundle.getInt("count");

			if (count != 0) {
				Log.e("START", "开始设置消息条数");
				tv_tips.setVisibility(View.VISIBLE);
				tv_tips.setText(String.valueOf(count));

			} else {
				tv_tips.setVisibility(View.GONE);
			}

			// 处理接收到的内容

		}

		public MyReceiver() {

			System.out.println("MyReceiver");

			// 构造函数，做一些初始化工作，本例中无任何作用

		}

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// 在登陆页面登陆成功后跳转
		if (resultCode == 1) {
			int gotowhere = data.getIntExtra("back", 0);
			if (gotowhere == 0) {

			
				mycount.putExtra("fromlogin", 1);
				tabHost.setCurrentTabByTag("mycount");
			}

		}
		// 在登陆页面返回跳转
		else if (resultCode == 2) {
			index.setChecked(true);

		}
	}

}
