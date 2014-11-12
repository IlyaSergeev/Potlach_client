package com.ilya.sergeev.potlach.mock;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;
import retrofit.RetrofitError;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;

class UsersGenerator
{
	public static UsersGenerator getGenerator()
	{
		return new UsersGenerator(ServerSvc.getServerApi().getApi(UserInfoSvcApi.class));
	}
	
	private static final String TAG = UsersGenerator.class.getName();
	private static final Random sRandom = new Random();
		
	
	private static final String[] sUserNames = new String[] { "Kate", "Anne", "Vasya", "John", "Bard", "Homer", "Liza", "Ilya" };
	
	public static final String DEFAULT_PASSWORD = "111111";
	
	private int mIndex = 0;

	private final List<UserInfo> mUsers = Lists.newArrayList();
	private final Set<String> mUserNames = Sets.newHashSet();
	private UserInfoSvcApi mUsersApi;
	
	public UsersGenerator(UserInfoSvcApi usersApi)
	{
		Assert.assertNotNull(usersApi);
		mUsersApi = usersApi;
	}
	
	public UserInfo createUser()
	{
		UserInfo userInfo = null;
		String userName = newUserName();
		try
		{
			userInfo = mUsersApi.createUser(userName, DEFAULT_PASSWORD);
			Log.d(TAG, "create user:" + userName);
		}
		catch (RetrofitError error)
		{
			userInfo = null;
		}
		if (userInfo != null)
		{
			mUsers.add(userInfo);
		}
		return userInfo;
	}
	
	public UserInfo anyUser()
	{
		if (mUsers.size() == 0)
		{
			return createUser();
		}
		return mUsers.get(sRandom.nextInt(mUsers.size()));
	}
	
	private String newUserName()
	{
		int nameInd = sRandom.nextInt(sUserNames.length);
		
		String userName = sUserNames[nameInd];
		if (mUserNames.contains(userName))
		{
			mIndex++;
			userName = String.format(Locale.getDefault(), "%s_%d", sUserNames[nameInd], mIndex) ;
		}
		mUserNames.add(userName);
		return userName;
	}
}
