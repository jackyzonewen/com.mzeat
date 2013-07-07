package com.mzeat.ui;

import java.io.Serializable;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.MzeatApplication.MyGeneralListener;
import com.mzeat.api.MzeatService;
import com.mzeat.image.BitmapManager;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageFetcher;
import com.mzeat.image.ImageResizer;
import com.mzeat.image.ImageCache.ImageCacheParams;
import com.mzeat.image.ImageWorker.ImageWorkerAdapter;
import com.mzeat.location.BaiduLocationOption;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingDetailActivity extends BaseActivity {

	private BitmapManager 				bmpManager;

	
	private String imgurl;

	private int load_from;
	private Shopping mShopping;

	private ImageView img;
	private TextView title;
	private TextView privilege;
	private TextView feature;
	private TextView open;
	private TextView tel;
	private TextView address;
	private TextView distance;
	private TextView comment;
	private TextView percost;
	private TextView content;
	private ImageButton back;
	private ImageButton contact;
	private RelativeLayout rl;
	private ImageView start;
	private TextView start_num;
	private boolean content_view = false;

	private RelativeLayout rl_route;
	private MzeatService service = new MzeatService();
	private Location location = null;
	private LocationClient mLocClient = null;

	private ImageResizer mImageWorker;
	private ImageCache mImageCache;
	// private AsyncImageView mImageView;

	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private String st_Latitude;
	private String st_Longitude;
	private String en_Latitude;
	private String en_Longitude;
	private GeoPoint en_gp;
	private GeoPoint st_gp;
	public final static String SER_KEY = "route";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shoppingdetail);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));

		Intent intent = getIntent();
		load_from = intent.getIntExtra("fromfood", 0);
		switch (load_from) {
		case 1:
			mShopping = (Shopping) intent
					.getSerializableExtra(FoodActivity.SER_KEY);
			break;
		case 2:
			mShopping = (Shopping) intent
					.getSerializableExtra(BuyActivity.SER_KEY);
			break;
		case 3:
			mShopping = (Shopping) intent
					.getSerializableExtra(HotelActivity.SER_KEY);
			break;
		case 4:
			mShopping = (Shopping) intent
					.getSerializableExtra(HappyActivity.SER_KEY);
			break;
		case 5:
			mShopping = (Shopping) intent
					.getSerializableExtra(SearchStoreActivity.SER_KEY);
			break;
		default:
			break;
		}
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.cb_near).setVisibility(View.GONE);
		img = (ImageView) findViewById(R.id.img_shopping);
		start = (ImageView) findViewById(R.id.img_start);
		title = (TextView) findViewById(R.id.shopping_title);
		privilege = (TextView) findViewById(R.id.num);
		feature = (TextView) findViewById(R.id.tv_feature);
		open = (TextView) findViewById(R.id.tv_open);
		tel = (TextView) findViewById(R.id.tv_phone);
		address = (TextView) findViewById(R.id.tv_address);
		distance = (TextView) findViewById(R.id.tv_location);
		comment = (TextView) findViewById(R.id.tv_comment);
		percost = (TextView) findViewById(R.id.tv_percost);
		content = (TextView) findViewById(R.id.tv_content);
		start_num = (TextView) findViewById(R.id.start_num);

		// mImageView = (AsyncImageView) findViewById(R.id.img_shopping);
		// asyncImageLoader = new AsyncImageLoader();
		if (mShopping != null) {
			title.setText(mShopping.getName());
			privilege.setText(mShopping.getMzeatvip());
			tel.setText(mShopping.getTel());
			address.setText(mShopping.getApi_address());
			distance.setText(StringUtils.formatDistance(mShopping.getDistance()));
			comment.setText(mShopping.getComment_count());
			feature.setText(mShopping.getCharacteristic());
			open.setText(mShopping.getOpen_time());
			if (mShopping.getMobile_brief().equals("")) {
				content.setText("暂无简介");
			} else {
				content.setText(mShopping.getMobile_brief());
			}

			start_num.setText(StringUtils.formatAvg_point(mShopping
					.getAvg_point()));
			imgurl = mShopping.getLogo();
			start.setBackgroundResource(CountStartNum.getStartNum(mShopping
					.getAvg_point()));
			if (imgurl.equals("")) {
				// img.setImageResource(R.drawable.empty_image);
				// mImageView.setImageResource(R.drawable.empty_image);
			} else {
				
				bmpManager.loadBitmap(imgurl,img, BitmapFactory.decodeResource(this.getResources(), R.drawable.empty_image));

				// mImageView.setUrl(imgurl);
				// 异步加载图片
				/**
				 * Drawable cachedImage = asyncImageLoader.loadDrawable(imgurl,
				 * img, new ImageCallback() {
				 * 
				 * @Override public void imageLoaded(Drawable imageDrawable,
				 *           ImageView imageView, String imageUrl) {
				 *           imageView.setImageDrawable(imageDrawable);
				 *           Log.e("imgw", String.valueOf(img.getWidth()));
				 *           Log.e("imgh", String.valueOf(img.getHeight())); }
				 *           }); if (cachedImage == null) {
				 *           img.setImageResource(R.drawable.empty_image); }
				 *           else { img.setImageDrawable(cachedImage);
				 *           Log.e("imgw", String.valueOf(img.getWidth()));
				 *           Log.e("imgh", String.valueOf(img.getHeight())); }
				 **/
/**
				mImageWorker = new ImageFetcher(ShoppingDetailActivity.this,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// mImageWorker.setAdapter(imageThumbWorkerUrlsAdapter);
				ImageCacheParams cacheParams = new ImageCacheParams(
						ImageCache.IMAGE_DIR);
				mImageWorker.setLoadingImage(R.drawable.empty_image);
				mImageCache = new ImageCache(ShoppingDetailActivity.this,
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
				//Intent intent = new Intent(ShoppingDetailActivity.this,ShoppingActivity.class);
				//startActivity(intent);
				finish();
			}
		});

		contact = (ImageButton) findViewById(R.id.btn_contact);
		contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String inputStr = tel.getText().toString();
					Log.e("call", "call");
					if (StringUtils.isMobile(inputStr) == true
							|| StringUtils.isPhone(inputStr) == true) {
						Intent myIntentDial = new Intent(Intent.ACTION_DIAL,
								Uri.parse("tel:" + inputStr));

						startActivity(myIntentDial);
						ShoppingDetailActivity.this.overridePendingTransition(
								R.anim.slide_right_in, R.anim.slide_left_out);
					} else {
						ShowToast.showToastShort(ShoppingDetailActivity.this,
								"无法获取电话，请你手动拨打！");
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
			}
		});

		rl = (RelativeLayout) findViewById(R.id.storeintroduce);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!content_view) {
					content.setVisibility(View.VISIBLE);
					content_view = true;
				} else {
					content.setVisibility(View.GONE);
					content_view = false;
				}
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
				if (StringUtils.checkDistance(mShopping.getDistance())) {
					// 其次判断网络是否连接
					if (CheckNetworkConnection
							.checkNetworkConnection(ShoppingDetailActivity.this)) {

						Intent intent = new Intent(ShoppingDetailActivity.this,
								RouteActivity.class);
						MzeatApplication.getInstance();
						Log.e("MzeatApplication.getInstance().init_result",
								String.valueOf(MzeatApplication.init_result));
						
						// 再次判断是否定位，百度地图是否初始化。
						if (!MzeatApplication.getInstance().lat.equals("")
								&& MzeatApplication.init_result
								) {
/**
							double st_geoLatitude = Double
									.valueOf(MzeatApplication.getInstance().lat) * 1E6;
							double st_geoLongitude = Double
									.valueOf(MzeatApplication.getInstance().lon) * 1E6;
							st_gp = new GeoPoint((int) st_geoLatitude,
									(int) st_geoLongitude);
							double en_geoLatitude = Double.valueOf(mShopping
									.getYpoint()) * 1E6;
							double en_geoLongitude = Double.valueOf(mShopping
									.getXpoint()) * 1E6;
							en_gp = new GeoPoint((int) en_geoLatitude,
									(int) en_geoLongitude);
							MKPlanNode stNode = new MKPlanNode();

							stNode.pt = st_gp;
							// Log.e("en_gp.getLatitudeE6()",
							// String.valueOf(st_gp.getLongitudeE6()));
							MKPlanNode enNode = new MKPlanNode();
							
							enNode.pt = en_gp;
							MzeatApplication.getInstance().mSearch.init(MzeatApplication.getInstance().mBMapManager, MzeatApplication.getInstance().mSearchListener);
							MzeatApplication.getInstance().mSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
							MzeatApplication.getInstance().mSearch
									.drivingSearch("梅州", stNode, "梅州", enNode);
							Log.e("MzeatApplication.getInstance().search_result",
									String.valueOf(MzeatApplication.search_result));
							MzeatApplication.getInstance();
							if (MzeatApplication.search_result == 0) {
								MKDrivingRouteResult res = MzeatApplication.getInstance().resultforsearch;
								Bundle mBundle = new Bundle();
								mBundle.putSerializable(SER_KEY, (Serializable) res);
								intent.putExtra("res", mBundle);
								startActivity(intent);
							}else {

								ShowToast.showToastShort(
										ShoppingDetailActivity.this,
										R.string.get_your_location);}
							**/
							 intent.putExtra("st_Latitude",
							 MzeatApplication.getInstance().lat);
							 Log.e("st_Latitude",MzeatApplication.getInstance().lat);
							 intent.putExtra("st_Longitude",
							 MzeatApplication.getInstance().lon);
							 intent.putExtra("en_Latitude",
							 String.valueOf(mShopping.getYpoint()));
							 intent.putExtra("en_Longitude",
							 String.valueOf(mShopping.getXpoint()));
							 startActivity(intent);

						} else {

							ShowToast.showToastShort(
									ShoppingDetailActivity.this,
									R.string.get_your_location);
							do {
								MzeatApplication.getInstance().mBMapManager
										.init(MzeatApplication.strKey,
												new MyGeneralListener());
							} while (!MzeatApplication.init_result);

						}

					} else {
						ShowToast.showToastShort(ShoppingDetailActivity.this,
								R.string.your_network_has_disconnected);
					}
				} else {
					ShowToast.showToastShort(ShoppingDetailActivity.this,
							R.string.routestorefaile);
				}

			}
		});
	}

	/**
	 * public ImageWorkerAdapter imageThumbWorkerUrlsAdapter = new
	 * ImageWorkerAdapter() {
	 * 
	 * @Override public Object getItem(int index) { return imgurl; }
	 * @Override public int getSize() { return 1; } };
	 **/
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocClient = MzeatApplication.getInstance().mLocationClient;
		if (mLocClient != null) {
			mLocClient
					.registerLocationListener(MzeatApplication.getInstance().myListener);
			mLocClient.setLocOption(BaiduLocationOption
					.getOption(ShoppingDetailActivity.this));
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
