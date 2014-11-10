package com.ilya.sergeev.potlach.mock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ilya.sergeev.potlach.LoginActivity;
import com.ilya.sergeev.potlach.R;
import com.ilya.sergeev.potlach.ServerSvc;

public class ContentGenerateActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_generate);
	}
	
	public void signoutClick(View sender)
	{
		ServerSvc.signout();
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
	public void generateSomeContent(View sender)
	{
		//TODO generate some content
	}
}
