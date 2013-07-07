package com.mzeat.util;

import com.mzeat.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static final String TAG = ConnectionChangeReceiver.class
			.getSimpleName();
	public boolean success = false;
	public ConnectionChangeReceiver() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG, "网络状态改变");

	

		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State state = connManager.getActiveNetworkInfo().getState();
		// 获取WIFI网络连接状态
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 判断是否正在使用WIFI网络
		if (State.CONNECTED == state) {
			success = true;
			Log.e(TAG, "网络连接");
		}
		// 获取GPRS网络连接状态
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		// 判断是否正在使用GPRS网络
		if (State.CONNECTED == state) {
			success = true;
			Log.e(TAG, "网络连接");
		}

		if (!success) {
			Log.e(TAG, "网络未连接");
			ShowToast.showToastShort(context,
					R.string.your_network_has_disconnected);
		}
	}
}
