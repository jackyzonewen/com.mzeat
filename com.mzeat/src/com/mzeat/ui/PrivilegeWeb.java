package com.mzeat.ui;


import com.mzeat.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PrivilegeWeb extends BaseActivity{

	private TextView tv_title;
	private WebView wv;
	private String context;
	private int formwhere;
	
	private TextView title_invite;
	private TextView tv_create_time;
	private RelativeLayout rl_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privilegeweb);
		initView();
		setViewData();
		
	}

	private void setViewData() {
		// TODO Auto-generated method stub
		
		Intent intent = getIntent();
		formwhere = intent.getIntExtra("from", 1);
		switch (formwhere) {
		case 1:
			tv_title.setText(R.string.privilege);
			break;
		case 2:
			tv_title.setText(R.string.sale);
			rl_content.setVisibility(View.VISIBLE);
			title_invite.setText(intent.getStringExtra("title"));
			tv_create_time.setText(intent.getStringExtra("create_time"));
			break;
		default:
			break;
		}
		context = intent.getStringExtra("context");
		wv.loadDataWithBaseURL(null, context,
				"text/html", "utf-8", null);
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		title_invite  = (TextView) findViewById(R.id.title_invite);
		tv_create_time  = (TextView) findViewById(R.id.tv_create_time);
		rl_content =(RelativeLayout) findViewById(R.id.rl_content);
		
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		wv = (WebView) findViewById(R.id.wv_detail_privilege);
		WebSettings webSettings= wv.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}
}
