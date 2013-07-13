package com.mzeat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.InviteDb;
import com.mzeat.model.Invite;
import com.mzeat.model.InviteReturn;
import com.mzeat.model.MyOrderItem;
import com.mzeat.model.Page;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.InviteListAdapter;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.ShowToast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class InviteActivity extends BaseActivity {

	private NetworkChange networkChange;
	private PullToRefreshListView mListView;
	private InviteListAdapter mAdapter;

	private LoadDataTask mLoadDataTask;

	private int load_result = 0;
	public int load_fromnetwork_success = 1;
	public int load_fromdb_success = 2;

	private boolean save_local = false;
	private InviteDb mDb;

	private ImageButton btn_back;

	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);

		mListView = (PullToRefreshListView) findViewById(R.id.invite_list);
		mListView.setOnRefreshListener(RefreshListener);
		mAdapter = new InviteListAdapter(this);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.title_invite);

	}

	ArrayList<Invite> mInvites = new ArrayList<Invite>();
	private TaskAdapter mTaskListener = new TaskAdapter() {
		ProgressDialog pg;

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			mListView.setRefreshing(true);
			mListView.setDisableScrollingWhileRefreshing(true);

		}

		public void onPostExecute(GenericTask task, TaskResult result) {

			mListView.onRefreshComplete();
			// Page mPage = new Page();
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {

				if (page == 1) {

					mInvites = mLoadDataTask.getInvite();
					
					mAdapter.setDataList(mInvites);
					mAdapter.notifyDataSetChanged();
					mListView.setAdapter(mAdapter);
					load_result = load_fromnetwork_success;

					if (!save_local) {
						mDb = new InviteDb(InviteActivity.this);
						mDb.add(mInvites);
						mDb.closeDB();
					}

					page = page + 1;

					mLoadDataTask = null;
				} else {

					page = page + 1;

					mInvites.addAll(mLoadDataTask.getInvite());
					mAdapter.notifyDataSetChanged();
					mLoadDataTask = null;
				}

				// ShowToast.showGetSuccess(FoodActivity.this);
			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showGetFaile(InviteActivity.this);

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(InviteActivity.this);
			} else if (result == TaskResult.NO_MORE_DATA) {
				mLoadDataTask = null;
				ShowToast.showNomoredata(InviteActivity.this);
			} else if (result == TaskResult.NOT_FOLLOWED_ERROR) {
				ShowToast.showNoOrder(InviteActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

			mListView.onRefreshComplete();

		}
	};

	private int page = 1;
	private ArrayList<Invite> invites = new ArrayList<Invite>();
	private Page mPage = null;
	private InviteReturn result = new InviteReturn();

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub
			if (mPage == null) {
				result = MzeatApplication.getInstance().getService()
						.getInviteReturn(String.valueOf(page));
				int code = Integer.valueOf(result.getOpen());
				if (code == MzeatService.RESULT_OK) {
					invites = (ArrayList<Invite>) result.getItem();
					Log.e("invite", String.valueOf(invites.size()));
					mPage = (Page) result.getPage();
					return TaskResult.OK;
				} else if (code == MzeatService.RESULT_FAILE) {
					return TaskResult.FAILED;
				} else if (code == MzeatService.RESULT_NODATA) {
					return TaskResult.NOT_FOLLOWED_ERROR;
				} else {
					return TaskResult.IO_ERROR;
				}
			} else {

				if (page <= Integer.valueOf(mPage.getPage_total())) {
					result = MzeatApplication.getInstance().getService()
							.getInviteReturn(String.valueOf(page));
					int code = Integer.valueOf(result.getOpen());
					if (code == MzeatService.RESULT_OK) {
						invites = (ArrayList<Invite>) result.getItem();

						mPage = (Page) result.getPage();
						return TaskResult.OK;
					} else if (code == MzeatService.RESULT_FAILE) {
						return TaskResult.FAILED;
					} else {
						return TaskResult.IO_ERROR;
					}
				} else {
					return TaskResult.NO_MORE_DATA;
				}

			}

		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		private ArrayList<Invite> getInvite() {
			return invites;
		}
	}

	private void loaddata() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.setListener(mTaskListener);
		try {
			mLoadDataTask.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private PullToRefreshBase.OnRefreshListener<ListView> RefreshListener = new PullToRefreshBase.OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {

			if (CheckNetworkConnection
					.checkNetworkConnection(InviteActivity.this)) {
				loaddata();

			} else {
				ShowToast.showToastShort(InviteActivity.this,
						R.string.your_network_has_disconnected);
				mListView.onRefreshComplete();
			}

		}

	};

	public class NetworkChange extends ConnectionChangeReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			if (super.success == true) {

				if (load_result != load_fromnetwork_success) {
					getOldData();
					loaddata();

					super.success = false;
				}

			} else {
				if (load_result == 0) {
					getOldData();
				}
			}
		}

	}

	private void getOldData() { // mShoppings = new ArrayList<Shopping>();
		mDb = new InviteDb(this);
		mInvites = mDb.getInvite();
		mDb.closeDB();
		if (mInvites.size() != 0) {
			mAdapter.setDataList(mInvites);
			mListView.setAdapter(mAdapter);
			load_result = load_fromdb_success;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// Log.e("onDestroy()", "onDestroy()");
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == LoadDataTask.Status.RUNNING) {
			mLoadDataTask.cancel(true);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		networkChange = new NetworkChange();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkChange, filter);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			unregisterReceiver(networkChange);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.e("food onSaveInstanceState", "food onSaveInstanceState");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask = null;
				mListView.onRefreshComplete();
				return true;
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
