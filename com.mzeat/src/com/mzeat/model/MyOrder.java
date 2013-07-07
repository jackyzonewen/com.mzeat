package com.mzeat.model;

import java.util.ArrayList;

public class MyOrder extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1151131358697362146L;

	private String user_login_status;
	private String totalPages;
	private String pageRows;
	private String nowPage;
	private String totalRows;
	private Page page;
	private ArrayList<MyOrderItem> item = new ArrayList<MyOrderItem>();

	public String getUser_login_status() {
		return user_login_status;
	}

	public void setUser_login_status(String user_login_status) {
		this.user_login_status = user_login_status;
	}

	public String getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	public String getPageRows() {
		return pageRows;
	}

	public void setPageRows(String pageRows) {
		this.pageRows = pageRows;
	}

	public String getNowPage() {
		return nowPage;
	}

	public void setNowPage(String nowPage) {
		this.nowPage = nowPage;
	}

	public String getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(String totalRows) {
		this.totalRows = totalRows;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public ArrayList<MyOrderItem> getItem() {
		return item;
	}

	public void setItem(ArrayList<MyOrderItem> item) {
		this.item = item;
	}

}
