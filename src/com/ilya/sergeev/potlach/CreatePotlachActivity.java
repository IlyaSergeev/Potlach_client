package com.ilya.sergeev.potlach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class CreatePotlachActivity extends ActionBarActivity
{
	public static final String PHOTO_PATH_ARG = "photo_path_arg";
	public static final String PHOTO_BITMAP_ARG = "photo_bitmap_arg";
	
	private Bitmap mImageBitmap = null;
	
	private Uri mTempImageFile = null;
	
	private ImageView mImageView;
	private ProgressBar mProgressBar;
	
	public static Intent createPotlachIntent(Uri imageUri, Context context)
	{
		Intent intent = new Intent(context, CreatePotlachActivity.class);
		intent.putExtra(CreatePotlachActivity.PHOTO_PATH_ARG, imageUri);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_potlach);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mImageView = (ImageView) findViewById(R.id.image_view);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		
		showImageLoadingProgress(false);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			if (extras.containsKey(PHOTO_BITMAP_ARG))
			{
				setImage((Bitmap) extras.get(PHOTO_BITMAP_ARG));
			}
			else if (extras.containsKey(PHOTO_PATH_ARG))
			{
				startLoadImage((Uri) extras.get(PHOTO_PATH_ARG));
			}
		}
	}
	
	private void setImage(Bitmap bitmap)
	{
		mImageBitmap = bitmap;
		if (mImageView != null)
		{
			mImageView.setImageBitmap(mImageBitmap);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mImageView.setImageBitmap(mImageBitmap);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			if (requestCode == DialogHelper.SELECT_PHOTO_FROM_GALERY_REQUEST)
			{
				startLoadImage(data.getData());
			}
			else if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST)
			{
				startLoadImage(mTempImageFile);
			}
		}
		else
		{
			if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST && mTempImageFile != null)
			{
				deleteTempFile();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		deleteTempFile();
	}
	
	private void showImageLoadingProgress(boolean isVisible)
	{
		mProgressBar.setVisibility(isVisible?View.VISIBLE:View.GONE);
		mImageView.setVisibility(isVisible?View.GONE:View.VISIBLE);
	}
	
	private void startLoadImage(Uri uri)
	{
		new AsyncTask<Uri, Void, Bitmap>()
		{
			protected void onPreExecute() 
			{
				setImage(null);
				showImageLoadingProgress(true);
			};
			
			@Override
			protected Bitmap doInBackground(Uri... params)
			{
				try
				{
					InputStream imageStream;
					imageStream = getContentResolver().openInputStream(params[0]);
					return BitmapFactory.decodeStream(imageStream);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			protected void onPostExecute(Bitmap result)
			{
				super.onPostExecute(result);
				setImage(result);
				showImageLoadingProgress(false);
			}
		}.execute(uri);
	}
	
	public void changeImageAction(View sender)
	{
		showCreatePotlachDialog();
	}
	
	private void showCreatePotlachDialog()
	{
		deleteTempFile();
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
			mTempImageFile = Uri.fromFile(photoFile);
			DialogHelper.showPhotoResourceDialog(this, mTempImageFile);
		}
	}
	
	private void deleteTempFile()
	{
		if (mTempImageFile != null)
		{
			File file = new File(mTempImageFile.getPath());
			if (file.exists())
			{
				file.delete();
			}
			mTempImageFile = null;
		}
	}
}
