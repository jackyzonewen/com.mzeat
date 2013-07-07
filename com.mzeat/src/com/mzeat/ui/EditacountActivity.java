package com.mzeat.ui;

import java.util.Calendar;

import com.mzeat.MzeatApplication;
import com.mzeat.PreferencesConfig;
import com.mzeat.R;
import com.mzeat.db.UserDb;
import com.mzeat.model.EditInfoReturn;
import com.mzeat.model.User;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class EditacountActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private TextView acount;
	private TextView email;
	private TextView birthday;

	private EditText oldpwd;
	private EditText newpwd;
	private EditText repeatpwd;
	private EditText mobie;

	private RadioGroup rg_sex;
	private RadioButton rd_unknow;
	private RadioButton rd_man;
	private RadioButton rd_women;

	DatePickerDialog date;
	private int mYear;
	private int mMonth;
	private int mDay;

	private int editYear;
	private int editMonth;
	private int editDay;

	private User user = new User();
	private UserDb userDb;

	private String oldpassword;
	private String newpassword;
	private String repeatpassword;
	private String mobile;
	private String b_day;
	private String sex;

	private ImageButton btn_save;
	private LoadDataTask mLoadDataTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editacount);
		userDb = new UserDb(this);
		user = userDb.getUser();
		userDb.closeDB();
		initView();
		setViewData();
	}

	private void setViewData() {
		// TODO Auto-generated method stub
		acount.setText(user.getUser_name());
		email.setText(user.getUser_email());

		mobie.setText(user.getMobile());
		mobile = user.getMobile();

		if (!user.getB_day().equals("") && StringUtils.isValidDate(user.getB_day())) {

			birthday.setText(user.getB_day());
			b_day = birthday.getText().toString().trim();

		}

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		date = new DatePickerDialog(EditacountActivity.this, dateListener,
				mYear, mMonth, mDay);

		sex = user.getSex();
		switch (Integer.valueOf(sex)) {
		case -1:
			rd_unknow.setChecked(true);
			break;
		case 1:
			rd_man.setChecked(true);
			break;
		case 0:
			rd_women.setChecked(true);
			break;
		default:
			break;
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		acount = (TextView) findViewById(R.id.tv_useracount);
		email = (TextView) findViewById(R.id.tv_email);
		birthday = (TextView) findViewById(R.id.tv_birthday);
		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				date.show();
			}
		});

		mobie = (EditText) findViewById(R.id.tv_mobie);
		oldpwd = (EditText) findViewById(R.id.tv_oldpwd);
		newpwd = (EditText) findViewById(R.id.tv_pwd);
		repeatpwd = (EditText) findViewById(R.id.tv_repeat);

		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		rg_sex.setOnCheckedChangeListener(this);
		rd_unknow = (RadioButton) findViewById(R.id.rd_unknow);
		rd_man = (RadioButton) findViewById(R.id.rd_man);
		rd_women = (RadioButton) findViewById(R.id.rd_women);

		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_save = (ImageButton) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit();
			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rd_unknow:
			sex = "-1";
			break;
		case R.id.rd_man:
			sex = "1";
			break;
		case R.id.rd_women:
			sex = "0";
			break;
		default:
			break;
		}

	}

	/**
	 * 日期控件的事件
	 */
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			editYear = year;
			editMonth = monthOfYear;
			editDay = dayOfMonth;
			updateDateDisplay();

		}
	};

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {

		birthday.setText(new StringBuilder()
				.append(editYear)
				.append("-")
				.append((editMonth + 1) < 10 ? "0" + (editMonth + 1)
						: (editMonth + 1)).append("-")
				.append((editDay < 10) ? "0" + editDay : editDay));
		b_day = birthday.getText().toString().trim();

	}

	private boolean checkInput() {
		 boolean setoldpwd = false;
		 boolean setnewpwd = false;
		 boolean setrepeat = false;
		oldpassword = oldpwd.getText().toString().trim();
		if (!oldpassword.equals("")) {
			if (!oldpassword.equals(MzeatApplication.getInstance()
					.getpPreferencesConfig().getString("pwd", ""))) {

				Toast.makeText(this, "初始密码不正确，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return false;
			}else {
				setoldpwd = true;
			}

			
		}
		
		
		newpassword = newpwd.getText().toString().trim();

		if (!newpassword.equals("")) {
			if (newpassword.length() < 4) {
				Toast.makeText(this, "密码长度不能小于4位，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return false;
			}else {
				setnewpwd = true;
			}
		}
		repeatpassword = repeatpwd.getText().toString().trim();
		if (!repeatpassword.equals("")) {
			if (!repeatpassword.equals(newpassword)) {
				Toast.makeText(this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return false;
			}else {
				setrepeat = true;
			}
		}
		
		if (!setoldpwd && setnewpwd && setrepeat) {
			Toast.makeText(this, "请输入初始密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		if (setoldpwd &&  !setnewpwd && setrepeat) {
			Toast.makeText(this, "请输入登陆密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		if (setoldpwd &&  setnewpwd && !setrepeat) {
			Toast.makeText(this, "请输入重复密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		if (setoldpwd &&  !setnewpwd && !setrepeat) {
			Toast.makeText(this, "请输入登陆密码和重复密码，或删除初始密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		if (!setoldpwd && setnewpwd && !setrepeat) {
			Toast.makeText(this, "请输入初始密码和重复密码，或删除登陆密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		if (!setoldpwd && !setnewpwd && setrepeat) {
			Toast.makeText(this, "请输入初始密码和登陆密码，或删除重复密码！", Toast.LENGTH_SHORT)
			.show();
			return false;
		}
		
		
		
		mobile = mobie.getText().toString().trim();
		if (!mobile.equals("")) {
			if (!StringUtils.isMobile(mobile)) {
				Toast.makeText(this, "手机号码格式有误，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
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
			pg = ProgressDialog.show(EditacountActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);
		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				if (!newpwd.getText().toString().equals("")) {
					MzeatApplication
							.getInstance()
							.getpPreferencesConfig()
							.setString("pwd",
									newpwd.getText().toString().trim());
				}
				newpwd.setText("");
				oldpwd.setText("");
				repeatpwd.setText("");
				ShowToast.showEditSuccess(EditacountActivity.this);
				userDb = new UserDb(EditacountActivity.this);
				user.setMobile(mobile);
				user.setB_day(b_day);
				user.setSex(sex);
				userDb.updataUser(user);
				userDb.closeDB();
			} else if (result == TaskResult.FAILED) {
				ShowToast.showEditFaile(EditacountActivity.this);
			} else {
				ShowToast.showError(EditacountActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};

	private EditInfoReturn result = new EditInfoReturn();

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub
			result = MzeatApplication
					.getInstance()
					.getService()
					.getEditInfoReturn(
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("email", ""),
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("pwd", ""), b_day, sex, mobile,
							newpassword);

			if (result.getOpen().equals("1")) {
				return TaskResult.OK;
			} else if (result.getOpen().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

	}

	private void edit() {

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
