package com.ilya.sergeev.potlach;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ServerSvc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGiftActivity extends ActionBarActivity
{
	public static final String PHOTO_PATH_ARG = "photo_path_arg";
	public static final String PHOTO_BITMAP_ARG = "photo_bitmap_arg";
	
	private Bitmap mImageBitmap = null;
	
	private Uri mTempImageFile = null;
	
	private ImageView mImageView;
	private ProgressBar mProgressBar;
	private EditText mTitleTextView;
	private EditText mMessageTextView;
	private View mLoadingView;
	private TextView mSendingTextView;
	
	private SendingTask mSeningTask = null;
	
	public static Intent createPotlachIntent(Uri imageUri, Context context)
	{
		Intent intent = new Intent(context, CreateGiftActivity.class);
		intent.putExtra(CreateGiftActivity.PHOTO_PATH_ARG, imageUri);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_gift);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		mImageView = (ImageView) findViewById(R.id.image_view);
		mImageView.setBackgroundResource(0);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mLoadingView = findViewById(R.id.loading_view);
		mSendingTextView = (TextView) findViewById(R.id.sending_text_view);
		
		showImageLoadingProgress(false);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			if (extras.containsKey(PHOTO_BITMAP_ARG))
			{
				setImage((Bitmap) extras.get(PHOTO_BITMAP_ARG));
			}
			else if (extras.containsKey(PHOTO_PATH_ARG))
			{
				startLoadImage((Uri) extras.get(PHOTO_PATH_ARG));
			}
		}
		
		mTitleTextView = (EditText) findViewById(R.id.title_edit_text);
		mTitleTextView.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				mTitleTextView.setError(null);
			}
		});
		
		mMessageTextView = (EditText) findViewById(R.id.message_text_view);
		mMessageTextView.setOnEditorActionListener(new EditText.OnEditorActionListener()
		{
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_SEND)
				{
					sendAction(v);
					return true;
				}
				return false;
			}
		});
	}
	
	private void setImage(Bitmap bitmap)
	{
		mImageBitmap = bitmap;
		if (mImageView != null)
		{
			mImageView.setImageBitmap(mImageBitmap);
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		mImageView.setImageBitmap(mImageBitmap);
		mLoadingView.setVisibility((mSeningTask != null) ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			if (requestCode == DialogHelper.SELECT_PHOTO_FROM_GALERY_REQUEST)
			{
				startLoadImage(data.getData());
			}
			else if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST)
			{
				startLoadImage(mTempImageFile);
			}
		}
		else
		{
			if (requestCode == DialogHelper.CREATE_NEW_PHOTO_REQUEST && mTempImageFile != null)
			{
				deleteTempFile();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		deleteTempFile();
	}
	
	private void showImageLoadingProgress(boolean isVisible)
	{
		mProgressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		mImageView.setVisibility(isVisible ? View.GONE : View.VISIBLE);
	}
	
	private void startLoadImage(Uri uri)
	{
		new AsyncTask<Uri, Void, Bitmap>()
		{
			protected void onPreExecute()
			{
				setImage(null);
				showImageLoadingProgress(true);
			};
			
			@Override
			protected Bitmap doInBackground(Uri... params)
			{
				try
				{
					InputStream imageStream;
					imageStream = getContentResolver().openInputStream(params[0]);
					return BitmapFactory.decodeStream(imageStream);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					return null;
				}
			}
			
			@Override
			protected void onPostExecute(Bitmap result)
			{
				super.onPostExecute(result);
				setImage(result);
				showImageLoadingProgress(false);
			}
		}.execute(uri);
	}
	
	public void changeImageAction(View sender)
	{
		showCreatePotlachDialog();
	}
	
	private void showCreatePotlachDialog()
	{
		deleteTempFile();
		File photoFile = null;
		try
		{
			photoFile = DialogHelper.createImageFile(this);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		if (photoFile != null)
		{
			mTempImageFile = Uri.fromFile(photoFile);
			DialogHelper.showPhotoResourceDialog(this, mTempImageFile);
		}
	}
	
	private void deleteTempFile()
	{
		if (mTempImageFile != null)
		{
			File file = new File(mTempImageFile.getPath());
			if (file.exists())
			{
				file.delete();
			}
			mTempImageFile = null;
		}
	}
	
	public void sendAction(View sender)
	{
		if (mTitleTextView.getText().length() == 0)
		{
			mTitleTextView.setError(getString(R.string.enter_some_words_as_a_title));
			mTitleTextView.requestFocus();
		}
		else
		{
			if (mImageBitmap != null)
			{
				startSending(mTitleTextView.getText().toString(), mMessageTextView.getText().toString(), mImageBitmap);
			}
		}
	}
	
	private void startSending(String title, String message, Bitmap imageBitmap)
	{
		cancelSending();
		mSeningTask = new SendingTask(title, message, imageBitmap);
		mSeningTask.execute();
	}
	
	private void cancelSending()
	{
		if (mSeningTask != null)
		{
			mSeningTask.cancel(false);
			mSeningTask = null;
		}
	}
	
	public void cancelLoading(View sender)
	{
		mSendingTextView.setText(R.string.cancelling_);
		cancelSending();
	}
	
	private class SendingTask extends AsyncTask<Void, Void, Boolean>
	{
		private final String mTitle;
		private final String mMessage;
		private final Bitmap mImageBitmap;
		
		public SendingTask(String title, String message, Bitmap imageBitmap)
		{
			mTitle = title;
			mMessage = message;
			mImageBitmap = imageBitmap;
		}
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mSendingTextView.setText(R.string.sending_);
			mLoadingView.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Boolean doInBackground(Void... params)
		{
			File imageFile = null;
			try
			{
				imageFile = DialogHelper.createImageFile(CreateGiftActivity.this);
				OutputStream out = new FileOutputStream(imageFile);
				mImageBitmap.compress(CompressFormat.JPEG, 75, out);
				
				GiftSvcApi giftsApi = ServerSvc.getServerApi().getApi(GiftSvcApi.class);
				Gift gift = new Gift(mTitle, mMessage);
				gift = giftsApi.createGift(gift);
				giftsApi.setImageData(gift.getId(), new TypedFile("image/jpg", imageFile));
				
				return true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			catch (RetrofitError ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				if (imageFile != null)
				{
					imageFile.delete();
				}
			}
			return false;
		}
		
		protected void onPostExecute(Boolean result)
		{
			if (result)
			{
				// TODO send broadcast to refresh screen
				onBackPressed();
			}
			mSeningTask = null;
			mLoadingView.setVisibility(View.GONE);
		};
		
		@Override
		protected void onCancelled()
		{
			super.onCancelled();
			mSeningTask = null;
			Toast.makeText(CreateGiftActivity.this, R.string.sending_was_cancelled, Toast.LENGTH_SHORT).show();
			mLoadingView.setVisibility(View.GONE);
		}
	}
}
