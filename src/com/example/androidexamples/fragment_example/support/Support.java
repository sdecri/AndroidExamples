/**
 * Support.java
 */
package com.example.androidexamples.fragment_example.support;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Button;

/**
 * @author Simone
 * 09/gen/2013
 */
public class Support {
	
	public static void onBodyButtonClick(Button button,Activity callerActivity) {
		new AlertDialog.Builder(callerActivity)
		.setTitle("INFO")
		.setMessage("You click: " + button.getText() + " button!")
	    .setNeutralButton("Ok", null)
	    .setIcon(android.R.drawable.ic_dialog_info)
	    .show();
	}

}
