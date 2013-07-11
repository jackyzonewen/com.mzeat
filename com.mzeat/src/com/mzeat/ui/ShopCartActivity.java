package com.mzeat.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.db.MycartDb;
import com.mzeat.model.ConfirmOrderItem;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.ui.adapter.MyCartAdapter;
import com.mzeat.util.JsonUtil;

import android.R.array;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopCartActivity extends BaseActivity {

	private ListView lv_cart;
	private MyCartAdapter mAdapter;
	private MycartDb mDb;
	private ArrayList<PrivilegeItem> mItem;

	private TextView tv_count;

	private String num;
	private String money;
	int oldnum = 0;
	float oldmoney = 0;
	private ImageButton btn_submit;
	MyReceiver receiver;
	
	LinearLayout ll_tips;
	LinearLayout ll_gotoprivilege;
	RelativeLayout ll_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopcart);

		lv_cart = (ListView) findViewById(R.id.lv_cart);
		mAdapter = new MyCartAdapter(this);
		mItem = new ArrayList<PrivilegeItem>();

		tv_count = (TextView) findViewById(R.id.tv_count);
		btn_submit = (ImageButton) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<ConfirmOrderItem> orderItems = new ArrayList<ConfirmOrderItem>();
				if (mItem.size() > 0) {
					for (int i = 0; i < mItem.size(); i++) {
						ConfirmOrderItem item = new ConfirmOrderItem(mItem.get(i).getGoods_id(),mItem.get(i).getNum(),mItem.get(i).getCount());
						orderItems.add(item);

					}
					JSONArray ja = new JSONArray();
					
					for (int i=0;i<orderItems.size();i++) {
					    ja.put(orderItems.get(i).getJSONObject());
					}
					JSONObject jsonObject = new JSONObject();
					oldmoney = 0.0f;
					for (int i = 0; i < mItem.size(); i++) {
						oldmoney += Float.valueOf(mItem.get(i).getCount());
					}
					try {
						jsonObject.put("total_prices", String.valueOf(oldmoney));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ja.put(jsonObject);
					Log.e("testjsonarray",ja.toString());
					
	
				}
			}
		});
		
		ll_tips = (LinearLayout) findViewById(R.id.ll_tips);
		ll_bottom = (RelativeLayout) findViewById(R.id.ll_bottom);
		ll_gotoprivilege = (LinearLayout) findViewById(R.id.ll_gotoprivilege);
		ll_gotoprivilege.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ShopCartActivity.this, PrivilegeActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.setCount");
		registerReceiver(receiver, filter);
		mDb = new MycartDb(this);

		mItem = mDb.getItem(MzeatApplication.getInstance()
				.getpPreferencesConfig().getString("email", ""));
		mDb.closeDB();
		mAdapter.clear();
		if (mItem.size() != 0) {
			
			ll_bottom.setVisibility(View.VISIBLE);
			lv_cart.setVisibility(View.VISIBLE);
			ll_tips.setVisibility(View.GONE);
			btn_submit.setVisibility(View.VISIBLE);
			mAdapter.setDataList(mItem);
			lv_cart.setAdapter(mAdapter);

			for (int i = 0; i < mItem.size(); i++) {
				oldmoney += Float.valueOf(mItem.get(i).getCount());
			}
			tv_count.setText("￥" + String.valueOf(oldmoney));
		}else {

			ll_bottom.setVisibility(View.GONE);
			lv_cart.setVisibility(View.GONE);
			ll_tips.setVisibility(View.VISIBLE);
			btn_submit.setVisibility(View.GONE);
		}

	}

	public class MyReceiver extends BroadcastReceiver {

		// 自定义一个广播接收器

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub

			System.out.println("OnReceiver");

			Bundle bundle = intent.getExtras();

			//num = bundle.getString("num");
			money = bundle.getString("money");

			
			tv_count.setText("￥" + money);
			if (money.equals("0.0")) {
				ll_bottom.setVisibility(View.GONE);
				lv_cart.setVisibility(View.GONE);
				ll_tips.setVisibility(View.VISIBLE);
				btn_submit.setVisibility(View.GONE);
			}
		}

		public MyReceiver() {

		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(MyIntent);
			// Log.e("back", "back");
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);

		oldmoney = 0f;
	}
}
