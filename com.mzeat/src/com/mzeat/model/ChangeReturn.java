package com.mzeat.model;

import java.util.ArrayList;

public class ChangeReturn extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2769518618785830379L;
	private ArrayList<Change> item;
	private Page page;

	public ArrayList<Change> getItem() {
		return item;
	}

	public void setItem(ArrayList<Change> item) {
		this.item = item;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
