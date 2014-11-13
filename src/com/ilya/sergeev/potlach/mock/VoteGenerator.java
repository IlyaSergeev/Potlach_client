package com.ilya.sergeev.potlach.mock;

import java.util.List;
import java.util.Random;

import retrofit.RetrofitError;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.ApplicationSettings;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.client.VoteSvcApi;

class VoteGenerator
{
	private static final String TAG = VoteGenerator.class.getName();
	
	private static final Random sRandom = new Random(); 
	
	private final UsersGenerator mUsersGenerator;
	private final GiftGenerator mGiftsGenerator;
	
	private final List<Vote> mVotes = Lists.newArrayList();
	
	public VoteGenerator(UsersGenerator usersGenerator, GiftGenerator giftsGenerator)
	{
		mUsersGenerator = usersGenerator;
		mGiftsGenerator = giftsGenerator;
	}
	
	public Vote createVote(Context context)
	{
		UserInfo userInfo = mUsersGenerator.anyUser();
		Gift gift = mGiftsGenerator.anyGift(context);
		
		if (userInfo == null || gift == null)
		{
			return null;
		}
		
		Vote vote = null;
		VoteSvcApi voteApi = UserSessionsFactory.getRestAdapterForUser(ApplicationSettings.SERVER_URL, userInfo.getName(), UsersGenerator.DEFAULT_PASSWORD).create(VoteSvcApi.class);
		try
		{
			 vote = voteApi.sendVote(gift.getId(), (sRandom.nextInt()%2 > 0)?1:-1);
			 mVotes.add(vote);
			 Log.d(TAG, "create vote user:" + userInfo.getName() + " giftId:" + gift.getId() + " vote:" + vote.getVote());
		}
		catch(RetrofitError ex)
		{
			vote = null;
		}
		return vote;
	}
}
