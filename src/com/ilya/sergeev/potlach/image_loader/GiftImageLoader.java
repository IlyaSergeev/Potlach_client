package com.ilya.sergeev.potlach.image_loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.client.Response;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;

public class GiftImageLoader
{
	private final MemoryCache memoryCache = new MemoryCache();
	private final ImageCache fileCache;
	private final Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private final ExecutorService executorService;
	private final GiftSvcApi mGiftApi;
	
	public GiftImageLoader(Context context, GiftSvcApi giftApi)
	{
		fileCache = new ImageCache(context);
		executorService = Executors.newFixedThreadPool(5);
		mGiftApi = giftApi;
	}
	
	//TODO add image
	int stub_id = 0;
	
	public void DisplayImage(Gift gift, int loader, ImageView imageView)
	{
		stub_id = loader;
		imageViews.put(imageView, urlFromGift(gift));
		Bitmap bitmap = memoryCache.get(urlFromGift(gift));
		imageView.setImageBitmap(null);
		imageView.setImageResource(loader);
		if (bitmap != null)
		{
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			queuePhoto(gift, imageView);
		}
	}
	
	private void queuePhoto(Gift gift, ImageView imageView)
	{
		PhotoToLoad p = new PhotoToLoad(gift, imageView);
		executorService.submit(new PhotosLoader(p));
	}
	
	private Bitmap getBitmap(Gift gift)
	{
		File cacheFile = fileCache.getFile(urlFromGift(gift));
		
		// from SD cache
		Bitmap b = decodeFile(cacheFile);
		if (b != null)
		{
			return b;
		}
		// from web
		try
		{
			Response response = mGiftApi.getData(gift.getId());
			Utils.CopyStream(response.getBody().in(), new FileOutputStream(cacheFile));
			return decodeFile(cacheFile);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	private static String urlFromGift(Gift gift)
	{
		return String.valueOf(gift.getId());
	}
	
	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f)
	{
		try
		{			
			return BitmapFactory.decodeStream(new FileInputStream(f));
		}
		catch (FileNotFoundException e)
		{
		}
		return null;
	}
	
	// Task for the queue
	private class PhotoToLoad
	{
		public Gift mGift;
		public ImageView mImageView;
		
		public PhotoToLoad(Gift gift, ImageView imageView)
		{
			mGift = gift;
			mImageView = imageView;
		}
	}
	
	class PhotosLoader implements Runnable
	{
		PhotoToLoad photoToLoad;
		
		PhotosLoader(PhotoToLoad photoToLoad)
		{
			this.photoToLoad = photoToLoad;
		}
		
		@Override
		public void run()
		{
			if (imageViewReused(photoToLoad))
			{
				return;
			}
			Bitmap bmp = getBitmap(photoToLoad.mGift);
			memoryCache.put(urlFromGift(photoToLoad.mGift), bmp);
			if (imageViewReused(photoToLoad))
			{
				return;
			}
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.mImageView.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	boolean imageViewReused(PhotoToLoad photoToLoad)
	{
		String tag = imageViews.get(photoToLoad.mImageView);
		if (tag == null || !tag.equals(urlFromGift(photoToLoad.mGift)))
		{
			return true;
		}
		return false;
	}
	
	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		
		public BitmapDisplayer(Bitmap b, PhotoToLoad p)
		{
			bitmap = b;
			photoToLoad = p;
		}
		
		public void run()
		{
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.mImageView.setImageBitmap(bitmap);
			else
				photoToLoad.mImageView.setImageResource(stub_id);
		}
	}
	
	public void clearCache()
	{
		memoryCache.clear();
		fileCache.clear();
	}
	
}
