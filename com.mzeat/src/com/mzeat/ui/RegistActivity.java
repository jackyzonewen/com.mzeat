package com.mzeat.ui;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.UserDb;
import com.mzeat.model.EditInfoReturn;
import com.mzeat.model.RegistInfo;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends BaseActivity {

	private TextView tv_title;

	
	
	private EditText et_acount;
	private EditText et_email;
	private EditText et_mobile;
	private EditText et_pwd;
	private EditText et_rpwd;
	
	
	private LinearLayout ll_bindmycount;
	private TextView tips_bindnewcount;
	private EditText et_mycount;
	private EditText et_mypwd;
	private ImageButton btn_bind;
	
	private String myacount;
	private String mypwd;
	
	private String acount;
	private String email;
	private String mobile;
	private String pwd;
	private String rpwd;

	private LoadDataTask mLoadDataTask;

	private ImageButton btn_regist;
	private InputMethodManager imm;
	int fromQQlogin = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		fromQQlogin = getIntent().getIntExtra("fromQQlogin", 0);
		initView();
		setViewData();
	}

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

				finish();
			}
		});

		et_acount = (EditText) findViewById(R.id.tv_regist_acount);
		et_email = (EditText) findViewById(R.id.tv_regist_email);
		et_mobile = (EditText) findViewById(R.id.tv_regist_mobile);
		et_pwd = (EditText) findViewById(R.id.tv_regist_pwd);
		et_rpwd = (EditText) findViewById(R.id.tv_repeatpwd);

		btn_regist = (ImageButton) findViewById(R.id.btn_save);
		btn_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

				regist();
			}
		});
		
		ll_bindmycount = (LinearLayout) findViewById(R.id.ll_bindmycount);
		tips_bindnewcount = (TextView) findViewById(R.id.tips_bindnewcount);
		et_mycount = (EditText) findViewById(R.id.tv_mycount);
		et_mypwd = (EditText) findViewById(R.id.tv_mypwd);
		btn_bind = (ImageButton) findViewById(R.id.btn_bind);
		
		if (fromQQlogin == 1) {
			ll_bindmycount.setVisibility(View.VISIBLE);
			tips_bindnewcount.setVisibility(View.VISIBLE);
		}
		

	}

	private void setViewData() {
		if (fromQQlogin == 1) {
			tv_title.setText(R.string.bindcount);
		}else {
			tv_title.setText(R.string.regist);
		}
		

	}

	private boolean checkInput() {

		acount = et_acount.getText().toString().trim();

		if ("".equals(acount)) {
			Toast.makeText(this, "请输入账号。", Toast.LENGTH_SHORT).show();
			return false;
		}

		email = et_email.getText().toString().trim();

		if ("".equals(email)) {
			Toast.makeText(this, "请输入邮箱。", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			if (!StringUtils.validateEmail(email)) {
				Toast.makeText(this, "邮箱格式有误，请重新输入。", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}

		mobile = et_mobile.getText().toString().trim();

		if ("".equals(mobile)) {
			Toast.makeText(this, "请输入手机号。", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			if (!StringUtils.isMobile(mobile)) {
				Toast.makeText(this, "手机号格式有误，请重新输入。", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}

		pwd = et_pwd.getText().toString().trim();

		if ("".equals(pwd) || pwd.length() < 4) {

			Toast.makeText(this, "密码长度不能少于4位", Toast.LENGTH_SHORT).show();
			return false;
		}

		rpwd = et_rpwd.getText().toString().trim();

		if (!rpwd.equals(pwd)) {
			Toast.makeText(this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
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
			pg = ProgressDialog.show(RegistActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setString("email", acount);
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setString("pwd", pwd);
				
				Toast.makeText(RegistActivity.this, mLoadDataTask
						.getRegistInfo().getInfo(), Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(RegistActivity.this,
						MainActivity.class);
				MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("loginstate", 1);
				MzeatApplication.getInstance().getpPreferencesConfig()
						.setInt("logout", 2);
				MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("fromregist", 1);
				startActivity(intent);
				finish();
			} else if (result == TaskResult.FAILED) {
			
				
				Toast.makeText(RegistActivity.this, mLoadDataTask
						.getRegistInfo().getInfo(), Toast.LENGTH_SHORT).show();
			} else {
				ShowToast.showError(RegistActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};

	private RegistInfo result = new RegistInfo();

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub
			result = MzeatApplication.getInstance().getService()
					.getRegist(email,rpwd , mobile,acount );

			if (result.getOpen().equals("1")) {
				return TaskResult.OK;
			} else if (result.getOpen().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}

		}

		private RegistInfo getRegistInfo() {
			return result;
		}

	}

	private void regist() {

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLoadDataTask != null) {
				mLoadDataTask.cancel(true);
				mLoadDataTask = null;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
