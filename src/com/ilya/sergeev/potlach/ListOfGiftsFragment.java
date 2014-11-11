package com.ilya.sergeev.potlach;

import java.util.Collection;
import java.util.List;

import retrofit.RetrofitError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.image_loader.GiftImageLoader;

public abstract class ListOfGiftsFragment extends MainContentFragment
{
	private ListView mListView;
	private ProgressBar mProgressBar;
	private View mNoGiftsView;
	private AsyncTask<Void, Void, List<Gift>> mReloadTask = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_gift_wall, container, false);
		
		mListView = (ListView) view.findViewById(R.id.list_view);
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
		mReloadTask = new AsyncTask<Void, Void, List<Gift>>()
		{
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				showProgress(true);
			}
			
			@Override
			protected List<Gift> doInBackground(Void... params)
			{
				
				List<Gift> gifts = null;
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
			protected void onPostExecute(List<Gift> gifts)
			{
				super.onPostExecute(gifts);
				
				showProgress(false);
				mReloadTask = null;
				
				if (mListView != null)
				{
					mListView.setAdapter(new GiftsAdapter(gifts, new GiftImageLoader(getActivity(), ServerSvc.getServerApi().getGiftsApi())));
					if (gifts == null || gifts.size() == 0)
					{
						mNoGiftsView.setVisibility(View.VISIBLE);
					}
				}
			}
		};
		mReloadTask.execute();
	}
	
	protected abstract Collection<Gift> getGifts();
	
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
