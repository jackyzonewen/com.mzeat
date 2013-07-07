package com.mzeat.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;

public class CheckNetworkConnection {

	/**
	 * 检查网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkConnection(Context context) {
		// 实例化ConnectivityManager
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 取得wifi状态信息
		android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// 取得mobie状态信息
		android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// 判断状态是否为连接状态，注意状态
		if (wifi.isConnected() || mobile.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static  boolean checkGPS(Context context) {

		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean provider = manager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		
		return provider;

	}
}
