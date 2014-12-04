/**
 * SaveAsFragment.java
 */
package com.example.androidexamples.storage_and_theme;

import java.util.zip.Inflater;

import com.example.androidexamples.R;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Simone
 * 09/feb/2013
 */
public class SaveAsFragment extends DialogFragment implements OnClickListener {

	//ATTRIBUTES
	private String fileName;
	private String[] existingFiles;
	private SaveAsFragmentListener owner;
	private EditText editText_fileName;
	
	//METHODS
	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setCancelable(true);
			
		LayoutInflater layoutInflater=(LayoutInflater) getActivity().getLayoutInflater();
		View dialogContent=layoutInflater.inflate(R.layout.storage_and_theme_save_as_dialog, null);
		
		editText_fileName=(EditText) getActivity().findViewById(R.id.editText_dialog_save_as);
		
		builder.setTitle(R.string.saveAsFileDialogMessage);
		builder.setPositiveButton(R.string.save, this);
		builder.setNeutralButton(R.string.cancel, this);
		builder.setView(dialogContent);
		
		existingFiles=getActivity().fileList();
		fileName=null;

		return builder.create();
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
	 */
	@SuppressWarnings("static-access")
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which==dialog.BUTTON_POSITIVE) {
			fileName=editText_fileName.getText().toString();
			if(!fileName.endsWith(".txt")) fileName+=".txt";
			
			//check if files already exist
			if(indexOf(existingFiles, fileName)>=0) {//file alredy exist --> ask for the overwriting
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setCancelable(true);
				builder.setTitle(R.string.warning);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage(getString(R.string.FileOverwritingConfirmation));
				builder.setPositiveButton(R.string.yes, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						owner.onSave(fileName);
					}
				});
				builder.setNegativeButton(R.string.no, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						//Toast.makeText(getActivity(), R.string.SetDifferentFileName, Toast.LENGTH_LONG).show();
					}
				});
				// create alert dialog
				AlertDialog alertDialog = builder.create();
 
				// show it
				alertDialog.show();
				
			}else//file doesn't exist
				owner.onSave(fileName);

		}else//no file to save
			owner.onSave(null);
	}
	

	
	//INTERFACES
	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			owner=(SaveAsFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + SaveAsFragmentListener.class.getSimpleName());
		}
	};
	
	public interface SaveAsFragmentListener{
		void onSave(String fileName);
	}
	
	private int indexOf(Object v[], Object o) {
		if(v==null || o==null) return -1;
		for(int i=0;i<v.length;i++) {
			if(v[i]!=null && v[i].equals(o)) return i;
		}
		return -1;
	}
}
