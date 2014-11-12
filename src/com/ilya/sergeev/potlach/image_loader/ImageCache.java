package com.ilya.sergeev.potlach.image_loader;

import java.io.File;

import android.content.Context;

class ImageCache
{
	private File mCacheDir;
	
	public ImageCache(Context context)
	{
		mCacheDir = context.getCacheDir();
		if (!mCacheDir.exists())
		{
			mCacheDir.mkdirs();
		}
	}
	
	public File getFile(String fileName)
	{
		return new File(mCacheDir, fileName);
	}
	
	public void clear()
	{
		File[] files = mCacheDir.listFiles();
		if (files == null)
		{
			return;
		}
		for (File f : files)
		{
			f.delete();
		}
	}
}
