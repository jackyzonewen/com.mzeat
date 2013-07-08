package com.mzeat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.model.User;
import com.mzeat.util.LogUtil;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends TabActivity {

	AlertDialog menuDialog;// menu菜单Dialog
	GridView menuGrid;
	View menuView;
	
	
	private final int ITEM_ABOUT = 0;// 关于
	private final int ITEM_EXIT = 1;// 退出
	
	
	/** 菜单图片 **/
	int[] menu_image_array = {  R.drawable.menu_quit,
			R.drawable.menu_about };
	/** 菜单文字 **/
	String[] menu_name_array = {  "关于","退出" };
	
	
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
		count = mConfig.getInt("count", 0);
		if (count != 0) {
			tv_tips.setText(String.valueOf(count));
			tv_tips.setVisibility(View.VISIBLE);

		}
		
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case ITEM_ABOUT:

					break;
				case ITEM_EXIT:// 文件管理

					break;
				default:
					break;
				}
				
				
			}
		});
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
				//tabHost.setCurrentTabByTag("shopcart");

				if (mConfig.getInt("loginstate", 0) == 1) {
					tabHost.setCurrentTabByTag("shopcart");

				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					intent.putExtra("frommycart", 1);
					startActivityForResult(intent, 1);
				}
				break;

			case R.id.radio_message:
			
				if (mConfig.getInt("loginstate", 0) == 1) {
					tabHost.setCurrentTabByTag("message");

				} else {
					Intent intent = new Intent(MainActivity.this,
							LoginActivity.class);
					intent.putExtra("frommessage", 1);
					startActivityForResult(intent, 1);
				}
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
		
		if (MzeatApplication.getInstance().getpPreferencesConfig().getInt("loginstate", 0) != 1) {
			MzeatApplication.getInstance().getpPreferencesConfig().setInt("count", 0);
			tv_tips.setVisibility(View.GONE);
		}
		LogUtil.getLogOnStart(TAG);
		
		//从通知跳转到我的消息
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
		//注册成功后跳到我的账号
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

			//System.out.println("OnReceiver");

			Bundle bundle = intent.getExtras();

			count = bundle.getInt("count");
			Log.e("count", String.valueOf(count));

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
			//System.out.println("MyReceiver");

			// 构造函数，做一些初始化工作，本例中无任何作用

		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}
	
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 返回为true 则显示系统menu
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		// 在登陆页面登陆成功后跳转
		if (resultCode == 1) {
			int gotowhere = data.getIntExtra("back", 0);
			if (gotowhere == 0) {

				//mycount.putExtra("fromlogin", 1);
				tabHost.setCurrentTabByTag("mycount");
				
			}

		}
		// 在登陆页面返回跳转
		else if (resultCode == 2) {
			
			Log.e("resultCode", String.valueOf(resultCode));
			index.setChecked(true);

		}else if (resultCode == 3) {
			rb_myorder.setChecked(true);
			tabHost.setCurrentTabByTag("shopcart");
		}else if (resultCode == 4) {
			rb_message.setChecked(true);
			tabHost.setCurrentTabByTag("message");
		}
	}

}
