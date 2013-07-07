package com.mzeat.http;

import java.io.File;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Wrap of org.apache.http.impl.client.DefaultHttpClient 
 * 封装DefaultHttpClient类，有post和get方法
 * @author lds
 * 
 */
public class HttpClient {

	private static final String TAG = "HttpClient";
	private static final boolean DEBUG = true;

	/** OK: Success! */
	public static final int OK = 200;
	/** Not Modified: There was no new data to return. */
	public static final int NOT_MODIFIED = 304;
	/**
	 * Bad Request: The request was invalid. An accompanying error message will
	 * explain why. This is the status code will be returned during rate
	 * limiting.
	 */
	public static final int BAD_REQUEST = 400;
	/** Not Authorized: Authentication credentials were missing or incorrect. */
	public static final int NOT_AUTHORIZED = 401;
	/**
	 * Forbidden: The request is understood, but it has been refused. An
	 * accompanying error message will explain why.
	 */
	public static final int FORBIDDEN = 403;
	/**
	 * Not Found: The URI requested is invalid or the resource requested, such
	 * as a user, does not exists.
	 */
	public static final int NOT_FOUND = 404;
	/**
	 * Not Acceptable: Returned by the Search API when an invalid format is
	 * specified in the request.
	 */
	public static final int NOT_ACCEPTABLE = 406;
	/**
	 * Internal Server Error: Something is broken. Please post to the group so
	 * the Weibo team can investigate.
	 */
	public static final int INTERNAL_SERVER_ERROR = 500;
	/** Bad Gateway: Weibo is down or being upgraded. */
	public static final int BAD_GATEWAY = 502;
	/**
	 * Service Unavailable: The Weibo servers are up, but overloaded with
	 * requests. Try again later. The search and trend methods use this to
	 * indicate when you are being rate limited.
	 */
	public static final int SERVICE_UNAVAILABLE = 503;

	private static final int CONNECTION_TIMEOUT_MS = 8 * 1000;
	private static final int SOCKET_TIMEOUT_MS = 8 * 1000;

	public static final int RETRIEVE_LIMIT = 20;
	public static final int RETRIED_TIME = 3;

	private static final String SERVER_HOST = "api.fanfou.com";

	private DefaultHttpClient mClient;
	private AuthScope mAuthScope;
	private BasicHttpContext localcontext;
	private String mUserId;
	private String mPassword;
	/**
	 * 需要加载http头中，每次请求都必须带上。 格式为：Mozilla/5.5 (设备名称;操作系统;设备串号;分辨率;用户语言) 产品名称/发布日期
	 * ver/版本号
	 */
	private String Mozilla = null;
	private static boolean isAuthenticationEnabled = false;

	/**
	 * 构造方法
	 */
	public HttpClient() {
		prepareHttpClient();
	}

	/**
	 * 构造方法
	 * 
	 * @param user_id
	 *            auth user
	 * @param password
	 *            auth password
	 */
	public HttpClient(String user_id, String password) {
		prepareHttpClient();
		setCredentials(user_id, password);
	}

	/**
	 * 重新设置 Empty the credentials
	 * 
	 */
	public void reset() {
		setCredentials("", "");
	}

	/**
	 * 取得用户ID
	 * 
	 * @return authed user id
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * 取得密码
	 * 
	 * @return authed user password
	 */
	public String getPassword() {
		return mPassword;
	}

	/**
	 * 设置代理
	 * 
	 * @param hostname
	 *            the hostname (IP or DNS name)
	 * @param port
	 *            the port number. -1 indicates the scheme default port.
	 * @param scheme
	 *            the name of the scheme. null indicates the default scheme
	 */
	public void setProxy(String host, int port, String scheme) {
		HttpHost proxy = new HttpHost(host, port, scheme);
		mClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}

