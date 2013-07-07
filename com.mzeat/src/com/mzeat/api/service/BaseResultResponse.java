package com.mzeat.api.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器返回的结果集，其中result为json数组，里面为业务对象集合
 * 
 * @author oliver
 * @version 1.0
 * @since 2012-12-21
 */
public class BaseResultResponse<T> extends BaseResponse {
	private static final long serialVersionUID = 6191641288524974347L;
	/**
	 * 列表数量
	 */
	private int totalCounts;

	private List<T> result;

	/**
	 * 取得结果
	 * 
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 设置结果
	 * 
	 * @param result
	 *            the result to set
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * 取得总数
	 * 
	 * @return the totalCounts
	 */
	public int getTotalCounts() {
		return totalCounts;
	}

	/**
	 * 设置总数
	 * 
	 * @param totalCounts
	 *            the totalCounts to set
	 */
	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

}
