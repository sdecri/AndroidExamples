package com.example.androidexamples.dialog_fragment;

import com.example.androidexamples.R;
import com.example.androidexamples.dialog_fragment.MultichoiceDialogFragment.onMultichoiceDialogFragmentListener;
import com.example.androidexamples.fragment_example.support.Contact;

import android.nfc.FormatException;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class DialogChoiceActivity extends FragmentActivity implements
		OnClickListener, onMultichoiceDialogFragmentListener {

	// ATTRIBUTES
	private Button button_multichoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_choice);

		if (getIntent() != null && getIntent().getExtras() != null)
			Toast.makeText(
					this,
					getResources().getString(R.string.tryWith) + ": "
							+ getIntent().getExtras().getString("example"),
					Toast.LENGTH_LONG).show();

		button_multichoice = (Button) findViewById(R.id.button_multichoiceDialog);
		button_multichoice.setOnClickListener(this);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dialog_choice, menu);
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

		case R.id.menu_settings:
			new AlertDialog.Builder(this).setTitle("Good")
					.setMessage("You clicked on menu item")
					.setNeutralButton("Ok", null)
					.setIcon(android.R.drawable.ic_dialog_info).show();

		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == button_multichoice) {
			FragmentManager fm = getSupportFragmentManager();
			MultichoiceDialogFragment multichoiceDialogFragment = null;
			try {
				multichoiceDialogFragment = new MultichoiceDialogFragment(
						new Contact[] {
								new Contact("Simone", "De Cristofaro",
										3929428039L,
										"simone.decristofaro85@gmail.com"),
								new Contact("Chiara", "Fiaschetti",
										3334047755L, "chiara.mcf86@gmail.com"),
								new Contact("Marco", "De Cristofaro",
										3292788296L,
										"marco.decristofaro@gmail.com"),
								new Contact("Giulia", "Fiaschetti",
										3491665809L,
										"giulia.fiaschetti@gmail.com"), });
			} catch (FormatException e) {
				e.printStackTrace();
			}
			multichoiceDialogFragment.show(fm, "fragment_edit_name");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.androidexamples.dialog_fragment.MultichoiceDialogFragment
	 * .onMultichoiceDialogFragmentListener
	 * #onChoice(com.example.androidexamples.fragment_example.support.Contact[])
	 */
	@Override
	public void onChoice(Contact[] contacts) {
		if (contacts == null || contacts.length == 0) {
			Toast.makeText(this, R.string.noItemSelected, Toast.LENGTH_LONG)
					.show();
		} else {
			StringBuilder sb = new StringBuilder(getResources().getString(
					R.string.youSelected)
					+ ": ");
			for (int i = 0; i < contacts.length; i++) {
				sb.append(contacts[i].getFirstName()).append(" ")
						.append(contacts[i].getLastName()).append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
			Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
		}

	}

}
