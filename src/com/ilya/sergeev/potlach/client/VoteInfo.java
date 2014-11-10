package com.ilya.sergeev.potlach.client;

public class VoteInfo
{
	private int votesUp = 0;
	private int votesDown = 0;
	
	private Vote userVote = null;

	public int getVotesUp()
	{
		return votesUp;
	}

	public void setVotesUp(int votesUp)
	{
		this.votesUp = votesUp;
	}

	public int getVotesDown()
	{
		return votesDown;
	}

	public void setVotesDown(int votesDown)
	{
		this.votesDown = votesDown;
	}

	public Vote getUserVote()
	{
		return userVote;
	}

	public void setUserVote(Vote userVote)
	{
		this.userVote = userVote;
	}
}
