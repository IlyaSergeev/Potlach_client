package com.ilya.sergeev.potlach;

public final class Broadcasts
{
	private Broadcasts()
	{
	}
	
	public static final String SIGN_OUT_BROADCAST = "com.ilya.sergeev.sign_out_broadcast";
	public static final String REFRESH_BROADCAST = "com.ilya.sergeev.refresh";
	public static final String SEARCH_GIFTS_BROADCAST = "com.ilya.sergeev.search_gift";
	public static final String SHOW_USER_GIFTS_BROADCAST = "com.ilya.sergeev.show_user_gifts";
	
	public static final String PARAM_KEY_WORD = "key_word";
	public static final String PARAM_USER_NAME = "user_name";
}
