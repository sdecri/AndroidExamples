/**
 * ConfirmDialogFragment.java
 */
package com.example.androidexamples.storage_and_theme;

import com.example.androidexamples.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author Simone
 * 11/gen/2013
 */
public class LoadDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	//ATTRIBUTES
	private String[] files;
	private LoadFragmentListener owner;
	private String fileName;
	
	//METHODS
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		files=getActivity().fileList();
		fileName=null;
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);
					
		builder.setTitle(R.string.selectFile);
		
		if (files.length>0) {
			builder.setSingleChoiceItems(files, 0, this);
			fileName=files[0];
			builder.setNeutralButton(R.string.cancel, this);
			builder.setPositiveButton(R.string.load, this);
		}else {
			builder.setMessage(R.string.noFileToLoad);
			builder.setPositiveButton(R.string.ok, this);
		}
			
		
		
		return builder.create();
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which==dialog.BUTTON_POSITIVE) {
			if(files.length>0)
				owner.onLoad(fileName);
		}else if(which== dialog.BUTTON_NEUTRAL)
			owner.onLoad(null);
		else
			fileName=files[which];
		
	}
		
	//INTERFACES
	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			owner=(LoadFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + LoadFragmentListener.class.getSimpleName());
		}
	};
	
	public interface LoadFragmentListener{
		void onLoad(String fileName);
	}
	
}
