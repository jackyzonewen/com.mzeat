package com.mzeat.model;

import java.io.Serializable;

public class U_commentlist_item  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3189691337229971422L;

	
	private String comment_id;
	private String share_id;
	private String uid;
	private String parent_id;
	private String content;
	
	private String create_time;
	private String scontent;
	
	private String user_name;
	/**
	 * 
	 */
	private String time;
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
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
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getScontent() {
		return scontent;
	}
	public void setScontent(String scontent) {
		this.scontent = scontent;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return false;
		}else {
			if(this.getClass() == o.getClass()){
				U_commentlist_item u = (U_commentlist_item) o;
				if(this.getComment_id().equals(u.getComment_id())){  
                    return true;  
                }else{  
                    return false;  
                }  
			}else {
				return false;
			}
		}
	}
	
	
}
