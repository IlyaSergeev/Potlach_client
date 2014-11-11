package com.ilya.sergeev.potlach;

import java.util.Collection;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class MyGiftsFragment extends ListOfGiftsFragment
{

	@Override
	protected Collection<Gift> getGifts()
	{
		return ServerSvc.getServerApi().getGiftsApi().getMyGifts();
	}
	
}
