package com.mzeat.api.service;

import java.io.Serializable;

/**
 * 服务器返回的基本数据包，包含code和mesage，实现Serializable
 * 
 * @author oliver
 * @version 1.0
 * @since 2012-12-20
 */
public class BaseResponse implements Serializable {
	private static final long serialVersionUID = 7844697070352999047L;
	public static final int SUCCESS = 0;

	/**
	 * 服务器错误代码
	 */
	private String open;
	private String act;
	private String act_2;

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getAct_2() {
		return act_2;
	}

	public void setAct_2(String act_2) {
		this.act_2 = act_2;
	}

}
