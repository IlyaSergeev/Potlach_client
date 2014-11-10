package com.ilya.sergeev.potlach.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class UserHelper
{
	private static final String USER_LOGIN_TAG = "user.login";
	
	public UserHelper()
	{
	}
	
	public static String getLogin(Context context)
	{
		return getPrefs(context).getString(USER_LOGIN_TAG, null);
	}
	
	public static boolean setLogin(Context context, String userName)
	{
		return getPrefs(context).edit().putString(USER_LOGIN_TAG, userName).commit();
	}
	
	private static SharedPreferences getPrefs(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
