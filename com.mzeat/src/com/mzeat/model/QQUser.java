package com.mzeat.model;

public class QQUser
{
	String name;
	String openid;
	String nick;
	String head;
	String sex;
	String id;
	boolean isSelected=false;
	
	

	public boolean isSelected()
	{
		return isSelected;
	}

	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getNick()
	{
		return nick;
	}

	public void setNick(String nick)
	{
		this.nick = nick;
	}

	public String getHead()
	{
		return head+"/"+50;
	}

	public void setHead(String head)
	{
		this.head = head;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
