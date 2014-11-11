package com.ilya.sergeev.potlach;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NavigationDrawerFragment extends Fragment
{
	
	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
	
	/**
	 * Per the design guidelines, you should show the drawer on launch until the user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	
	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;
	
	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;
	
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	
	public NavigationDrawerFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
		
		if (savedInstanceState != null)
		{
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}
		
		selectItem(mCurrentSelectedPosition);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				selectItem(position);
			}
		});
		String userName = UserHelper.getLogin(inflater.getContext());
		if (TextUtils.isEmpty(userName))
		{
			userName = "NO USER NAME";
		}
		mDrawerListView.setAdapter(new ArrayAdapter<String>(
				getActionBar().getThemedContext(),
				R.layout.layout_menu_item,
				R.id.text1,
				new String[] {
						userName,
						getString(R.string.new_gifts),
						getString(R.string.top_rate),
						getString(R.string.search_gifts),
						getString(R.string.settings),
						getString(R.string.sign_out),
				}));
		
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		
		return mDrawerListView;
	}
	
	public boolean isDrawerOpen()
	{
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}
	
	/**
	 * Users of this fragment must call this method to set up the navigation drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout)
	{
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(
				getActivity(),
				mDrawerLayout,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close
				)
				{
					@Override
					public void onDrawerClosed(View drawerView)
					{
						super.onDrawerClosed(drawerView);
						if (!isAdded())
						{
							return;
						}
						
						getActivity().supportInvalidateOptionsMenu();
					}
					
					@Override
					public void onDrawerOpened(View drawerView)
					{
						super.onDrawerOpened(drawerView);
						if (!isAdded())
						{
							return;
						}
						
						if (!mUserLearnedDrawer)
						{
							mUserLearnedDrawer = true;
							SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
							sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
						}
						
						getActivity().supportInvalidateOptionsMenu();
					}
				};
		
		if (!mUserLearnedDrawer && !mFromSavedInstanceState)
		{
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
		
		mDrawerLayout.post(new Runnable()
		{
			@Override
			public void run()
			{
				mDrawerToggle.syncState();
			}
		});
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	
	private void selectItem(int position)
	{
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null)
		{
			mDrawerListView.setItemChecked(position, true);
		}
		if (mDrawerLayout != null)
		{
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		if (mCallbacks != null)
		{
			if (position == 5)
			{
				mCallbacks.onSignOutSelect();
			}
			else
			{
				mCallbacks.onNavigationDrawerItemSelected(getSelectionActionType(position));
			}
		}
	}
	
	private SectionActionType getSelectionActionType(int position)
	{
		switch (position)
		{
			case 0:
				return SectionActionType.POTLACH_MY;
				
			case 1:
				return SectionActionType.POTLACH_WALL;
				
			case 2:
				return SectionActionType.POTLACH_TOP_RATE;
				
			case 3:
				return SectionActionType.POTLACH_SEARCH;
				
			case 4:
				return SectionActionType.SETTINGS;
				
			default:
				throw new IllegalArgumentException("Unknown menu section");
		}
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		try
		{
			mCallbacks = (NavigationDrawerCallbacks) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private ActionBar getActionBar()
	{
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}
	
	/**
	 * Callbacks interface that all activities using this fragment must implement.
	 */
	public static interface NavigationDrawerCallbacks
	{
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(SectionActionType actionType);
		
		void onSignOutSelect();
	}
}
