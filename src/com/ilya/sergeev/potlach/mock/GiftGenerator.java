package com.ilya.sergeev.potlach.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import retrofit.RetrofitError;
import retrofit.mime.TypedFile;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.ApplicationConsts;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.UserInfo;

class GiftGenerator
{
	private static final String TAG = GiftGenerator.class.getName();
	
	public static GiftGenerator getGenerator(UsersGenerator userGenerator)
	{
		return new GiftGenerator(userGenerator);
	}
	
	private static final Random sRandom = new Random();
	
	private static final String[] sTitles = new String[] { "Wonderfull", "Perfect", "Unreal", "Wow", "I like it!!" };
	private static final String[] sMessages = new String[] { "Good place", "So wonderfull", "Never see this", "E-e-e-e", "Look at this" };
	
	private int index = 0;
	
	private final List<Gift> mGifts = Lists.newArrayList();
	private final UsersGenerator mUserGenerator;
	
	public GiftGenerator(UsersGenerator userGenerator)
	{
		mUserGenerator = userGenerator;
	}
	
	public Gift createGift(Context context)
	{
		Gift gift = new Gift(anyTitle(), anyMessage());
		UserInfo user = mUserGenerator.anyUser();
		GiftSvcApi giftApi = UserSessionsFactory.getRestAdapterForUser(ApplicationConsts.SERVER_URL, user.getName(), UsersGenerator.DEFAULT_PASSWORD).create(GiftSvcApi.class);
		try
		{
			int i = sRandom.nextInt(20);
			gift = giftApi.createGift(gift);
			mGifts.add(gift);
			
			File to = null;
			if (context != null)
			{
				try
				{
					String imageFileName = "image" + i;
					File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					
					to = File.createTempFile(
							imageFileName,
							".jpg",
							storageDir
							);
					InputStream in = context.getAssets().open("image" + i + ".jpg");
					OutputStream out = new FileOutputStream(to);
					
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0)
					{
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
				giftApi.setImageData(gift.getId(), new TypedFile("image/jpg", to));
			}
			Log.d(TAG, "create gift user:" + user.getName() + " giftId:" + gift.getId());
		}
		catch (RetrofitError ex)
		{
			gift = null;
		}
		return gift;
	}
	
	public Gift anyGift(Context context)
	{
		if (mGifts.size() == 0)
		{
			return createGift(context);
		}
		return mGifts.get(sRandom.nextInt(mGifts.size()));
	}
	
	public String anyTitle()
	{
		index++;
		int titleIndex = sRandom.nextInt(sTitles.length);
		return sTitles[titleIndex] + "   " + index;
	}
	
	public String anyMessage()
	{
		index++;
		
		String message = "";
		int wordsCount = sRandom.nextInt(15) + 1;
		for (int i = 0; i < wordsCount; i++)
		{
			int titleIndex = sRandom.nextInt(sMessages.length);
			message += sMessages[titleIndex] + " ";
		}
		return message;
	}
}
