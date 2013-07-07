package com.mzeat.task;

/**
 * 抽象类，任务适配器，实现任务监听接口
 * @author windhuiyi
 *
 */
public abstract class TaskAdapter implements TaskListener {

	public abstract String getName();

	public void onPreExecute(GenericTask task) {
		// TODO Auto-generated method stub

	}

	public void onPostExecute(GenericTask task, TaskResult result) {
		// TODO Auto-generated method stub

	}

	public void onProgressUpdate(GenericTask task, Object param) {
		// TODO Auto-generated method stub

	}

	public void onCancelled(GenericTask task) {
		// TODO Auto-generated method stub

	}

}
