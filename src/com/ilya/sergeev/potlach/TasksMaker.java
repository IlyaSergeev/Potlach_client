package com.ilya.sergeev.potlach;

import retrofit.RetrofitError;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.client.TouchSvcApi;
import com.ilya.sergeev.potlach.client.VoteSvcApi;

public class TasksMaker extends IntentService
{
	private static final String TASK_TAG = "task";
	
	private static final String TASK_VOTE_UP = "vote_up";
	private static final String TASK_VOTE_DOWN = "vote_down";
	private static final String TASK_TOUCH = "touch";
	
	public static String GIFT_ID_TAG = "gift_id";
	
	public static Intent getTouchIntent(Context context, long giftId)
	{
		Intent voteIntent = new Intent(context, TasksMaker.class);
		voteIntent.putExtra(TASK_TAG, TASK_TOUCH);
		voteIntent.putExtra(GIFT_ID_TAG, giftId);
		return voteIntent;
	}
	
	public static Intent getVoteUpIntent(Context context, long giftId)
	{
		return getVoteIntent(context, TASK_VOTE_UP, giftId);
	}
	
	public static Intent getVoteDownIntent(Context context, long giftId)
	{
		return getVoteIntent(context, TASK_VOTE_DOWN, giftId);
	}
	
	private static Intent getVoteIntent(Context context, String voteTask, long giftId)
	{
		Intent voteIntent = new Intent(context, TasksMaker.class);
		voteIntent.putExtra(TASK_TAG, voteTask);
		voteIntent.putExtra(GIFT_ID_TAG, giftId);
		return voteIntent;
	}
	
	public TasksMaker()
	{
		super(TasksMaker.class.getName());
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		if (intent == null)
		{
			return;
			
		}
		Bundle extras = intent.getExtras();
		if (extras == null || !extras.containsKey(TASK_TAG))
		{
			return;
		}
		switch (extras.getString(TASK_TAG))
		{
			case TASK_VOTE_UP:
				vote(extras, 1);
				break;
			
			case TASK_VOTE_DOWN:
				vote(extras, -1);
				break;
				
			case TASK_TOUCH:
				touchGift(extras);
				break;
			
			default:
				break;
		}
	}
	
	private void touchGift(Bundle args)
	{
		try
		{
			ServerSvc
					.getServerApi()
					.getApi(TouchSvcApi.class)
					.touch(args.getLong(GIFT_ID_TAG));
		}
		catch (RetrofitError ex)
		{
			ex.printStackTrace();
			// TODO send logout error
		}
	}
	
	private void vote(Bundle args, int vote)
	{
		try
		{
			ServerSvc
					.getServerApi()
					.getApi(VoteSvcApi.class)
					.sendVote(args.getLong(GIFT_ID_TAG), vote);
		}
		catch (RetrofitError ex)
		{
			ex.printStackTrace();
			// TODO send logout error
		}
	}
}
