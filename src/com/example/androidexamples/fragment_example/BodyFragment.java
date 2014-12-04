/**
 * BodyFragment.java
 */
package com.example.androidexamples.fragment_example;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidexamples.*;

/**
 * @author Simone
 * 07/gen/2013
 */
public class BodyFragment extends Fragment implements OnClickListener{
	
	//GUI
	private EditText editText_firstName;
	private EditText editText_lastName;
	private EditText editText_phone;
	private EditText editText_email;
	private Button button_clear;
	private Button button_call;
	
	//COMUNICATION INTERFACE
	/**
	 * Listner that must be implemented by the activity
	 * containing the Fragment.
	 * Interface for communication with activity
	 */
	public interface OnBodyFragmentListener{
		
		/**
		 * Method used to pass to the owner activity
		 * the selected contact
		 * @param c The <code>Contact</code> selected
		 */
		public void OnButtonClick(Button b);
	}
	
	/**
	 * Listner of the activity that contains the Fragment
	 */
	private OnBodyFragmentListener onBodyFragmentListener;
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			onBodyFragmentListener=(OnBodyFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + OnBodyFragmentListener.class.getSimpleName());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.fragment_example_fr_body, container, false);
		
		editText_firstName=(EditText)view.findViewById(R.id.editText_firstName);
		editText_lastName=(EditText)view.findViewById(R.id.editText_lastName);
		editText_phone=(EditText)view.findViewById(R.id.editText_phone);
		editText_email=(EditText)view.findViewById(R.id.editText_email);
		button_clear=(Button)view.findViewById(R.id.button_clear);
		button_clear.setOnClickListener(this);
		button_call=(Button)view.findViewById(R.id.button_call);
		button_call.setOnClickListener(this);

		return view;
	}

	

	/**
	 * @param contact the contact to set
	 */
	public void setContact(
			com.example.androidexamples.fragment_example.support.Contact contact) {
		
		editText_firstName.setText(contact.getFirstName());
		editText_lastName.setText(contact.getLastName());
		editText_phone.setText(""+contact.getPhone());
		editText_email.setText(contact.getEmail());
		
	}

	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		onBodyFragmentListener.OnButtonClick((Button)v);//onBodyFragmentListener --> is a mask for the owner activity
	}
	
	
	
	
}
