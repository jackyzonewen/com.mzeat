package com.mzeat.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 响应,将响应转换为Stream、String、Document、JSONObject、JSONArray、Reader
 * 
 * @author windhuiyi
 * 
 */
public class Response {
	// private final static String TAG = "HttpClient";
	private final static String TAG = "Response";

	private final static boolean DEBUG = true;

	private static ThreadLocal<DocumentBuilder> builders = new ThreadLocal<DocumentBuilder>() {
		@Override
		protected DocumentBuilder initialValue() {
			try {
				return DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				throw new ExceptionInInitializerError(ex);
			}
		}
	};

	private int statusCode;
	private Document responseAsDocument = null;
	private String responseAsString = null;
	private InputStream is;
	private HttpURLConnection con;
	private boolean streamConsumed = false;

	private HttpResponse response;
	private StatusLine statusLine;
	private Header[] responseHeader;

	/**
	 * Response构造方法
	 */
	public Response() {

	}

	/**
	 * Response构造方法
	 */
	public Response(HttpResponse response, String url) throws IOException {
		this.response = response;
		this.statusLine = response.getStatusLine();
		this.statusCode = statusLine.getStatusCode();
		this.responseHeader = response.getAllHeaders();

		HttpEntity entity = response.getEntity();
		this.is = entity.getContent();
		Header contentEncoding = entity.getContentEncoding();
		if (null != is && contentEncoding != null
				&& "gzip".equals(contentEncoding.getValue())) {
			// the response is gzipped
			is = new GZIPInputStream(is);
		}
	}

	// for test purpose
	/* package */Response(String content) {
		this.responseAsString = content;
	}

	/**
	 * 取得状态码
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 取得响应头
	 * @param name
	 * @return
	 */
	public String getResponseHeader(String name) {
		if (response != null) {
			Header[] header = response.getHeaders(name);
			if (header.length > 0) {
				return header[0].getValue();
			}
		}
		return null;
	}

	/**
	 * Returns the response stream.返回响应流<br>
	 * This method cannot be called after calling asString() or asDcoument()这个方法不能在调用asString()或者asDcoument()之后调用<br>
	 * It is suggested to call disconnect() after consuming the stream.建议在消耗流之后断开连接的时候调用。
	 * 
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 * @return response body stream
	 * @throws HttpException
	 * @see #disconnect()
	 */
	public InputStream asStream() {
		if (streamConsumed) {
			throw new IllegalStateException("Stream has already been consumed.");
		}
		return is;
	}

	/**
	 * Returns the response body as string.像字符串一样返回响应体<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 * @return response body
	 * @throws HttpException
	 */
	public String asString() throws ResponseException {
		if (null == responseAsString) {
			BufferedReader br;
			try {
				InputStream stream = asStream();
				if (null == stream) {
					return null;
				}
				br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
				StringBuffer buf = new StringBuffer();
				String line;
				while (null != (line = br.readLine())) {
					buf.append(line).append("\n");
				}
				this.responseAsString = buf.toString();
				this.responseAsString = unescape(responseAsString);
				// log(responseAsString);
				stream.close();
				streamConsumed = true;
			} catch (NullPointerException npe) {
				// don't remember in which case npe can be thrown
				throw new ResponseException(npe.getMessage(), npe);
			} catch (IOException ioe) {
				throw new ResponseException(ioe.getMessage(), ioe);
			}
		}
		return responseAsString;
	}

	/**
	 * Returns the response body as org.w3c.dom.Document.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 * @return response body as org.w3c.dom.Document
	 * @throws HttpException
	 */
	public Document asDocument() throws ResponseException {
		if (null == responseAsDocument) {
			try {
				// it should be faster to read the inputstream directly.
				// but makes it difficult to troubleshoot
				this.responseAsDocument = builders.get().parse(
						new ByteArrayInputStream(asString().getBytes("UTF-8")));
			} catch (SAXException saxe) {
				throw new ResponseException(
						"The response body was not well-formed:\n"
								+ responseAsString, saxe);
			} catch (IOException ioe) {
				throw new ResponseException(
						"There's something with the connection.", ioe);
			} catch (DOMException dome) {
				throw new ResponseException(
						"The response body was not well-formed:\n"
								+ responseAsString, dome);
			}
		}
		return responseAsDocument;
	}

	/**
	 * Returns the response body as sinat4j.org.json.JSONObject.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 * @return response body as sinat4j.org.json.JSONObject
	 * @throws HttpException
	 */
	public JSONObject asJSONObject() throws ResponseException {
		try {
			return new JSONObject(asString());
		} catch (JSONException jsone) {
			throw new ResponseException(jsone.getMessage() + ":"
					+ this.responseAsString, jsone);
		}
	}

	/**
	 * Returns the response body as sinat4j.org.json.JSONArray.<br>
	 * Disconnects the internal HttpURLConnection silently.
	 * 
	 * @return response body as sinat4j.org.json.JSONArray
	 * @throws HttpException
	 */
	public JSONArray asJSONArray() throws ResponseException {
		try {
			return new JSONArray(asString());
		} catch (Exception jsone) {
			throw new ResponseException(jsone.getMessage() + ":"
					+ this.responseAsString, jsone);
		}
	}

	/**
	 * 将InputStream转化成InputStreamReader
	 * @return
	 */
	public InputStreamReader asReader() {
		try {
			return new InputStreamReader(is, "UTF-8");
		} catch (java.io.UnsupportedEncodingException uee) {
			return new InputStreamReader(is);
		}
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		con.disconnect();
	}

	private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");

	/**
	 * Unescape UTF-8 escaped characters to string.
	 * 使用UTF-8编码转义字符到字符串
	 * @author pengjianq...@gmail.com
	 * 
	 * @param original
	 *            The string to be unescaped.
	 * @return The unescaped string
	 */
	public static String unescape(String original) {
		Matcher mm = escaped.matcher(original);
		StringBuffer unescaped = new StringBuffer();
		while (mm.find()) {
			mm.appendReplacement(unescaped, Character.toString((char) Integer
					.parseInt(mm.group(1), 10)));
		}
		mm.appendTail(unescaped);
		return unescaped.toString();
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "Response [statusCode=" + statusCode + ", responseAsDocument="
				+ responseAsDocument + ", responseAsString=" + responseAsString
				+ ", is=" + is + ", con=" + con + ", streamConsumed="
				+ streamConsumed + ", response=" + response + ", statusLine="
				+ statusLine + ", responseHeader="
				+ Arrays.toString(responseHeader) + "]";
	}

	/**
	 * 取得响应字符串
	 * @return
	 */
	public String getResponseAsString() {
		return responseAsString;
	}

	/**
	 * 设置响应字符串
	 * @param responseAsString
	 */
	public void setResponseAsString(String responseAsString) {
		this.responseAsString = responseAsString;
	}

	/**
	 * 设置状态码
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
