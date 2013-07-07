package com.mzeat.model;

public class Shopping extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3458601431836801575L;

	private String id;
	private String name;
	private String logo;
	private String xpoint;
	private String ypoint;
	private String api_address;
	private String mzeatvip;
	private String tel;
	private String comment_count;
	private String brand_id;
	private String distance;
	private String mobile_brief;
	private String avg_point;
	private String Characteristic;
	private String open_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getApi_address() {
		return api_address;
	}

	public void setApi_address(String api_address) {
		this.api_address = api_address;
	}

	public String getMzeatvip() {
		return mzeatvip;
	}

	public void setMzeatvip(String mzeatvip) {
		this.mzeatvip = mzeatvip;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getComment_count() {
		return comment_count;
	}

	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}

	public String getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(String brand_id) {
		this.brand_id = brand_id;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCharacteristic() {
		return Characteristic;
	}

	public void setCharacteristic(String characteristic) {
		Characteristic = characteristic;
	}

	public String getOpen_time() {
		return open_time;
	}

	public void setOpen_time(String open_time) {
		this.open_time = open_time;
	}

	public String getAvg_point() {
		return avg_point;
	}

	public void setAvg_point(String avg_point) {
		this.avg_point = avg_point;
	}

	public String getMobile_brief() {
		return mobile_brief;
	}

	public void setMobile_brief(String mobile_brief) {
		this.mobile_brief = mobile_brief;
	}

}
