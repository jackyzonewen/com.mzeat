package com.mzeat.model;

public class Signin extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6324030088457168645L;
	
	private String status;
    private String info;
    private String score;
    private String poin;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
