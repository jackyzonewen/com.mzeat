package com.mzeat.ui;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.db.My_shareDb;
import com.mzeat.db.U_commentlist_itemDb;
import com.mzeat.model.My_share;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.ui.adapter.My_commentAdapter;
import com.mzeat.ui.adapter.My_shareAdapter;
import com.mzeat.ui.widget.MyListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MessageActivity extends BaseActivity {

	private MyListView lv_my_share;
	private MyListView lv_my_comment;
	private TextView tv_title;

	private ArrayList<My_share> my_shares;
	private ArrayList<U_commentlist_item> u_commentlist_items;
	private My_shareDb mShareDb;
	private U_commentlist_itemDb mCommentlist_itemDb;
	private My_shareAdapter mShareAdapter;
	private My_commentAdapter mCommentAdapter;

	MyReceiver receiver;
	PullToRefreshScrollView sl_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		initView();
		setViewData();

	}

	private void setViewData() {
		// TODO Auto-generated method stub

		mShareDb = new My_shareDb(this);
		my_shares = mShareDb.getMy_share();
		mShareDb.closeDB();
		if (my_shares.size() > 0) {
			mShareAdapter = new My_shareAdapter(this);
			mShareAdapter.setDataList(my_shares);
			lv_my_share.setAdapter(mShareAdapter);
			lv_my_share.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String share_id = my_shares.get(position).getShare_id();
					Intent intent = new Intent(MessageActivity.this,
							ShareDetailActivity.class);
					intent.putExtra("share_id", share_id);
					startActivity(intent);
					mShareDb = new My_shareDb(MessageActivity.this);
					mShareDb.delete(share_id);
					mShareDb.closeDB();
					int count = MzeatApplication.getInstance()
							.getpPreferencesConfig().getInt("count", 0);
					if (count != 0) {
						count = count - 1;
						MzeatApplication.getInstance().getpPreferencesConfig()
								.setInt("count", count);
					}
				}
			});
		} else {
			mShareAdapter = new My_shareAdapter(this);
			mShareAdapter.clear();
			lv_my_share.setAdapter(mShareAdapter);
		}

		mCommentlist_itemDb = new U_commentlist_itemDb(this);
		u_commentlist_items = mCommentlist_itemDb.getItems();
		mCommentlist_itemDb.closeDB();
		if (u_commentlist_items.size() > 0) {
			mCommentAdapter = new My_commentAdapter(this);
			mCommentAdapter.setDataList(u_commentlist_items);
			lv_my_comment.setAdapter(mCommentAdapter);
			lv_my_comment.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String share_id = u_commentlist_items.get(position).getShare_id();
					String comment_id = u_commentlist_items.get(position).getComment_id();

					Intent intent = new Intent(MessageActivity.this,
							ShareDetailActivity.class);
					intent.putExtra("share_id", share_id);
					intent.putExtra("comment_id", comment_id);
					startActivity(intent);
					mCommentlist_itemDb = new U_commentlist_itemDb(MessageActivity.this);
					mCommentlist_itemDb.delete(comment_id);
					mCommentlist_itemDb.closeDB();
					int count = MzeatApplication.getInstance()
							.getpPreferencesConfig().getInt("count", 0);
					if (count != 0) {
						count = count - 1;
						MzeatApplication.getInstance().getpPreferencesConfig()
								.setInt("count", count);
					}
				}
			});
		} else {
			mCommentAdapter = new My_commentAdapter(this);
			mCommentAdapter.clear();
			lv_my_comment.setAdapter(mCommentAdapter);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		sl_message = (PullToRefreshScrollView) findViewById(R.id.sl_message);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.message);
		
		lv_my_share = (MyListView) findViewById(R.id.lv_my_share);
		lv_my_comment = (MyListView) findViewById(R.id.lv_my_comment);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setViewData();
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.setViewData");
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}

	public class MyReceiver extends BroadcastReceiver {

		// 自定义一个广播接收器

		@Override
		public void onReceive(Context context, Intent intent) {

			// TODO Auto-generated method stub

			Log.e("	setViewData()", "setViewData()");
			setViewData();
		}

		public MyReceiver() {
			// 构造函数，做一些初始化工作，本例中无任何作用
		}

	}
}
