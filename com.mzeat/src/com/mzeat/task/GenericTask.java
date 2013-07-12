package com.mzeat.task;

import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 抽象类GenericTask,从AsyncTask继承，并实现Observer
 * 
 * @author windhuiyi
 * 
 */
public abstract class GenericTask extends AsyncTask<TaskParams, Object, TaskResult> implements Observer {

	private static final String TAG = "GenericTask";

	private TaskListener mListener = null;
	private boolean isCancelable = true;

	/**
	 * 抽象方法_doInBackground
	 * @param params
	 * @return
	 */
	abstract protected TaskResult _doInBackground(TaskParams... params);

	/**
	 * 设置监听者
	 * @param taskListener
	 */
	public void setListener(TaskListener taskListener) {
		mListener = taskListener;
	}

	/**
	 * 取得监听者
	 * @return
	 */
	public TaskListener getListener() {
		return mListener;
	}

	/**
	 * 显示任务进度
	 * @param values
	 */
	public void doPublishProgress(Object... values) {
		super.publishProgress(values);
	}

	/**
	 * 取消任务
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();

		if (mListener != null) {
			mListener.onCancelled(this);
		}
		//Log.i(TAG, mListener.getName() + " has been Cancelled.");
	}

	/**
	 * 任务执行完成
	 */
	@Override
	protected void onPostExecute(TaskResult result) {
		super.onPostExecute(result);

		if (mListener != null) {
			mListener.onPostExecute(this, result);
		}
	}

	/**
	 * 任务准备执行
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mListener != null) {
			mListener.onPreExecute(this);
		}
	}

	/**
	 * 任务进度情况
	 */
	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);

		if (mListener != null) {
			if (values != null && values.length > 0) {
				mListener.onProgressUpdate(this, values[0]);
			}
		}
	}

	/**
	 * 任务后台执行
	 */
	@Override
	protected TaskResult doInBackground(TaskParams... params) {
		return _doInBackground(params);
	}

	/**
	 * 更新
	 */
	public void update(Observable o, Object arg) {
		if (TaskManager.CANCEL_ALL == (Integer) arg && isCancelable) {
			if (getStatus() == GenericTask.Status.RUNNING) {
				cancel(true);
			}
		}
	}

	/**
	 * 设置是否可以取消
	 * @param flag
	 */
	public void setCancelable(boolean flag) {
		isCancelable = flag;
	}

}
