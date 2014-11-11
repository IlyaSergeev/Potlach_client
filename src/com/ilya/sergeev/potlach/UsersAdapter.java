package com.ilya.sergeev.potlach;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilya.sergeev.potlach.client.UserInfo;

public class UsersAdapter extends BaseAdapter
{
	private final List<UserInfo> mUsers;
	
	public UsersAdapter(List<UserInfo> users)
	{
		super();
		mUsers = users;
	}
	
	@Override
	public int getCount()
	{
		if (mUsers == null)
		{
			return 0;
		}
		return mUsers.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mUsers.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Context context = parent.getContext();
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_activated_2, parent, false);
		}
		
		UserInfo user = mUsers.get(position);
		
		TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
		text1.setText(user.getName());
		
		TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
		text2.setText(context.getString(R.string.rating_) + user.getRating());
		
		return convertView;
	}
	
}
