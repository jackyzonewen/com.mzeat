package com.mzeat.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mzeat.AppException;
import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.UIHelper;
import com.mzeat.api.MsgService;
import com.mzeat.db.My_shareDb;
import com.mzeat.db.U_commentlist_itemDb;
import com.mzeat.db.UserDb;
import com.mzeat.image.BitmapManager;
import com.mzeat.model.EditUserFace;
import com.mzeat.model.Signin;
import com.mzeat.model.User;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.FoodActivity.NetworkChange;
import com.mzeat.ui.widget.LoadingDialog;
import com.mzeat.util.ConnectionChangeReceiver;
import com.mzeat.util.FileUtils;
import com.mzeat.util.ImageUtils;
import com.mzeat.util.LogUtil;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MycountActivity extends BaseActivity implements OnClickListener {

	private String TAG = "MycountActivity";
	private User user = new User();
	private UserDb mUserDb = new UserDb(MycountActivity.this);;
	// private int fromlogin;
	private BitmapManager bmpManager;
	private TextView name;
	private TextView balance;
	private TextView jifen;
	private TextView usercardstate;
	private TextView signinstate;
	private TextView mobile;
	private ImageButton btn_logout;

	private ImageView img_user;
	private RelativeLayout rl_singin;
	private RelativeLayout rl_usercard;
	private RelativeLayout rl_myorder;

	private LoadDataTask mLoadDataTask;
	private NetworkChange networkChange;
	private boolean reflash = false;
	private ImageButton edit;

	private SigninTask mSigninTask;

	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/MZEAT/Portrait/";

	private Uri origUri;
	private Uri cropUri;
	private File protraitFile;
	private Bitmap protraitBitmap;
	private String protraitPath;
	private final static int CROP = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// LogUtil.getLogOnStart(TAG);
		setContentView(R.layout.activity_mycount);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.empty_image));

		// Log.e("MycountActivity", user.getUid());
		initView();
	}

	private void initView() {
		name = (TextView) findViewById(R.id.tv_name);
		balance = (TextView) findViewById(R.id.tv_balance);
		jifen = (TextView) findViewById(R.id.tv_jifen);
		usercardstate = (TextView) findViewById(R.id.tv_usercard);
		signinstate = (TextView) findViewById(R.id.tv_signin);
		mobile = (TextView) findViewById(R.id.tv_phone);
		img_user = (ImageView) findViewById(R.id.img_user);
		rl_singin = (RelativeLayout) findViewById(R.id.rl_signin);
		rl_usercard = (RelativeLayout) findViewById(R.id.rl_usercard);

		btn_logout = (ImageButton) findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);

		edit = (ImageButton) findViewById(R.id.edit);
		edit.setOnClickListener(this);

		rl_myorder = (RelativeLayout) findViewById(R.id.rl_myorder);
		rl_myorder.setOnClickListener(this);
		img_user.setOnClickListener(editerClickListener);

	}

	private void setViewData() {
		// TODO Auto-generated method stub
		name.setText(user.getUser_name());
		balance.setText(user.getUser_money_format());

		if (user.getUser_avatar().equals("")) {
			img_user.setImageResource(R.drawable.empty_image);
		} else {

			bmpManager.loadBitmap(user.getUser_avatar(), img_user,
					BitmapFactory.decodeResource(this.getResources(),
							R.drawable.empty_image));

		}
		jifen.setText(user.getScore());
		if (user.getMzeatno().equals("")) {
			usercardstate.setText(R.string.unbound);
			rl_usercard.setClickable(true);
			rl_usercard.setOnClickListener(this);
		} else {
			usercardstate.setText(user.getMzeatno());
			rl_usercard.setClickable(false);
		}

		if (user.getT_sign_info().equals("0")) {
			signinstate.setText(R.string.unsignin);
			rl_singin.setClickable(true);
			rl_singin.setOnClickListener(this);
		} else {
			signinstate.setText(R.string.signedin);
			rl_singin.setClickable(false);
		}
		mobile.setText(user.getMobile());
	}

	private View.OnClickListener editerClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			CharSequence[] items = { getString(R.string.img_from_album),
					getString(R.string.img_from_camera) };
			imageChooseItem(items);
		}
	};

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(this)
				.setTitle("上传头像").setIcon(android.R.drawable.btn_star)
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 判断是否挂载了SD卡
						String storageState = Environment
								.getExternalStorageState();
						if (storageState.equals(Environment.MEDIA_MOUNTED)) {
							File savedir = new File(FILE_SAVEPATH);
							if (!savedir.exists()) {
								savedir.mkdirs();
							}
						} else {
							UIHelper.ToastMessage(MycountActivity.this,
									"无法保存上传的头像，请检查SD卡是否挂载");
							return;
						}

						// 输出裁剪的临时文件
						String timeStamp = new SimpleDateFormat(
								"yyyyMMddHHmmss").format(new Date());
						// 照片命名
						String origFileName = "osc_" + timeStamp + ".jpg";
						String cropFileName = "osc_crop_" + timeStamp + ".jpg";

						// 裁剪头像的绝对路径
						protraitPath = FILE_SAVEPATH + cropFileName;
						Log.e("protraitPath", protraitPath);
						protraitFile = new File(protraitPath);

						origUri = Uri.fromFile(new File(FILE_SAVEPATH,
								origFileName));
						cropUri = Uri.fromFile(protraitFile);

						// 相册选图
						if (item == 0) {
							startActionPickCrop(cropUri);
						}
						// 手机拍照
						else if (item == 1) {
							startActionCamera(origUri);
						}
					}
				}).create();

		imageDialog.show();
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	private void startActionPickCrop(Uri output) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	private void startActionCamera(Uri output) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	private void startActionCrop(Uri data, Uri output) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
	}

	private LoadingDialog loading;

	/**
	 * 上传新照片
	 */
	private void uploadNewPhoto() {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					EditUserFace res = (EditUserFace) msg.obj;
					// 提示信息
					// UIHelper.ToastMessage(MycountActivity.this,
					// res.getErrorMessage());
					if (res.getOpen().equals("1")) {
						// 显示新头像
						img_user.setImageBitmap(protraitBitmap);
						login();
					}
				} else if (msg.what == -1 && msg.obj != null) {
					((AppException) msg.obj).makeToast(MycountActivity.this);
				}
			}
		};

		if (loading != null) {
			loading.setLoadText("正在上传头像···");
			loading.show();
		}

		new Thread() {
			public void run() {
				// 获取头像缩略图
				if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
					protraitBitmap = ImageUtils.loadImgThumbnail(protraitPath,
							200, 200);
				}

				if (protraitBitmap != null) {
					Message msg = new Message();
					Log.e("protraitFile.getName()", protraitFile.getName());
					try {
						EditUserFace res = MzeatApplication.getInstance()
								.getService().getUserFace(protraitFile);

						if (res != null && res.getOpen().equals("1")) {
							// 保存新头像到缓存
							String filename = FileUtils.getFileName(user
									.getUser_avatar());
							Log.e("filename", filename);
							ImageUtils.saveImage(MycountActivity.this,
									filename, protraitBitmap);
						}
						msg.what = 1;
						msg.obj = res;
					} catch (IOException e) {
						msg.what = -1;
						msg.obj = e;
						e.printStackTrace();
					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_logout:
			Intent sInten = new Intent("com.mzeat.msg");
			stopService(sInten);

			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("count", 0);

			My_shareDb my_shareDb = new My_shareDb(MycountActivity.this);
			if (my_shareDb.getMy_share() != null
					&& my_shareDb.getMy_share().size() > 0) {
				my_shareDb.deleteAll();
			}

			my_shareDb.closeDB();

			U_commentlist_itemDb u_commentlist_itemDb = new U_commentlist_itemDb(
					MycountActivity.this);

			if (u_commentlist_itemDb.getItems() != null
					&& u_commentlist_itemDb.getItems().size() > 0) {
				u_commentlist_itemDb.deleteAll();
			}

			u_commentlist_itemDb.closeDB();

			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("isMsg", 0);
			
			PreferencesConfig mConfig = MzeatApplication.getInstance()
					.getpPreferencesConfig();
			mConfig.setString("email", "");
			mConfig.setString("pwd", "");
			mConfig.setInt("loginstate", 0);
			intent = new Intent(MycountActivity.this, LoginActivity.class);
			startActivityForResult(intent, 1);
			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("logout", 1);

			// finish();
			break;
		case R.id.edit:
			intent = new Intent(MycountActivity.this, EditacountActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_usercard:
			intent = new Intent(MycountActivity.this,
					CardActivateActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.rl_signin:
			signin();
			break;
		case R.id.rl_myorder:
			intent = new Intent(MycountActivity.this, MyOrderActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	protected void onResume() {
		super.onResume();

		// Log.e("fromregist",
		// String.valueOf(MzeatApplication.getInstance()
		// .getpPreferencesConfig().getInt("fromregist", 0)));
		// 从注册页面注册成功跳转到我的账号
		if (MzeatApplication.getInstance().getpPreferencesConfig()
				.getInt("fromregist", 0) == 1) {
			login();
			MzeatApplication.getInstance().getpPreferencesConfig()
					.setInt("fromregist", 0);
		}
		mUserDb = new UserDb(MycountActivity.this);
		user = mUserDb.getUser();
		mUserDb.closeDB();
		if (user.getUid() != null) {
			setViewData();
		}
		// 从登陆页面登陆成功跳转到我的账号
		// Intent intent = getIntent();
		// intent.getIntExtra("fromlogin", 0);
		// Log.e("fromlogin", String.valueOf(intent.getIntExtra("fromlogin",
		// 0)));
		// if (intent.getIntExtra("fromlogin", 0) == 1) {
		// mUserDb = new UserDb(MycountActivity.this);
		// user = mUserDb.getUser();
		// mUserDb.closeDB();
		// setViewData();
		// }

		// 其他界面跳转到我的账号
		
		

		networkChange = new NetworkChange();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkChange, filter);

	};

	protected void onPause() {
		super.onPause();
		LogUtil.getLogOnPause(TAG);
		try {
			unregisterReceiver(networkChange);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		LogUtil.getLogOnDestroy(TAG);
	};

	protected void onStart() {
		super.onStart();
		LogUtil.getLogOnStart(TAG);
	};

	protected void onRestart() {
		super.onRestart();
		LogUtil.getLogOnRestart(TAG);
	};

	ProgressDialog pg;
	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			pg = ProgressDialog.show(MycountActivity.this,
					getString(R.string.dialog_tips), "刷新账号信息", true, true,
					cancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {

				mUserDb = new UserDb(MycountActivity.this);
				mUserDb.add(mLoadDataTask.getUser());
				mUserDb.closeDB();
				setViewData();
				reflash = true;

				ShowToast.showToastShort(MycountActivity.this, "刷新成功！");
			} else if (result == TaskResult.FAILED) {
				ShowToast.showToastShort(MycountActivity.this, "刷新失败！");
			} else {
				ShowToast.showError(MycountActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};
	private String act = "login";
	private String r_type = "1";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			String email = MzeatApplication.getInstance()
					.getpPreferencesConfig().getString("email", "");
			String pwd = MzeatApplication.getInstance().getpPreferencesConfig()
					.getString("pwd", "");
			user = MzeatApplication.getInstance().getService()
					.getUser(act, r_type, email, pwd);
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

		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.setListener(mTaskListener);

		mLoadDataTask.execute();

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

	ProgressDialog pg_sign;
	private TaskAdapter signTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			pg_sign = ProgressDialog.show(MycountActivity.this,
					getString(R.string.dialog_tips), "正在签到", true, true,
					signin_cancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg_sign.dismiss();
			pg_sign = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				login();
				ShowToast.showToastShort(MycountActivity.this, "签到成功！");
			} else if (result == TaskResult.FAILED) {
				ShowToast.showToastShort(MycountActivity.this, "签到失败！");
			} else {
				ShowToast.showError(MycountActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};
	private Signin signin = new Signin();

	private class SigninTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			signin = MzeatApplication.getInstance().getService().getSignin();
			if (signin.getOpen().equals("1")) {
				return TaskResult.OK;
			} else if (signin.getOpen().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

	}

	private void signin() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (null != mSigninTask
				&& mSigninTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mSigninTask = new SigninTask();
		mSigninTask.setListener(signTaskListener);

		mSigninTask.execute();

	}

	DialogInterface.OnCancelListener signin_cancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			clearSigninTask();
		}

	};

	private void clearSigninTask() {
		// TODO Auto-generated method stub
		if (null != mSigninTask
				&& mSigninTask.getStatus() == GenericTask.Status.RUNNING) {
			mSigninTask.cancel(true);
			mSigninTask.setListener(null);
			mSigninTask = null;
		}

	}

	public class NetworkChange extends ConnectionChangeReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			super.onReceive(context, intent);
			if (super.success == true) {
				if (!reflash) {
					login();
				}

				super.success = false;
				// Log.e("loaddata()", "loaddata()");

			} else {

			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mLoadDataTask = null;
			Intent MyIntent = new Intent(Intent.ACTION_MAIN);
			MyIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(MyIntent);
			// Log.e("back", "back");
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			login();
		}

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
			startActionCrop(origUri, cropUri);// 拍照后裁剪
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			uploadNewPhoto();// 上传新照片
			break;
		}
	}
}
