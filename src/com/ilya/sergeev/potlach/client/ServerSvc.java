package com.ilya.sergeev.potlach.client;

import retrofit.RetrofitError;

import com.ilya.sergeev.potlach.ApplicationSettings;

public class ServerSvc
{
	
	private static String sUserName = null;
	
	private static ServerSvcApi sServerApi;
	
	public static synchronized String getUserName()
	{
		return sUserName;
	}
	
	private static synchronized void setUserName(String userName)
	{
		sUserName = userName;
	}
	
	public static synchronized ServerSvcApi getServerApi()
	{
		if (sServerApi == null)
		{
			sServerApi = createNotUserApi();
			setUserName(null);
		}
		return sServerApi;
	}
	
	public static synchronized ServerSvcApi signin(String userName, String password) throws RetrofitError
	{
		sServerApi = ServerSvcApi.createServerApi(ApplicationSettings.SERVER_URL, userName, password);
		setUserName(userName);
		
		return sServerApi;
	}
	
	public static synchronized ServerSvcApi signout()
	{
		sServerApi = createNotUserApi();
		setUserName(null);
		return sServerApi;
	}
	
	private static ServerSvcApi createNotUserApi()
	{
		return ServerSvcApi.createServerApi(ApplicationSettings.SERVER_URL, "not_user", "not_user");
	}
}
