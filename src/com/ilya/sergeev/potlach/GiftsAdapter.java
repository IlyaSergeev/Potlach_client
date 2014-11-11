package com.ilya.sergeev.potlach;

import java.io.IOException;
import java.util.List;

import retrofit.client.Response;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ServerSvc;

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
		
		final Gift gift = mGifts.get(position);
		
		// TODO extract loading to background
		final View loadingText = convertView.findViewById(R.id.loading_view);
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
		
		new AsyncTask<Void, Void, Bitmap>()
		{
			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				
				imageView.setBackgroundResource(R.drawable.border);
				imageView.setImageBitmap(null);
				loadingText.setVisibility(View.VISIBLE);
			}
			
			@Override
			protected Bitmap doInBackground(Void... params)
			{
				GiftSvcApi giftApi = ServerSvc.getServerApi().getGiftsApi();
				Response response = giftApi.getData(gift.getId());
				Bitmap result = null;
				try
				{
					result = BitmapFactory.decodeStream(response.getBody().in());
				}
				catch (IOException e)
				{
					result = null;
					e.printStackTrace();
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(Bitmap result)
			{
				super.onPostExecute(result);
				
				if (result != null)
				{
					imageView.setBackgroundResource(0);
					imageView.setImageBitmap(result);
					loadingText.setVisibility(View.GONE);
				}
			}
		}.execute();
		
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
		titleTextView.setText(gift.getTitle());
		
		TextView messageTextView = (TextView) convertView.findViewById(R.id.message_text_view);
		messageTextView.setText(gift.getMessage());
		
		return convertView;
	}
}
