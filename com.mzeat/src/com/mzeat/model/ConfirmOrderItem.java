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
	public ConfirmOrderItem(String goods_id,String num, String count) {
		this.goods_id = goods_id;
		this.num = num;
		this.count = count;
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
	
	
}
