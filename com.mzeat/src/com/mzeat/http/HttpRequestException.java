package com.mzeat.http;

/**
 * HTTP请求错误,从HttpException继承
 * HTTP StatusCode is not 200 NOT_MODIFIED: 304 BAD_REQUEST: 400 NOT_FOUND: 404
 * NOT_ACCEPTABLE: 406
 */
public class HttpRequestException extends HttpException {


	private static final long serialVersionUID = 1L;

	/**
	 * HttpRequestException构造方法
	 * @param cause
	 */
	public HttpRequestException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRequestException构造方法
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public HttpRequestException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRequestException构造方法
	 * @param msg
	 * @param cause
	 */
	public HttpRequestException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRequestException构造方法
	 * @param msg
	 * @param statusCode
	 */
	public HttpRequestException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRequestException构造方法
	 * @param msg
	 */
	public HttpRequestException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
