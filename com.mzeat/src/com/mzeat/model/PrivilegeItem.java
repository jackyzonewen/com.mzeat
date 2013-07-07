package com.mzeat.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrivilegeItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4965469504424067993L;
	
	private String city_name;
	private String goods_id;
	private String title;
	private String image;
	private String buy_count;
	private String start_date;
	private String end_date;
	private String ori_price;
	private String cur_price;
	private String goods_brief;
	private String ori_price_format;
	private String cur_price_format;
	private String discount;
	private String address;
	private String num_unit;
	private String limit_num;
	private String goods_desc;
	private String sp_detail;
	private String sp_tel;
	private String saving_format;
	private String less_time;
	private String has_attr;
	private String has_delivery;
	private String has_mcod;
	private String has_cart;
	private String change_cart_request_server;
	private String xpoint;
	private String ypoint;
	private String time_status;
	private String count_image;
	private String buy_status;
	private String buy_type;
	private String start_times;
	private String sp_open_times;
	private String num;
	private String count;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
	/**
	 * 计算总价格，解决精度不够精确问题。
	 * @return
	 */
	public String getCount() {
		BigDecimal bd1 = new BigDecimal(getNum());
		BigDecimal bd2 = new BigDecimal(getCur_price());
		
		return bd1.multiply(bd2).toString();
	}
	public String getTime_status() {
		return time_status;
	}
	public void setTime_status(String time_status) {
		this.time_status = time_status;
	}
	public String getBuy_status() {
		return buy_status;
	}
	public void setBuy_status(String buy_status) {
		this.buy_status = buy_status;
	}
	public String getSp_tel() {
		return sp_tel;
	}
	public void setSp_tel(String sp_tel) {
		this.sp_tel = sp_tel;
	}
	public String getXpoint() {
		return xpoint;
	}
	public void setXpoint(String xpoint) {
		this.xpoint = xpoint;
	}
	public String getYpoint() {
		return ypoint;
	}
	public void setYpoint(String ypoint) {
		this.ypoint = ypoint;
	}
	private String distance;
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getBuy_count() {
		return buy_count;
	}
	public void setBuy_count(String buy_count) {
		this.buy_count = buy_count;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getOri_price() {
		return ori_price;
	}
	public void setOri_price(String ori_price) {
		this.ori_price = ori_price;
	}
	public String getCur_price() {
		return cur_price;
	}
	public void setCur_price(String cur_price) {
		this.cur_price = cur_price;
	}
	
	public String getGoods_brief() {
		return goods_brief;
	}
	public void setGoods_brief(String goods_brief) {
		this.goods_brief = goods_brief;
	}

	public String getOri_price_format() {
		return ori_price_format;
	}
	public void setOri_price_format(String ori_price_format) {
		this.ori_price_format = ori_price_format;
	}
	public String getCur_price_format() {
		return cur_price_format;
	}
	public void setCur_price_format(String cur_price_format) {
		this.cur_price_format = cur_price_format;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNum_unit() {
		return num_unit;
	}
	public void setNum_unit(String num_unit) {
		this.num_unit = num_unit;
	}
	public String getLimit_num() {
		return limit_num;
	}
	public void setLimit_num(String limit_num) {
		this.limit_num = limit_num;
	}

	public String getGoods_desc() {
		return goods_desc;
	}
	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}
	
	public String getSp_detail() {
		return sp_detail;
	}
	public void setSp_detail(String sp_detail) {
		this.sp_detail = sp_detail;
	}
	public String getSaving_format() {
		return saving_format;
	}
	public void setSaving_format(String saving_format) {
		this.saving_format = saving_format;
	}
	public String getLess_time() {
		return less_time;
	}
	public void setLess_time(String less_time) {
		this.less_time = less_time;
	}
	public String getHas_attr() {
		return has_attr;
	}
	public void setHas_attr(String has_attr) {
		this.has_attr = has_attr;
	}
	public String getHas_delivery() {
		return has_delivery;
	}
	public void setHas_delivery(String has_delivery) {
		this.has_delivery = has_delivery;
	}
	public String getHas_mcod() {
		return has_mcod;
	}
	public void setHas_mcod(String has_mcod) {
		this.has_mcod = has_mcod;
	}
	public String getHas_cart() {
		return has_cart;
	}
	public void setHas_cart(String has_cart) {
		this.has_cart = has_cart;
	}
	public String getChange_cart_request_server() {
		return change_cart_request_server;
	}
	public void setChange_cart_request_server(String change_cart_request_server) {
		this.change_cart_request_server = change_cart_request_server;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getBuy_type() {
		return buy_type;
	}
	public void setBuy_type(String buy_type) {
		this.buy_type = buy_type;
	}
	public String getCount_image() {
		return count_image;
	}
	public void setCount_image(String count_image) {
		this.count_image = count_image;
	}
	public String getStart_times() {
		return start_times;
	}
	public void setStart_times(String start_times) {
		this.start_times = start_times;
	}
	public String getSp_open_times() {
		return sp_open_times;
	}
	public void setSp_open_times(String sp_open_times) {
		this.sp_open_times = sp_open_times;
	}
	
	
}
