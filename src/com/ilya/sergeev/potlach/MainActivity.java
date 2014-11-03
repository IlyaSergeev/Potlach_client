package com.ilya.sergeev.potlach;

import java.io.File;
import java.io.IOException;

import com.ilya.sergeev.potlach.model.Broadcasts;
import com.ilya.sergeev.potlach.model.UserHelper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private CharSequence mTitle;
	
	private Uri mTempPhotoFile = null;
	private SectionActionType mCurrentActionType;
	
	private BroadcastReceiver mSignOutReceiver = new BroadcastReceiver()
	{
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		registerReceiver(mSignOutReceiver, new IntentFilter(Broadcasts.SIGN_OUT_BROADCAST));
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		unregisterReceiver(mSignOutReceiver);
	}
	
	@Override
	public void onNavigationDrawerItemSelected(SectionActionType actionType)
	{
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, FragmentFactory.getInstance(actionType))
				.commit();
	}
	
	@Override
	public void onSignOutSelect()
	{
		showSigOutDialog();
	}
	
	private void showSigOutDialog()
	{
		new AlertDialog.Builder(this)
				.setTitle("Are you  realy want to sign out?")
				.setPositiveButton("Sign Out", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						UserHelper.signOut(MainActivity.this);
					}
				})
				.create().show();
	}
	
	private void showCreatePotlachDialog()
	{
		deleteTempImage();
		File photoFile = null;
		try
		{
			photoFile = DialogHelper.createImageFile();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		if (photoFile != null)
		{
			mTempPhotoFile = Uri.fromFile(photoFile);
			DialogHelper.showPhotoResourceDialog(this, mTempPhotoFile);
		}
	}
	
	private void deleteTempImage()
	{
		if (mTempPhotoFile != null)
		{
			File file = new File(mTempPhotoFile.getPath());
			if (file.exists())
			{
				file.delete();
			}
			mTempPhotoFile = null;
		}
	}
	
	public void onSectionAttached(SectionActionType actionType)
	{
		switch (actionType)
		{
			case POTLACH_MY:
				mTitle = getString(R.string.my_gifts);
				break;
			
			case POTLACH_WALL:
				mTitle = getString(R.string.new_gifts);
				break;
			
			case POTLACH_TOP_RATE:
				mTitle = getString(R.string.top_rate);
				break;
			
			case POTLACH_SEARCH:
				mTitle = getString(R.string.search_gifts);
				break;
			
			case SETTINGS:
				mTitle = getString(R.string.settings);
				break;
			
			default:
				mTitle = null;
		}
		mCurrentActionType = actionType;
		invalidateOptionsMenu();
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
			
			switch (mCurrentActionType)
			{
				case POTLACH_MY:
				case POTLACH_WALL:
					getMenuInflater().inflate(R.menu.menu_add_gift, menu);
					restoreActionBar();
					return true;
					
				case POTLACH_SEARCH:
					getMenuInflater().inflate(R.menu.menu_search, menu);
					MenuItem searchItem = menu.findItem(R.id.action_search);
					SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
					searchView.setQueryHint("Поиск");
					//TODO replace method
					searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
					{
						
						@Override
						public boolean onQueryTextSubmit(String arg0)
						{
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override
						public boolean onQueryTextChange(String arg0)
						{
							// TODO Auto-generated method stub
							return false;
						}
					});
					restoreActionBar();
					return true;
					
				case POTLACH_TOP_RATE:
				case SETTINGS:
				default:
					break;
			}
			restoreActionBar();
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_add_potlach)
		{
			showCreatePotlachDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		deleteTempImage();
	}
	
	private static class FragmentFactory
	{
		public static MainContentFragment getInstance(SectionActionType actionType)
		{
			MainContentFragment resultFragment = null;
			Bundle args = new Bundle();
			switch (actionType)
			{
				case POTLACH_MY:
					resultFragment = new MyGiftsFragment();
					break;
				
				case POTLACH_WALL:
					resultFragment = new GiftWallFragment();
					break;
				
				case POTLACH_TOP_RATE:
					resultFragment = new TopRateFragment();
					break;
				
				case POTLACH_SEARCH:
					resultFragment = new SearchFragment();
					break;
				
				case SETTINGS:
					resultFragment = new SettingsFragment();
					break;
				
				default:
					throw new IllegalArgumentException("user click unknown menu section");
			}
			if (resultFragment != null)
			{
				args.putString(MainContentFragment.ARG_SECTION_NUMBER, actionType.name());
				resultFragment.setArguments(args);
			}
			return resultFragment;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			if (requestCode == DialogHelper.SELECT_PHOTO_FROM_GALERY_REQUEST)
			{
				startActivity(CreateGiftActivity.createPotlachIntent(data.getData(), this));
			}
			else if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST)
			{
				startActivity(CreateGiftActivity.createPotlachIntent(mTempPhotoFile, this));
			}
		}
		else
		{
			if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST && mTempPhotoFile != null)
			{
				deleteTempImage();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}