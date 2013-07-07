package com.mzeat.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.api.MzeatService;
import com.mzeat.db.ShareItemDb;
import com.mzeat.image.BitmapManager;
import com.mzeat.model.Comment;
import com.mzeat.model.Page;
import com.mzeat.model.Share;
import com.mzeat.model.ShareDetail;
import com.mzeat.model.ShareItem;
import com.mzeat.model.ShareItemImgs;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.CommentListAdapter;
import com.mzeat.ui.adapter.GridViewAdapter;
import com.mzeat.ui.widget.MyGridView;
import com.mzeat.ui.widget.MyListView;
import com.mzeat.util.CheckNetworkConnection;
import com.mzeat.util.ShowToast;
import com.mzeat.util.SmileyParser;
import com.mzeat.util.StringUtils;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShareDetailActivity extends BaseActivity {

	private TextView username;
	private TextView content;
	private TextView say;
	private TextView creat_time;
	private ImageView img_user;
	private TextView tv_title;
	private LoadDataTask mLoadDataTask;
	private BitmapManager bmpManager;
	private MyGridView gridView;
	private GridViewAdapter gridViewAdapter;
	private MyListView mListView;
	private CommentListAdapter mAdapter;

	private ImageButton btn_reflash;
	private ImageButton btn_say;
	
	private RelativeLayout rl_say;
	SmileyParser parser ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharedetail);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.empty_image));
		Intent intent = getIntent();
		share_id = intent.getStringExtra("share_id");
		SmileyParser.init(this);
		
		 parser = SmileyParser.getInstance();
		initView();

		setViewData();
		loaddata();
	}

	private void setViewData() {
		// TODO Auto-generated method stub

		tv_title.setText(R.string.microshare);

	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLoadDataTask != null) {
					mLoadDataTask.cancel(true);
					mLoadDataTask.setListener(null);
					mLoadDataTask = null;
				}
				
				finish();
			}
		});
		username = (TextView) findViewById(R.id.username);
		creat_time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
		say = (TextView) findViewById(R.id.saycount);
		img_user = (ImageView) findViewById(R.id.img_user);
		gridView = (MyGridView) findViewById(R.id.grid_view);
		mListView = (MyListView) findViewById(R.id.lv_comment);

		btn_reflash = (ImageButton) findViewById(R.id.btn_reflash);
		btn_reflash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CheckNetworkConnection
						.checkNetworkConnection(ShareDetailActivity.this)) {

					if (mLoadDataTask != null
							&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
						ShowToast.showMessage(ShareDetailActivity.this,
								R.string.loadding_data);
						return;
					}

					loaddata();
				} else {
					ShowToast.showMessage(ShareDetailActivity.this,
							R.string.your_network_has_disconnected);
				}
			}
		});
		
		rl_say = (RelativeLayout) findViewById(R.id.rl_say);
		rl_say.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if (MzeatApplication.getInstance().getpPreferencesConfig()
						.getInt("loginstate", 0) == 1) {
					intent = new Intent(ShareDetailActivity.this,
							CommentActivity.class);
					intent.putExtra("share_id", shareDetail.getShare_id());
					intent.putExtra("isReply", false);
					startActivityForResult(intent, 1);
				} else {
					intent = new Intent(ShareDetailActivity.this,
							LoginActivity.class);
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromshare", 1);
					startActivity(intent);

				}
			}
		});
		btn_say = (ImageButton) findViewById(R.id.btn_say);
		btn_say.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if (MzeatApplication.getInstance().getpPreferencesConfig()
						.getInt("loginstate", 0) == 1) {
					intent = new Intent(ShareDetailActivity.this,
							CommentActivity.class);
					intent.putExtra("share_id", shareDetail.getShare_id());
					intent.putExtra("isReply", false);
					startActivityForResult(intent, 1);
				} else {
					intent = new Intent(ShareDetailActivity.this,
							LoginActivity.class);
					MzeatApplication.getInstance().getpPreferencesConfig()
							.setInt("fromshare", 1);
					startActivity(intent);

				}
			}
		});

	}

	ProgressDialog pg;
	ArrayList<Comment> list = new ArrayList<Comment>();
	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			pg = ProgressDialog.show(ShareDetailActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				username.setText(shareDetail.getUser_name());
				creat_time.setText(shareDetail.getTime());
				say.setText("评论" + "(" + shareDetail.getComment_count() + ")");
				if (!StringUtils.isEmpty(shareDetail.getUser_avatar())) {
					// loadImage(position, holder.img_user);
					bmpManager.loadBitmap(shareDetail.getUser_avatar(),
							img_user, BitmapFactory.decodeResource(
									ShareDetailActivity.this.getResources(),
									R.drawable.empty_image));
				}
				if (!shareDetail.getTitle().equals("")) {
					StringBuffer sb = new StringBuffer();
					sb.append("#");
					sb.append(shareDetail.getTitle());
					sb.append("#");
					sb.append(shareDetail.getContent());

					SpannableStringBuilder spannable = new SpannableStringBuilder(
							sb.toString());
					int begin = 0;
					spannable.setSpan(new ForegroundColorSpan(Color.RED),
							begin, shareDetail.getTitle().length() + 2,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

					content.setText( parser.addSmileySpans(spannable));
				} else {
					content.setText(parser.addSmileySpans(shareDetail.getContent()));
				}

				ArrayList<String> smallImg = new ArrayList<String>();
				for (int i = 0; i < shareDetail.getImgs().size(); i++) {
					String img = shareDetail.getImgs().get(i).getSmall_img();
					smallImg.add(img);
				}

				ArrayList<ShareItemImgs> imgs = (ArrayList<ShareItemImgs>) shareDetail
						.getImgs();
				if (imgs.size() > 0) {
					gridViewAdapter = new GridViewAdapter(
							ShareDetailActivity.this, imgs);
					gridViewAdapter.setDataList(smallImg);
					gridView.setAdapter(gridViewAdapter);
				}

				list = (ArrayList<Comment>) shareDetail.getComments().getList();
				if (list != null) {
					//Log.e("list", String.valueOf(list.size()));
					mAdapter = new CommentListAdapter(ShareDetailActivity.this);
					mAdapter.setDataList(list);
					mListView.setAdapter(mAdapter);
					mListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent;
							if (MzeatApplication.getInstance().getpPreferencesConfig()
									.getInt("loginstate", 0) == 1) {
								intent = new Intent(ShareDetailActivity.this,
										CommentActivity.class);
								intent.putExtra("share_id", shareDetail.getShare_id());
								intent.putExtra("isReply", true);
								intent.putExtra("parent_id", shareDetail.getComments().getList().get(position).getComment_id());
								intent.putExtra("user_name", shareDetail.getComments().getList().get(position).getUser_name());
								startActivityForResult(intent, 1);
							} else {
								intent = new Intent(ShareDetailActivity.this,
										LoginActivity.class);
								MzeatApplication.getInstance().getpPreferencesConfig()
										.setInt("fromshare", 1);
								startActivity(intent);

							}	
						}
					});
				} else {
					mAdapter = new CommentListAdapter(ShareDetailActivity.this);
					mAdapter.clear();
					mAdapter.notifyDataSetChanged();
					mListView.setAdapter(mAdapter);
				}

				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showGetFaile(ShareDetailActivity.this);

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(ShareDetailActivity.this);
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

	private ShareDetail shareDetail = new ShareDetail();
	private String share_id;

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub

			shareDetail = MzeatApplication.getInstance().getService()
					.getShareDetail(share_id);
			int code = Integer.valueOf(shareDetail.getOpen());
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
			finish();
		}
		

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
		
			if (CheckNetworkConnection
					.checkNetworkConnection(ShareDetailActivity.this)) {
				loaddata();
			} else {
				ShowToast.showMessage(ShareDetailActivity.this,
						R.string.your_network_has_disconnected);
			}

		}
	}
}
