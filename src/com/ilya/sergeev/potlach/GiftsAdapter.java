package com.ilya.sergeev.potlach;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.image_loader.GiftImageLoader;

public class GiftsAdapter extends BaseAdapter
{
	private final List<Gift> mGifts;
	private final GiftImageLoader mImageLoader;
	
	public GiftsAdapter(List<Gift> gifts, GiftImageLoader imageLoader)
	{
		super();
		
		mGifts = gifts;
		mImageLoader = imageLoader;
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
		
		final Gift gift = mGifts.get(position);
		
		// TODO extract loading to background
		View loadingText = convertView.findViewById(R.id.loading_view);
		loadingText.setVisibility(View.GONE);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
		imageView.setBackgroundResource(0);
		
		mImageLoader.DisplayImage(gift, 0, imageView);
		
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
		titleTextView.setText(gift.getTitle());
		
		TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
		messageTextView.setText(gift.getMessage());
		
		return convertView;
	}
}
