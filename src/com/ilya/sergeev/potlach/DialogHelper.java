package com.ilya.sergeev.potlach;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;

final class DialogHelper
{
	public static final int SELECT_PHOTO_FROM_GALERY_REQUEST = 101;
	public static final int CREATE_NEW_PHOTO_REQUEST = 102;
	
	private DialogHelper()
	{
	}
	
	public static void showPhotoResourceDialog(final Activity activity)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Photo resource")
				.setItems(new String[]
				{
						activity.getString(R.string.get_from_galery),
						activity.getString(R.string.make_new_phote),
				},
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								int requestCode = 0;
								Intent intent = null;
								switch (which)
								{
									case 0:
										intent = new Intent(Intent.ACTION_PICK);
										intent.setType("image/*");
										requestCode = SELECT_PHOTO_FROM_GALERY_REQUEST;
										break;
									
									case 1:
										intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										requestCode = CREATE_NEW_PHOTO_REQUEST;
										break;
								}
								if (intent.resolveActivity(activity.getPackageManager()) != null)
								{
									activity.startActivityForResult(intent, requestCode);
								}
							}
						});
		builder.create().show();
	}
}
