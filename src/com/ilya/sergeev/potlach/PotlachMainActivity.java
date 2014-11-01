package com.ilya.sergeev.potlach;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PotlachMainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private CharSequence mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}
	
	@Override
	public void onNavigationDrawerItemSelected(SectionActionType actionType)
	{
		switch (actionType)
		{
			case SIGN_OUT:
				// TODO make sign out dialog
				break;
			
			case POTLACH_CREATE:
				// TODO make create potlach dialog
				break;
			
			default:
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.container, FragmentFactory.getInstance(actionType))
						.commit();
				break;
		}
	}
	
	public void onSectionAttached(SectionActionType actionType)
	{
		switch (actionType)
		{
			case POTLACH_MY:
				mTitle = getString(R.string.user_name);
				break;
			
			case POTLACH_WALL:
				mTitle = getString(R.string.new_potlatchs);
				break;
			
			case POTLACH_VOTED:
				mTitle = getString(R.string.voted_potlaches);
				break;
			
			case POTLACH_SEARCH:
				mTitle = getString(R.string.search_potlaches);
				break;
			
			case SETTINGS:
				mTitle = getString(R.string.settings);
				break;
			
			default:
				mTitle = null;
		}
	}
	
	public void restoreActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (!mNavigationDrawerFragment.isDrawerOpen())
		{
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add_potlach)
		{
			// TODO create potlach
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private static class FragmentFactory
	{
		public static PotlachContentFragment getInstance(SectionActionType actionType)
		{
			PotlachContentFragment resultFragment = null;
			Bundle args = new Bundle();
			switch (actionType)
			{
				case POTLACH_MY:
					resultFragment = new MyPotlachFragment();
					// TODO set filter as MY POTLACHES
					break;
				
				case POTLACH_WALL:
					resultFragment = new PotlachWallFragment();
					// TODO set filter as ALL POTLACHES
					break;
				
				case POTLACH_VOTED:
					resultFragment = new VotedPotlachFragment();
					// TODO set filter as VOTED POTLACHES
					break;
				
				case POTLACH_SEARCH:
					resultFragment = new SearchFragment();
					// TODO set filter as SEARCH FRAGMENT
					break;
				
				case SETTINGS:
					resultFragment = new SettingsFragment();
					// TODO set filter as SETTINGS FRAGMENT
					break;
				
				case POTLACH_CREATE:
					resultFragment = new CreatePotlachFragment();
					// TODO set filter as CREATE POTLACH FRAGMENT
					break;
				
				default:
					throw new IllegalArgumentException("user click unknown menu section");
			}
			if (resultFragment != null)
			{
				args.putString(PotlachContentFragment.ARG_SECTION_NUMBER, actionType.name());
				resultFragment.setArguments(args);
			}
			return resultFragment;
		}
		
	}
}