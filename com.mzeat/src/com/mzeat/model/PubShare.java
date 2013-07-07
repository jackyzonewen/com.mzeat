package com.mzeat.model;

public class PubShare extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4503101225884840942L;
	private String status;
	private String info;

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
