package com.mzeat.model;

import java.io.Serializable;

import java.util.ArrayList;

public class MyOrderItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6493414377107256763L;
	private String id;
	private String sn;

	//private String create_time;

	private String create_time_format;
	private String total_money;
	private String money;
	private String total_money_format;
	private String money_format;
	private String status;
	private String num;

	private ArrayList<MyOrederGood> orderGoods = new ArrayList<MyOrederGood>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getCreate_time_format() {
		return create_time_format;
	}

	public void setCreate_time_format(String create_time_format) {
		this.create_time_format = create_time_format;
	}

	public String getTotal_money() {
		return total_money;
	}

	public void setTotal_money(String total_money) {
		this.total_money = total_money;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTotal_money_format() {
		return total_money_format;
	}

	public void setTotal_money_format(String total_money_format) {
		this.total_money_format = total_money_format;
	}

	public String getMoney_format() {
		return money_format;
	}

	public void setMoney_format(String money_format) {
		this.money_format = money_format;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public ArrayList<MyOrederGood> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(ArrayList<MyOrederGood> orderGoods) {
		this.orderGoods = orderGoods;
	}

}
