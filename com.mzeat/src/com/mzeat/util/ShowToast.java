package com.mzeat.util;

import android.content.Context;
import android.widget.Toast;

public class ShowToast {

	private static Context context;
	private static Toast mToast;

	

	public static void showToastShort(Context context, String content) {

		if (ShowToast.context == context) {
			mToast.cancel();
			mToast.setText(content);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.show();
		}else {
			ShowToast.context = context;
			mToast = Toast.makeText(context,content,Toast.LENGTH_SHORT); 
			mToast.show();
		}
	}

	public static void showToastShort(Context context, int content) {

		if (ShowToast.context == context) {
			mToast.cancel();
			mToast.setText(content);
			mToast.setDuration(Toast.LENGTH_SHORT);
			mToast.show();
		}else {
			ShowToast.context = context;
			mToast = Toast.makeText(context,content,Toast.LENGTH_SHORT); 
			mToast.show();
		}
	}

	public static void showMessage(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	public static void showMessage(Context context, int content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	public static void showToastLong(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public static void showToastLong(Context context, int content) {
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

	public static void showGetSuccess(Context context) {
		Toast.makeText(context, "获取成功！", Toast.LENGTH_SHORT).show();
	}

	public static void showGetFaile(Context context) {
		Toast.makeText(context, "获取失败！", Toast.LENGTH_SHORT).show();
	}
	
	public static void showNoOrder(Context context) {
		Toast.makeText(context, "没有订单！", Toast.LENGTH_SHORT).show();
	}
	public static void showEditSuccess(Context context) {
		Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
	}

	public static void showEditFaile(Context context) {
		Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
	}
	public static void showNomoredata(Context context) {
		Toast.makeText(context, "已经加载全部。", Toast.LENGTH_SHORT).show();
	}

	public static void showSuccess(Context context) {
		Toast.makeText(context, "操作成功！", Toast.LENGTH_SHORT).show();
	}

	public static void showFaile(Context context) {
		Toast.makeText(context, "操作失败！", Toast.LENGTH_SHORT).show();
	}

	public static void showTips(Context context) {
		Toast.makeText(context, "请连接网络！", Toast.LENGTH_SHORT).show();
	}

	public static void showError(Context context) {
		Toast.makeText(context, "获取失败！", Toast.LENGTH_SHORT).show();
	}
	
	public static void showLoginSuccess(Context context) {
		Toast.makeText(context, "登陆成功！", Toast.LENGTH_SHORT).show();
	}
	public static void showLoginFaile(Context context) {
		Toast.makeText(context, "登陆失败,请检查账号或密码是否输入正确！", Toast.LENGTH_SHORT).show();
	}
}
