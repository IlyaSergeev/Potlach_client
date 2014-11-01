package com.ilya.sergeev.potlach;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PotlachMainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private CharSequence mTitle;
	
	private Uri mTempPhotoFile = null;
	
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
				showSigOutDialog();
				break;
			
			case POTLACH_CREATE:
				showCreatePotlachDialog();
				break;
			
			default:
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, FragmentFactory.getInstance(actionType))
						.commit();
				break;
		}
	}
	
	private void showSigOutDialog()
	{
		new AlertDialog.Builder(this)
				.setTitle("Do you want to sign out?")
				.setPositiveButton("YES", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						
					}
				})
				.setNegativeButton("NO", new DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						
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
			
			case POTLACH_CREATE:
				mTitle = getString(R.string.create_ptlach_action);
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
		public static PotlachContentFragment getInstance(SectionActionType actionType)
		{
			PotlachContentFragment resultFragment = null;
			Bundle args = new Bundle();
			switch (actionType)
			{
				case POTLACH_MY:
					resultFragment = new MyPotlachFragment();
					break;
				
				case POTLACH_WALL:
					resultFragment = new PotlachWallFragment();
					break;
				
				case POTLACH_VOTED:
					resultFragment = new VotedPotlachFragment();
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
				args.putString(PotlachContentFragment.ARG_SECTION_NUMBER, actionType.name());
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
				startActivity(CreatePotlachActivity.createPotlachIntent(data.getData(), this));
			}
			else if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST)
			{
				startActivity(CreatePotlachActivity.createPotlachIntent(mTempPhotoFile, this));
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
