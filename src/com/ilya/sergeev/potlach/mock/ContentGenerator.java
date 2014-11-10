package com.ilya.sergeev.potlach.mock;

import android.content.Context;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.UserInfo;

class ContentGenerator
{
	private final UsersGenerator mUserGenerator;
	private final GiftGenerator mGiftsGenerator;
	private final VoteGenerator mVoteGenerator;
	
	public ContentGenerator()
	{
		mUserGenerator = UsersGenerator.getGenerator();
		mGiftsGenerator = GiftGenerator.getGenerator(mUserGenerator);
		mVoteGenerator = new VoteGenerator(mUserGenerator, mGiftsGenerator);
	}
	
	public void createSomeUsers(int usersCount)
	{
		while (usersCount > 0)
		{
			UserInfo user = mUserGenerator.createUser();
			if (user != null)
			{
				usersCount--;
			}
		}
	}
	
	public void createSomeGifts(int giftsCount, Context context)
	{
		while(giftsCount > 0)
		{
			Gift gift = mGiftsGenerator.createGift(context);
			if (gift != null)
			{
				giftsCount--;
			}
		}
	}
	
	public void createSomeVotes(int voteLeadsCount, Context context)
	{
		while (voteLeadsCount > 0)
		{
			mVoteGenerator.createVote(context);
			voteLeadsCount--;
		}
	}
}
