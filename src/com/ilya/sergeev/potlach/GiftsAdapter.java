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
	public interface VoteListener
	{
		public void pressLike(GiftInfo giftInfo);
		
		public void pressDislike(GiftInfo giftInfo);
	}
	
	private final List<GiftInfo> mGifts;
	private final GiftImageLoader mImageLoader;
	private final VoteListener mListener;
	
	public GiftsAdapter(List<GiftInfo> gifts, GiftImageLoader imageLoader, VoteListener listener)
	{
		super();
		
		mGifts = gifts;
		mImageLoader = imageLoader;
		mListener = listener;
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
		
		final GiftInfo giftInfo = mGifts.get(position);
		
		Gift gift = giftInfo.getGift();
		Vote vote = giftInfo.getVote();
		
		final ImageButton likeButton = (ImageButton) convertView.findViewById(R.id.like_button);
		final ImageButton dislikeButton = (ImageButton) convertView.findViewById(R.id.dislike_button);
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
		likeButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				likeButton.setEnabled(false);
				dislikeButton.setEnabled(true);

				if (mListener != null)
				{
					mListener.pressLike(giftInfo);
				}
			}
		});
		dislikeButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				likeButton.setEnabled(true);
				dislikeButton.setEnabled(false);
				
				if (mListener != null)
				{
					mListener.pressDislike(giftInfo);
				}
			}
		});
		
		TextView seeTextView = (TextView) convertView.findViewById(R.id.rating_text_view);
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
