package com.mzeat.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * json工具类
 * 
 * @author windhuiyi
 * 
 */
public class JsonUtils {

	public static JSONObject fromObject(Object object) throws JSONException {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat("yyyy-MM-dd hh:mm:ss");
		gsonb.registerTypeAdapter(java.util.Date.class,
				new DateTimeTypeAdapter());
		Gson gson = gsonb.create();
		return new JSONObject(gson.toJson(object));
	}

	public static JSONArray fromObject_Array(Object object)
			throws JSONException {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return new JSONArray(gson.toJson(object));
	}

	
	/**
	 * 用GJSON将String转换成对象
	 * @param jsonString
	 * @param beanclass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object toBean(String jsonString, Class beanclass) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = gsonb.create();
		return gson.fromJson(jsonString, beanclass);
	}

	/**
	 * <code>toBean</code>
	 * 
	 * @description: TODO(json对象转化为类)
	 * @param object
	 * @param beanclass
	 * @return
	 * @since Apr 11, 2011 zhangzhanqiang
	 */
	@SuppressWarnings("unchecked")
	public static Object toBean(JSONObject object, Class beanclass) {
		return toBean(object.toString(), beanclass);
	}
}
