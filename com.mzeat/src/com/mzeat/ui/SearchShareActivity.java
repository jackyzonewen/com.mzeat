package com.mzeat.ui;

import java.util.LinkedList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.model.Page;
import com.mzeat.model.Share;
import com.mzeat.model.ShareItem;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.ShareListAdapter;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchShareActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	private ShareListAdapter mAdapter;
	private LoadDataTask mLoadDataTask;
	private int page = 1;
	private ImageButton btn_back;
	private ImageView img;
	private TextView tv_title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seachstore);
		Intent intent = getIntent();
		tag = intent.getStringExtra("searchcontent");
		mListView = (PullToRefreshListView) findViewById(R.id.shopping_list);
		mListView.setOnRefreshListener(RefreshListener);

		mListView.setOnItemClickListener(mItemClickListener);

		mAdapter = new ShareListAdapter(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.microshare);
		findViewById(R.id.cb_near).setVisibility(View.GONE);
		img = (ImageView) findViewById(R.id.searchNothing);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tag = getIntent().getStringExtra("searchcontent");
		loaddata();

	}

	LinkedList<ShareItem> mItems = new LinkedList<ShareItem>();
	private TaskAdapter mTaskListener = new TaskAdapter() {
		// ProgressDialog pg;

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

					mItems = mLoadDataTask.getShareItems();

					mAdapter.setDataList(mItems);
					mListView.setAdapter(mAdapter);

					page = page + 1;
				} else {

					mItems = mLoadDataTask.getShareItems();
					mAdapter.notifyDataSetChanged();
					page = page + 1;
				}

				// ShowToast.showGetSuccess(FoodActivity.this);
			} else if (result == TaskResult.FAILED) {
				//img.setVisibility(View.VISIBLE);
				//mListView.setVisibility(View.GONE);
				// ShowToast.showGetFaile(SearchStoreActivity.this);
				ShowToast.showMessage(SearchShareActivity.this, "没有相关微分享，请输入其他搜索内容。");

			} else if (result == TaskResult.IO_ERROR) {
				ShowToast.showMessage(SearchShareActivity.this, "没有相关微分享，请输入其他搜索内容。");
			} else if (result == TaskResult.NO_MORE_DATA) {
				ShowToast.showNomoredata(SearchShareActivity.this);
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

	private LinkedList<ShareItem> mShareItems = new LinkedList<ShareItem>();
	private Page mPage = null;
	private String tag = "";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			Share mShare = new Share();
			// TODO Auto-generated method stub

			Log.e("page", String.valueOf(page));
			if (mPage == null) {

				mShare = MzeatApplication.getInstance().getService()
						.getShareList(String.valueOf(page),tag);
				int code = Integer.valueOf(mShare.getOpen()) ;
				if (code == MzeatService.RESULT_OK) {
					mShareItems = (LinkedList<ShareItem>) mShare.getItem();
					mPage = (Page) mShare.getPage();
					return TaskResult.OK;
				} else if (code == MzeatService.RESULT_FAILE) {
					return TaskResult.FAILED;
				} else {
					return TaskResult.IO_ERROR;
				}
			} else {

				if (page <= Integer.valueOf(mPage.getPage_total())) {
					mShare = MzeatApplication.getInstance().getService()
							.getShareList(String.valueOf(page),tag);
					int code = Integer.valueOf(mShare.getOpen()) ;
					if (code == MzeatService.RESULT_OK) {
						mShareItems = (LinkedList<ShareItem>) mShare.getItem();
						mPage = (Page) mShare.getPage();
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

		private LinkedList<ShareItem> getShareItems() {
			return mShareItems;
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
					.checkNetworkConnection(SearchShareActivity.this)) {

				loaddata();

			} else {
				ShowToast.showToastShort(SearchShareActivity.this,
						R.string.your_network_has_disconnected);
				mListView.onRefreshComplete();
			}

		}
	};

	ListView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (CheckNetworkConnection
					.checkNetworkConnection(SearchShareActivity.this)) {
				mListView.setSelected(true);
				Intent intent = new Intent(SearchShareActivity.this,
						ShareDetailActivity.class);
				intent.putExtra("share_id", mItems.get(position - 1)
						.getShare_id());
				startActivity(intent);

			} else {
				ShowToast.showToastShort(SearchShareActivity.this,
						R.string.your_network_has_disconnected);
			}
		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("onDestroy()", "onDestroy()");
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == LoadDataTask.Status.RUNNING) {
			mLoadDataTask.cancel(true);
		}

		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask = null;
				Log.e("cancel", "cancel");
				mListView.onRefreshComplete();
				return true;
			}

		}
		// return true;
		return super.onKeyDown(keyCode, event);
	}

}
