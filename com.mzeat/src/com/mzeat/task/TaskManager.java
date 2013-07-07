package com.mzeat.task;

import java.util.Observable;
import java.util.Observer;

import android.util.Log;

/**
 * 任务管理，从Observable继承。
 * @author windhuiyi
 *
 */
public class TaskManager extends Observable {
	private static final String TAG = "TaskManager";

	public static final Integer CANCEL_ALL = 1;

	/**
	 * 取消全部
	 */
	public void cancelAll() {
		Log.i(TAG, "All task Cancelled.");
		setChanged();
		notifyObservers(CANCEL_ALL);
	}

	/**
	 * 添加任务
	 * @param task
	 */
	public void addTask(Observer task) {
		super.addObserver(task);
	}
}
