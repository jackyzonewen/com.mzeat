package com.mzeat.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.db.My_shareDb;
import com.mzeat.db.U_commentlist_itemDb;
import com.mzeat.model.My_share;
import com.mzeat.model.U_commentlist;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.MainActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MsgService extends Service {
	private MediaPlayer tipsVoice = null;
	int isMsg;
	int result = 0;
	int count = 0;
	Thread thread = null;
	Timer timer;
	MyTask myTask;
	private LoadDataTask mLoadDataTask;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		super.onCreate();
		isMsg = MzeatApplication.getInstance().getpPreferencesConfig()
				.getInt("isMsg", 0);
		if (tipsVoice == null) {
			tipsVoice = new MediaPlayer();
			tipsVoice = MediaPlayer.create(this, R.raw.ring);
			tipsVoice.setLooping(false);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		timer = new Timer();
		myTask = new MyTask();

		try {
			timer.scheduleAtFixedRate(myTask, 1, 60000);
		} catch (Exception e) {
			// TODO: handle exception
			myTask.cancel();
			timer.cancel();

		}
		Log.e("service on start", "service on start");

	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (checkNetworkConnection(MsgService.this)) {
					Log.e("START", "开始获取未读信息");

					isMsg(isMsg);
				} else {
					Log.e("Stop", "没有获取信息");
				}

				break;
			}
		};
	};

	private class MyTask extends TimerTask {
		@Override
		public void run() {

			Message message = new Message();
			message.what = 1;
			mHandler.sendMessage(message);

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		if (tipsVoice != null) {

			// tipsVoice.stop();
			tipsVoice.release();
		}
		if (timer != null) {
			timer.cancel();

		}
		if (myTask != null) {
			myTask.cancel();
		}
		Log.e("service onDestroy", "service onDestroy");

		super.onDestroy();
	}

	private void isVoice() {

		if (tipsVoice != null) {
			tipsVoice = MediaPlayer.create(this, R.raw.ring);
			tipsVoice.setLooping(false);
			tipsVoice.start();
		} else {
			tipsVoice = new MediaPlayer();
			tipsVoice = MediaPlayer.create(this, R.raw.ring);
			tipsVoice.setLooping(false);
			tipsVoice.start();
		}

	}

	private void isMsg(int isMsg) {

		if (isMsg == 1) {
			getMsg();
		}
	}

	private void setNotification() {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象，指定了 图标，标题，和时间

		Notification notification = new Notification(R.drawable.ic_launcher,
				"梅州城市通", System.currentTimeMillis());
		Intent intent = new Intent(MsgService.this, MainActivity.class);
		// intent.putExtra("formnotice", 1);

		MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("fromnotice", 1);
		// 指定一个跳转的intent
		// 0
		PendingIntent pendingIntent = PendingIntent.getActivity(
				MsgService.this, 0, intent, 0);

		// 设定事件信息
		notification.setLatestEventInfo(getApplicationContext(), "梅州城市通", "你有"
				+ count + "条未读信息", pendingIntent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 自动终止
		manager.notify(0, notification);// 发起通知
	}

	public static boolean checkNetworkConnection(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// 注意状态
		if (wifi.isConnected() || mobile.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	protected static boolean isTopActivity(Context context) {

		String packageName = "com.mzeat";

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);

		if (tasksInfo.size() > 0) {

			System.out.println("---------------包名-----------"
					+ tasksInfo.get(0).topActivity.getPackageName());

			// 应用程序位于堆栈的顶层

			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {

				return true;

			}

		}

		return false;

	}

	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				count = Integer.valueOf(u_commentlist.getTotal());

				int oldCount = MzeatApplication.getInstance()
						.getpPreferencesConfig().getInt("count", 0);
				if (count != 0) {
					// 当客户端本地消息数为0
					if (oldCount == 0) {
						sendNotice();
					} else // 当客户端本地消息数不为0
					{
						// 当本地消息数与请求消息数不等
						if (count != oldCount) {
							sendNotice();
						} else {// 当本地消息数与请求消息数相等

							My_shareDb my_shareDb = new My_shareDb(
									MsgService.this);
							ArrayList<My_share> oldMy_shares = my_shareDb
									.getMy_share();
							ArrayList<My_share> newMy_shares = u_commentlist
									.getMy_share();
							my_shareDb.closeDB();

							// 先比较我的分享的评论
							if (oldMy_shares.size() != newMy_shares.size()) {
								sendNotice();
							} else {
								boolean sendnotice = oldMy_shares
										.containsAll(newMy_shares);
								if (!sendnotice) {
									sendNotice();
								} else{ // 再比较我的评论的回复
								
									U_commentlist_itemDb u_commentlist_itemDb = new U_commentlist_itemDb(
											MsgService.this);
									ArrayList<U_commentlist_item> oldItems = u_commentlist_itemDb
											.getItems();
									ArrayList<U_commentlist_item> newItems = u_commentlist
											.getItem();
									u_commentlist_itemDb.closeDB();

									boolean itemequal = oldItems
											.containsAll(newItems);
									if (!itemequal) {
										sendNotice();
									}
								}
							}
						}
					}

				}else {
					sendNotice();
				}

			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};

	private void sendNotice() {
		MzeatApplication.getInstance().getpPreferencesConfig()
				.setInt("count", count);

		My_shareDb my_shareDb = new My_shareDb(MsgService.this);
		if (my_shareDb.getMy_share() != null
				&& my_shareDb.getMy_share().size() > 0) {
			my_shareDb.deleteAll();
		}

		if (u_commentlist.getMy_share() != null) {
			my_shareDb.add(u_commentlist.getMy_share());
		}

		my_shareDb.closeDB();

		U_commentlist_itemDb u_commentlist_itemDb = new U_commentlist_itemDb(
				MsgService.this);

		if (u_commentlist_itemDb.getItems() != null
				&& u_commentlist_itemDb.getItems().size() > 0) {
			u_commentlist_itemDb.deleteAll();
		}

		if (u_commentlist.getItem() != null) {
			u_commentlist_itemDb.add(u_commentlist.getItem());
		}

		u_commentlist_itemDb.closeDB();

		if (!isTopActivity(MsgService.this)) {
			setNotification();
			isVoice();
		}

		// 刷新主页面的消息数
		Intent intent = new Intent();
		intent.putExtra("count", count);
		intent.setAction("android.intent.action.setTextView");
		// intent.setAction("android.intent.action.setViewData");// action与接收器相同
		sendBroadcast(intent);

		// 刷新消息界面
		Intent mIntent = new Intent();
		mIntent.setAction("android.intent.action.setViewData");// action与接收器相同
		sendBroadcast(mIntent);

		Log.e("sendnotice", "sendnotice");
	}

	U_commentlist u_commentlist;

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {

			// TODO Auto-generated method stub

			u_commentlist = MzeatApplication
					.getInstance()
					.getService()
					.getU_commentlist(
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("email", ""),
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("pwd", ""));

			if (u_commentlist.getOpen().equals("1")) {
				return TaskResult.OK;
			} else if (u_commentlist.getOpen().equals("0")) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

	}

	private void getMsg() {

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
}