	/**
	 * 取消代理
	 */
	public void removeProxy() {
		mClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	/**
	 * 启用调试
	 */
	private void enableDebug() {
		Log.i(TAG, "enable apache.http debug");

		java.util.logging.Logger.getLogger("org.apache.http").setLevel(
				java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(
				java.util.logging.Level.FINER);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(
				java.util.logging.Level.OFF);
	}

	/**
	 * 准备HttpClient Setup DefaultHttpClient
	 * 
	 * Use ThreadSafeClientConnManager.
	 * 
	 */
	private void prepareHttpClient() {
		if (DEBUG) {
			enableDebug();
		}

		// Create and initialize HTTP parameters 建立和初始化HTTP参数
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 10);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry 建立和初始化scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// 建立一个ThreadSafeClientConnManager的HttpClient
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);

		// TODO: need to release this connection in httpRequest()
		// cm.releaseConnection(conn, validDuration, timeUnit);

		// Setup BasicAuth 设置BasicAuth
		BasicScheme basicScheme = new BasicScheme();
		mAuthScope = new AuthScope(SERVER_HOST, AuthScope.ANY_PORT);

		// mClient.setAuthSchemes(authRegistry);
		mClient.setCredentialsProvider(new BasicCredentialsProvider());

		// Generate BASIC scheme object and stick it to the local
		// execution context
		localcontext = new BasicHttpContext();
		localcontext.setAttribute("preemptive-auth", basicScheme);

		// first request interceptor
		mClient.addRequestInterceptor(preemptiveAuth, 0);
	}

