package com.mzeat.ui;

import java.util.List;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.util.LogUtil;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		if (MzeatApplication.getInstance().getpPreferencesConfig()
					.getInt("loginstate", 0) == 1) {
			MzeatApplication.getInstance().getpPreferencesConfig()
			.setInt("isMsg", 1);
			if (!isServiceRunning(this, "com.mzeat.api.MsgService")) {
				Intent intent = new Intent("com.mzeat.msg");
				startService(intent);
			}
		}
		
		LogUtil.getLogOnCreat("Welcome");
		Handler x = new Handler();
		x.postDelayed(new splashhandler(), 3000);
	}

	class splashhandler implements Runnable {

		public void run() {
			// String isFirstLuanch = sp.get("isFirstLuanch");
			// if (isFirstLuanch.equals("")) {
			// sp.set("isFirstLuanch", "true");
			// startActivity(new Intent(getApplication(),
			// FirstLuanchView.class));
			// WelcomActivity.this.finish();
			// } else {
			startActivity(new Intent(getApplication(), MainActivity.class));
			finish();
			// }
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtil.getLogOnResume("Welcome");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtil.getLogOnStart("Welcome");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtil.getLogOnStop("Welcome");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtil.getLogOnRestart("Welcome");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.getLogOnDestroy("Welcome");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtil.getLogOnPause("Welcome");
	
	}
	
	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(50);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

}
