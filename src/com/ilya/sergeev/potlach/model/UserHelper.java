package com.ilya.sergeev.potlach.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class UserHelper
{
	private static final String TOKET_TAG = "user.token";
	private static final String USER_NAME_TAG = "user.name";
	
	public UserHelper()
	{
	}
	
	public static String getName(Context context)
	{
		return getPrefs(context).getString(USER_NAME_TAG, null);
	}
	
	public static String getToken(Context context)
	{
		return getPrefs(context).getString(TOKET_TAG, null);
	}
	
	public static boolean setName(String userName, Context context)
	{
		return getPrefs(context)
				.edit()
				.putString(USER_NAME_TAG, userName)
				.commit();
	}
	
	public static boolean setToken(String token, Context context)
	{
		return getPrefs(context)
				.edit()
				.putString(TOKET_TAG, token)
				.commit();
	}
	
	private static SharedPreferences getPrefs(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
