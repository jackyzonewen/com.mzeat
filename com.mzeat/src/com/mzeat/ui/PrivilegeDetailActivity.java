package com.mzeat.ui;


import com.baidu.location.LocationClient;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mzeat.AppManager;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.MzeatApplication.MyGeneralListener;
import com.mzeat.api.MzeatService;
import com.mzeat.db.MycartDb;
import com.mzeat.image.BitmapManager;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageFetcher;
import com.mzeat.image.ImageResizer;
import com.mzeat.image.ImageCache.ImageCacheParams;
import com.mzeat.location.BaiduLocationOption;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PrivilegeDetailActivity extends BaseActivity {
	private BitmapManager bmpManager;
	private String imgurl;

	private PrivilegeItem mItem;
	private TextView tv_title;

	private ImageView img;
	private TextView title;
	private TextView nowprice;
	private TextView oldprice;
	private TextView privilege;
	private TextView save;
	private TextView home;
	private TextView open;
	private TextView lefttime;
	private TextView tel;
	private TextView address;
	private TextView distance;

	private ImageButton back;
	private ImageButton buy;
	private RelativeLayout rl_route;
	private RelativeLayout rl;
	private RelativeLayout rl_phone;
	private LocationClient mLocClient = null;

	private ImageResizer mImageWorker;
	private ImageCache mImageCache;
	// private AsyncImageView mImageView;

	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	public final static String SER_KEY = "route";

	private MycartDb mDb;
	private static final int FROM_Privilege = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privilegedetail);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));
		Intent intent = getIntent();
		mItem = (PrivilegeItem) intent
				.getSerializableExtra(PrivilegeActivity.SER_KEY);
		Log.e("mitem", mItem.getAddress());
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		// findViewById(R.id.cb_near).setVisibility(View.GONE);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.privilege);
		img = (ImageView) findViewById(R.id.img_privilege);
		title = (TextView) findViewById(R.id.privilege_title);
		nowprice = (TextView) findViewById(R.id.money);
		oldprice = (TextView) findViewById(R.id.tv_price);
		privilege = (TextView) findViewById(R.id.tv_cutdown);
		save = (TextView) findViewById(R.id.tv_save);
		home = (TextView) findViewById(R.id.tv_home);
		open = (TextView) findViewById(R.id.tv_open);
		tel = (TextView) findViewById(R.id.tv_phone);
		address = (TextView) findViewById(R.id.tv_address);
		distance = (TextView) findViewById(R.id.tv_location);
		lefttime = (TextView) findViewById(R.id.tv_lefttime);
		open = (TextView) findViewById(R.id.tv_open);

		if (mItem != null) {
			title.setText(Html.fromHtml(mItem.getGoods_brief()));
			privilege.setText(mItem.getDiscount());
			tel.setText(mItem.getSp_tel());
			address.setText(mItem.getAddress());
			distance.setText(StringUtils.formatDistance(mItem.getDistance()));
			save.setText(mItem.getSaving_format());
			nowprice.setText(mItem.getCur_price());
			oldprice.setText(mItem.getOri_price_format());
			home.setText(mItem.getSp_detail());
			lefttime.setText(mItem.getLess_time());
			open.setText(mItem.getSp_open_times());
			imgurl = mItem.getCount_image();

			if (imgurl.equals("")) {
				img.setImageResource(R.drawable.empty_image);
			} else {
				
				bmpManager.loadBitmap(imgurl,img, BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));

/**
				mImageWorker = new ImageFetcher(PrivilegeDetailActivity.this,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// mImageWorker.setAdapter(imageThumbWorkerUrlsAdapter);
				ImageCacheParams cacheParams = new ImageCacheParams(
						ImageCache.IMAGE_DIR);
				mImageWorker.setLoadingImage(R.drawable.empty_image);
				mImageCache = new ImageCache(PrivilegeDetailActivity.this,
						cacheParams);
				mImageWorker.setImageCache(mImageCache);
				mImageWorker.loadImage(imgurl, img);
				Log.e("imgw", String.valueOf(img.getWidth()));
				Log.e("imgh", String.valueOf(img.getHeight()));
				**/
			}

		}
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("FINISH", "FINISH");
				finish();
			}
		});

		buy = (ImageButton) findViewById(R.id.btn_buy);

		int timestate = Integer.valueOf(mItem.getTime_status());
		int buystate = Integer.valueOf(mItem.getBuy_status());
		switch (timestate) {
		case 0:
			buy.setBackgroundResource(R.drawable.tuan_start);
			buy.setClickable(false);
			break;
		case 1:
			if (buystate == 2) {
				buy.setBackgroundResource(R.drawable.tuan_last_end);
				buy.setClickable(false);
			} else {
				buy.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int islogin = MzeatApplication.getInstance()
								.getpPreferencesConfig()
								.getInt("loginstate", 0);
						if (islogin == 0) {
							Intent intent = new Intent(
									PrivilegeDetailActivity.this,
									LoginActivity.class);
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.setInt("fromprivilege", 1);
							startActivity(intent);
						} else {

							mDb = new MycartDb(PrivilegeDetailActivity.this);
							PrivilegeItem isExitItem = mDb.getSelectItem(
									MzeatApplication.getInstance()
											.getpPreferencesConfig()
											.getString("email", ""),
									mItem.getGoods_id());
							if (isExitItem.getGoods_id() == null) {
								mItem.setNum("1");
								mDb.add(mItem);
								mDb.closeDB();
								
							} else {
								
								isExitItem.setNum(String.valueOf(Integer.valueOf(isExitItem.getNum()) + 1));
								mDb.updateCart(isExitItem);
								mDb.closeDB();
								
							}
							
							
							AlertDialog.Builder ad = new AlertDialog.Builder(PrivilegeDetailActivity.this);
							ad.setTitle("提示");
							ad.setMessage("成功加入购物车");
							ad.setPositiveButton("继续购物", new DialogInterface.OnClickListener() {// 退出按钮
										@Override
										public void onClick(DialogInterface dialog, int i) {
											
										}
									});
							ad.setNegativeButton("查看购物车", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int i) {
									Intent intent = new Intent(
											PrivilegeDetailActivity.this,
											MainActivity.class);
									MzeatApplication.getInstance()
											.getpPreferencesConfig()
											.setInt("tomyorder", 1);
									startActivity(intent);
								}
							});
							ad.show();// 显示对话框

						}
					}
				});
			}
			break;
		case 2:
			buy.setBackgroundResource(R.drawable.tuan_end);
			buy.setClickable(false);
			break;
		default:
			break;
		}

		rl_phone = (RelativeLayout) findViewById(R.id.rl_phone);
		if (!mItem.getSp_tel().equals("")) {
			rl_phone.setClickable(true);
			rl_phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						String inputStr = tel.getText().toString();
						Log.e("call", "call");
						if (StringUtils.isMobile(inputStr) == true
								|| StringUtils.isPhone(inputStr) == true) {
							Intent myIntentDial = new Intent(
									Intent.ACTION_DIAL, Uri.parse("tel:"
											+ inputStr));

							startActivity(myIntentDial);
							PrivilegeDetailActivity.this
									.overridePendingTransition(
											R.anim.slide_right_in,
											R.anim.slide_left_out);
						} else {
							ShowToast.showToastShort(
									PrivilegeDetailActivity.this,
									"无法获取电话，请你手动拨打！");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e.getMessage());
					}
				}
			});
		}

		rl = (RelativeLayout) findViewById(R.id.storeintroduce);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PrivilegeDetailActivity.this,
						PrivilegeWeb.class);
				intent.putExtra("context", mItem.getGoods_desc());
				intent.putExtra("from", FROM_Privilege);
				startActivity(intent);
			}
		});

		rl_route = (RelativeLayout) findViewById(R.id.rl_distance);
		rl_route.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("MzeatApplication.getInstance().init_result",
						String.valueOf(MzeatApplication.getInstance().mBMapManager
								.init(MzeatApplication.strKey,
										new MzeatApplication.MyGeneralListener())));
				// 首先判断商家距离是否准确。
				if (StringUtils.checkDistance(mItem.getDistance())) {
					// 其次判断网络是否连接
					if (CheckNetworkConnection
							.checkNetworkConnection(PrivilegeDetailActivity.this)) {

						Intent intent = new Intent(
								PrivilegeDetailActivity.this,
								RouteActivity.class);
						MzeatApplication.getInstance();
						Log.e("MzeatApplication.getInstance().init_result",
								String.valueOf(MzeatApplication.init_result));

						// 再次判断是否定位，百度地图是否初始化。
						if (!MzeatApplication.getInstance().lat.equals("")
								&& MzeatApplication.init_result) {

							intent.putExtra("st_Latitude",
									MzeatApplication.getInstance().lat);
							Log.e("st_Latitude",
									MzeatApplication.getInstance().lat);
							intent.putExtra("st_Longitude",
									MzeatApplication.getInstance().lon);
							intent.putExtra("en_Latitude",
									String.valueOf(mItem.getYpoint()));
							intent.putExtra("en_Longitude",
									String.valueOf(mItem.getXpoint()));
							startActivity(intent);

						} else {

							ShowToast.showToastShort(
									PrivilegeDetailActivity.this,
									R.string.get_your_location);
							do {
								MzeatApplication.getInstance().mBMapManager
										.init(MzeatApplication.strKey,
												new MyGeneralListener());
							} while (!MzeatApplication.init_result);

						}

					} else {
						ShowToast.showToastShort(PrivilegeDetailActivity.this,
								R.string.your_network_has_disconnected);
					}
				} else {
					ShowToast.showToastShort(PrivilegeDetailActivity.this,
							R.string.routestorefaile);
				}

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocClient = MzeatApplication.getInstance().mLocationClient;
		if (mLocClient == null) {
			mLocClient
					.registerLocationListener(MzeatApplication.getInstance().myListener);
			mLocClient.setLocOption(BaiduLocationOption
					.getOption(PrivilegeDetailActivity.this));
			if (!mLocClient.isStarted()) {
				mLocClient.start();
				Log.e("shoppingdetailmlocclient", "start");
			}

		}

		MzeatApplication.getInstance().mBMapManager.init(
				MzeatApplication.strKey, new MKGeneralListener() {

					@Override
					public void onGetPermissionState(int iError) {
						// TODO Auto-generated method stub
						if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
							Log.e("iError", String.valueOf(iError));
							Toast.makeText(
									MzeatApplication.getInstance()
											.getApplicationContext(),
									"您的网络出错啦！", Toast.LENGTH_LONG).show();

						} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
							Toast.makeText(
									MzeatApplication.getInstance()
											.getApplicationContext(),
									"输入正确的检索条件！", Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onGetNetworkState(int arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLocClient
				.unRegisterLocationListener(MzeatApplication.getInstance().myListener);
		mLocClient.stop();
		Log.e("shoppingdetailmlocclient", "stop");
	}
}
