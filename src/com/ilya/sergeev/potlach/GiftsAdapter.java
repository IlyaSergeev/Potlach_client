package com.ilya.sergeev.potlach;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya.sergeev.potlach.client.Gift;

public class GiftsAdapter extends BaseAdapter
{
	private final List<Gift> mGifts;
	
	public GiftsAdapter(List<Gift> gifts)
	{
		super();
		mGifts = gifts;
	}
	
	@Override
	public int getCount()
	{
		if (mGifts == null)
		{
			return 0;
		}
		return mGifts.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mGifts.get(position);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gift, parent, false);
		}
		
		Gift gift = mGifts.get(position);
		
		// TODO extract loading to background
		View loadingText = convertView.findViewById(R.id.loading_view);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
		if (!TextUtils.isEmpty(gift.getUrl()))
		{
			try
			{
				URL url = new URL(gift.getUrl());
				imageView.setImageBitmap(BitmapFactory.decodeStream(url.openStream()));
				imageView.setBackgroundResource(0);
				loadingText.setVisibility(View.GONE);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				imageView.setBackgroundResource(R.drawable.border);
				imageView.setImageBitmap(null);
				loadingText.setVisibility(View.VISIBLE);
			}
		}
		else
		{
			imageView.setBackgroundResource(R.drawable.border);
			imageView.setImageBitmap(null);
			loadingText.setVisibility(View.VISIBLE);
		}
		
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
		titleTextView.setText(gift.getTitle());
		
		TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
		messageTextView.setText(gift.getMessage());
		
		return convertView;
	}
	
}
