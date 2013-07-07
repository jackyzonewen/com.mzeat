package com.mzeat.api.service;

import java.util.ArrayList;

import com.mzeat.model.Advs;

public class PostersResponse extends BaseResultResponse<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7296763677253012184L;
	private ArrayList<Advs> advs;

	public ArrayList<Advs> getAdvs() {
		return advs;
	}

	public void setAdvs(ArrayList<Advs> advs) {
		this.advs = advs;
	}
}
