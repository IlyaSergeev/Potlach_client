package com.ilya.sergeev.potlach.client;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.ApplicationConsts;
import com.ilya.sergeev.potlach.unsafe.EasyHttpClient;

public class ServerSvcApi
{
	public static ServerSvcApi createServerApi(String server, String user, String passwrod)
	{
		RestAdapter adapter = new SecuredRestBuilder()
				.setLoginEndpoint(server + UserInfoSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(passwrod)
				.setClientId(ApplicationConsts.CLIENT_ID)
				.setClient(new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build();
		
		return new ServerSvcApi(adapter);
	}
	
	private final UserInfoSvcApi mUsersApi;
	private final VoteSvcApi mVotesApi;
	private final GiftSvcApi mGiftsApi;
	
	private ServerSvcApi(RestAdapter adapter)
	{
		this(adapter.create(UserInfoSvcApi.class), adapter.create(VoteSvcApi.class), adapter.create(GiftSvcApi.class));
	}
	
	private ServerSvcApi(UserInfoSvcApi usersApi, VoteSvcApi votesApi, GiftSvcApi giftsApi)
	{
		mUsersApi = usersApi;
		mVotesApi = votesApi;
		mGiftsApi = giftsApi;
	}
	
	public UserInfoSvcApi getUsersApi()
	{
		return mUsersApi;
	}
	
	public VoteSvcApi getVotesApi()
	{
		return mVotesApi;
	}
	
	public GiftSvcApi getGiftsApi()
	{
		return mGiftsApi;
	}
}
