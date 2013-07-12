package com.mzeat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.PosterDb;
import com.mzeat.image.BitmapManager;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageFetcher;
import com.mzeat.image.ImageResizer;
import com.mzeat.image.ImageCache.ImageCacheParams;
import com.mzeat.image.PosterManager;
import com.mzeat.location.BaiduLocationOption;
import com.mzeat.model.Advs;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.GroupAdapter;
import com.mzeat.ui.adapter.ImageFragmentAdapter;
import com.mzeat.ui.widget.CirclePageIndicator;
import com.mzeat.ui.widget.MulitPointTouchListener;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;

public class IndexActivity extends BaseActivity {

	private Spinner sp_searchType;
	private ArrayAdapter arrayAdapter_SearchType;

	private ViewPager mPager;
	private CirclePageIndicator indicator;
	private ImageFragmentAdapter mAdapter;
	private LoadDataTask mLoadDataTask;

	private NetworkChange networkChange;

	private PosterDb mPosterDb;

	private ImageButton btn_shopping;
	private ImageButton btn_privilege;
	private ImageButton btn_sale;
	private ImageButton btn_coupon;
	private ImageButton btn_mircoshare;
	private ImageButton btn_shop;
	private ImageButton btn_change;
	private ImageButton btn_invite;

	private EditText et_search;
	private Button btn_search;
	private int searchtype = 0;

	private LinearLayout ll_poster;
	private int load_result = 0;
	public static final int load_fromnetwork_success = 1;
	public static final int load_fromdb_success = 2;


	private LocationClient mLocClient = null;

	private PopupWindow popupWindow;
	private ListView lv_group;
	private View view;
	private List<String> groups;
	private TextView tv_search_group;
	private ArrayList<String> list;

	
	private PosterManager bmpManager;
	private InputMethodManager imm;

	// BDLocation mBdLocation = mLocClient.get;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.bmpManager = new PosterManager(BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		mPager = (ViewPager) findViewById(R.id.home_pager);
		indicator = (CirclePageIndicator) findViewById(R.id.home_indicator);
		ll_poster = (LinearLayout) findViewById(R.id.poster);
		//sp_searchType = (Spinner) findViewById(R.id.sp_searchtype);
		// arrayAdapter_SearchType = ArrayAdapter.createFromResource(this,
		// R.array.searchtype, R.layout.spinnerdown);
		//arrayAdapter_SearchType.setDropDownViewResource(R.layout.spinnerdown);

		list = new ArrayList<String>();
		list.add("商家");
		list.add("团购");
		list.add("分享");

		//arrayAdapter_SearchType = new ArrayAdapter<String>(this,
		//		R.layout.spinner, list);
		//sp_searchType.setAdapter(arrayAdapter_SearchType);
		//sp_searchType.setOnItemSelectedListener(new OnItemSelectedListener() {

		//	@Override
		//	public void onItemSelected(AdapterView<?> arg0, View arg1,
		//			int arg2, long arg3) {
				// TODO Auto-generated method stub
		//		switch (arg2) {
		//		case 0:
		//			searchtype = 0;
		//			break;

		//		default:
		//			break;
		//		}
		//	}

		//	@Override
		//	public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

		//	}

	//	});

		btn_shopping = (ImageButton) findViewById(R.id.btn_shopping);
		btn_privilege = (ImageButton) findViewById(R.id.btn_privilege);
		btn_sale = (ImageButton) findViewById(R.id.btn_sale);
		btn_coupon = (ImageButton) findViewById(R.id.btn_coupon);
		btn_mircoshare = (ImageButton) findViewById(R.id.btn_microshare);
		btn_shop = (ImageButton) findViewById(R.id.btn_shop);
		btn_change = (ImageButton) findViewById(R.id.btn_change);
		btn_invite = (ImageButton) findViewById(R.id.btn_invite);

