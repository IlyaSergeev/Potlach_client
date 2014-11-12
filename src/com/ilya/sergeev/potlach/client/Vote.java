package com.ilya.sergeev.potlach.client;

import com.google.common.base.Objects;

public class Vote
{
	public static Vote createVoteUp(String userName, long giftId)
	{
		return new Vote(userName, giftId, 1);
	}
	
	public static Vote createVoteDown(String userName, long giftId)
	{
		return new Vote(userName, giftId, -1);
	}
	
	private long id;
	
	private String userName;
	
	private long giftId;
	
	private int vote;
	
	public Vote()
	{
	}
	
	public Vote(String userName, long giftId)
	{
		this.userName = userName;
		this.giftId = giftId;
	}
	
	private Vote(String userName, long giftId, int voteValue)
	{
		this.userName = userName;
		this.giftId = giftId;
		this.vote = voteValue;
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getVote()
	{
		return vote;
	}
	
	public void setVote(int vote)
	{
		this.vote = vote;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public long getGiftId()
	{
		return giftId;
	}
	
	public void setGiftId(long giftId)
	{
		this.giftId = giftId;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getUserName(), getGiftId(), getVote());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof Vote)
				&& getGiftId() == ((Vote) obj).getGiftId()
				&& Objects.equal(getUserName(), ((Vote) obj).getUserName())
				&& getVote() == ((Vote) obj).getVote();
	}
}
