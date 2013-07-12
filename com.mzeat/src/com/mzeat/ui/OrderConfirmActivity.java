package com.mzeat.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mzeat.R;
import com.mzeat.db.UserDb;
import com.mzeat.model.ConfirmOrderItem;
import com.mzeat.model.Shopping;
import com.mzeat.model.User;
import com.mzeat.ui.adapter.ConfirmOrderAdapter;
import com.mzeat.ui.widget.MyListView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OrderConfirmActivity extends BaseActivity {
	
	
	private TextView tv_title;
	
	private MyListView lv_cart;
	private EditText et_note;
	private RadioGroup rg_paytype;
	private RadioButton rd_zhifubao;
	private TextView tv_all_product_count;
	private TextView tv_should_pay;
	private TextView tv_jifen_change;
	private EditText et_edit_mobile;
	private ImageButton btn_contact;
	private ArrayList<ConfirmOrderItem> orderItems = new ArrayList<ConfirmOrderItem>();
	private String total_count;
	
	
	private ConfirmOrderAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmorder);
		
		Intent intent = getIntent();
		orderItems = (ArrayList<ConfirmOrderItem>) intent
				.getSerializableExtra(ShopCartActivity.SER_KEY);
		total_count = intent.getStringExtra("total_count");
		mAdapter = new ConfirmOrderAdapter(this);
		mAdapter.setDataList(orderItems);
		initView();
		setViewData();
	}
	
	private void initView(){
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		lv_cart =  (MyListView) findViewById(R.id.lv_cart);
		
		et_note = (EditText) findViewById(R.id.et_note);
		
		rg_paytype = (RadioGroup) findViewById(R.id.rg_paytype);
		rg_paytype.setOnCheckedChangeListener(checkedChangeListener);
		rd_zhifubao = (RadioButton) findViewById(R.id.rd_zhifubao);
		rd_zhifubao.setChecked(true);
		
		tv_all_product_count = (TextView) findViewById(R.id.tv_all_product_count);
		tv_should_pay = (TextView) findViewById(R.id.tv_should_pay);
		tv_jifen_change = (TextView) findViewById(R.id.tv_jifen_change);
		
		et_edit_mobile = (EditText) findViewById(R.id.et_edit_mobile);
		
		btn_contact = (ImageButton) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject object = new JSONObject();
				JSONArray ja = new JSONArray();

				for (int i = 0; i < orderItems.size(); i++) {
					ja.put(orderItems.get(i).getJSONObject());
				}
				try {
					object.put("item", ja);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				try {
					object.put("total_count",total_count);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					object.put("order_note",et_note.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					object.put("mobile",et_edit_mobile.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					object.put("pay_type",pay_type);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Log.e("jsonarray", object.toString());
			}
		});
		
		
	
	}
	
	private void setViewData(){
		
		tv_title.setText("提交订单");
		lv_cart.setAdapter(mAdapter);
		
		tv_all_product_count.setText("￥"+total_count);
		tv_should_pay.setText("￥"+total_count);
		
		UserDb userDb = new UserDb(this);
		User user = userDb.getUser();
		if (user.getMobile() != null) {
			et_edit_mobile.setText(user.getMobile());
		}
		
		
	}
	private String pay_type = "20"; 
	
	
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rd_zhifubao:
				pay_type = "20";
				break;
			case R.id.rd_leftmoney:
				pay_type = "15";
				break;
			default:
				break;
			}
		}
		
	};
	
	
}
