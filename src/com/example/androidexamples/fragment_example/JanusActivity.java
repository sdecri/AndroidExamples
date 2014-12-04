package com.example.androidexamples.fragment_example;

import com.example.androidexamples.R;

import com.example.androidexamples.fragment_example.support.Contact;
import com.example.androidexamples.fragment_example.support.Support;
import com.example.androidexamples.fragment_example.BodyFragment.OnBodyFragmentListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class JanusActivity extends FragmentActivity implements MenuFragment.MenuFragmentListener, OnBodyFragmentListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//The layout is choosed according to the current orentation (landscape or portrait)
		setContentView(R.layout.fragment_example_activity_janus);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(getIntent()!=null && getIntent().getExtras()!=null)
			Toast.makeText(this, getResources().getString(R.string.tryWith)+": " + getIntent().getExtras().getString("example"), Toast.LENGTH_LONG).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_janus, menu);
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
	 * @see com.example.androidexamples.fragment_example.MenuFragment.OnMenuFragListener#onMenuFragListener(com.example.androidexamples.fragment_example.structures.Contact)
	 */
	@Override
	public void onSelectContact(Contact c) {
		
		//get body fragment (native method is getFragmentManager()
		BodyFragment bodyFragment=(BodyFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_body);
		
		// if bodyFragment is not null and in layout, set text, else launch BodyActivity
		if(bodyFragment!=null && bodyFragment.isInLayout()) {
			bodyFragment.setContact(c);
		}else {
			Intent intent_bodyActivity=new Intent(this,BodyActivity.class);
			intent_bodyActivity.putExtra("Contact", c);
			startActivity(intent_bodyActivity);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.example.androidexamples.fragment_example.BodyFragment.OnBodyFragmentListener#OnButtonClick(android.widget.Button)
	 */
	@Override
	public void OnButtonClick(Button b) {
		Support.onBodyButtonClick(b, this);
	}

	
	
	
}
