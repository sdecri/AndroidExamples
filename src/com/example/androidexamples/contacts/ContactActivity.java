/**
 * ConcatcActivity.java
 */
package com.example.androidexamples.contacts;

import com.example.androidexamples.R;
import com.example.androidexamples.contacts.ContactFragment.Comunicable;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * @author Simone
 *         06/dic/2014
 */
public class ContactActivity extends Activity implements Comunicable {

    // ATRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact);

        if (getIntent() != null && getIntent().getExtras() != null)
            Toast.makeText(
                    this,
                    getResources().getString(R.string.tryWith) + ": "
                            + getIntent().getExtras().getString("example"), Toast.LENGTH_LONG)
                    .show();

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

        case R.id.menu_settings:
            new AlertDialog.Builder(this).setTitle("Good").setMessage("You clicked on menu item")
                    .setNeutralButton("Ok", null).setIcon(android.R.drawable.ic_dialog_info).show();

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onContactSelected(ContactInfo contact) {

        new AlertDialog.Builder(this).setTitle(contact.getDisplayName())
                .setMessage("Selected: " + contact)
                .setNeutralButton(getResources().getString(R.string.ok), null).show();
    }

}
