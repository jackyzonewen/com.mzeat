package com.mzeat;


import com.mzeat.util.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 选项设置，可以设置和获取的数据类型有：String、int、long、float
 * 
 * @author windhuiyi
 * 
 */
public class PreferencesConfig {

	private SharedPreferences mPref;
	private Context mContext;

	/**
	 * PreferencesConfig构造方法
	 * @param context
	 */
	public PreferencesConfig(Context context) {
		mContext = context;
		mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
	}

	/**
	 * 获得参数
	 * 
	 * @param name
	 * @return String
	 */
	public String getString(String name, String defaultValue) {
		String value = mPref.getString(name, defaultValue);
		return value;
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param value
	 */
	public void setString(String name, String value) {
		Editor editor = mPref.edit();
		editor.putString(name, value);
		editor.commit();
		return;
	}

	/**
	 * 获得参数
	 * 
	 * @param name
	 * @return int
	 */
	public int getInt(String name, int defaultValue) {
		String value = getString(name, String.valueOf(defaultValue));
		if (StringUtils.isEmpty(value))
			return 0;
		else
			return Integer.parseInt(value);
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param int
	 */
	public void setInt(String name, int value) {
		setString(name, String.valueOf(value));
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param long
	 */
	public void setLong(String name, long value) {
		setString(name, String.valueOf(value));
	}

	/**
	 * 获得参数
	 * 
	 * @param name
	 * @return long
	 */
	public long getLong(String name, long defaultValue) {
		String value = getString(name, String.valueOf(defaultValue));
		if (StringUtils.isEmpty(value))
			return 0;
		return Long.valueOf(value);
	}

	/**
	 * 获得参数
	 * 
	 * @param name
	 * @return float
	 */
	public float getFloat(String name, float defaultValue) {
		String value = getString(name, String.valueOf(defaultValue));
		if (StringUtils.isEmpty(value))
			return 0;
		return Float.valueOf(value);
	}

	/**
	 * 设置参数
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat(String name, Float value) {
		setString(name, String.valueOf(value));
	}
}
