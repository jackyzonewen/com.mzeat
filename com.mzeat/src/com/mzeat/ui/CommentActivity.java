package com.mzeat.ui;

import java.util.ArrayList;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.model.CommentReturn;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.FaceGridViewAdapter;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;
import com.mzeat.util.SmileyParser;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CommentActivity extends BaseActivity {

	private EditText et_comment;
	private ImageButton btn_comment;
	private ImageButton btn_face;
	private TextView tv_reply;
	private String content;

	private LoadDataTask mLoadDataTask;
	private InputMethodManager imm;
	private Dialog builder; 
	  private String[] mSmileyTexts;
	  public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		//软键盘管理类
				imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				 mSmileyTexts = getResources().getStringArray(DEFAULT_SMILEY_TEXTS);

				initView();
		setViewData();
	}

	private String replycontent;

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		et_comment = (EditText) findViewById(R.id.et_comment);

		btn_comment = (ImageButton) findViewById(R.id.btn_comment);
		btn_face = (ImageButton) findViewById(R.id.btn_face);
		tv_reply = (TextView) findViewById(R.id.tv_reply);
		boolean isReply = getIntent().getBooleanExtra("isReply", false);
		if (isReply) {
			parent_id = getIntent().getStringExtra("parent_id");
			btn_comment.setBackgroundResource(R.drawable.selector_replay);
			et_comment.setHint(R.string.hint_reply);
			tv_reply.setVisibility(View.VISIBLE);

			StringBuffer sb = new StringBuffer();
			sb.append("回复@");
			sb.append(getIntent().getStringExtra("user_name"));
			sb.append(":");
			replycontent = sb.toString();
			SpannableStringBuilder spannable = new SpannableStringBuilder(
					sb.toString());

			spannable.setSpan(new ForegroundColorSpan(Color.RED), 3,
					sb.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			tv_reply.setText(spannable);
			// tv_reply.setText("回复@"+getIntent().getStringExtra("user_name")+":");
		} else {
			btn_comment.setBackgroundResource(R.drawable.selector_comment);
		}
		btn_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				if (checkInput()) {
					if (CheckNetworkConnection
							.checkNetworkConnection(CommentActivity.this)) {
						
						
						loaddata();
					} else {
						ShowToast.showMessage(CommentActivity.this,
								R.string.your_network_has_disconnected);
					}

				}
			}
		});
		
		btn_face.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				createExpressionDialog();
			}
		});

	}

	private String share_id;

	private void setViewData() {
		// TODO Auto-generated method stub
		share_id = getIntent().getStringExtra("share_id");

	}
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(CommentActivity.this);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("默认表情");
		builder.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
					et_comment.append(mSmileyTexts[arg2]);
				builder.dismiss();
			}
		});
	}
	
	/**
	 * 生成一个表情对话框中的gridview
	 * @return
	 */
	SmileyParser smileyParser;
	ArrayList<Integer> listItems = new ArrayList<Integer>();
	private GridView createGridView() {
		final GridView view = new GridView(this);
	
		
		
		FaceGridViewAdapter mAdapter = new FaceGridViewAdapter(this);
		//SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.team_layout_single_expression_cell, new String[]{"image"}, new int[]{R.id.image});
		view.setAdapter(mAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(3);
		view.setVerticalSpacing(3);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		view.setPadding(5, 5, 5, 5);
		return view;
	}

	private boolean checkInput() {
		content = et_comment.getText().toString().trim();
		if (content.equals("")) {
			if (getIntent().getBooleanExtra("isReply", false)) {
				ShowToast.showMessage(this, "请输入回复内容！");
			} else {
				ShowToast.showMessage(this, "请输入评论内容！");
			}

			return false;

		} else {
			if (getIntent().getBooleanExtra("isReply", false)) {
				content = replycontent + et_comment.getText().toString().trim();
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
			pg = ProgressDialog.show(CommentActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				ShowToast.showMessage(CommentActivity.this, "评论成功！");
				setResult(1);
				finish();
				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showGetFaile(CommentActivity.this);

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(CommentActivity.this);
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

	private CommentReturn commentReturn = new CommentReturn();
	private String is_relay = "";
	private String parent_id = "";

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub

			commentReturn = MzeatApplication.getInstance().getService()
					.getCommentReturn(share_id, is_relay, content, parent_id);
			int code = Integer.valueOf(commentReturn.getOpen());
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

		return super.onKeyDown(keyCode, event);
	}
}
