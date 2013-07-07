package com.mzeat.util;

import android.util.Log;

public class LogUtil {

	public static void getLogOnCreat(String activityname){
		Log.e(activityname, "oncreat");
	}
	public static void getLogOnResume(String activityname){
		Log.e(activityname, "onresume");
	}
	public static void getLogOnPause(String activityname){
		Log.e(activityname, "onPause");
	}
	public static void getLogOnStop(String activityname){
		Log.e(activityname, "onStop");
	}
	public static void getLogOnDestroy(String activityname){
		Log.e(activityname, "onDestroy");
	}
	
	public static void getLogOnStart(String activityname){
		Log.e(activityname, "onStart");
	}
	public static void getLogOnRestart(String activityname){
		Log.e(activityname, "onRestart");
	}
}
