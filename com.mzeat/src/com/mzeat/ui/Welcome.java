package com.mzeat.ui;

import com.mzeat.R;
import com.mzeat.util.LogUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Welcome extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
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
}
