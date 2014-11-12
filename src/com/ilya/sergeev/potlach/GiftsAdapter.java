package com.ilya.sergeev.potlach;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.image_loader.GiftImageLoader;

public class GiftsAdapter extends BaseAdapter
{
	private final List<GiftInfo> mGifts;
	private final GiftImageLoader mImageLoader;
	
	public GiftsAdapter(List<GiftInfo> gifts, GiftImageLoader imageLoader)
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
		
		GiftInfo giftInfo = mGifts.get(position);
		
		Vote vote = giftInfo.getVote();
		ImageButton likeButton = (ImageButton) convertView.findViewById(R.id.like_button);
		ImageButton dislikeButton = (ImageButton) convertView.findViewById(R.id.dislike_button);
		likeButton.setEnabled(true);
		dislikeButton.setEnabled(true);
		if (vote != null)
		{
			if (vote.getVote() > 0)
			{
				likeButton.setEnabled(false);
			}
			else if (vote.getVote() < 0)
			{
				dislikeButton.setEnabled(false);
			}
		}
		//TODO set action on like or dislike
		
		Gift gift = giftInfo.getGift();
		
		TextView seeTextView =  (TextView) convertView.findViewById(R.id.rating_text_view);
		seeTextView.setText(String.valueOf(gift.getRating()));
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);		
		mImageLoader.DisplayImage(gift, R.drawable.image_mock, imageView);
		
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
		titleTextView.setText(gift.getTitle());
		
		TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
		messageTextView.setText(gift.getMessage());
		
		TextView authorTextView = (TextView) convertView.findViewById(R.id.author_view);
		Date createDate = new Date(gift.getDate());
		String authorText = String.format("%s\n%s\n%s", gift.getOwner(), SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(createDate), SimpleDateFormat.getDateInstance().format(createDate));
		authorTextView.setText(authorText);
		
		return convertView;
	}
}
