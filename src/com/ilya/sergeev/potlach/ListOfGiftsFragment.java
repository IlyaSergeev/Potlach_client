package com.ilya.sergeev.potlach;

import java.util.Collection;
import java.util.List;

import retrofit.RetrofitError;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.image_loader.GiftImageLoader;

public abstract class ListOfGiftsFragment extends MainContentFragment
{
	private ListView mListView;
	private ProgressBar mProgressBar;
	private View mNoGiftsView;
	private AsyncTask<Void, Void, List<GiftInfo>> mReloadTask = null;
	private GiftImageLoader mImageLoader;
	private GiftsAdapter.VoteListener mVoteListener = new GiftsAdapter.VoteListener()
	{
		
		@Override
		public void pressLike(GiftInfo giftInfo)
		{
			long giftId = giftInfo.getGift().getId();
			Vote vote = giftInfo.getVote();
			if (vote == null)
			{
				vote = new Vote(null, giftId);
				giftInfo.setVote(vote);
			}
			vote.setVote(1);
			
			Activity activity = getActivity();
			if (activity != null)
			{
				Intent voteUpIntent = TasksMaker.getVoteUp(activity, giftId);
				activity.startService(voteUpIntent);
			}
		}
		
		@Override
		public void pressDislike(GiftInfo giftInfo)
		{
			long giftId = giftInfo.getGift().getId();
			Vote vote = giftInfo.getVote();
			if (vote == null)
			{
				vote = new Vote(null, giftId);
				giftInfo.setVote(vote);
			}
			vote.setVote(-1);
			
			Activity activity = getActivity();
			if (activity != null)
			{
				Intent voteDownIntent = TasksMaker.getVoteDown(activity, giftId);
				activity.startService(voteDownIntent);
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		mImageLoader = new GiftImageLoader(inflater.getContext(), ServerSvc.getServerApi().getGiftsApi());
		View view = inflater.inflate(R.layout.fragment_gift_wall, container, false);
		
		mListView = (ListView) view.findViewById(R.id.list_view);
		mListView.setOnItemClickListener(new ListView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Object obj = parent.getAdapter().getItem((int) id);
				if (obj instanceof GiftInfo)
				{
					GiftInfo giftInfo = (GiftInfo) obj;
					Gift gift = giftInfo.getGift(); 
					//TODO show single gift 
				}
			}
		});
		mListView.setOnScrollListener(new ListView.OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				mImageLoader.setCanUpdate(scrollState == SCROLL_STATE_IDLE);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
			}
		});
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		mNoGiftsView = view.findViewById(R.id.no_gifts_view);
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if (mReloadTask == null)
		{
			reloadPotlaches();
			showProgress(false);
		}
		else
		{
			showProgress(true);
		}
	}
	
	private void reloadPotlaches()
	{
		if (mReloadTask != null)
		{
			mReloadTask.cancel(false);
		}
		mReloadTask = new AsyncTask<Void, Void, List<GiftInfo>>()
		{
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				showProgress(true);
			}
			
			@Override
			protected List<GiftInfo> doInBackground(Void... params)
			{
				
				List<GiftInfo> gifts = null;
				try
				{
					gifts = Lists.newArrayList(getGifts());
				}
				catch (RetrofitError ex)
				{
					// TODO make some logic
					ex.printStackTrace();
					gifts = null;
				}
				return gifts;
			}
			
			@Override
			protected void onPostExecute(List<GiftInfo> gifts)
			{
				super.onPostExecute(gifts);
				
				showProgress(false);
				mReloadTask = null;
				
				if (mListView != null)
				{
					mListView.setAdapter(new GiftsAdapter(gifts, mImageLoader, mVoteListener));
					if (gifts == null || gifts.size() == 0)
					{
						mNoGiftsView.setVisibility(View.VISIBLE);
					}
				}
			}
		};
		mReloadTask.execute();
	}
	
	protected abstract Collection<GiftInfo> getGifts();
	
	private void showProgress(boolean progressEnable)
	{
		if (mListView != null)
		{
			mListView.setVisibility(progressEnable ? View.GONE : View.VISIBLE);
		}
		if (mProgressBar != null)
		{
			mProgressBar.setVisibility(progressEnable ? View.VISIBLE : View.GONE);
		}
		mNoGiftsView.setVisibility(View.GONE);
	}
}
