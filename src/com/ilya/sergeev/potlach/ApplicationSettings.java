package com.ilya.sergeev.potlach;

import android.content.Context;
import android.preference.PreferenceManager;

public final class ApplicationSettings
{
	private ApplicationSettings()
	{
	}
	
	public static final String CLIENT_ID = "mobile";
	public static final String SERVER_URL = "http://192.168.2.125:8080";
	
	private static final String SHOW_BAD_GIFTS_TAG = "show_bad_gifts";
	
	public static final boolean showBadGifts(Context context)
	{
		return PreferenceManager
				.getDefaultSharedPreferences(context)
				.getBoolean(SHOW_BAD_GIFTS_TAG, true);
	}
	
	public static final boolean setShowBadGifts(boolean show, Context context)
	{
		return PreferenceManager
				.getDefaultSharedPreferences(context)
				.edit()
				.putBoolean(SHOW_BAD_GIFTS_TAG, show)
				.commit();
	}
}
