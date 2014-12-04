/**
 * ContactFromFragment.java
 */
package com.example.androidexamples.sqlite;

import com.example.androidexamples.R;
import com.example.androidexamples.fragment_example.BodyFragment.OnBodyFragmentListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Simone De Cristofaro
 * 16/gen/2014
 */
public class ContactFoundFragment extends Fragment {		
	
	//COMUNICATION INTERFACE
	/**
	 * Listner that must be implemented by the activity
	 * containing the Fragment.
	 * Interface for communication with activity
	 */
	public interface ContactFoundFragmentListener{
		
		/**
		 * 
		 * @param v
		 */
		public void contactFoundFragmentCallback(View v);
	}
	
	/**
	 * Listner of the activity that contains the Fragment
	 */
	private ContactFoundFragmentListener contactFoundFragment;
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			contactFoundFragment=(ContactFoundFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + ContactFoundFragmentListener.class.getSimpleName());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.sqlite_contact_fragment, container, false);
		
		return view;
	}

	
}
