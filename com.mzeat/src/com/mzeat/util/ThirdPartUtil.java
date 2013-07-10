package com.mzeat.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import android.app.Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mzeat.model.QQUser;

public class ThirdPartUtil
{
	

	

	private static String toMd5(String str)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
		{
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++)
		{
			int val = ((int) md5Bytes[i]) & 0xff;
			if ( val < 16 )
			{
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b)
	{
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s)
	{
		try
		{
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		}
		catch ( NoSuchAlgorithmException e )
		{
			e.printStackTrace();
		}

		return "";
	}

	
	

	

	
	

	

	public static String getQQUserInfo(String token, String openId)
	{
		String url = "https://graph.qq.com/user/get_simple_userinfo";
		HashMap<String, Object> paras = new HashMap<String, Object>();
		paras.put("access_token", token);
		paras.put("oauth_consumer_key", Constants.APP_ID);
		paras.put("openid", openId);
		paras.put("format", "json");

		try
		{
			InputStream stream = HttpUtils.get(url, paras);
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while((line = in.readLine()) != null)
			{
				buffer.append(line);
			}
			String str = buffer.toString();
			return str;
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return null;
	}

	


	public static String uploadFile(String uri, String filePath) throws IOException
	{
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		DataOutputStream ds = null;
		try
		{
			File file = new File(filePath);
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file1\";filename=\"" + file.getName() + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(file);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while((length = fStream.read(buffer)) != -1)
			{
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));

			StringBuffer strBuffer = new StringBuffer();
			String line = "";
			while((line = in.readLine()) != null)
			{
				strBuffer.append(line);
			}
			return strBuffer.toString();

		}
		catch ( Exception e )
		{
			System.err.println(e);
		}
		finally
		{
			if ( ds != null )
				ds.close();
		}
		return "";
	}
}
