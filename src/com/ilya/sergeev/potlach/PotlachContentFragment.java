package com.ilya.sergeev.potlach;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

abstract class PotlachContentFragment extends Fragment
{
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		Bundle args = getArguments();
		if (args != null && args.containsKey(ARG_SECTION_NUMBER))
		{
			((PotlachMainActivity) activity).onSectionAttached(SectionActionType.valueOf(args.getString(ARG_SECTION_NUMBER)));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
	}
}
