package com.ilya.sergeev.potlach;

import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.unsafe.EasyHttpClient;

public class ServerSvc
{
	public static final String CLIENT_ID = "mobile";
	public static final String SERVER_URL = "http://192.168.2.125:8080";
	
	private static String sUserName = null;
	private static UserInfoSvcApi sServerApi;
	
	public static synchronized String getUserName()
	{
		return sUserName;
	}
	
	private static synchronized void setUserName(String userName)
	{
		sUserName = userName;
	}
	
	public static synchronized UserInfoSvcApi getServerApi()
	{
		if (sServerApi == null)
		{
			sServerApi = createNotUserApi();
			setUserName(null);
		}
		return sServerApi;
	}
	
	public static synchronized UserInfoSvcApi signin(String userName, String password) throws RetrofitError
	{
		sServerApi = createServerApiNotUser(SERVER_URL, userName, password);
		setUserName(userName);
		
		return sServerApi;
	}
	
	public static synchronized UserInfoSvcApi signout()
	{
		sServerApi = createNotUserApi();
		setUserName(null);
		return sServerApi;
	}
	
	private static UserInfoSvcApi createNotUserApi()
	{
		return createServerApiNotUser(SERVER_URL, "not_user", "not_user");
	}
	
	private static UserInfoSvcApi createServerApiNotUser(String server, String userName, String password)
	{
		return new SecuredRestBuilder()
				.setLoginEndpoint(server + UserInfoSvcApi.TOKEN_PATH)
				.setUsername(userName)
				.setPassword(password)
				.setClientId(CLIENT_ID)
				.setClient(new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(UserInfoSvcApi.class);
	}
}
