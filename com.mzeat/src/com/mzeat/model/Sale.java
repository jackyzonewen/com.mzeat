package com.mzeat.model;

import java.io.Serializable;

public class Sale implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 539381403110927189L;

	
	private String notice_id	;
	private String title;
	private String create_time;
	private String content;
	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