	/**
	 * HttpRequestInterceptor for DefaultHttpClient 默认HTTP客户端的请求拦截器
	 */
	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
		@Override
		public void process(final HttpRequest request, final HttpContext context) {
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(),
						targetHost.getPort());
				Credentials creds = credsProvider.getCredentials(authScope);
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}
	};

	/**
	 * Setup Credentials for HTTP Basic Auth 给HTTP基本认证设置证书
	 * 
	 * @param username
	 * @param password
	 */
	public void setCredentials(String username, String password) {
		mUserId = username;
		mPassword = password;
		mClient.getCredentialsProvider().setCredentials(mAuthScope,
				new UsernamePasswordCredentials(username, password));
		isAuthenticationEnabled = true;
	}

	/**
	 * post方法1,有参数，没有文件，带验证。
	 * 
	 * @param url
	 * @param postParams
	 * @param authenticated
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, ArrayList<BasicNameValuePair> postParams,
			boolean authenticated) throws HttpException {
		if (null == postParams) {
			postParams = new ArrayList<BasicNameValuePair>();
		}
		return httpRequest(url, postParams, authenticated, HttpPost.METHOD_NAME);
	}

	public Response post(String url,
			ArrayList<BasicNameValuePair> postParams, ArrayList<File> file, ArrayList<String> filename
			 ) throws HttpException {
		return httpRequest(url, postParams, file, filename, false, HttpPost.METHOD_NAME);
	}
	
	/**
	 * POST方法之2，有参数，没有文件，不带验证。
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, ArrayList<BasicNameValuePair> params)
			throws HttpException {
		return httpRequest(url, params, false, HttpPost.METHOD_NAME);
	}

	/**
	 * POST方法之3，无参数，没有文件，带验证。
	 * 
	 * @param url
	 * @param authenticated
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, boolean authenticated)
			throws HttpException {
		return httpRequest(url, null, authenticated, HttpPost.METHOD_NAME);
	}

	/**
	 * post方法之4，无参数，没有文件，不带验证。
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url) throws HttpException {
		return httpRequest(url, null, false, HttpPost.METHOD_NAME);
	}

	/**
	 * POST方法之5，有参数，带一个文件和文件名。
	 * 
	 * @param url
	 * @param params
	 * @param file
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, ArrayList<BasicNameValuePair> params, File file,String filename) throws HttpException {
		return httpRequest(url, params, file, filename,false, HttpPost.METHOD_NAME);
	}

	/**
	 * POST方法之6，无参数，带一个文件和文件名。
	 * 
	 * @param url
	 * @param file
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, File file, String filename) throws HttpException {
		return httpRequest(url, null, file,filename ,false, HttpPost.METHOD_NAME);
	}
	
	/**
	 * POST方法之7，无参数，带一个文件和文件名，带验证。
	 * 
	 * @param url
	 * @param file
	 * @param authenticate
	 * @return
	 * @throws HttpException
	 */
	public Response post(String url, File file, String filename,boolean authenticate)
			throws HttpException {
		return httpRequest(url, null, file,filename, authenticate, HttpPost.METHOD_NAME);
	}

	/**
	 * GET方法之一
	 * 
	 * @param url
	 * @param params
	 * @param authenticated
	 * @return
	 * @throws HttpException
	 */
	public Response get(String url, ArrayList<BasicNameValuePair> params,
			boolean authenticated) throws HttpException {
		return httpRequest(url, params, authenticated, HttpGet.METHOD_NAME);
	}

	/**
	 * GET方法之一
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws HttpException
	 */
	public Response get(String url, ArrayList<BasicNameValuePair> params)
			throws HttpException {
		return httpRequest(url, params, false, HttpGet.METHOD_NAME);
	}

	/**
	 * HttpGet方法之一
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 * 
	 */
	public Response get(String url) throws HttpException {
		return httpRequest(url, null, false, HttpGet.METHOD_NAME);
	}

	/**
	 * GET方法之一
	 * 
	 * @param url
	 * @param authenticated
	 * @return
	 * @throws HttpException
	 */
	public Response get(String url, boolean authenticated) throws HttpException {
		return httpRequest(url, null, authenticated, HttpGet.METHOD_NAME);
	}

	/**
	 * HttpRequest方法之一,没有文件
	 * 
	 * @param url
	 * @param postParams
	 * @param authenticated
	 * @param httpMethod
	 * @return
	 * @throws HttpException
	 */
	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, boolean authenticated,
			String httpMethod) throws HttpException {
		return httpRequest(url, postParams, null,"", authenticated, httpMethod);
	}

	/**
	 * Execute the DefaultHttpClient
	 * 
	 * @param url
	 *            target
	 * @param postParams
	 * @param file
	 *            can be NULL
	 * @param authenticated
	 *            need or not
	 * @param httpMethod
	 *            HttpPost.METHOD_NAME HttpGet.METHOD_NAME
	 *            HttpDelete.METHOD_NAME
	 * @return Response from server
	 * @throws HttpException
	 *             此异常包装了一系列底层异常 <br />
	 * <br />
	 *             1. 底层异常, 可使用getCause()查看: <br />
	 *             <li>URISyntaxException, 由`new URI` 引发的.</li> <li>IOException,
	 *             由`createMultipartEntity` 或 `UrlEncodedFormEntity` 引发的.</li>
	 *             <li>IOException和ClientProtocolException,
	 *             由`HttpClient.execute` 引发的.</li><br />
	 * 
	 *             2. 当响应码不为200时报出的各种子类异常: <li>HttpRequestException,
	 *             通常发生在请求的错误,如请求错误了 网址导致404等, 抛出此异常, 首先检查request log,
	 *             确认不是人为错误导致请求失败</li> <li>HttpAuthException, 通常发生在Auth失败,
	 *             检查用于验证登录的用户名/密码/KEY等</li> <li>HttpRefusedException,
	 *             通常发生在服务器接受到请求, 但拒绝请求, 可是多种原因, 具体原因 服务器会返回拒绝理由,
	 *             调用HttpRefusedException#getError#getMessage查看</li> <li>
	 *             HttpServerException, 通常发生在服务器发生错误时, 检查服务器端是否在正常提供服务</li> <li>
	 *             HttpException, 其他未知错误.</li>
	 * @throws UnsupportedEncodingException
	 */
	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, File file,String filename,
			boolean authenticated, String httpMethod) throws HttpException {
		Log.i(TAG, "Sending " + httpMethod + " request to " + url);
		// 开始时间
		long startTime = System.currentTimeMillis();

		String keyUrl = url;
		// 构造请求url
		if (null != postParams) {
			StringBuilder sb = new StringBuilder();
			for (BasicNameValuePair nameValue : postParams) {
				sb.append(nameValue.getName());
				sb.append("=");
				sb.append(nameValue.getValue());
			}
			keyUrl = url + "?" + sb.toString();
			Log.e(TAG, keyUrl);
		}

		URI uri = createURI(url);

		HttpResponse response = null;
		Response res = null;
		HttpUriRequest method = null;

		// Create POST, GET or DELETE METHOD
		// 建立POST,GET或DELECT方法
		method = createMethod(httpMethod, uri, file,filename, postParams);
		// Setup ConnectionParams
		// 建立连接参数
		SetupHTTPConnectionParams(method);
		// Execute Request
		// 执行请求
		try {

			response = mClient.execute(method, localcontext);
			res = new Response(response, keyUrl);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (IOException ioe) {
			throw new HttpException(ioe.getMessage(), ioe);
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (NullPointerException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		}
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			// It will throw a weiboException while status code is not 200
			HandleResponseStatusCode(statusCode, res);
		} else {
			Log.e(TAG, "response is null");
		}

		//结束时间
		long endTime = System.currentTimeMillis();
		Log.d(TAG, "Http request in " + (endTime - startTime));

		// TODO: response内容DEBUG输出
		return res;

	}
	/**
	 * Execute the DefaultHttpClient
	 * 
	 * @param url
	 *            target
	 * @param postParams
	 * @param file
	 *            can be NULL
	 * @param authenticated
	 *            need or not
	 * @param httpMethod
	 *            HttpPost.METHOD_NAME HttpGet.METHOD_NAME
	 *            HttpDelete.METHOD_NAME
	 * @return Response from server
	 * @throws HttpException
	 *             此异常包装了一系列底层异常 <br />
	 * <br />
	 *             1. 底层异常, 可使用getCause()查看: <br />
	 *             <li>URISyntaxException, 由`new URI` 引发的.</li> <li>IOException,
	 *             由`createMultipartEntity` 或 `UrlEncodedFormEntity` 引发的.</li>
	 *             <li>IOException和ClientProtocolException,
	 *             由`HttpClient.execute` 引发的.</li><br />
	 * 
	 *             2. 当响应码不为200时报出的各种子类异常: <li>HttpRequestException,
	 *             通常发生在请求的错误,如请求错误了 网址导致404等, 抛出此异常, 首先检查request log,
	 *             确认不是人为错误导致请求失败</li> <li>HttpAuthException, 通常发生在Auth失败,
	 *             检查用于验证登录的用户名/密码/KEY等</li> <li>HttpRefusedException,
	 *             通常发生在服务器接受到请求, 但拒绝请求, 可是多种原因, 具体原因 服务器会返回拒绝理由,
	 *             调用HttpRefusedException#getError#getMessage查看</li> <li>
	 *             HttpServerException, 通常发生在服务器发生错误时, 检查服务器端是否在正常提供服务</li> <li>
	 *             HttpException, 其他未知错误.</li>
	 * @throws UnsupportedEncodingException
	 */
	public Response httpRequest(String url,
			ArrayList<BasicNameValuePair> postParams, ArrayList<File> file, ArrayList<String> filename,
			boolean authenticated, String httpMethod) throws HttpException {
		Log.i(TAG, "Sending " + httpMethod + " request to " + url);
		// 开始时间
		long startTime = System.currentTimeMillis();

		String keyUrl = url;
		// 构造请求url
		if (null != postParams) {
			StringBuilder sb = new StringBuilder();
			for (BasicNameValuePair nameValue : postParams) {
				sb.append(nameValue.getName());
				sb.append("=");
				sb.append(nameValue.getValue());
				//sb.append("?");
			}
			//keyUrl = url + "?" + sb.toString();
			keyUrl = url  + sb.toString();
			Log.e(TAG, keyUrl);
		}

		URI uri = createURI(url);

		HttpResponse response = null;
		Response res = null;
		HttpUriRequest method = null;

		// Create POST, GET or DELETE METHOD
		// 建立POST,GET或DELECT方法
		method = createMethod(httpMethod, uri, file,filename, postParams);
		// Setup ConnectionParams
		// 建立连接参数
		SetupHTTPConnectionParams(method);
		// Execute Request
		// 执行请求
		try {

			response = mClient.execute(method, localcontext);
			res = new Response(response, keyUrl);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (IOException ioe) {
			throw new HttpException(ioe.getMessage(), ioe);
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		} catch (NullPointerException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException(e.getMessage(), e);
		}
		if (response != null) {
			int statusCode = response.getStatusLine().getStatusCode();
			// It will throw a weiboException while status code is not 200
			HandleResponseStatusCode(statusCode, res);
		} else {
			Log.e(TAG, "response is null");
		}

		//结束时间
		long endTime = System.currentTimeMillis();
		Log.d(TAG, "Http request in " + (endTime - startTime));

		// TODO: response内容DEBUG输出
		return res;

	}
	/**
	 * CreateURI from URL string 
	 * 从URL字符串建立URI
	 * @param url
	 * @return request URI
	 * @throws HttpException
	 *             Cause by URISyntaxException
	 */
	private URI createURI(String url) throws HttpException {
		URI uri;

		try {
			uri = new URI(url);
			Log.e(TAG, url);
		} catch (URISyntaxException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new HttpException("Invalid URL.");
		}

		return uri;
	}

	/**
	 * 创建可带一个File的MultipartEntity
	 * 
	 * @param filename
	 *            文件名
	 * @param file
	 *            文件
	 * @param postParams
	 *            其他POST参数
	 * @return 带文件和其他参数的Entity
	 * @throws UnsupportedEncodingException
	 */
	private MultipartEntity createMultipartEntity(String filename, File file,
			ArrayList<BasicNameValuePair> postParams)
			throws UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();
		// Don't try this. Server does not appear to support chunking.
		// entity.addPart("media", new InputStreamBody(imageStream, "media"));

		entity.addPart(filename, new FileBody(file));
		Charset chars = Charset.forName("UTF-8"); 
		for (BasicNameValuePair param : postParams) {
			entity.addPart(param.getName(), new StringBody(param.getValue(),chars));
		}
		return entity;
	}

	/**
	 * 创建可带几个File的MultipartEntity
	 * 
	 * @param filename
	 *            文件名
	 * @param file
	 *            文件
	 * @param postParams
	 *            其他POST参数
	 * @return 带文件和其他参数的Entity
	 * @throws UnsupportedEncodingException
	 * @throws HttpException 
	 */
	private MultipartEntity createMultipartEntity(ArrayList<String> filename, ArrayList<File> file,
			ArrayList<BasicNameValuePair> postParams)
			throws UnsupportedEncodingException, HttpException {
		MultipartEntity entity = new MultipartEntity();
		// Don't try this. Server does not appear to support chunking.
		// entity.addPart("media", new InputStreamBody(imageStream, "media"));
		for (int i = 0; i < file.size(); i++) {
			entity.addPart(filename.get(i), new FileBody(file.get(i)));
		}
		
		Charset chars = Charset.forName("UTF-8"); 
		for (BasicNameValuePair param : postParams) {
			entity.addPart(param.getName(), new StringBody(param.getValue(),chars));
		}
		
		return entity;
	}
	
	

	/**
	 * Create request method, such as POST, GET, DELETE 建立请求方法，如POST/GET/DELETE
	 * 
	 * @param httpMethod
	 *            "GET","POST","DELETE"
	 * @param uri
	 *            请求的URI
	 * @param file
	 *            可为null
	 * @param postParams
	 *            POST参数
	 * @return httpMethod Request implementations for the various HTTP methods
	 *         like GET and POST.
	 * @throws HttpException
	 *             createMultipartEntity 或 UrlEncodedFormEntity引发的IOException
	 */
	private HttpUriRequest createMethod(String httpMethod, URI uri, File file,String filename,
			ArrayList<BasicNameValuePair> postParams) throws HttpException {

		HttpUriRequest method;

		if (httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
			// POST METHOD

			HttpPost post = new HttpPost(uri);
			// See this:
			// http://groups.google.com/group/twitter-development-talk/browse_thread/thread/e178b1d3d63d8e3b
			post.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);

			try {
				HttpEntity entity = null;
				// 当文件不为空时
				if (null != file) {
					entity = createMultipartEntity(filename, file, postParams);
					post.setEntity(entity);
				} else if (null != postParams)// 当参数不为空时
				{
					entity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
					post.setEntity(entity);
					
				}
			} catch (IOException ioe) {
				throw new HttpException(ioe.getMessage(), ioe);
			}

			method = post;
		} else if (httpMethod.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
			method = new HttpDelete(uri);
		} else {
			method = new HttpGet(uri);
		}

		return method;
	}
	/**
	 * Setup HTTPConncetionParams 
	 * 设置HTTP连接参数
	 * @param method
	 */
	private void SetupHTTPConnectionParams(HttpUriRequest method) {
		
		HttpConnectionParams.setConnectionTimeout(method.getParams(),CONNECTION_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(method.getParams(), SOCKET_TIMEOUT_MS);
		HttpClientParams.setRedirecting(method.getParams(), true);
		mClient.setHttpRequestRetryHandler(requestRetryHandler);
		method.addHeader("Accept-Encoding", "gzip, deflate");
		method.addHeader("User-Agent", Mozilla);

		//手动设置重定向
		mClient.setRedirectHandler(new RedirectHandler() {
			@Override
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
						|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| statusCode == HttpStatus.SC_SEE_OTHER
						|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)
					return true;
				else
					return false;
			}

			@Override
			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
						|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| statusCode == HttpStatus.SC_SEE_OTHER
						|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
					Header[] headers = response.getHeaders("Location");

					if (headers != null && headers.length != 0) {
						String newUrl = headers[headers.length - 1].getValue();
						try {
							if (newUrl.indexOf("c/..\\") != -1) {
								String url = newUrl.replace("c/..\\", "");
								url = url.replace("\\", "/");
								newUrl = url; 
								Log.e(TAG, url);
							}
							return new URI(newUrl);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				return null;
			}
		});

	}

	/**
	 * Create request method, such as POST, GET, DELETE 建立请求方法，如POST/GET/DELETE
	 * 
	 * @param httpMethod
	 *            "GET","POST","DELETE"
	 * @param uri
	 *            请求的URI
	 * @param file
	 *            可为null
	 * @param postParams
	 *            POST参数
	 * @return httpMethod Request implementations for the various HTTP methods
	 *         like GET and POST.
	 * @throws HttpException
	 *             createMultipartEntity 或 UrlEncodedFormEntity引发的IOException
	 */
	private HttpUriRequest createMethod(String httpMethod, URI uri, ArrayList<File> file,ArrayList<String> filename,
			ArrayList<BasicNameValuePair> postParams) throws HttpException {

		HttpUriRequest method;

		if (httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
			// POST METHOD

			HttpPost post = new HttpPost(uri);
			// See this:
			// http://groups.google.com/group/twitter-development-talk/browse_thread/thread/e178b1d3d63d8e3b
			post.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);

			try {
				HttpEntity entity = null;
				// 当文件不为空时
				if (null != file) {
					entity = createMultipartEntity(filename, file, postParams);
					post.setEntity(entity);
				} else if (null != postParams)// 当参数不为空时
				{
					entity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
					post.setEntity(entity);
					
				}
			} catch (IOException ioe) {
				throw new HttpException(ioe.getMessage(), ioe);
			}

			method = post;
		} else if (httpMethod.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
			method = new HttpDelete(uri);
		} else {
			method = new HttpGet(uri);
		}

		return method;
	}
	/**
	 * 解析HTTP错误码
	 * 
	 * @param statusCode
	 * @return
	 */
	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

	/**
	 * 判断是否启用认证
	 * 
	 * @return
	 */
	public boolean isAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	/**
	 * 是否启用日志
	 * 
	 * @param msg
	 */
	public static void log(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	/**
	 * Handle Status code 处理状态码
	 * 
	 * @param statusCode
	 *            响应的状态码
	 * @param res
	 *            服务器响应
	 * @throws HttpException
	 *             当响应码不为200时都会报出此异常:<br />
	 *             <li>HttpRequestException, 通常发生在请求的错误,如请求错误了 网址导致404等, 抛出此异常,
	 *             首先检查request log, 确认不是人为错误导致请求失败</li> <li>HttpAuthException,
	 *             通常发生在Auth失败, 检查用于验证登录的用户名/密码/KEY等</li> <li>
	 *             HttpRefusedException, 通常发生在服务器接受到请求, 但拒绝请求, 可是多种原因, 具体原因
	 *             服务器会返回拒绝理由, 调用HttpRefusedException#getError#getMessage查看</li>
	 *             <li>HttpServerException, 通常发生在服务器发生错误时, 检查服务器端是否在正常提供服务</li>
	 *             <li>HttpException, 其他未知错误.</li>
	 */
	private void HandleResponseStatusCode(int statusCode, Response res)
			throws HttpException {
		String msg = getCause(statusCode) + "\n";

		switch (statusCode) {
		// It's OK, do nothing
		case OK:
			break;

		// Mine mistake, Check the Log
		case NOT_MODIFIED:
		case BAD_REQUEST:
		case NOT_FOUND:
		case NOT_ACCEPTABLE: {
			JSONObject json = res.asJSONObject();
			try {
				throw new HttpException(msg + json.getString("msg"), statusCode);
			} catch (JSONException e) {
				throw new HttpException(msg + "unknow error!", statusCode);
			}

		}
		// UserName/Password incorrect
		case NOT_AUTHORIZED:
			throw new HttpAuthException(msg + res.asString(), statusCode);

			// Server will return a error message, use
			// HttpRefusedException#getError() to see.
		case FORBIDDEN:
			throw new HttpRefusedException(msg, statusCode);

			// Something wrong with server
		case INTERNAL_SERVER_ERROR:
		case BAD_GATEWAY:
		case SERVICE_UNAVAILABLE:
			throw new HttpServerException(msg, statusCode);

			// Others
		default:
			throw new HttpException(msg + res.asString(), statusCode);
		}
	}

	/**
	 * 编码字符串
	 * 
	 * @param value
	 * @return
	 * @throws HttpException
	 */
	public static String encode(String value) throws HttpException {
		try {
			return URLEncoder.encode(value, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e_e) {
			throw new HttpException(e_e.getMessage(), e_e);
		}
	}

	/**
	 * 编码参数
	 * 
	 * @param params
	 * @return
	 * @throws HttpException
	 */
	public static String encodeParameters(ArrayList<BasicNameValuePair> params)
			throws HttpException {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < params.size(); j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(params.get(j).getName(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(params.get(j).getValue(),
								"UTF-8"));
				Log.e("url", buf.toString());
			} catch (java.io.UnsupportedEncodingException neverHappen) {
				throw new HttpException(neverHappen.getMessage(), neverHappen);
			}
		}
		return buf.toString();
	}

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试N次
			if (executionCount >= RETRIED_TIME) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

}