		mOnClickListener clickListener = new mOnClickListener();
		btn_shopping.setOnClickListener(clickListener);
		btn_privilege.setOnClickListener(clickListener);
		btn_sale.setOnClickListener(clickListener);
		btn_coupon.setOnClickListener(clickListener);
		btn_mircoshare.setOnClickListener(clickListener);
		btn_shop.setOnClickListener(clickListener);
		btn_change.setOnClickListener(clickListener);
		btn_invite.setOnClickListener(clickListener);

		et_search = (EditText) findViewById(R.id.et_search);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 
				String searchcontent = et_search.getText().toString().trim();
				if (!StringUtils.isEmpty(searchcontent)) {
					if (CheckNetworkConnection
							.checkNetworkConnection(IndexActivity.this)) {

						switch (searchtype) {
						case 0:

							if (!MzeatApplication.getInstance().lat.equals("")) {
								Intent intent = new Intent(IndexActivity.this,
										SearchStoreActivity.class);
								intent.putExtra("searchcontent", searchcontent);
								intent.putExtra("Latitude",
										MzeatApplication.getInstance().lat);
								intent.putExtra("Longitude",
										MzeatApplication.getInstance().lon);
								startActivity(intent);
								et_search.setText("");
							}

							else {
								ShowToast.showToastShort(IndexActivity.this,
										R.string.get_your_location);

							}
							break;
						case 1: 

							if (!MzeatApplication.getInstance().lat.equals("")) {
								Intent intent = new Intent(IndexActivity.this,
										PrivilegeActivity.class);
								intent.putExtra("searchcontent", searchcontent);
								intent.putExtra("Latitude",
										MzeatApplication.getInstance().lat);
								intent.putExtra("Longitude",
										MzeatApplication.getInstance().lon);
								startActivity(intent);
								et_search.setText("");
							}

							else {
								ShowToast.showToastShort(IndexActivity.this,
										R.string.get_your_location);

							}
							break;
						case 2:
							if (CheckNetworkConnection.checkNetworkConnection(IndexActivity.this)) {
								Intent intent = new Intent(IndexActivity.this,
										SearchShareActivity.class);
								intent.putExtra("searchcontent", searchcontent);
								startActivity(intent);
								et_search.setText("");
							}else {
								ShowToast.showMessage(IndexActivity.this, R.string.your_network_has_disconnected);
							}
							
						default:
							break;
						}

					} else {
						ShowToast.showToastShort(IndexActivity.this,
								R.string.your_network_has_disconnected);
					}

				} else {
					ShowToast.showToastShort(IndexActivity.this,
							R.string.searchcontent);
				}
			}
		});

		tv_search_group = (TextView) findViewById(R.id.tv_search_group);

		tv_search_group.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showWindow(v);
			}
		});
	}

	/**
	 * 显示
	 * 
	 * @param parent
	 */
	private void showWindow(View parent) {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = layoutInflater.inflate(R.layout.group_list, null);

			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			// 加载数据
			groups = new ArrayList<String>();
			groups.add("搜商家");
			groups.add("搜团购");
			groups.add("搜分享");

			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, 200, LayoutParams.WRAP_CONTENT);
		}

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;

		Log.i("coder", "windowManager.getDefaultDisplay().getWidth()/2:"
				+ windowManager.getDefaultDisplay().getWidth() / 2);
		//
		Log.i("coder", "popupWindow.getWidth()/2:" + popupWindow.getWidth() / 2);

		Log.i("coder", "xPos:" + xPos);

		popupWindow.showAsDropDown(parent, 0, 0);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				searchtype = position;
				tv_search_group.setText(list.get(position));
				if (popupWindow != null) {
					popupWindow.dismiss();
				}

			}
		});

	}

	private ArrayList<View> pageViews = new ArrayList<View>();

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mLocClient = MzeatApplication.getInstance().mLocationClient;
		if (mLocClient != null) {
			mLocClient
					.registerLocationListener(MzeatApplication.getInstance().myListener);
			mLocClient.setLocOption(BaiduLocationOption
					.getOption(IndexActivity.this));
			mLocClient.start();
			Log.e("IndexActivity onResume()", "onResume()");
		}
		if (load_result != load_fromnetwork_success) {
			networkChange = new NetworkChange();
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			registerReceiver(networkChange, filter);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("IndexActivity onPause()", "onPause()");
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == LoadDataTask.Status.RUNNING)
			mLoadDataTask.cancel(true);
		if (networkChange != null) {
			try {
				unregisterReceiver(networkChange);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
			
		

		if (mLocClient.isStarted()) {
			mLocClient.unRegisterLocationListener(MzeatApplication
					.getInstance().myListener);
			mLocClient.stop();
			Log.e("IndexActivity onPause()", "onPause()");
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("IndexActivity onDestroy()", "onDestroy()");
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == LoadDataTask.Status.RUNNING)
			mLoadDataTask.cancel(true);
		mLoadDataTask = null;
		super.onDestroy();
	}

	private void getPoster() {
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

	private TaskAdapter mTaskListener = new TaskAdapter() {
		ProgressDialog pg;

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			// pg = ProgressDialog.show(IndexActivity.this,
			// getString(R.string.dialog_tips),
			// getString(R.string.loading));
		}
		ArrayList<Advs> advss = new ArrayList<Advs>();
		public void onPostExecute(GenericTask task, TaskResult result) {
			// pg.dismiss();
			// pg = null;
			// TODO 判断TaskReult的返回值是否ok
			ArrayList<String> urlImages = new ArrayList<String>();
			
			if (result == TaskResult.OK) {
				try {
					advss = mLoadDataTask.getResponse();

					for (int i = 0; i < advss.size(); i++) {
						urlImages.add(advss.get(i).getImg());
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				// mAdapter = new ImageFragmentAdapter(IndexActivity.this,
				// getSupportFragmentManager(), urlImages);
				int oldsize = urlImages.size();
				if (img_poster.size() != 0) {
					urlImages.retainAll(img_poster);
					if (urlImages.size() != oldsize) {
						urlImages.removeAll(img_poster);
						urlImages.addAll(img_poster);
						pageViews.clear();
						for (int i = 0; i < urlImages.size(); i++) {


							
							RelativeLayout ll = (RelativeLayout) LayoutInflater.from(
									IndexActivity.this).inflate(R.layout.img_poster, null);
							ProgressBar pb = (ProgressBar)  ll.findViewById(R.id.imagezoomdialog_progress)	;
							ImageView imgView =  (ImageView) ll.findViewById(R.id.sharephoto);
							bmpManager.loadBitmap(urlImages.get(i), imgView,pb);
							
							pageViews.add(ll);
							Log.e("getPoster", "getPoster");
						}
					}
				}else {
					pageViews.clear();
					for (int i = 0; i < urlImages.size(); i++) {


						
						RelativeLayout ll = (RelativeLayout) LayoutInflater.from(
								IndexActivity.this).inflate(R.layout.img_poster, null);
						ProgressBar pb = (ProgressBar)  ll.findViewById(R.id.imagezoomdialog_progress)	;
						ImageView imgView =  (ImageView) ll.findViewById(R.id.sharephoto);
						bmpManager.loadBitmap(urlImages.get(i), imgView,pb);
						
						pageViews.add(ll);
						 Log.e("getPoster", "getPoster");
					}
				}
				
				mPager.setAdapter(new myPagerView());
				ll_poster.setVisibility(View.GONE);
				mPager.setVisibility(View.VISIBLE);
				indicator.setViewPager(mPager);
				indicator.setSnap(true);

				mPosterDb = new PosterDb(IndexActivity.this);
				mPosterDb.add(advss);
				mPosterDb.closeDB();
				load_result = load_fromnetwork_success;
				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showToastShort(IndexActivity.this,
						R.string.posterfailetips);
			} else {
				mLoadDataTask = null;
				ShowToast.showToastShort(IndexActivity.this,
						R.string.postererrortips);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};

	private String act = "index";
	private String r_type = "1";

	/**
	 * 加载数据任务
	 * 
	 * @author windhuiyi
	 * 
	 */
	private class LoadDataTask extends GenericTask {
		ArrayList<Advs> advss = new ArrayList<Advs>();

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// MzeatApplication application = MzeatApplication.getInstance();

			Map<String, Object> result = new HashMap<String, Object>();
			MzeatService service = new MzeatService();
			result = service.getPoster(act, r_type);

			int code = (Integer) result.get("code");
			if (code == MzeatService.RESULT_OK) {
				advss = (ArrayList<Advs>) result.get("advs");
				return TaskResult.OK;
			} else if (code == 0) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}

		}

		public ArrayList<Advs> getResponse() {
			return advss;
		}

	};

	public class NetworkChange extends ConnectionChangeReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			if (super.success == true) {

				if (load_result != load_fromnetwork_success) {
					//
					if (!showposter) {
						showOldPoster();
					}
					getPoster();
					
					super.success = false;
				}

			} else {
				if (load_result == 0) {
					// Log.e("load_result", String.valueOf(load_result));
					showOldPoster();
					// Log.e("showoldposter", "showoldposter");
				}

			}
		}

	}
	boolean showposter = false;
	ArrayList<String> img_poster = new ArrayList<String>();
	private void showOldPoster() {
		
		mPosterDb = new PosterDb(IndexActivity.this);
		img_poster = mPosterDb.getImgs();
		mPosterDb.closeDB();
		if (img_poster.size() != 0) {
		

			for (int i = 0; i < img_poster.size(); i++) {

				RelativeLayout ll = (RelativeLayout) LayoutInflater.from(
						IndexActivity.this).inflate(R.layout.img_poster, null);
				ProgressBar pb = (ProgressBar)  ll.findViewById(R.id.imagezoomdialog_progress)	;
				ImageView imgView =  (ImageView) ll.findViewById(R.id.sharephoto);
				bmpManager.loadBitmap(img_poster.get(i), imgView,pb);
				
				pageViews.add(ll);
				 Log.e("showoldposter", "showoldposter");
			}

			mPager.setAdapter(new myPagerView());
			ll_poster.setVisibility(View.GONE);
			mPager.setVisibility(View.VISIBLE);
			indicator.setViewPager(mPager);
			indicator.setSnap(true);
			load_result = load_fromdb_success;
			showposter = true;
		}

	}

	private class mOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_shopping:
				// onPause();
				mLoadDataTask = null;
				intent.setClass(IndexActivity.this, ShoppingActivity.class);
				startActivity(intent);

				break;
			case R.id.btn_privilege:
				intent.setClass(IndexActivity.this, PrivilegeActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_microshare:
				intent.setClass(IndexActivity.this, ShareActivity.class);
				startActivity(intent);
				break;
				//修改了位置,变成招聘
			case R.id.btn_shop:
				intent.setClass(IndexActivity.this, InviteActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_sale:
				intent.setClass(IndexActivity.this, SaleActivity.class);
				startActivity(intent);
				break;
				//修改了位置，变成兑换
			case R.id.btn_coupon:
				intent.setClass(IndexActivity.this,ChangeActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_change:
				
				//intent.setClass(IndexActivity.this,PubShareActivity.class);
				//startActivity(intent);
				
				if (MzeatApplication.getInstance().getpPreferencesConfig()
						.getInt("loginstate", 0) == 1) {
					intent = new Intent(IndexActivity.this,
							PubShareActivity.class);
					startActivityForResult(intent, 1);

				} else {
					intent = new Intent(IndexActivity.this, LoginActivity.class);
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromsharelist", 1);
					ShowToast.showMessage(IndexActivity.this, "请您先登录再发分享。");
					startActivity(intent);

				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mLoadDataTask = null;
			Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(MyIntent);
			// Log.e("back", "back");
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
