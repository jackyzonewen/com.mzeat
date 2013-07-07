package com.mzeat.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShareDetail extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7813628880000935078L;

	private String share_id;
	private String uid;
	private String user_name;
	private String user_avatar;
	private String title;
	
	private String content;
	private String comment_count;
	private String time;
	private List<ShareItemImgs> imgs;
	private Comments comments;
	public String getShare_id() {
		return share_id;
	}
	public void setShare_id(String share_id) {
		this.share_id = share_id;
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
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
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
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<ShareItemImgs> getImgs() {
		return imgs;
	}
	public void setImgs(List<ShareItemImgs> imgs) {
		this.imgs = imgs;
	}
	public Comments getComments() {
		return comments;
	}
	public void setComments(Comments comments) {
		this.comments = comments;
	}
	public String getComment_count() {
		return comment_count;
	}
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
}
