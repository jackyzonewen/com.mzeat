package com.mzeat.ui;

import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.db.UserDb;
import com.mzeat.model.User;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText et_acount;
	private EditText et_pwd;
	private ImageButton login;
	private ImageButton back;
	private ImageButton regist;
	private String name;
	private String password;
	private LoadDataTask mLoadDataTask;
	public final static String SER_KEY = "login";
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		et_acount = (EditText) findViewById(R.id.et_acount);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		login = (ImageButton) findViewById(R.id.btn_login);
		login.setOnClickListener(this);
		back = (ImageButton) findViewById(R.id.btn_back);
		back.setOnClickListener(this);
		regist = (ImageButton) findViewById(R.id.btn_regist);
		regist.setOnClickListener(this);
	}

	/**
	 * 检测用户输入
	 */
	private boolean checkInput() {
		name = et_acount.getText().toString().trim();
		password = et_pwd.getText().toString().trim();
		// 检测邮箱或手机
		if ("".equals(name)) {
			Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
			return false;
		}

		if ("".equals(password) || password.length() < 4) {

			Toast.makeText(this, "密码长度不能少于4位", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	ProgressDialog pg;
	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			pg = ProgressDialog.show(LoginActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				PreferencesConfig mConfig = MzeatApplication.getInstance()
						.getpPreferencesConfig();
				mConfig.setInt("loginstate", 1);
				mConfig.setString("email", name);
				mConfig.setString("pwd", password);

				UserDb userdb = new UserDb(LoginActivity.this);
				userdb.add(mLoadDataTask.getUser());
				userdb.closeDB();

				Intent mIntent = new Intent("com.mzeat.msg");
				MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("isMsg", 1);
				startService(mIntent);
				//设置启动服务TAG
				//setResult(1);
				
				int fromprivilege = MzeatApplication.getInstance()
						.getpPreferencesConfig().getInt("fromprivilege", 0);

				if (fromprivilege == 1) {
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromprivilege", 0);
					finish();
				}

				int fromshare = MzeatApplication.getInstance()
						.getpPreferencesConfig().getInt("fromshare", 0);

				if (fromshare == 1) {
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromshare", 0);
					Intent intent = new Intent(LoginActivity.this, CommentActivity.class);
					startActivity(intent);
					finish();
				}
				
				int fromsharelist = MzeatApplication.getInstance()
						.getpPreferencesConfig().getInt("fromsharelist", 0);

				if (fromsharelist == 1) {
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromsharelist", 0);
					Intent intent = new Intent(LoginActivity.this, PubShareActivity.class);
					startActivity(intent);
					finish();
				}

				// 保证跳转到我的账号
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("logout", 0);
				Intent intent = new Intent();
				intent.putExtra("back", 0);

				// Bundle mBundle = new Bundle();
				// mBundle.putSerializable(SER_KEY, mLoadDataTask.getUser());
				// intent.putExtras(mBundle);
				setResult(1, intent);
				finish();

				ShowToast.showLoginSuccess(LoginActivity.this);
			} else if (result == TaskResult.FAILED) {
				ShowToast.showLoginFaile(LoginActivity.this);
			} else {
				ShowToast.showError(LoginActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};
	User user = new User();
	private String act = "login";
	private String r_type = "1";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			user = MzeatApplication.getInstance().getService()
					.getUser(act, r_type, name, password);
			if (user.getUser_login_status().equals("1")) {
				return TaskResult.OK;
			} else if (user.getUser_login_status().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

		private User getUser() {
			return user;
		}
	}

	private void login() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (checkInput()) {
			if (null != mLoadDataTask
					&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
				return;

			mLoadDataTask = new LoadDataTask();
			mLoadDataTask.setListener(mTaskListener);

			mLoadDataTask.execute();

		}
	}

	DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			clearTask();
		}

	};

	private void clearTask() {
		// TODO Auto-generated method stub
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
			mLoadDataTask.cancel(true);
			mLoadDataTask = null;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_login:
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

			if (CheckNetworkConnection
					.checkNetworkConnection(LoginActivity.this)) {
				if (checkInput()) {
					login();
				}
			} else {
				ShowToast.showToastShort(LoginActivity.this,
						R.string.your_network_has_disconnected);
			}
			break;
		case R.id.btn_back:
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

			int fromprivilege = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromprivilege", 0);

			if (fromprivilege == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromprivilege", 0);
				finish();
			}
			
			int fromshare = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromshare", 0);

			if (fromshare == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromshare", 0);
				finish();
			}
			int fromsharelist = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromsharelist", 0);

			if (fromsharelist == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromsharelist", 0);
				finish();
				
			}
			intent = new Intent(LoginActivity.this, MainActivity.class);
			// startActivity(intent);
			setResult(2, intent);
			finish();
			break;
		case R.id.btn_regist:
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 
			intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLoadDataTask != null) {
				mLoadDataTask.cancel(true);
				mLoadDataTask = null;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			int fromprivilege = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromprivilege", 0);

			if (fromprivilege == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromprivilege", 0);
				finish();
			}
			int fromshare = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromshare", 0);

			if (fromshare == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromshare", 0);
				finish();
			}
			
			int fromsharelist = MzeatApplication.getInstance()
					.getpPreferencesConfig().getInt("fromsharelist", 0);

			if (fromsharelist == 1) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("fromsharelist", 0);
				finish();
				
			}

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			setResult(2, intent);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
