package com.ilya.sergeev.potlach;

import java.util.List;

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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rating, parent, false);
		}
		
		UserInfo user = mUsers.get(position);
		
		TextView text1 = (TextView) convertView.findViewById(R.id.author_text_view);
		text1.setText(user.getName());
		
		TextView text2 = (TextView) convertView.findViewById(R.id.rating_text_view);
		text2.setText(String.valueOf(user.getRating()));
		
		return convertView;
	}
	
}
