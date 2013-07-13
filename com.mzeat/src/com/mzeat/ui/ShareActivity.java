package com.mzeat.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.ShareItemDb;
import com.mzeat.model.Page;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Share;
import com.mzeat.model.ShareItem;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.ShareListAdapter;
import com.mzeat.ui.widget.CustomListView;
import com.mzeat.ui.widget.CustomListView.OnUpdateListListener;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.ShowToast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends BaseActivity {
	private Handler handler;
	private NetworkChange networkChange;
	private ShareListAdapter mAdapter;

	private LoadDataTask mLoadDataTask;

	private int load_result = 0;
	public int load_fromnetwork_success = 1;
	public int load_fromdb_success = 2;

	private boolean save_local = false;

	public final static String SER_KEY = "privilege";
	private TextView tv_title;
	private CustomListView mListView;
	float density;

	public static int STATE_REFLASH = 1;
	public static int STATE_LOADMORE = 2;
	public int STATE = 0;

	private ShareItemDb mDb;

	private ImageButton btn_pubshare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		handler = new Handler();
		initView();
		setViewData();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		density = dm.density;
		MzeatApplication.getInstance().getpPreferencesConfig()
				.setFloat("density", density);
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mListView = (CustomListView) findViewById(R.id.list_share);

		mAdapter = new ShareListAdapter(this);
		mListView.setAdapter(mAdapter, handler);
		mListView.setonRefreshListener(new MyOnRefreshListener());
		mListView.setOnItemClickListener(mItemClickListener);
		btn_pubshare = (ImageButton) findViewById(R.id.btn_pubshare);
		btn_pubshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if (MzeatApplication.getInstance().getpPreferencesConfig()
						.getInt("loginstate", 0) == 1) {
					intent = new Intent(ShareActivity.this,
							PubShareActivity.class);
					startActivityForResult(intent, 1);

				} else {
					intent = new Intent(ShareActivity.this, LoginActivity.class);
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromsharelist", 1);
					ShowToast.showMessage(ShareActivity.this, "请您先登录再发分享。");
					startActivity(intent);

				}
			}
		});
	}

	private void setViewData() {
		tv_title.setText(R.string.microshare);
	}

	ProgressDialog pg;

	LinkedList<ShareItem> mShareItem = new LinkedList<ShareItem>();
	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			if (STATE == 0) {
				pg = ProgressDialog
						.show(ShareActivity.this,
								getString(R.string.dialog_tips),
								getString(R.string.loading), true, true,
								cancelListener);
			}

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			if (STATE == 0) {
				pg.dismiss();
				pg = null;
			}
			mListView.onRefreshComplete();
			mListView.setIsRefreshable(true);
			mListView.setClickable(true);
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {

				if (page == 1 && STATE == 0) {

					mShareItem = mLoadDataTask.getShareItem();
					mAdapter.setDataList(mShareItem);
					mListView.setAdapter(mAdapter, handler);
					load_result = load_fromnetwork_success;
					mLoadDataTask = null;
				}
				

				if (page == 1) {
					if (!save_local) {
						mDb = new ShareItemDb(ShareActivity.this);
						mDb.add(mShareItem);
						mDb.closeDB();
						save_local = true;
					}
				}
				switch (STATE) {
				case 1:
					LinkedList<ShareItem> reflashdata = new LinkedList<ShareItem>();

					reflashdata = mLoadDataTask.getShareItem();
					//Log.e("reflashdata", String.valueOf(reflashdata.size()));
					/**
					 * Collections.sort(reflashdata, new Comparator<ShareItem>()
					 * { public int compare(ShareItem arg0, ShareItem arg1) {
					 * return arg0.getShare_id().compareTo( arg1.getShare_id());
					 * } });
					 **/
					if (mShareItem.size() > 0) {
						
						mShareItem.clear();
						mShareItem.addAll(reflashdata);
						ShowToast.showMessage(ShareActivity.this, "更新成功");
						/**
						int osize = mShareItem.size();
						int nsize = reflashdata.size();
						Log.e("osize", String.valueOf(osize));
						Log.e("nsize", String.valueOf(nsize));

						for (int i = 0; i < reflashdata.size(); i++) {
							boolean add = false;
							String nid = reflashdata.get(i).getShare_id();
							Log.e("nid", nid);
							for (int j = 0; j < mShareItem.size(); j++) {
								String oid = mShareItem.get(j).getShare_id();
								Log.e("oid", oid);
								if (oid.equals(nid)) {

									add = false;
									break;
								} else {
									add = true;

								}

							}
							if (add) {
								mShareItem.addFirst(reflashdata.get(i));
							}

						}

						if (osize == mShareItem.size()) {
							Toast.makeText(ShareActivity.this, "暂无更新，休息一会儿",
									Toast.LENGTH_SHORT).show();

						} else {

							int updatenum = mShareItem.size() - osize;
							Toast.makeText(ShareActivity.this,
									"更新了" + updatenum + "条微分享",
									Toast.LENGTH_SHORT).show();
						}
						**/
						load_result = load_fromnetwork_success;

					} else {
						mShareItem.addAll(reflashdata);
						mAdapter.setDataList(mShareItem);
						mListView.setAdapter(mAdapter, handler);
						load_result = load_fromnetwork_success;

					}

					// mAdapter.notifyDataSetChanged();

					break;
				case 2:
					LinkedList<ShareItem> loadmoredata = new LinkedList<ShareItem>();

					loadmoredata = mLoadDataTask.getShareItem();

					/**
					 * Collections.sort(loadmoredata, new
					 * Comparator<ShareItem>() { public int compare(ShareItem
					 * arg0, ShareItem arg1) { return
					 * arg0.getShare_id().compareTo( arg1.getShare_id()); } });
					 **/

					if (mShareItem.size() > 0) {
						int _osize = loadmoredata.size();
						int _nsize = mShareItem.size();
						//Log.e("_osize", String.valueOf(_osize));
						//Log.e("_osize", String.valueOf(_nsize));
						for (int i = 0; i < loadmoredata.size(); i++) {
							boolean add = false;
							String nid = loadmoredata.get(i).getShare_id();

							for (int j = 0; j < mShareItem.size(); j++) {
								String oid = mShareItem.get(j).getShare_id();
								if (oid.equals(nid)) {
									add = false;
									break;
								} else {
									add = true;

								}

							}

							if (add) {
								mShareItem.addLast(loadmoredata.get(i));
							}

						}

						if (_nsize == mShareItem.size()) {
							Toast.makeText(ShareActivity.this, "暂无更多，休息一会儿",
									Toast.LENGTH_SHORT).show();

						} else {
							// "加载了" + loadmorenum + "条微分享"
							int loadmorenum = mShareItem.size() - _nsize;
							Toast.makeText(ShareActivity.this, "加载成功",
									Toast.LENGTH_SHORT).show();
						}
						load_result = load_fromnetwork_success;
					} else {
						mShareItem.addAll(loadmoredata);
						mAdapter.setDataList(mShareItem);
						mListView.setAdapter(mAdapter, handler);
						load_result = load_fromnetwork_success;
					}
					loadmore_page = loadmore_page + 1;

					break;
				default:
					break;
				}

				mAdapter.notifyDataSetChanged();
				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showGetFaile(ShareActivity.this);

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(ShareActivity.this);
			} else if (result == TaskResult.NO_MORE_DATA) {
				mLoadDataTask = null;
				ShowToast.showNomoredata(ShareActivity.this);
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
	DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			clearTask();
		}

	};

	private void clearTask() {
		// TODO Auto-generated method stub
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
			mLoadDataTask.cancel(true);
			mLoadDataTask = null;
		}

	}

	private int reflash_page = 1;
	private int loadmore_page = 2;
	private int page = 1;
	private LinkedList<ShareItem> shareItem = new LinkedList<ShareItem>();
	private Page mPage = null;
	private String tag = "";
	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub
			Share share = new Share();
			if (mPage == null) {

				switch (STATE) {
				case 1:
					page = reflash_page;
					break;
				case 2:
					page = loadmore_page;
					break;
				default:
					break;
				}

				share = MzeatApplication.getInstance().getService()
						.getShareList(String.valueOf(page),tag);
				int code = Integer.valueOf(share.getOpen());
				if (code == MzeatService.RESULT_OK) {

					shareItem = (LinkedList<ShareItem>) share.getItem();
					mPage = (Page) share.getPage();
					return TaskResult.OK;
				} else if (code == MzeatService.RESULT_FAILE) {
					return TaskResult.FAILED;
				} else {
					return TaskResult.IO_ERROR;
				}
			} else {

				switch (STATE) {
				case 1:
					page = reflash_page;
					break;
				case 2:
					page = loadmore_page;
					break;
				default:
					break;
				}

				if (page <= Integer.valueOf(mPage.getPage_total())) {
					share = MzeatApplication.getInstance().getService()
							.getShareList(String.valueOf(page),tag);
					int code = Integer.valueOf(share.getOpen());
					if (code == MzeatService.RESULT_OK) {
						shareItem = (LinkedList<ShareItem>) share.getItem();
						mPage = (Page) share.getPage();
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
			//super.onCancelled();
		}

		private LinkedList<ShareItem> getShareItem() {
			return shareItem;
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

	public class NetworkChange extends ConnectionChangeReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			if (super.success == true) {

				if (load_result != load_fromnetwork_success) {
					//getOldData();
					loaddata();

					super.success = false;
				}

			} else {
				if (load_result == 0) {
					//getOldData();
				}
			}
		}

	}

	private void getOldData() {
		mDb = new ShareItemDb(this);
		mShareItem = mDb.getItem();
		mDb.closeDB();
		if (mShareItem.size() != 0) {
			mAdapter.setDataList(mShareItem);
			mListView.setAdapter(mAdapter, handler);
			mListView.setonRefreshListener(new MyOnRefreshListener());
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask.setListener(null);
				mLoadDataTask = null;
				mListView.onRefreshComplete();
				mListView.setIsRefreshable(true);
				mListView.setClickable(true);
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
		
	}

	ListView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			if (CheckNetworkConnection
					.checkNetworkConnection(ShareActivity.this)) {
				mListView.setSelected(true);
				Intent intent = new Intent(ShareActivity.this,
						ShareDetailActivity.class);
				intent.putExtra("share_id", mShareItem.get(position - 1)
						.getShare_id());
				startActivity(intent);

			} else {
				ShowToast.showToastShort(ShareActivity.this,
						R.string.your_network_has_disconnected);
			}

		}

	};

	class MyOnRefreshListener implements OnUpdateListListener {

		@Override
		public void onRefresh() {
			STATE = STATE_REFLASH;
			mListView.setIsRefreshable(false);
			loaddata();

		}

		@Override
		public void onLoadMore() {
			STATE = STATE_LOADMORE;
			mListView.setClickable(false);
			loaddata();

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode  == 1) {
			STATE = STATE_REFLASH;
			mListView.setIsRefreshable(false);
			loaddata();
		}
	}
}
