package com.ilya.sergeev.potlach;

import java.util.Collection;

import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class NewGiftFragment extends ListOfGiftsFragment
{
	
	@Override
	protected Collection<GiftInfo> getGifts()
	{
		return ServerSvc.getServerApi().getGiftsApi().getAllGifts();
	}
}
