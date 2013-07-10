package com.mzeat.model;

public class QQInfo
{
	String access_token;
	String openId;
	String clientId;
	String elapseTime;
	String headurl;
	String nickname;
	
 
	public String getHeadurl()
	{
		return headurl;
	}
	public void setHeadurl(String headurl)
	{
		this.headurl = headurl;
	}
	public String getNickname()
	{
		return nickname;
	}
	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}
	public String getAccess_token()
	{
		return access_token;
	}
	public void setAccess_token(String access_token)
	{
		this.access_token = access_token;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getElapseTime()
	{
		return elapseTime;
	}
	public void setElapseTime(String elapseTime)
	{
		this.elapseTime = elapseTime;
	}
}

