package com.mzeat.model;

import java.io.Serializable;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4395621672363152688L;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPage_total() {
		return page_total;
	}
	public void setPage_total(String page_total) {
		this.page_total = page_total;
	}
	private String page;
	private String page_total;

}
