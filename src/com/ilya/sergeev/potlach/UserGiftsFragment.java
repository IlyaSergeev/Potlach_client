package com.ilya.sergeev.potlach;

import java.util.Collection;

import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class UserGiftsFragment extends ListOfGiftsFragment
{	
	@Override
	protected Collection<GiftInfo> getGifts()
	{
		String userName = getArguments().getString(Broadcasts.PARAM_USER_NAME);
		return ServerSvc.getServerApi().getApi(GiftSvcApi.class).getGifts(userName);
	}
	
}
