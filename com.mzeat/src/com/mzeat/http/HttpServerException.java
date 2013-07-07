package com.mzeat.http;

/**
 * HTTP服务器错误，从HttpException继承 HTTP 
 * StatusCode is not 200 INTERNAL_SERVER_ERROR:
 * 500 BAD_GATEWAY: 502 SERVICE_UNAVAILABLE: 503
 */
public class HttpServerException extends HttpException {

	private static final long serialVersionUID = 1L;

	/**
	 * HttpServerException构造方法
	 * 
	 * @param cause
	 */
	public HttpServerException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpServerException构造方法
	 * 
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public HttpServerException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpServerException构造方法
	 * 
	 * @param msg
	 * @param cause
	 */
	public HttpServerException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpServerException构造方法
	 * 
	 * @param msg
	 * @param statusCode
	 */
	public HttpServerException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * HttpServerException构造方法
	 * 
	 * @param msg
	 */
	public HttpServerException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
