package com.mzeat.model;

import java.io.Serializable;

public class Invite implements Serializable {

	/**
	 * 
	 */

	
	private static final long serialVersionUID = -7908289551293415363L;

	
	private String yczp_id;
	private String Post;
	private String sex;
	private String Degree;
	private String number;
	private String Treatment;
	private String Phone;
	private String contact;
	private String Address;
	private String Claim;
	private String Business;
	private String create_time;
	public String getYczp_id() {
		return yczp_id;
	}
	public void setYczp_id(String yczp_id) {
		this.yczp_id = yczp_id;
	}
	public String getPost() {
		return Post;
	}
	public void setPost(String post) {
		Post = post;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDegree() {
		return Degree;
	}
	public void setDegree(String degree) {
		Degree = degree;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTreatment() {
		return Treatment;
	}
	public void setTreatment(String treatment) {
		Treatment = treatment;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getClaim() {
		return Claim;
	}
	public void setClaim(String claim) {
		Claim = claim;
	}
	public String getBusiness() {
		return Business;
	}
	public void setBusiness(String business) {
		Business = business;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
