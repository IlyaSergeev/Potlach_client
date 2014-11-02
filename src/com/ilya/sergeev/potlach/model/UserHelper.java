package com.ilya.sergeev.potlach.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public final class UserHelper
{
	private static final String TOKET_TAG = "user.token";
	private static final String USER_LOGIN_TAG = "user.login";
	
	public UserHelper()
	{
	}
	
	public static String getLogin(Context context)
	{
		return getPrefs(context).getString(USER_LOGIN_TAG, null);
	}
	
	public static String getToken(Context context)
	{
		return getPrefs(context).getString(TOKET_TAG, null);
	}
	
	public static boolean signIn(String userLogin, String token, Context context)
	{
		return getPrefs(context)
				.edit()
				.putString(USER_LOGIN_TAG, userLogin)
				.putString(TOKET_TAG, token)
				.commit();
	}
	
	public static boolean signOut(Context context)
	{
		boolean result = getPrefs(context)
				.edit()
				.remove(TOKET_TAG)
				.remove(USER_LOGIN_TAG)
				.commit();
		
		if (result)
		{
			context.sendBroadcast(new Intent(Broadcasts.SIGN_OUT_BROADCAST));
		}
		return result;
	}
	
	public static boolean hasValidToken(Context context)
	{
		return !TextUtils.isEmpty(getToken(context));
	}
	
	private static SharedPreferences getPrefs(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
