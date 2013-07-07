package com.mzeat.model;

import java.io.Serializable;

public class MyOrederGood implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 406306711145825798L;

	private String id;
	private String goods_id;
	private String name;
	private String num;
	private String price;
	private String price_format;
	private String total_money;
	private String total_money_format;
	private String attr_content;
	private String image;
	private String info;
	private String sn;
	private String sn_password;
	private String sn_end_time;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getSn_password() {
		return sn_password;
	}
	public void setSn_password(String sn_password) {
		this.sn_password = sn_password;
	}
	public String getSn_end_time() {
		return sn_end_time;
	}
	public void setSn_end_time(String sn_end_time) {
		this.sn_end_time = sn_end_time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice_format() {
		return price_format;
	}
	public void setPrice_format(String price_format) {
		this.price_format = price_format;
	}
	public String getTotal_money() {
		return total_money;
	}
	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}
	public String getTotal_money_format() {
		return total_money_format;
	}
	public void setTotal_money_format(String total_money_format) {
		this.total_money_format = total_money_format;
	}
	public String getAttr_content() {
		return attr_content;
	}
	public void setAttr_content(String attr_content) {
		this.attr_content = attr_content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

}
