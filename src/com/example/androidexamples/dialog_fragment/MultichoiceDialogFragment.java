/**
 * ConfirmDialogFragment.java
 */
package com.example.androidexamples.dialog_fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.androidexamples.R;
import com.example.androidexamples.fragment_example.support.Contact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Simone
 * 11/gen/2013
 */
public class MultichoiceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, OnMultiChoiceClickListener {

	//ATTRIBUTES
	private List<Contact> selectedContacts;
	private Contact[] contacts;
	private String choices[];
	private onMultichoiceDialogFragmentListener owner;
	
	//CONSTRUCTORS
	public MultichoiceDialogFragment(Contact[] contacts) {
		super();
		this.contacts=contacts;
		selectedContacts=new ArrayList<Contact>(contacts.length);
	}
		
	
	//METHODS
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);
		choices = new String[this.contacts.length];
		boolean choicesStatus[]=new boolean[this.contacts.length];
		for(int i=0;i<this.contacts.length;i++) {
			choices[i]=this.contacts[i].getFirstName() + " " +  this.contacts[i].getLastName();
		}
					
		builder.setTitle(R.string.multichoiceDialogExample);
		builder.setPositiveButton(R.string.yes, this);
		builder.setNegativeButton(R.string.no, this);
		builder.setNeutralButton(R.string.cancel, this);
		builder.setMultiChoiceItems(choices, choicesStatus, this);
		
		return builder.create();
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onClick(DialogInterface dialog, int which) {
		Contact[] selectedCotact_array=null;
		if(which==dialog.BUTTON_POSITIVE) {
			selectedCotact_array=new Contact[selectedContacts.size()];
			selectedContacts.toArray(selectedCotact_array);
		}
		owner.onChoice(selectedCotact_array);
	}
	
	
	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnMultiChoiceClickListener#onClick(android.content.DialogInterface, int, boolean)
	 */
	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if(selectedContacts.contains(this.contacts[which])) {
			if(!isChecked) selectedContacts.remove(this.choices[which]); 
		}else {
			if(isChecked) selectedContacts.add(this.contacts[which]); 
		}
	}
	
	//INTERFACES
	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			owner=(onMultichoiceDialogFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + onMultichoiceDialogFragmentListener.class.getSimpleName());
		}
	};
	
	public interface onMultichoiceDialogFragmentListener{
		void onChoice(Contact[] contacts);
	}
	
}
