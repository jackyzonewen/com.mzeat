package com.mzeat.model;

import java.io.Serializable;

public class ShareItemImgs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5986139885027732851L;
	private String share_id;
	private String id;
	private String img;
	private String small_img;
	private String type;
	private String img_width;
	private String img_height;
	public String getShare_id() {
		return share_id;
	}
	public void setShare_id(String share_id) {
		this.share_id = share_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImg_width() {
		return img_width;
	}
	public void setImg_width(String img_width) {
		this.img_width = img_width;
	}
	public String getImg_height() {
		return img_height;
	}
	public void setImg_height(String img_height) {
		this.img_height = img_height;
	}
	
}
