package com.ilya.sergeev.potlach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingsFragment extends MainContentFragment
{
	private CheckBox mCheckBox;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
				
		mCheckBox = (CheckBox) view.findViewById(R.id.show_bad_gifts);		
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		mCheckBox.setChecked(ApplicationSettings.showBadGifts(getActivity()));
		mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				ApplicationSettings.setShowBadGifts(isChecked, getActivity());
			}
		});
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mCheckBox.setOnCheckedChangeListener(null);
	}
}
