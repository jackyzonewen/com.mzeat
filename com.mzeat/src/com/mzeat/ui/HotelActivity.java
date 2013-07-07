package com.mzeat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.FoodDb;
import com.mzeat.db.HotelDb;
import com.mzeat.model.Page;
import com.mzeat.model.Shopping;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.GenerateListViewWithImagesBaseAdapter;
import com.mzeat.ui.adapter.ShoppingAdapter;
import com.mzeat.ui.adapter.ShoppingNearAdapter;
import com.mzeat.ui.widget.CustomViewPager;
import com.mzeat.ui.widget.CustomViewpage;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.ShowToast;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class HotelActivity extends BaseActivity {

	private CustomViewPager pager;
	private ArrayList<View> pageViews;

	private NetworkChange networkChange;
	private PullToRefreshListView mListView;
	private GenerateListViewWithImagesBaseAdapter<Shopping> mAdapter;

	private PullToRefreshListView nListView;
	private GenerateListViewWithImagesBaseAdapter<Shopping> nAdapter;

	private LoadDataTask mLoadDataTask;

	private int load_result = 0;
	public int load_fromnetwork_success = 1;
	public int load_fromdb_success = 2;

	private boolean save_local = false;
	//private FoodDb mFoodDb;
	private HotelDb mHotelDb;
	public final static String SER_KEY = "hotel";

	private ImageButton btn_back;
	// private ImageButton near;

	private CheckBox cb_sort;

	private int checkwhat = check_normal;
	private static final int check_near = 1;
	private static final int check_normal = 2;
	private int near_page = 1;
	private int normal_page = 1;
	ArrayList<Shopping> data_near = new ArrayList<Shopping>();
	ArrayList<Shopping> data_normal = new ArrayList<Shopping>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food);
		
	//	pager = new CustomViewpage(this);
		pager =  (CustomViewPager) findViewById(R.id.vp_contains);
		pager.setPagingEnabled(false);
		Init();
		pager.setAdapter(new myPagerView());

		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {

					//if (mLoadDataTask != null) {
					//	cb_sort.setChecked(true);
					//	checkwhat = check_near;
					//}else {
						cb_sort.setChecked(false);
					//	checkwhat = check_normal;
					//}
					if (mLoadDataTask != null) {
						mLoadDataTask.setCancelable(true);
						mLoadDataTask.setListener(null);
						mLoadDataTask = null;
						mListView.onRefreshComplete();
						nListView.onRefreshComplete();
					}
					
				} else {
					//if (mLoadDataTask != null) {
					//	cb_sort.setChecked(false);
					//	checkwhat = check_normal;
					//}else {
						cb_sort.setChecked(true);
						checkwhat = check_near;
					//}
					
					if (mLoadDataTask != null) {
						mLoadDataTask.setCancelable(true);
						mLoadDataTask.setListener(null);
						mLoadDataTask = null;
						nListView.onRefreshComplete();
						mListView.onRefreshComplete();
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				/**if (mLoadDataTask != null) {
					mLoadDataTask.setCancelable(true);
					mLoadDataTask.setListener(null);
					mLoadDataTask = null;
					nListView.onRefreshComplete();
					mListView.onRefreshComplete();
				}**/
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				/**if (mLoadDataTask != null) {
					mLoadDataTask.setCancelable(true);
					mLoadDataTask.setListener(null);
					mLoadDataTask = null;
					nListView.onRefreshComplete();
					mListView.onRefreshComplete();
				}**/
			}
		});

		//mListView = (PullToRefreshListView) findViewById(R.id.shopping_list);
		mListView.setOnRefreshListener(RefreshListener);
		mListView.setOnItemClickListener(mItemClickListener);
		mAdapter = new ShoppingAdapter(this);

		//nListView = (PullToRefreshListView) findViewById(R.id.shopping_list);
		nListView.setOnRefreshListener(RefreshListener);
		nListView.setOnItemClickListener(mItemClickListener);
		nAdapter = new ShoppingNearAdapter(this);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Log.e("TEST","FINISH");
				Intent intent = new Intent(HotelActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * near = (ImageButton) findViewById(R.id.btn_near);
		 * 
		 * near.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 *           stub
		 * 
		 * 
		 *           } });
		 **/
		cb_sort = (CheckBox) findViewById(R.id.cb_near);
		cb_sort.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					pager.setCurrentItem(1);
					checkwhat = check_near;
					listgps = "1";
					//nListView.setAdapter(nAdapter);
					// nListView.setVisibility(View.VISIBLE);
					// mListView.setVisibility(View.GONE);
					if (data_near.size() == 0) {

						if (!MzeatApplication.getInstance().lat.equals("")) {
							Latitude = String.valueOf(MzeatApplication
									.getInstance().lat);
							Longitude = String.valueOf(MzeatApplication
									.getInstance().lon);

							if (CheckNetworkConnection
									.checkNetworkConnection(HotelActivity.this)) {

								if (null != mLoadDataTask
										&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
									ShowToast.showToastShort(HotelActivity.this,
											R.string.loadding_data);
									return;
								}

								loaddata();

							} else {
								ShowToast.showToastShort(HotelActivity.this,
										R.string.your_network_has_disconnected);
							}
						} else {
							ShowToast.showToastShort(HotelActivity.this,
									R.string.get_your_location);
						}

					} else {
						// nAdapter.clear();
						// nAdapter.addDatas(data_near);
						nAdapter.notifyDataSetChanged();
						// mListView.setAdapter(nAdapter);

						// mListView.notifyAll();
					}

				} else {
					pager.setCurrentItem(0);
					checkwhat = check_normal;
					listgps = "";
					//mListView.setAdapter(mAdapter);
					// mAdapter = new ShoppingAdapter(FoodActivity.this);
					// nListView.setVisibility(View.GONE);
					// mListView.setVisibility(View.VISIBLE);
					if (data_normal.size() == 0) {
						if (!MzeatApplication.getInstance().lat.equals("")) {
							Latitude = String.valueOf(MzeatApplication
									.getInstance().lat);
							Longitude = String.valueOf(MzeatApplication
									.getInstance().lon);

							if (CheckNetworkConnection
									.checkNetworkConnection(HotelActivity.this)) {

								if (null != mLoadDataTask
										&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
									ShowToast.showToastShort(HotelActivity.this,
											R.string.loadding_data);
									return;
								}
								loaddata();

							} else {
								ShowToast.showToastShort(HotelActivity.this,
										R.string.your_network_has_disconnected);
							}
						} else {
							ShowToast.showToastShort(HotelActivity.this,
									R.string.get_your_location);
						}

					} else {
						// mAdapter.clear();
						// mAdapter.addDatas(data_normal);
						mAdapter.notifyDataSetChanged();
						// mListView.setAdapter(mAdapter);

						// mListView.notifyAll();
					}

				}
			}
		});

	}

	/***
	 * init title and pageview
	 */
	void Init() {
		pageViews = new ArrayList<View>();

		mListView = (PullToRefreshListView) LayoutInflater.from(this).inflate(
				R.layout.listview_food, null);
		nListView = (PullToRefreshListView) LayoutInflater.from(this).inflate(
				R.layout.listview_food, null);

		pageViews.add(mListView);
		pageViews.add(nListView);

	}

	/***
	 * viewpager 的数据源
	 * 
	 * @author zhangjia
	 * 
	 */
	class myPagerView extends PagerAdapter {
		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		/***
		 * 获取每一个item， 类于listview中的getview
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

	}

	ArrayList<Shopping> shoppings = new ArrayList<Shopping>();
	private TaskAdapter mTaskListener = new TaskAdapter() {
		ProgressDialog pg;

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			switch (checkwhat) {
			case check_near:
				nListView.setRefreshing(true);
				nListView.setDisableScrollingWhileRefreshing(true);
				break;
			case check_normal:
				mListView.setRefreshing(true);
				mListView.setDisableScrollingWhileRefreshing(true);
				break;
			default:
				break;
			}

		}

		public void onPostExecute(GenericTask task, TaskResult result) {

			switch (checkwhat) {
			case check_near:
				nListView.onRefreshComplete();
				break;
			case check_normal:
				mListView.onRefreshComplete();
				break;
			default:
				break;
			}
			// Page mPage = new Page();
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {

				if (page == 1) {

					switch (checkwhat) {
					case check_near:
						data_near = mLoadDataTask.getShoppings();
						nAdapter.setDataList(data_near);
						nAdapter.notifyDataSetChanged();
						nListView.setAdapter(nAdapter);
						near_page = near_page + 1;
						break;
					case check_normal:

						data_normal = mLoadDataTask.getShoppings();
						mAdapter.setDataList(data_normal);
						normal_page = normal_page + 1;
						mAdapter.notifyDataSetChanged();
						mListView.setAdapter(mAdapter);
						load_result = load_fromnetwork_success;
						if (!save_local) {
							 mHotelDb = new  HotelDb(HotelActivity.this);
							 mHotelDb.add(data_normal);
							 mHotelDb.closeDB();
						}
						break;
					default:
						break;
					}

					// shoppings = mLoadDataTask.getShoppings();

					// mAdapter.setDataList(shoppings);
					// mListView.setAdapter(mAdapter);

					// page = page + 1;

					mLoadDataTask = null;
				} else {

					// page = page + 1;
					switch (checkwhat) {
					case check_near:

						data_near.addAll(mLoadDataTask.getShoppings());

						nAdapter.notifyDataSetChanged();

						near_page = near_page + 1;
						// mListView.setAdapter(nAdapter);
						break;
					case check_normal:

						data_normal.addAll(mLoadDataTask.getShoppings());

						mAdapter.notifyDataSetChanged();
						// mListView.setAdapter(mAdapter);
						normal_page = normal_page + 1;
						break;
					default:
						break;
					}
					mLoadDataTask = null;
				}

				// ShowToast.showGetSuccess(FoodActivity.this);
			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showGetFaile(HotelActivity.this);

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(HotelActivity.this);
			} else if (result == TaskResult.NO_MORE_DATA) {
				mLoadDataTask = null;
				ShowToast.showNomoredata(HotelActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

			switch (checkwhat) {
			case check_near:
				nListView.onRefreshComplete();
				break;
			case check_normal:
				mListView.onRefreshComplete();
				break;
			default:
				break;
			}

		}
	};

	private String act = "merchantlist";
	private int page = 1;
	private String r_type = "1";
	private String cate_id = "11";
	private Map<String, Object> result = new HashMap<String, Object>();
	private ArrayList<Shopping> mShoppings = new ArrayList<Shopping>();
	private Page mPage = null;
	private String Latitude = "";
	private String Longitude = "";
	private String keyword = "";
	private String listgps = "";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			MzeatService mService = new MzeatService();
			// TODO Auto-generated method stub

			switch (checkwhat) {
			case check_near:
				page = near_page;
				listgps = "1";
				break;
			case check_normal:
				page = normal_page;
				listgps = "";
				break;
			default:
				break;
			}
			// Log.e("page", String.valueOf(page));
			if (mPage == null) {
				result = MzeatApplication
						.getInstance()
						.getService()
						.getShoppingList(act, r_type, String.valueOf(page),
								cate_id, Longitude, Latitude, listgps, keyword);
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
					result = MzeatApplication
							.getInstance()
							.getService()
							.getShoppingList(act, r_type, String.valueOf(page),
									cate_id, Longitude, Latitude, listgps,
									keyword);
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

			switch (checkwhat) {
			case check_near:
				if (CheckNetworkConnection
						.checkNetworkConnection(HotelActivity.this)) {
					if (!MzeatApplication.getInstance().lat.equals("")) {
						Latitude = String.valueOf(MzeatApplication
								.getInstance().lat);
						Longitude = String.valueOf(MzeatApplication
								.getInstance().lon);
						loaddata();

					} else {
						ShowToast.showToastShort(HotelActivity.this,
								R.string.get_your_location);
						nListView.onRefreshComplete();
					}
				} else {
					ShowToast.showToastShort(HotelActivity.this,
							R.string.your_network_has_disconnected);
					nListView.onRefreshComplete();
				}
				break;
			case check_normal:
				if (CheckNetworkConnection
						.checkNetworkConnection(HotelActivity.this)) {
					if (!MzeatApplication.getInstance().lat.equals("")) {
						Latitude = String.valueOf(MzeatApplication
								.getInstance().lat);
						Longitude = String.valueOf(MzeatApplication
								.getInstance().lon);
						loaddata();

					} else {
						ShowToast.showToastShort(HotelActivity.this,
								R.string.get_your_location);
						mListView.onRefreshComplete();
					}
				} else {
					ShowToast.showToastShort(HotelActivity.this,
							R.string.your_network_has_disconnected);
					mListView.onRefreshComplete();
				}

				break;
			default:
				break;
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

					if (!MzeatApplication.getInstance().lat.equals("")) {
						Log.e("networkchange lat",
								MzeatApplication.getInstance().lat);
						Latitude = MzeatApplication.getInstance().lat;
						Longitude = MzeatApplication.getInstance().lon;
						checkwhat = check_normal;
						loaddata();

					} else {
						ShowToast.showToastShort(HotelActivity.this,
								R.string.get_your_location);

					}

					super.success = false;
					// Log.e("loaddata()", "loaddata()");
				}

			} else {
				if (load_result == 0) {
					getOldFood();
					// Log.e("getOldFood()", "getOldFood()");
				}
			}
		}

	}

	private void getOldFood() {
		// mShoppings = new ArrayList<Shopping>();
		 mHotelDb = new  HotelDb(this);
		 data_normal = mHotelDb.getShoppings();
		 mHotelDb.closeDB();
		if (data_normal.size() != 0) {
			mAdapter.setDataList(data_normal);
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
		Log.e("food onDestroy", "food onDestroy");
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
		Log.e("food onResume", "food onResume");

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("food onPause()", "food onPause()");
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
				Log.e("cancel", "cancel");
				switch (checkwhat) {
				case check_near:
					nListView.onRefreshComplete();
					break;
				case check_normal:
					mListView.onRefreshComplete();
					break;
				default:
					break;
				}

				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(HotelActivity.this, MainActivity.class);
			startActivity(intent);
		}

		return true;
		// return super.onKeyDown(keyCode, event);
	}

	private static final int fromfood = 3;
	ListView.OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch (checkwhat) {
			case check_near:
				if (data_near != null && data_near.size() > 0) {
					Intent intent = new Intent(HotelActivity.this,
							ShoppingDetailActivity.class);
					Shopping mShopping = data_near.get(position - 1);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable(SER_KEY, mShopping);
					intent.putExtras(mBundle);
					intent.putExtra("fromfood", fromfood);
					startActivity(intent);

				}
				break;
			case check_normal:

				if (data_normal != null && data_normal.size() > 0) {
					Intent intent = new Intent(HotelActivity.this,
							ShoppingDetailActivity.class);
					Shopping mShopping = data_normal.get(position - 1);
					Bundle mBundle = new Bundle();
					mBundle.putSerializable(SER_KEY, mShopping);
					intent.putExtras(mBundle);
					intent.putExtra("fromfood", fromfood);
					startActivity(intent);

				}
				break;
			default:

				break;
			}

		}

	};

}
