package com.ilya.sergeev.potlach.mock;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ilya.sergeev.potlach.LoginActivity;
import com.ilya.sergeev.potlach.R;
import com.ilya.sergeev.potlach.client.ServerSvc;

public class ContentGenerateActivity extends Activity
{
	private ContentGenerator mContentGenerator;
	
	private View mGenerateView;
	private View mProgressView;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		mContentGenerator = new ContentGenerator();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_generate);
		
		mProgressView = findViewById(R.id.progress_bar);
		mGenerateView = findViewById(R.id.create_content_view);
	}
	
	public void signoutClick(View sender)
	{
		ServerSvc.signout();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
	private void setProgressView(boolean progressActive)
	{
		if (progressActive)
		{
			mGenerateView.setVisibility(View.GONE);
			mProgressView.setVisibility(View.VISIBLE);
		}
		else
		{
			mGenerateView.setVisibility(View.VISIBLE);
			mProgressView.setVisibility(View.GONE);
		}
	}
	
	public void generateSomeContent(View sender)
	{
		new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				setProgressView(true);
			}
			
			@Override
			protected Void doInBackground(Void... params)
			{
				Random random = new Random();
				mContentGenerator.createSomeUsers(random.nextInt(5) + 2);
				mContentGenerator.createSomeGifts(random.nextInt(15) + 3, ContentGenerateActivity.this);
				mContentGenerator.createSomeVotes(random.nextInt(20) + 4, ContentGenerateActivity.this);
				mContentGenerator.createSomeTouches(random.nextInt(100), ContentGenerateActivity.this);
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				super.onPostExecute(result);
				setProgressView(false);
			}
			
		}.execute();
		
	}
}
