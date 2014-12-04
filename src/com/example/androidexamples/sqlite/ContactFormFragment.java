/**
 * ContactFromFragment.java
 */
package com.example.androidexamples.sqlite;

import com.example.androidexamples.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * @author Simone De Cristofaro
 * 16/gen/2014
 */
public class ContactFormFragment extends Fragment {

	//GUI
	private EditText editText_firstName;
	private EditText editText_lastName;
	private EditText editText_phone;
	private EditText editText_email;
		
	
	//COMUNICATION INTERFACE
	/**
	 * Listner that must be implemented by the activity
	 * containing the Fragment.
	 * Interface for communication with activity
	 */
	public interface ContactFormFragmentListener{
		
		/**
		 * 
		 * @param v
		 */
		public void contactFormFragmentCallback(View v);
	}
	
	/**
	 * Listner of the activity that contains the Fragment
	 */
	private ContactFormFragmentListener contactFormFragmentListener;
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			contactFormFragmentListener=(ContactFormFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + ContactFormFragmentListener.class.getSimpleName());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.sqlite_form_fragment, container, false);
		
		editText_firstName=(EditText)view.findViewById(R.id.editText_firstName);
		editText_lastName=(EditText)view.findViewById(R.id.editText_lastName);
		editText_phone=(EditText)view.findViewById(R.id.editText_phone);
		editText_email=(EditText)view.findViewById(R.id.editText_email);

		return view;
	}

	
}
