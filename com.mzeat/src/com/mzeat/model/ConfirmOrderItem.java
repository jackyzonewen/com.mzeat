package com.mzeat.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmOrderItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8896123145763884287L;
	
	private String goods_id;
	private String num;
	private String count;
	private String price;
	private String url;
	private String product;
	
	public ConfirmOrderItem(String goods_id,String num, String count,String price,String url,String product) {
		this.goods_id = goods_id;
		this.num = num;
		this.count = count;
		this.price = price;
		this.url = url;
		this.product = product;
    }
	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("goods_id", goods_id);
            obj.put("num", num);
            obj.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	
	
}
