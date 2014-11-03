package com.ilya.sergeev.potlach;

import java.util.concurrent.TimeUnit;

import com.ilya.sergeev.potlach.model.Settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class SettingsFragment extends PotlachContentFragment
{
	private RadioGroup mUpdateGroup;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_settings, container, false);
		
		mUpdateGroup = (RadioGroup) view.findViewById(R.id.update_radio_group);
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		switch ((int)(TimeUnit.MILLISECONDS.toMinutes(Settings.getUpdatePeriodMills(getActivity()))))
		{
			case 1:
				mUpdateGroup.check(R.id.radio_minut_1);
				break;
			
			case 5:
				mUpdateGroup.check(R.id.radio_minut_5);
				break;
			
			case 60:
				mUpdateGroup.check(R.id.radio_minut_60);
				break;
			
			default:
				mUpdateGroup.check(0);
				break;
		}
		
		mUpdateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				long updateMinuts = 1;
				switch (checkedId)
				{
					case R.id.radio_minut_1:
						updateMinuts = 1;
						break;
					
					case R.id.radio_minut_5:
						updateMinuts = 5;
						break;
					
					case R.id.radio_minut_60:
						updateMinuts = 60;
						break;
				}
				Settings.setUpdatePeriod(TimeUnit.MINUTES.toMillis(updateMinuts), getActivity());
			}
		});
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		mUpdateGroup.setOnCheckedChangeListener(null);
	}
}
