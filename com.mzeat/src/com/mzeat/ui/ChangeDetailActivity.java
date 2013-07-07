package com.mzeat.ui;


import com.mzeat.R;
import com.mzeat.image.BitmapManager;
import com.mzeat.model.Change;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangeDetailActivity extends BaseActivity {
	private BitmapManager 				bmpManager;
	private String imgurl;

	private Change mItem;
	private TextView change_title;

	private ImageView img_change;
	private TextView tv_jf;
	private TextView tv_title;
	private TextView tv_content;

	private ImageButton back;
	private ImageButton btn_change;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changedetaill);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));
		Intent intent = getIntent();
		mItem = (Change) intent
				.getSerializableExtra(ChangeActivity.SER_KEY);
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		// findViewById(R.id.cb_near).setVisibility(View.GONE);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.changestore);
		
		change_title = (TextView) findViewById(R.id.change_title);
		tv_jf = (TextView) findViewById(R.id.tv_jf);
		img_change = (ImageView) findViewById(R.id.img_change);
		tv_content = (TextView) findViewById(R.id.tv_content);

		if (mItem != null) {
			change_title.setText(mItem.getTitle());
			tv_jf.setText(mItem.getScore());
			tv_content.setText(mItem.getContent());
			imgurl = mItem.getCount_image();

			if (imgurl.equals("")) {
				img_change.setImageResource(R.drawable.empty_image);
			} else {
				
				bmpManager.loadBitmap(imgurl,img_change, BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));


			}

		}
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_change = (ImageButton) findViewById(R.id.btn_change);
		
	}


}
