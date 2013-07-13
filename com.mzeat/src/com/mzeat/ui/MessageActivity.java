package com.mzeat.ui;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mzeat.AppManager;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MsgService;
import com.mzeat.db.My_shareDb;
import com.mzeat.db.U_commentlist_itemDb;
import com.mzeat.model.My_share;
import com.mzeat.model.U_commentlist;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.My_commentAdapter;
import com.mzeat.ui.adapter.My_shareAdapter;
import com.mzeat.ui.widget.MyListView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MessageActivity extends Activity {

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
	LinearLayout ll_tips;
	LinearLayout ll_gotoshare;
	ScrollView mScrollView;

	private LoadDataTask mLoadDataTask;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		initView();
		// setViewData();

	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 结束Activity&从堆栈中移除
				AppManager.getAppManager().finishActivity(this);
	}
	private void setViewData() {
		// TODO Auto-generated method stub
		boolean isNoMessage = false;
		mShareDb = new My_shareDb(this);
		my_shares = mShareDb.getMy_share();
		mShareDb.closeDB();
		if (my_shares.size() > 0) {

			ll_tips.setVisibility(View.GONE);

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
					count = MzeatApplication.getInstance()
							.getpPreferencesConfig().getInt("count", 0);
					if (count != 0) {
						count = count - 1;
						MzeatApplication.getInstance().getpPreferencesConfig()
								.setInt("count", count);
					}

					Intent mIntent = new Intent();
					mIntent.putExtra("count", count);
					mIntent.setAction("android.intent.action.setTextView");
					sendBroadcast(mIntent);
				}
			});
		} else {

			isNoMessage = true;

			mShareAdapter = new My_shareAdapter(this);
			mShareAdapter.clear();
			lv_my_share.setAdapter(mShareAdapter);
		}

		mCommentlist_itemDb = new U_commentlist_itemDb(this);
		u_commentlist_items = mCommentlist_itemDb.getItems();
		mCommentlist_itemDb.closeDB();
		if (u_commentlist_items.size() > 0) {

			ll_tips.setVisibility(View.GONE);

			mCommentAdapter = new My_commentAdapter(this);
			mCommentAdapter.setDataList(u_commentlist_items);
			lv_my_comment.setAdapter(mCommentAdapter);
			lv_my_comment.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String share_id = u_commentlist_items.get(position)
							.getShare_id();
					String comment_id = u_commentlist_items.get(position)
							.getComment_id();

					Intent intent = new Intent(MessageActivity.this,
							ShareDetailActivity.class);
					intent.putExtra("share_id", share_id);
					intent.putExtra("comment_id", comment_id);
					startActivity(intent);
					mCommentlist_itemDb = new U_commentlist_itemDb(
							MessageActivity.this);
					mCommentlist_itemDb.delete(comment_id);
					mCommentlist_itemDb.closeDB();
					count = MzeatApplication.getInstance()
							.getpPreferencesConfig().getInt("count", 0);
					if (count != 0) {
						count = count - 1;
						MzeatApplication.getInstance().getpPreferencesConfig()
								.setInt("count", count);

					}

					Intent mIntent = new Intent();
					mIntent.putExtra("count", count);
					mIntent.setAction("android.intent.action.setTextView");
					sendBroadcast(mIntent);
				}
			});
		} else {
			if (isNoMessage) {
				ll_tips.setVisibility(View.VISIBLE);

			}
			mCommentAdapter = new My_commentAdapter(this);
			mCommentAdapter.clear();
			lv_my_comment.setAdapter(mCommentAdapter);
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_back).setVisibility(View.GONE);
		sl_message = (PullToRefreshScrollView) findViewById(R.id.sl_message);
		sl_message.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				getMsg();
			}
		});
		mScrollView = sl_message.getRefreshableView();

		ll_tips = (LinearLayout) findViewById(R.id.ll_tips);

		ll_gotoshare = (LinearLayout) findViewById(R.id.ll_gotoshare);
		ll_gotoshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MessageActivity.this, ShareActivity.class);
				startActivity(intent);
			}
		});

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.message);

		lv_my_share = (MyListView) findViewById(R.id.lv_my_share);
		lv_my_comment = (MyListView) findViewById(R.id.lv_my_comment);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask.setListener(null);
				mLoadDataTask = null;
				sl_message.onRefreshComplete();
			} else {
				Intent MyIntent = new Intent(Intent.ACTION_MAIN);
				MyIntent.addCategory(Intent.CATEGORY_HOME);
				startActivity(MyIntent);
			}
			return true;
			// Log.e("back", "back");
		}
		
		//if (keyCode == KeyEvent.KEYCODE_MENU) {
		//	super.onKeyDown(keyCode, event);
		//}
		return super.onKeyDown(keyCode, event);
		//
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

	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			sl_message.setRefreshing(true);
			sl_message.setDisableScrollingWhileRefreshing(true);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				sl_message.onRefreshComplete();

				count = Integer.valueOf(u_commentlist.getTotal());

				int oldCount = MzeatApplication.getInstance()
						.getpPreferencesConfig().getInt("count", 0);
				if (count != 0) {
					// 当客户端本地消息数为0
					if (oldCount == 0) {
						sendNotice();
					} else // 当客户端本地消息数不为0
					{
						// 当本地消息数与请求消息数不等
						if (count != oldCount) {
							sendNotice();
						} else {// 当本地消息数与请求消息数相等

							My_shareDb my_shareDb = new My_shareDb(
									MessageActivity.this);
							ArrayList<My_share> oldMy_shares = my_shareDb
									.getMy_share();
							ArrayList<My_share> newMy_shares = u_commentlist
									.getMy_share();
							my_shareDb.closeDB();

							// 先比较我的分享的评论
							if (oldMy_shares.size() != newMy_shares.size()) {
								sendNotice();
							} else {
								boolean sendnotice = oldMy_shares
										.containsAll(newMy_shares);
								if (!sendnotice) {
									sendNotice();
								} else { // 再比较我的评论的回复

									U_commentlist_itemDb u_commentlist_itemDb = new U_commentlist_itemDb(
											MessageActivity.this);
									ArrayList<U_commentlist_item> oldItems = u_commentlist_itemDb
											.getItems();
									ArrayList<U_commentlist_item> newItems = u_commentlist
											.getItem();
									u_commentlist_itemDb.closeDB();

									boolean itemequal = oldItems
											.containsAll(newItems);
									if (!itemequal) {
										sendNotice();
									}
								}
							}
						}
					}

				} else {
					sendNotice();
				}

			} else if (result == TaskResult.FAILED) {
				sl_message.onRefreshComplete();

			} else {
				sl_message.onRefreshComplete();

			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};

	private void sendNotice() {
		MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("count", count);

		My_shareDb my_shareDb = new My_shareDb(MessageActivity.this);
		if (my_shareDb.getMy_share() != null
				&& my_shareDb.getMy_share().size() > 0) {
			my_shareDb.deleteAll();
		}

		if (u_commentlist.getMy_share() != null) {
			my_shareDb.add(u_commentlist.getMy_share());
		}

		my_shareDb.closeDB();

		U_commentlist_itemDb u_commentlist_itemDb = new U_commentlist_itemDb(
				MessageActivity.this);

		if (u_commentlist_itemDb.getItems() != null
				&& u_commentlist_itemDb.getItems().size() > 0) {
			u_commentlist_itemDb.deleteAll();
		}

		if (u_commentlist.getItem() != null) {
			u_commentlist_itemDb.add(u_commentlist.getItem());
		}

		u_commentlist_itemDb.closeDB();

		// 刷新主页面的消息数
		Intent intent = new Intent();
		intent.putExtra("count", count);
		intent.setAction("android.intent.action.setTextView");
		// intent.setAction("android.intent.action.setViewData");// action与接收器相同
		sendBroadcast(intent);

		// 刷新消息界面
		Intent mIntent = new Intent();
		mIntent.setAction("android.intent.action.setViewData");// action与接收器相同
		sendBroadcast(mIntent);

		Log.e("sendnotice", "sendnotice");
	}

	U_commentlist u_commentlist;

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			u_commentlist = MzeatApplication
					.getInstance()
					.getService()
					.getU_commentlist(
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("email", ""),
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("pwd", ""));

			if (u_commentlist.getOpen().equals("1")) {
				return TaskResult.OK;
			} else if (u_commentlist.getOpen().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

	}

	private void getMsg() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.setListener(mTaskListener);

		mLoadDataTask.execute();

	}
}
