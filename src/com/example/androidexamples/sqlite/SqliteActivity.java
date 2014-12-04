package com.example.androidexamples.sqlite;

import com.example.androidexamples.R;
import com.example.androidexamples.R.layout;
import com.example.androidexamples.R.menu;
import com.example.androidexamples.sqlite.ContactFormFragment.ContactFormFragmentListener;
import com.example.androidexamples.sqlite.ContactFoundFragment.ContactFoundFragmentListener;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SqliteActivity extends FragmentActivity 
	implements ContactFormFragmentListener, ContactFoundFragmentListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlite);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(getIntent()!=null && getIntent().getExtras()!=null)
			Toast.makeText(this, getResources().getString(R.string.tryWith)+": " + getIntent().getExtras().getString("example"), Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sqlite, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	/* (non-Javadoc)
	 * @see com.example.androidexamples.sqlite.ContactFoundFragment.ContactFoundFragmentListener#contactFoundFragmentCallback(android.view.View)
	 */
	@Override
	public void contactFoundFragmentCallback(View v) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.example.androidexamples.sqlite.ContactFormFragment.ContactFormFragmentListener#contactFormFragmentCallback(android.view.View)
	 */
	@Override
	public void contactFormFragmentCallback(View v) {
		// TODO Auto-generated method stub
		
	}

}
