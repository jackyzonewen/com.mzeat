package com.mzeat.task;

/**
 * 任务监听接口
 * @author windhuiyi
 *
 */
public interface TaskListener {
	/**
	 * 取得名字
	 * @return
	 */
	String getName();

	/**
	 * 任务准备执行
	 * @param task
	 */
	void onPreExecute(GenericTask task);

	/**
	 * 任务执行完成
	 * @param task
	 * @param result
	 */
	void onPostExecute(GenericTask task, TaskResult result);

	/**
	 * 任务进度情况
	 * @param task
	 * @param param
	 */
	void onProgressUpdate(GenericTask task, Object param);

	/**
	 * 取消任务
	 * @param task
	 */
	void onCancelled(GenericTask task);
}
