package com.mzeat.ui;

import java.util.ArrayList;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.UserDb;
import com.mzeat.model.CardActivate;
import com.mzeat.model.Comment;
import com.mzeat.model.ShareDetail;
import com.mzeat.model.ShareItemImgs;
import com.mzeat.model.User;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.CommentListAdapter;
import com.mzeat.ui.adapter.GridViewAdapter;
import com.mzeat.util.ShowToast;
import com.mzeat.util.StringUtils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CardActivateActivity extends BaseActivity {

	private TextView tv_title;

	private EditText et_truename;
	private EditText et_mobienum;
	private EditText et_cardnum;
	private EditText et_cardpwd;

	private String truename;
	private String mobienum;
	private String cardnum;
	private String cardpwd;

	private ImageButton btn_save;

	private LoadDataTask mLoadDataTask;
	private InputMethodManager imm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cardactivate);
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

		initView();
		setViewData();
	}

	private void initView() {
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

				finish();
			}
		});

		tv_title = (TextView) findViewById(R.id.tv_title);
		et_truename = (EditText) findViewById(R.id.tv_truename);
		et_mobienum = (EditText) findViewById(R.id.tv_mobile);
		et_cardnum = (EditText) findViewById(R.id.tv_cardnum);
		et_cardpwd = (EditText) findViewById(R.id.tv_cardpwd);
		btn_save = (ImageButton) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkInput()) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 

					loaddata();
				}
			}
		});
	}

	private void setViewData() {
		tv_title.setText(R.string.activatecard);

		UserDb userdb = new UserDb(this);
		User user = userdb.getUser();
		userdb.closeDB();
		if (!user.getMobile().equals("")) {
			et_mobienum.setText(user.getMobile());
		}
	}

	private boolean checkInput() {

		truename = et_truename.getText().toString().trim();
		if ("".equals(truename)) {
			Toast.makeText(this, "真实姓名不能为空，请输入真实姓名！", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		mobienum = et_mobienum.getText().toString().trim();
		if ("".equals(mobienum)) {
			Toast.makeText(this, "手机号不能为空，请输入手机号！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			if (!StringUtils.isMobile(mobienum)) {
				Toast.makeText(this, "请正确输入手机号！", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		cardnum = et_cardnum.getText().toString().trim();
		if ("".equals(cardnum)) {
			Toast.makeText(this, "会员卡号不能为空，请输入会员卡号！", Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		cardpwd = et_cardpwd.getText().toString().trim();
		if ("".equals(cardpwd) || cardpwd.length() < 5) {
			Toast.makeText(this, "密码长度小于5位，请正确输入！", Toast.LENGTH_SHORT).show();
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
			pg = ProgressDialog.show(CardActivateActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				Toast.makeText(CardActivateActivity.this,
						cardActivate.getUser_info_return(), Toast.LENGTH_SHORT)
						.show();
				setResult(1);
				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {

				Toast.makeText(CardActivateActivity.this,
						cardActivate.getUser_info_return(), Toast.LENGTH_SHORT)
						.show();
				mLoadDataTask = null;

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showFaile(CardActivateActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};
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

	private CardActivate cardActivate = new CardActivate();

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub

			cardActivate = MzeatApplication.getInstance().getService()
					.getCardActivate(cardnum, cardpwd, truename, mobienum);
			int code = Integer.valueOf(cardActivate.getOpen());
			if (code == MzeatService.RESULT_OK) {

				return TaskResult.OK;
			} else if (code == MzeatService.RESULT_FAILE) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}

	private void loaddata() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.setListener(mTaskListener);
		try {
			mLoadDataTask.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask.setListener(null);
				mLoadDataTask = null;
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
		}

		return true;
	}
}
