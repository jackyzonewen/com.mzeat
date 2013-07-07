package com.mzeat.http;

/**
 * 解析response时出现IOException, JSONException等,从HttpException继承。
 */
public class ResponseException extends HttpException {

	private static final long serialVersionUID = -9161304367990941666L;

	/**
	 * ResponseException构造方法
	 * @param cause
	 */
	public ResponseException(Exception cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ResponseException构造方法
	 * @param msg
	 * @param cause
	 * @param statusCode
	 */
	public ResponseException(String msg, Exception cause, int statusCode) {
		super(msg, cause, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ResponseException构造方法
	 * @param msg
	 * @param cause
	 */
	public ResponseException(String msg, Exception cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ResponseException构造方法
	 * @param msg
	 * @param statusCode
	 */
	public ResponseException(String msg, int statusCode) {
		super(msg, statusCode);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ResponseException构造方法
	 * @param msg
	 */
	public ResponseException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
