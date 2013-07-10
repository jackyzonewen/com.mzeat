package com.mzeat.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils
{
	/**
	 * 发送一个GET请求
	 * 
	 * @param urlString
	 *            请求路径：http://192.168.10.253:8080/web/login
	 * @param params
	 *            请求参数{phone=母鸡, password=123}
	 * @return
	 */
	public static InputStream get(String urlString, HashMap<String, Object> params) throws Exception
	{
		StringBuilder sb = new StringBuilder(urlString);

		if ( params != null && !params.isEmpty() )
		{
			// http://192.168.10.253:8080/web/login?
			sb.append("?");

			for (Entry<String, Object> entry : params.entrySet())
			{
				String key = entry.getKey();
				String value = entry.getValue().toString();

				// 需要对参数进行转码
				//
				// http://192.168.10.253:8080/web/login?phone=123&
				sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
			}

			// 删除最后一个字符串&
			// http://192.168.10.253:8080/web/login?phone=123
			sb.deleteCharAt(sb.length() - 1);
		}

		System.out.println(sb.toString());
		URL url = new URL(sb.toString());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方法
		conn.setRequestMethod("GET");

		if ( conn.getResponseCode() == 200 )
		{ // 请求正确
			return conn.getInputStream();
		}

		return null;
	}

	public static InputStream post(String urlString, HashMap<String, Object> params) throws Exception
	{
		URL url = new URL(urlString);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");

		if ( params != null )
		{
			StringBuilder sb = new StringBuilder();
			for (Entry<String, Object> entry : params.entrySet())
			{
				String key = entry.getKey();
				String value = entry.getValue().toString();

				// 需要对参数进行转码
				//
				// phone=123&password=123&
				sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
			}
			// 删除最后一个字符串&
			// phone=123&password=123
			sb.deleteCharAt(sb.length() - 1);

			byte[] data = sb.toString().getBytes();
			// 设置请求头信息
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", data.length + "");

			// 设置请求体(将参数写到服务器端)
			conn.setDoOutput(true); // 设置这个参数才可以写出数据
			OutputStream os = conn.getOutputStream();
			os.write(data);
			os.flush();
			os.close();
		}

		if ( conn.getResponseCode() == 200 )
		{ // 请求正确
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * 通过HttpClient发送一个post请求
	 */
	public static InputStream postByHttpClient(String urlString, HashMap<String, Object> params) throws Exception
	{
		// 初始化一个POST请求
		HttpPost post = new HttpPost(urlString);

		if ( params != null )
		{
			// 初始化请求参数
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Entry<String, Object> entry : params.entrySet())
			{
				String key = entry.getKey();
				String value = entry.getValue().toString();

				BasicNameValuePair pair = new BasicNameValuePair(key, value);
				parameters.add(pair);
			}
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
			// 设置请求参数
			post.setEntity(formEntity);
		}

		// 相当于一个浏览器
		HttpClient client = new DefaultHttpClient();
		// 执行post请求
		HttpResponse response = client.execute(post);

		//Print.out("响应码：" + response.getStatusLine().getStatusCode());

		if ( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
		{
			// 封装了服务器端返回的数据
			HttpEntity responseEntity = response.getEntity();
			// 将服务器返回的输入流 解析为 字符串
			// EntityUtils.toString(responseEntity)

			return responseEntity.getContent();
		}
		return null;
	}

}
