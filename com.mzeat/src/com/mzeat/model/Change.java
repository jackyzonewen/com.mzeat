package com.mzeat.model;

import java.io.Serializable;

public class Change implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7543775933471832171L;

	private String goods_id;
	private String title;
	private String score;
	private String bought;
	private String image;
	private String count_image;
	private String content;
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
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getBought() {
		return bought;
	}
	public void setBought(String bought) {
		this.bought = bought;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getCount_image() {
		return count_image;
	}
	public void setCount_image(String count_image) {
		this.count_image = count_image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
