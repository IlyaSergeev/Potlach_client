package com.ilya.sergeev.potlach.client;

import com.google.common.base.Objects;

public class GiftInfo
{
	private Gift gift;
	private Vote vote;
	private boolean wasTouched;
	
	public GiftInfo()
	{
		
	}
	
	public GiftInfo(Gift gift, Vote vote, boolean wasTouched)
	{
		this.gift = gift;
		this.vote = vote;
		this.wasTouched = wasTouched;
	}
	
	public Gift getGift()
	{
		return gift;
	}
	
	public void setGift(Gift gift)
	{
		this.gift = gift;
	}
	
	public Vote getVote()
	{
		return vote;
	}
	
	public void setVote(Vote vote)
	{
		this.vote = vote;
	}
	
	public boolean isWasTouched()
	{
		return wasTouched;
	}
	
	public void setWasTouched(boolean wasTouched)
	{
		this.wasTouched = wasTouched;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(gift, vote);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof GiftInfo) &&
				Objects.equal(gift, ((GiftInfo) obj).getGift()) &&
				Objects.equal(vote, ((GiftInfo) obj).getVote());
	}
}
