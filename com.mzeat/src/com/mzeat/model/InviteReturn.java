package com.mzeat.model;

import java.util.ArrayList;

public class InviteReturn extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1191380685763769556L;

	private ArrayList<Invite> item;
	private Page page;

	public ArrayList<Invite> getItem() {
		return item;
	}

	public void setItem(ArrayList<Invite> item) {
		this.item = item;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
