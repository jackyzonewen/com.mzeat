package com.mzeat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.model.Page;
import com.mzeat.model.Shopping;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.GenerateListViewWithImagesBaseAdapter;
import com.mzeat.ui.adapter.ShoppingAdapter;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class SearchStoreActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	private GenerateListViewWithImagesBaseAdapter<Shopping> mAdapter;
	private LoadDataTask mLoadDataTask;
	private int page = 1;
	private Location location = null;
	private ImageButton btn_back;
	public final static String SER_KEY = "search";
	private MzeatService mService = new MzeatService();
	private ImageView img;

	
	private ImageButton near;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seachstore);
		Intent intent = getIntent();
		keyword = intent.getStringExtra("searchcontent");
		Latitude = intent.getStringExtra("Latitude");
		Longitude = intent.getStringExtra("Longitude");
		mListView = (PullToRefreshListView) findViewById(R.id.shopping_list);
		mListView.setOnRefreshListener(RefreshListener);

		mListView.setOnItemClickListener(mItemClickListener);

		mAdapter = new ShoppingAdapter(this);
		
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

		loaddata();

	}

	ArrayList<Shopping> shoppings = new ArrayList<Shopping>();
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

					shoppings = mLoadDataTask.getShoppings();

					mAdapter.setDataList(shoppings);
					mListView.setAdapter(mAdapter);

					page = page + 1;
				} else {

					shoppings.addAll(mLoadDataTask.getShoppings());
					mAdapter.notifyDataSetChanged();
					page = page + 1;
				}

				// ShowToast.showGetSuccess(FoodActivity.this);
			} else if (result == TaskResult.FAILED) {
				//img.setVisibility(View.VISIBLE);
				//mListView.setVisibility(View.GONE);
				// ShowToast.showGetFaile(SearchStoreActivity.this);
				ShowToast.showMessage(SearchStoreActivity.this, "没有相关商家，请输入其他搜索内容。");

			} else if (result == TaskResult.IO_ERROR) {
				ShowToast.showMessage(SearchStoreActivity.this, "没有相关商家，请输入其他搜索内容	。");
			} else if (result == TaskResult.NO_MORE_DATA) {
				ShowToast.showNomoredata(SearchStoreActivity.this);
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

	private String act = "merchantlist";
	private String r_type = "1";
	private String cate_id = "";
	private Map<String, Object> result = new HashMap<String, Object>();
	private ArrayList<Shopping> mShoppings = new ArrayList<Shopping>();
	private Page mPage = null;
	private String Latitude = "";
	private String Longitude = "";
	private String listgps = "";
	private String keyword = "";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			MzeatService mService = new MzeatService();
			// TODO Auto-generated method stub

			Log.e("page", String.valueOf(page));
			if (mPage == null) {

				result = mService.getShoppingList(act, r_type,
						String.valueOf(page), cate_id, Longitude, Latitude,
						listgps, keyword);
				int code = (Integer) result.get("code");
				if (code == MzeatService.RESULT_OK) {
					mShoppings = (ArrayList<Shopping>) result.get("item");
					mPage = (Page) result.get("page");
					return TaskResult.OK;
				} else if (code == MzeatService.RESULT_FAILE) {
					return TaskResult.FAILED;
				} else {
					return TaskResult.IO_ERROR;
				}
			} else {

				if (page <= Integer.valueOf(mPage.getPage_total())) {
					result = mService.getShoppingList(act, r_type,
							String.valueOf(page), cate_id, Longitude, Latitude,
							listgps, keyword);
					int code = (Integer) result.get("code");
					if (code == MzeatService.RESULT_OK) {
						mShoppings = (ArrayList<Shopping>) result.get("item");
						mPage = (Page) result.get("page");
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

		private ArrayList<Shopping> getShoppings() {
			return mShoppings;
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
					.checkNetworkConnection(SearchStoreActivity.this)) {

				loaddata();

			} else {
				ShowToast.showToastShort(SearchStoreActivity.this,
						R.string.your_network_has_disconnected);
				mListView.onRefreshComplete();
			}

		}
	};

	private static final int fromSearch = 5;
	ListView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (shoppings != null && shoppings.size() > 0) {
				Intent intent = new Intent(SearchStoreActivity.this,
						ShoppingDetailActivity.class);
				Shopping mShopping = shoppings.get(position - 1);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(SER_KEY, mShopping);
				intent.putExtras(mBundle);
				intent.putExtra("fromfood", fromSearch);
				startActivity(intent);

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
