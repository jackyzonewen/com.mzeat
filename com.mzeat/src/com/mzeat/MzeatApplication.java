package com.mzeat;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.mzeat.api.IMzeatService;
import com.mzeat.api.MzeatService;
import com.mzeat.image.ImageCache;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MzeatApplication extends Application {

	private static MzeatApplication instance;
	public static ImageCache mImageCache;

	private IMzeatService service;
	private PreferencesConfig mPreferencesConfig;

	public boolean m_bKeyRight = true;
	public static boolean init_result = false;
	
	public BMapManager mBMapManager = null;
	public static final String strKey = "1FEB4D697C458BF982CFBC2DA1B6EB4EFD72A728";

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public MySearchListener mSearchListener = new MySearchListener();
	
	//public static  int search_result = 1;
	//public static boolean init_search_result = false;
	//public MKSearch mSearch = new MKSearch(); // 搜索模块，也可去掉地图模块独立使用
	//public  MKDrivingRouteResult resultforsearch = new MKDrivingRouteResult(); 
	
	
	public static MzeatApplication getInstance() {
		return instance;
	}

	public String lon = "";
	public String lat = "";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		service = new MzeatService();
		mPreferencesConfig = new PreferencesConfig(this);
		initEngineManager(this);
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		//initMKSearch();

	}

	public IMzeatService getService() {
		return service;
	}

	public PreferencesConfig getpPreferencesConfig() {
		return mPreferencesConfig;
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}
		//init_result = mBMapManager.init(strKey, new MyGeneralListener());
		// Log.e("MzeatApplication.getInstance().init_result",
		// String.valueOf(init_result));
		
		while(!init_result){

			init_result = mBMapManager.init(strKey, new MyGeneralListener());
			Log.e("MzeatApplication.getInstance().init_result",
					String.valueOf(init_result));
		} 
		if (!init_result) {
			Toast.makeText(
					MzeatApplication.getInstance().getApplicationContext(),
					"BaiduMap  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}
/**
	public void initMKSearch(){
		
		do {
			init_search_result = mSearch.init(mBMapManager,  mSearchListener);
		} while (!init_search_result);
	}
	**/
	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			Log.e("onGetNetworkState", String.valueOf(iError));

			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Log.e("iError", String.valueOf(iError));
				init_result = false;
				Toast.makeText(
						MzeatApplication.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();

			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						MzeatApplication.getInstance().getApplicationContext(),
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {

			Log.e("onGetPermissionState", String.valueOf(iError));
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(
						MzeatApplication.getInstance().getApplicationContext(),
						"输入正确的授权Key！", Toast.LENGTH_LONG).show();
				MzeatApplication.getInstance().m_bKeyRight = false;
			}
		}

	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb1 = new StringBuffer(256);
			lat = sb1.append(location.getLatitude()).toString();
			// Log.e("location", sb1.toString());
			StringBuffer sb2 = new StringBuffer(256);
			lon = sb2.append(location.getLongitude()).toString();
			// Log.e("location", sb2.toString());
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());

			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			// Log.e("latitude", String.valueOf(location.getLatitude()));
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				// sb.append("\n省：");
				// sb.append(location.getProvince());
				// sb.append("\n市：");
				// sb.append(location.getCity());
				// sb.append("\n区/县：");
				// sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocationClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			// Log.e("location", sb.toString());

		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {

		}
	}

	public  class MySearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			// TODO Auto-generated method stub
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null) {
				// Log.e("error", String.valueOf(error));
				Toast.makeText(
						MzeatApplication.getInstance().getApplicationContext(),
						"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
				return;

			}
			//search_result = error;
			//res = resultforsearch;
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}
	
	public static boolean net = true;
	public static final String dirPath = Environment
			.getExternalStorageDirectory() + "/Wallpaper/temp/";
	public static Map<String, String> localUrl = new LinkedHashMap<String, String>();
	public static Map<String, SoftReference<Bitmap>> softCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>();
	@SuppressWarnings("serial")
	public static final HashMap<String, Bitmap> hardCache = new LinkedHashMap<String, Bitmap>(
			50, 0.75f, true) {
		protected boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
			if (size() > 30) {
				softCache.put(eldest.getKey(),
						new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else {
				return false;
			}
		};
	};
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
	public void setProperty(String key,String value){
		AppConfig.getAppConfig(this).set(key, value);
	}
}
