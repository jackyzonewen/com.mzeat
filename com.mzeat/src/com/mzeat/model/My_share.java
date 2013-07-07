package com.mzeat.model;

import java.io.Serializable;

public class My_share implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7910687377641749365L;

	
	private String share_id;
	private String content;
	private String is_read;
	public String getShare_id() {
		return share_id;
	}
	public void setShare_id(String share_id) {
		this.share_id = share_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIs_read() {
		return is_read;
	}
	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null) {
			return false;
		}else {
			if(this.getClass() == o.getClass()){
				My_share u = (My_share) o;
				if(this.getShare_id().equals(u.getShare_id())){  
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
