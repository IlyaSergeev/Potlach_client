package com.ilya.sergeev.potlach.client;

import com.google.common.base.Objects;

public class UserInfo
{
	private long id;
	
	private String name;
	private String password;
	
	private int rating = 0;
	
	public UserInfo()
	{
		
	}
	
	public UserInfo(String name, String password)
	{
		this.name = name;
		this.password = password;
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getName());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof UserInfo) && Objects.equal(getName(), ((UserInfo) obj).getName());
	}
	
	public int getRating()
	{
		return rating;
	}
	
	public void setRating(int rating)
	{
		this.rating = rating;
	}
}
