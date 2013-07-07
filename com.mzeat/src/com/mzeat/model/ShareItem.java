package com.mzeat.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ShareItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6461767758967898926L;

	private String share_id;
	private String 	title;
	private String 	content;

	private String 	reply_count;

	private String 	user_avatar;

	private String 	user_name;

	private String 	create_time;

	
	private String	img ;

	private String small_img;
	public String getShare_id() {
		return share_id;
	}


	public void setShare_id(String share_id) {
		this.share_id = share_id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getReply_count() {
		return reply_count;
	}


	public void setReply_count(String reply_count) {
		this.reply_count = reply_count;
	}


	public String getUser_avatar() {
		return user_avatar;
	}


	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}


	public String getUser_name() {
		return user_name;
	}


	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public String getCreate_time() {
		return create_time;
	}


	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}


	public String getSmall_img() {
		return small_img;
	}


	public void setSmall_img(String small_img) {
		this.small_img = small_img;
	}


	

	
}
