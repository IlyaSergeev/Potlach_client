package com.ilya.sergeev.potlach;

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
import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;

public class TopRateFragment extends MainContentFragment
{
	private ListView mListView;
	private ProgressBar mProgressBar;
	private AsyncTask<Void, Void, List<UserInfo>> mRefreshTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_top_rate, container, false);
		
		mListView = (ListView) view.findViewById(R.id.list_view);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if (mRefreshTask == null)
		{
			refreshTopRate();
			showProgress(false);
		}
		else
		{
			showProgress(true);
		}
	}
	
	private void refreshTopRate()
	{
		if (mRefreshTask != null)
		{
			mRefreshTask.cancel(false);
		}
		mRefreshTask = new AsyncTask<Void, Void, List<UserInfo>>()
		{
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				
				showProgress(true);
			}
			
			@Override
			protected List<UserInfo> doInBackground(Void... params)
			{
				UserInfoSvcApi userApi = ServerSvc.getServerApi().getApi(UserInfoSvcApi.class);
				
				List<UserInfo> users = null;
				try
				{
					users = Lists.newLinkedList(userApi.getTopUsers());
				}
				catch (RetrofitError ex)
				{
					//TODO
//					getActivity().sendBroadcast(new Intent(Broadcasts.SIGN_OUT_BROADCAST));
					users = null;
				}
				return users;
			}
			
			@Override
			protected void onPostExecute(List<UserInfo> users)
			{
				super.onPostExecute(users);
				
				showProgress(false);
				mRefreshTask = null;
				
				if (mListView != null)
				{
					mListView.setAdapter(new UsersAdapter(users));
				}
			}
			
		};
		mRefreshTask.execute();
	}
	
	private void showProgress(boolean isVisible)
	{
		if (mListView != null)
		{
			mListView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
		}
		if (mProgressBar != null)
		{
			mProgressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		}
	}
}
