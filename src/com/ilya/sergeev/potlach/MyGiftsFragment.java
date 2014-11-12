package com.ilya.sergeev.potlach;

import java.util.Collection;

import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class MyGiftsFragment extends ListOfGiftsFragment
{

	@Override
	protected Collection<GiftInfo> getGifts()
	{
		return ServerSvc.getServerApi().getApi(GiftSvcApi.class).getMyGifts();
	}
	
}
