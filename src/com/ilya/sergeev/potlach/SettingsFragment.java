package com.ilya.sergeev.potlach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.sergeev.potlach.image_loader.MemoryCache;

public class SettingsFragment extends MainContentFragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}
	
	public void clearCache(View sender)
	{
		new MemoryCache().clear();
	}
}
