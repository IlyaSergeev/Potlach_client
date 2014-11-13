package com.ilya.sergeev.potlach.mock;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.ApplicationSettings;
import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.unsafe.EasyHttpClient;

class UserSessionsFactory
{
	public static RestAdapter getRestAdapterForUser(String server, String user, String password)
	{
		return new SecuredRestBuilder()
				.setLoginEndpoint(server + UserInfoSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(password)
				.setClientId(ApplicationSettings.CLIENT_ID)
				.setClient(new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build();
	}
}
