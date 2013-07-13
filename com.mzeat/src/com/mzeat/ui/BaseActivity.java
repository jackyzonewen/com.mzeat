package com.mzeat.ui;

import com.mzeat.AppManager;
import com.mzeat.MzeatApplication;
import com.mzeat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 应用程序Activity的基类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add("menu");// 必须创建一项
		// setIcon()方法为菜单设置图标，这里使用的是系统自带的图标，同学们留意一下,以
		// android.R开头的资源是系统提供的，我们自己提供的资源是以R开头的
		menu.clear();
		menu.add(Menu.NONE, Menu.FIRST, 1, "返回主页").setIcon(R.drawable.menu_return);
		//return super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		case Menu.FIRST:
			Intent intent = new Intent(BaseActivity.this, MainActivity.class);
			//intent.putExtra("backtohome", 1);
			MzeatApplication.getInstance().getpPreferencesConfig().setInt("backtohome", 1);
			startActivity(intent);
			AppManager.getAppManager().finishActivity(this);
			break;

		default:
			break;
		}

		return false;

	}

}
