package com.mzeat.http;

/**
 * HTTP认证错误，从HttpRefusedException继承
 * NOT AUTHORIZED, HTTP CODE 401
 */
public class HttpAuthException extends HttpRefusedException {

	


	private static final long serialVersionUID = 1L;

	/**
	 * HttpAuthException构造方法
	 * @param cause
	 */
	public HttpAuthException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpAuthException构造方法
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public HttpAuthException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpAuthException构造方法
	 */
	public HttpAuthException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpAuthException构造方法
	 * @param msg
	 * @param statusCode
	 */
	public HttpAuthException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpAuthException构造方法
	 * @param msg
	 */
	public HttpAuthException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
