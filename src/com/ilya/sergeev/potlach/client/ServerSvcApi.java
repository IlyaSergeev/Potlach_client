package com.ilya.sergeev.potlach.client;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.ApplicationConsts;
import com.ilya.sergeev.potlach.unsafe.EasyHttpClient;

public class ServerSvcApi
{
	public static ServerSvcApi createServerApi(String server, String user, String password)
	{
		RestAdapter adapter = new SecuredRestBuilder()
				.setLoginEndpoint(server + UserInfoSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(password)
				.setClientId(ApplicationConsts.CLIENT_ID)
				.setClient(new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build();
		
		return new ServerSvcApi(adapter);
	}
	
	private final RestAdapter mRestAdapter;
	
	private ServerSvcApi(RestAdapter adapter)
	{
		mRestAdapter = adapter;
	}
	
	public <T> T getApi(Class<T> apiClass)
	{
		return mRestAdapter.create(apiClass);
	}
	
}
