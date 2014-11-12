package com.ilya.sergeev.potlach.client;

import retrofit.http.POST;
import retrofit.http.Query;

public interface TouchSvcApi
{
	public static final String TOUCH_PATH = "/touch";
	
	public static final String GIFT_ID_PARAM = "gift";
	
	@POST(TOUCH_PATH)
	public Touch touch(@Query(GIFT_ID_PARAM) long giftId);
}
