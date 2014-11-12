package com.ilya.sergeev.potlach;

import java.util.Collection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class SearchFragment extends ListOfGiftsFragment
{
	private String mKeyString = "";
	
	public void setKeyString(String keyString)
	{
		mKeyString = keyString;
		reloadGifts();
	}
	
	private BroadcastReceiver mBroadcast = new BroadcastReceiver()
	{
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			setKeyString(intent.getExtras().getString(Broadcasts.PARAM_KEY_WORD));
		}
	};
	
	@Override
	protected Collection<GiftInfo> getGifts()
	{
		if (TextUtils.isEmpty(mKeyString))
		{
			return null;
		}
		return ServerSvc.getServerApi().getApi(GiftSvcApi.class).searchGift(mKeyString);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		getActivity().registerReceiver(mBroadcast, new IntentFilter(Broadcasts.SEARCH_GIFTS_BROADCAST));
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		getActivity().unregisterReceiver(mBroadcast);
	}
}
