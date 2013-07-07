package com.mzeat.model;

import java.util.ArrayList;

public class U_commentlist extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6553242257694144213L;

	private ArrayList<My_share> my_share = new ArrayList<My_share>();
	
	private ArrayList<U_commentlist_item> item = new ArrayList<U_commentlist_item>();
	
	private String total;
	
	private Page page;

	public ArrayList<My_share> getMy_share() {
		return my_share;
	}

	public void setMy_share(ArrayList<My_share> my_share) {
		this.my_share = my_share;
	}

	public ArrayList<U_commentlist_item> getItem() {
		return item;
	}

	public void setItem(ArrayList<U_commentlist_item> item) {
		this.item = item;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	
}
