package com.mzeat.model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Share extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7813628880000935078L;

	private Page page;

	private LinkedList<ShareItem> item = new LinkedList<ShareItem>();

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public LinkedList<ShareItem> getItem() {
		return item;
	}

	public void setItem(LinkedList<ShareItem> item) {
		this.item = item;
	}
}
