package com.mzeat.model;

import java.io.Serializable;

public class BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5396153389945414895L;
	
	
	private String open;
	private String act;
	private String act_2;
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getAct_2() {
		return act_2;
	}
	public void setAct_2(String act_2) {
		this.act_2 = act_2;
	}
}
