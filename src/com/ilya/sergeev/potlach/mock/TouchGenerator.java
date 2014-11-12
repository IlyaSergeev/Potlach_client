package com.ilya.sergeev.potlach.mock;

import retrofit.RetrofitError;
import android.content.Context;

import com.ilya.sergeev.potlach.ApplicationConsts;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.TouchSvcApi;
import com.ilya.sergeev.potlach.client.UserInfo;

class TouchGenerator
{
	private final GiftGenerator mGiftGenerator;
	private final UsersGenerator mUserGenerator;
	
	public TouchGenerator(GiftGenerator giftGenerator, UsersGenerator userGenerator)
	{
		mGiftGenerator = giftGenerator;
		mUserGenerator = userGenerator;
	}
	
	public boolean createTouche(Context context)
	{
		try
		{
			UserInfo user = mUserGenerator.anyUser();
			TouchSvcApi touchApi = UserSessionsFactory
					.getRestAdapterForUser(ApplicationConsts.SERVER_URL, user.getName(), UsersGenerator.DEFAULT_PASSWORD)
					.create(TouchSvcApi.class);
			
			Gift gift = mGiftGenerator.anyGift(context);
			return touchApi.touch(gift.getId()) != null;
		}
		catch (RetrofitError ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
}
