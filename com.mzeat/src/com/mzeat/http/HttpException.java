package com.mzeat.http;

/**
 * HTTP错误
 * HTTP StatusCode is not 200
 * HTTP 状态码 不是200。
 */
public class HttpException extends Exception {

	private static final long serialVersionUID = 1L;
	private int statusCode = -1;

	/**
	 * HttpException构造方法
	 * @param msg
	 */
	public HttpException(String msg) {
		super(msg);
	}

	/**
	 * HttpException构造方法
	 * @param cause
	 */
	public HttpException(Exception cause) {
		super(cause);
	}
	
	/**
	 * HttpException构造方法
	 * @param msg
	 * @param statusCode
	 */
	public HttpException(String msg, int statusCode) {
		super(msg);
		this.statusCode = statusCode;
	}

	/**
	 * HttpException构造方法
	 * @param msg
	 * @param cause
	 */
	public HttpException(String msg, Exception cause) {
		super(msg, cause);
	}

	/**
	 * HttpException构造方法
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public HttpException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;
	}

	/**
	 * 取得状态码
	 * @return statusCode
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

}
