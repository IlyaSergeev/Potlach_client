package com.ilya.sergeev.potlach;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CreatePotlachFragment extends PotlachContentFragment
{	
	private Bitmap mImageBitmap = null;
	private Uri mImageUri = null;
	
	private ImageView mImageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_potlach_create, container, false);
		
		mImageView = (ImageView) view.findViewById(R.id.image_view);
		//TODO initialize view
		return view;
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if(mImageUri != null)
		{
			startLoadImage(mImageUri);
			mImageUri = null;
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		mImageView.setImageBitmap(mImageBitmap);
	}

	public Bitmap getImage()
	{
		return mImageBitmap;
	}

	public void setImage(Bitmap imageBitmap)
	{
		this.mImageBitmap = imageBitmap;
		if (mImageView != null)
		{
			mImageView.setImageBitmap(imageBitmap);
		}
	}
	
	public void setImageUri(Uri imageUri)
	{
		setImage(null);
		
		if (getActivity() != null)
		{
			startLoadImage(imageUri);
		}
		else
		{
			mImageUri = imageUri;
		}
	}
	
	private void startLoadImage(Uri uri)
	{
		new AsyncTask<Uri, Void, Bitmap>()
		{			
			@Override
			protected Bitmap doInBackground(Uri... params)
			{
				Activity activity = getActivity();
				if (activity == null)
				{
					return null;
				}
				try
				{
					InputStream imageStream;
					imageStream = getActivity().getContentResolver().openInputStream(params[0]);
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
			}
		}.execute(uri);
	}
}
