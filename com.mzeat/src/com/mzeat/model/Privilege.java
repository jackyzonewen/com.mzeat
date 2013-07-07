package com.mzeat.model;

import java.util.ArrayList;
import java.util.List;

public class Privilege extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3536296315622406592L;

	private ArrayList<PrivilegeItem> item = new ArrayList<PrivilegeItem>()  ;
	private Page page = new Page();
	public ArrayList<PrivilegeItem> getItem() {
		return item;
	}

	public void setItem(ArrayList<PrivilegeItem> item) {
		this.item = item;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
}
