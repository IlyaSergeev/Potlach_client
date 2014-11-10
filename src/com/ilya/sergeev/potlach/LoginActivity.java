package com.ilya.sergeev.potlach;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.ilya.sergeev.potlach.client.ServerSvc;
import com.ilya.sergeev.potlach.client.SimpleMessage;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.mock.ContentGenerateActivity;
import com.ilya.sergeev.potlach.model.UserHelper;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>
{
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	// UI references.
	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
	private View mProgressView;
	private View mLoginFormView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Set up the login form.
		mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
		populateAutoComplete();
		
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
			{
				if (id == R.id.login || id == EditorInfo.IME_NULL)
				{
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		
		Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
		mEmailSignInButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptLogin();
			}
		});
		
		mLoginFormView = findViewById(R.id.login_form);
		mProgressView = findViewById(R.id.login_progress);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		mEmailView.setText(UserHelper.getLogin(this));
	}
	
	private void populateAutoComplete()
	{
		if (VERSION.SDK_INT >= 14)
		{
			// Use ContactsContract.Profile (API 14+)
			getLoaderManager().initLoader(0, null, this);
		}
		else if (VERSION.SDK_INT >= 8)
		{
			// Use AccountManager (API 8+)
			new SetupEmailAutoCompleteTask().execute(null, null);
		}
	}
	
	/**
	 * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email, missing fields, etc.), the errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin()
	{
		if (mAuthTask != null)
		{
			return;
		}
		
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		
		// Store values at the time of the login attempt.
		String email = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email))
		{
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		else if (!isLoginValid(email))
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (cancel)
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress(true);
			mAuthTask = new UserLoginTask(email, password);
			mAuthTask.execute((Void) null);
		}
	}
	
	private boolean isLoginValid(String email)
	{
		return "admin".equals(email) || email.contains("@");
	}
	
	private boolean isPasswordValid(String password)
	{
		return true; // password.length() > 4;
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show)
	{
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(
					show ? 0 : 1).setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
			
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(
					show ? 1 : 0).setListener(new AnimatorListenerAdapter()
			{
				@Override
				public void onAnimationEnd(Animator animation)
				{
					mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		}
		else
		{
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
	{
		if (Build.VERSION.SDK_INT > 14)
		{
			return onCreateLoaderApi14(i, bundle);
		}
		return null;
	}
	
	@TargetApi(14)
	private Loader<Cursor> onCreateLoaderApi14(int i, Bundle bundle)
	{
		return new CursorLoader(this,
				// Retrieve data rows for the device user's 'profile' contact.
				Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
						ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
				
				// Select only email addresses.
				ContactsContract.Contacts.Data.MIMETYPE +
						" = ?", new String[] { ContactsContract.CommonDataKinds.Email
						.CONTENT_ITEM_TYPE },
				
				// Show primary email addresses first. Note that there won't be
				// a primary email address if the user hasn't specified one.
				ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
	{
		List<String> emails = new ArrayList<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			emails.add(cursor.getString(ProfileQuery.ADDRESS));
			cursor.moveToNext();
		}
		
		addEmailsToAutoComplete(emails);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader)
	{
		
	}
	
	private interface ProfileQuery
	{
		String[] PROJECTION = {
				ContactsContract.CommonDataKinds.Email.ADDRESS,
				ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
		};
		
		int ADDRESS = 0;
		@SuppressWarnings("unused")
		int IS_PRIMARY = 1;
	}
	
	/**
	 * Use an AsyncTask to fetch the user's email addresses on a background thread, and update the email text field with results on the main UI thread.
	 */
	class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>>
	{
		
		@Override
		protected List<String> doInBackground(Void... voids)
		{
			ArrayList<String> emailAddressCollection = new ArrayList<String>();
			
			// Get all emails from the user's contacts and copy them to a list.
			ContentResolver cr = getContentResolver();
			Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					null, null, null);
			while (emailCur.moveToNext())
			{
				String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
						.CommonDataKinds.Email.DATA));
				emailAddressCollection.add(email);
			}
			emailCur.close();
			
			return emailAddressCollection;
		}
		
		@Override
		protected void onPostExecute(List<String> emailAddressCollection)
		{
			addEmailsToAutoComplete(emailAddressCollection);
		}
	}
	
	private void addEmailsToAutoComplete(List<String> emailAddressCollection)
	{
		// Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
		ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(LoginActivity.this,
						android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
		
		mEmailView.setAdapter(adapter);
	}
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, UserInfoSvcApi>
	{
		
		private final String mEmail;
		private final String mPassword;
		
		UserLoginTask(String email, String password)
		{
			mEmail = email;
			mPassword = password;
		}
		
		@Override
		protected UserInfoSvcApi doInBackground(Void... params)
		{
			UserInfoSvcApi serverApi = null;
			try
			{
				serverApi = ServerSvc.signin(mEmail, mPassword).getUsersApi();
				SimpleMessage helloMsg = serverApi.getHello();
				if (!Objects.equal(helloMsg.getUserName(), mEmail))
				{
					throw new IllegalAccessError();
				}
			}
			catch (IllegalAccessError e)
			{
				serverApi = null;
			}
			catch (RetrofitError e)
			{
				e.printStackTrace();
				if (!e.isNetworkError())
				{
					try
					{
						serverApi = ServerSvc.signout().getUsersApi();
						serverApi.createUser(mEmail, mPassword);
						
						serverApi = ServerSvc.signin(mEmail, mPassword).getUsersApi();
						SimpleMessage helloMsg = serverApi.getHello();
						if (!Objects.equal(helloMsg.getUserName(), mEmail))
						{
							throw new IllegalAccessError();
						}
						else
						{
							runOnUiThread(new Runnable()
							{
								
								@Override
								public void run()
								{
									Toast.makeText(LoginActivity.this, getString(R.string.registrate_user_) + mEmail, Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
					catch (RetrofitError ex)
					{
						ex.printStackTrace();
						serverApi = null;
					}
				}
				else
				{
					serverApi = null;
				}
			}
			return serverApi;
		}
		
		@Override
		protected void onPostExecute(final UserInfoSvcApi serverApi)
		{
			mAuthTask = null;
			showProgress(false);
			
			if (serverApi != null)
			{
				String userName = ServerSvc.getUserName();
				UserHelper.setLogin(LoginActivity.this, userName);
				if ("admin".equals(userName))
				{
					startActivity(new Intent(LoginActivity.this, ContentGenerateActivity.class));
				}
				else
				{
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
				}
				finish();
			}
			else
			{
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}
		
		@Override
		protected void onCancelled()
		{
			mAuthTask = null;
			showProgress(false);
		}
	}
}
