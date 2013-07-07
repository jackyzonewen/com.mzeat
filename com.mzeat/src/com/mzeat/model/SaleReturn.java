package com.mzeat.model;

import java.util.ArrayList;

public class SaleReturn extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2649583753286808542L;

	
	public ArrayList<Sale> getItem() {
		return item;
	}
	public void setItem(ArrayList<Sale> item) {
		this.item = item;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	private ArrayList<Sale> item ;
	private Page page;
}
