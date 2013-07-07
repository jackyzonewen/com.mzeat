package com.mzeat.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Comments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8109705363173768743L;
	private List<Comment> list ;
	private Page page ;
	public List<Comment> getList() {
		return list;
	}
	public void setList(List<Comment> list) {
		this.list = list;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
}
