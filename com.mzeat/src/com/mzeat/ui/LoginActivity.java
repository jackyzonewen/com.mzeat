package com.mzeat.ui;




import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.db.UserDb;
import com.mzeat.model.QQ_Login_Return;
import com.mzeat.model.User;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.Constants;
import com.mzeat.util.ShowToast;
import com.mzeat.util.ThirdPartUtil;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.TencentOpenAPI2;
import com.tencent.tauth.TencentOpenHost;
//import com.tencent.tauth.UiError;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.http.Callback;
import com.tencent.tauth.http.TDebug;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
	private GetCountTask mCountTask;
	public final static String SER_KEY = "login";
	private InputMethodManager imm;
	private int frommycart;
	private int frommessage;
	
	private RelativeLayout rl_qq;
	//private Tencent mTencent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		//mTencent = Tencent.createInstance(Constants.APP_ID, this.getApplicationContext());
		
		registerIntentReceivers();
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		frommycart = getIntent().getIntExtra("frommycart", 0);
		frommessage = getIntent().getIntExtra("frommessage", 0);
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
		
		rl_qq = (RelativeLayout) findViewById(R.id.rl_qq);
		rl_qq.setOnClickListener(this);
	}

    private void onClickLogin() { 	
       // if (!mTencent.isSessionValid()) {
            //IUiListener listener = new BaseUiListener() {
            //    @Override
            //    protected void doComplete(JSONObject values) {
           //         Log.e("values", values.toString());
          //      }
          //  };
           // mTencent.login(this, Constants.SCOPE, listener);
       // } else {
         //   mTencent.logout(this);
          //  updateLoginButton();
       // }
    }
	/**
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(JSONObject response) {
        	//ShowToast.showMessage(LoginActivity.this, response.toString());
        	Log.e("QQ_login",  response.toString());
        	
        	try {
				qq_id = response.getString("openid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            doComplete(response);
            getQQ_login();
            
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
           // ShowToast.showMessage(LoginActivity.this, "onError:" +"code:" + e.errorCode + ", msg:"
           //         + e.errorMessage + ", detail:" + e.errorDetail);
        }

        @Override
        public void onCancel() {
        	//  ShowToast.showMessage(  LoginActivity.this, "onCancel");
        }
    }
    **/
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

	ProgressDialog pg_qq;
	private TaskAdapter mGetCountTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			
			pg_qq = ProgressDialog.show(LoginActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, qqcancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg_qq.dismiss();
			pg_qq = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				PreferencesConfig mConfig = MzeatApplication.getInstance()
						.getpPreferencesConfig();
				mConfig.setInt("loginstate", 1);
				mConfig.setString("email", qq_Login_Return.getEmail());
				mConfig.setString("pwd", qq_Login_Return.getUser_pwd());

				

				Intent mIntent = new Intent("com.mzeat.msg");
				MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("isMsg", 1);
				startService(mIntent);
				

				Intent nIntent = new Intent(LoginActivity.this,MainActivity.class);
				MzeatApplication.getInstance().getpPreferencesConfig().setInt("fromQQlogin", 1);
				MzeatApplication.getInstance().getpPreferencesConfig().setInt("fromregist", 1);
				startActivity(nIntent);
				//Intent intent = new Intent();
				//intent.putExtra("back", 0);
				//setResult(1, intent);
				finish();

				ShowToast.showLoginSuccess(LoginActivity.this);
			} else if (result == TaskResult.FAILED) {
				Intent nIntent = new Intent(LoginActivity.this,RegistActivity.class);
				nIntent.putExtra("fromQQlogin", 1);
				startActivity(nIntent);
				//ShowToast.showLoginFaile(LoginActivity.this);
				ShowToast.showMessage(LoginActivity.this, "没有绑定账号，请绑定账号。");
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
	private String qq_id = "";
	private QQ_Login_Return qq_Login_Return;

	private class GetCountTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			qq_Login_Return = MzeatApplication.getInstance().getService()
					.getQq_Login_Return(qq_id);
			if (qq_Login_Return.getResulttype().equals("1")) {
				return TaskResult.OK;
			} else if (qq_Login_Return.getResulttype().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

		
	}

	private void getQQ_login() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

			if (null != mCountTask
					&& mCountTask.getStatus() == GenericTask.Status.RUNNING)
				return;

			mCountTask = new GetCountTask();
			mCountTask.setListener(mGetCountTaskListener);

			mCountTask.execute();
	}

	DialogInterface.OnCancelListener qqcancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			qqclearTask();
		}

	};

	private void qqclearTask() {
		// TODO Auto-generated method stub
		if (null != mCountTask
				&& mCountTask.getStatus() == GenericTask.Status.RUNNING) {
			mCountTask.cancel(true);
			mCountTask = null;
		}

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
				Log.e("frommycart", String.valueOf(frommycart));
				Log.e("frommessage", String.valueOf(frommessage));
				if (frommycart == 1) {
					setResult(3, intent);
				}else if (frommessage == 1) {
					setResult(4, intent);
				}else {
					setResult(1, intent);
				}
				
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
			
		case R.id.rl_qq:
			TencentOpenAPI2.logIn(getApplicationContext(), mOpenId, scope, Constants.APP_ID, "_self", CALLBACK, null, null);

			//onClickLogin();
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if ( receiver != null )
		{
			unregisterReceiver(receiver);
		}
	}
	private void registerIntentReceivers()
	{
		receiver = new AuthReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(TencentOpenHost.AUTH_BROADCAST);
		registerReceiver(receiver, filter);
	}
	private static final String CALLBACK = "auth://tauth.qq.com/";
	private String scope = "get_simple_userinfo,add_pic_t,add_t,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album,get_idollist";// ��Ȩ��Χ
	private AuthReceiver receiver;

	public String mAccessToken , mOpenId , mError , mClientId , mExpiresIn;
	//public static final int PROGRESS = 0;

	//@Override
	//protected Dialog onCreateDialog(int id)
	//{
	//	Dialog dialog = null;
	//	switch (id)
	//	{
	//		case PROGRESS:
	//			dialog = new ProgressDialog(this);
	//			((ProgressDialog) dialog).setMessage("请求中,请稍等...");
	//			break;
	//	}
	//	return dialog;
	//}
	
	ProgressDialog PROGRESS;
	
	public class AuthReceiver extends BroadcastReceiver
	{
		private static final String TAG = "AuthReceiver";

		@Override
		public void onReceive(Context context, Intent intent)
		{
			
			
			Bundle exts = intent.getExtras();
			String raw = exts.getString("raw");
			String access_token = exts.getString(TencentOpenHost.ACCESS_TOKEN);
			String expires_in = exts.getString(TencentOpenHost.EXPIRES_IN);
			String error_ret = exts.getString(TencentOpenHost.ERROR_RET);
			String error_des = exts.getString(TencentOpenHost.ERROR_DES);
			Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s", raw, access_token, expires_in));

			if ( access_token != null )
			{
				mAccessToken = access_token;
				mExpiresIn = expires_in;
				if ( !isFinishing() )
				{
					//showDialog(PROGRESS);
					PROGRESS = ProgressDialog.show(LoginActivity.this,
							null,
							"请求中,请稍等...");
				}

				TencentOpenAPI.openid(access_token, new Callback()
				{
					@Override
					public void onCancel(int flag)
					{

					}

					@Override
					public void onSuccess(final Object obj)
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								PROGRESS.dismiss();
								//dismissDialog(PROGRESS);
								mOpenId = ((OpenId) obj).getOpenId();
								mClientId = ((OpenId) obj).getClientId();
								MzeatApplication.getInstance().getpPreferencesConfig().setString("qq_id", mOpenId);
								qq_id = mOpenId;
								getQQ_login();

								
							}
						});
					}

					@Override
					public void onFail(int ret, final String msg)
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								PROGRESS.dismiss();
								//dismissDialog(PROGRESS);
								TDebug.msg(msg, getApplicationContext());
								ShowToast.showMessage(LoginActivity.this, "QQ登录失败。");
							}
						});
					}
				});
			}
			if ( error_ret != null )
			{
				mError = "获取access token失败" + "\n错误码: " + error_ret + "\n错误信息: " + error_des;
			}
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
