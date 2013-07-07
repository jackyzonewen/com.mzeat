package com.mzeat.model;

public class User extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4597324791207773705L;

	

	private String user_login_status;
	private String info;
	private String uid;
	private String user_name;
	private String user_email;
	private String user_money;
	private String user_money_format;
	private String user_avatar;
	private String group_id;
	private String mzeatno;
	private String score;
	private String t_sign_info;
	public String getB_day() {
		return b_day;
	}

	public void setB_day(String b_day) {
		this.b_day = b_day;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	private String b_day;
	private String sex;
	private String mobile;

	//private HomeUser home_user;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getMzeatno() {
		return mzeatno;
	}

	public void setMzeatno(String mzeatno) {
		this.mzeatno = mzeatno;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getT_sign_info() {
		return t_sign_info;
	}

	public void setT_sign_info(String t_sign_info) {
		this.t_sign_info = t_sign_info;
	}

	public String getUser_login_status() {
		return user_login_status;
	}

	public void setUser_login_status(String user_login_status) {
		this.user_login_status = user_login_status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_money() {
		return user_money;
	}

	public void setUser_money(String user_money) {
		this.user_money = user_money;
	}

	public String getUser_money_format() {
		return user_money_format;
	}

	public void setUser_money_format(String user_money_format) {
		this.user_money_format = user_money_format;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	
/**
	public HomeUser getHome_user() {
		return home_user;
	}

	public void setHome_user(HomeUser home_user) {
		this.home_user = home_user;
	}
**/


}
