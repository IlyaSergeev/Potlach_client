package com.ilya.sergeev.potlach.image_loader;

import java.io.File;

import android.content.Context;

class ImageCache
{
	private File cacheDir;
	
	public ImageCache(Context context)
	{
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
		{
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "TempImages");
		}
		else
		{
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists())
		{
			cacheDir.mkdirs();
		}
	}
	
	public File getFile(Object key)
	{
		String filename = String.valueOf(key.hashCode());
		File f = new File(cacheDir, filename);
		return f;
	}
	
	public void clear()
	{
		File[] files = cacheDir.listFiles();
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
