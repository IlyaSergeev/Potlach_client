package com.ilya.sergeev.potlach.model;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Settings
{
	private static final String UPDATE_PERIOD_TAG = "settings.update_period";
	
	private Settings()
	{
	}
	
	private static SharedPreferences getPrefs(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static long getUpdatePeriodMills(Context context)
	{
		return getPrefs(context).getLong(UPDATE_PERIOD_TAG, TimeUnit.MINUTES.toMillis(1));
	}
	
	public static boolean setUpdatePeriod(long updatePeriodMills, Context context)
	{
		return getPrefs(context)
				.edit()
				.putLong(UPDATE_PERIOD_TAG, updatePeriodMills)
				.commit();
	}
}
