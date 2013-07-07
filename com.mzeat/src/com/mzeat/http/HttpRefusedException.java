package com.mzeat.http;

/**
 * HTTP拒绝错误，从 HttpException继承
 * HTTP StatusCode is 403, Server refuse the request
 */
public class HttpRefusedException extends HttpException {
	/**
	 * 服务器返回来的错误信息
	 */

	/**
	 * HttpRefusedException构造方法
	 * @param cause
	 */
	public HttpRefusedException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRefusedException
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public HttpRefusedException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRefusedException
	 * @param msg
	 * @param cause
	 */
	public HttpRefusedException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRefusedException
	 * @param msg
	 * @param statusCode
	 */
	public HttpRefusedException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpRefusedException
	 * @param msg
	 */
	public HttpRefusedException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
