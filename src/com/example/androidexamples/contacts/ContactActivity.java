/**
 * ConcatcActivity.java
 */
package com.example.androidexamples.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.androidexamples.R;
import com.example.androidexamples.contacts.ContactFragment.Comunicable;
import com.example.utils.Util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * @author Simone
 *         06/dic/2014
 */
public class ContactActivity extends FragmentActivity implements Comunicable {

    // STATIC ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final String[] COLUMNS_CONTACT = new String[] { RawContacts._ID,
            Contacts.LOOKUP_KEY, Contacts.DISPLAY_NAME, Contacts.PHOTO_THUMBNAIL_URI };

    private static final String[] COLUMNS_ACCOUNT = new String[] { RawContacts.ACCOUNT_NAME,
            RawContacts.ACCOUNT_TYPE };

    // ATRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private List<AccountInfo> accounts;
    HashMap<String, AccountInfo> mapAccounts;
    private ContactInfo[] contacts;

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getAccountsInfo();

        setContentView(R.layout.contact_activity);

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

    // GET <<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * @return the contacts
     */
    public ContactInfo[] getContacts() {

        return contacts;
    }

    // SUPPORT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void getAccountsInfo() {

        // get the accounts
        AccountManager accountManager = AccountManager.get(this);
        Account[] acc = accountManager.getAccounts();
        accounts = new ArrayList<AccountInfo>(acc.length);
        mapAccounts = new HashMap<String, AccountInfo>(acc.length);
        for (Account account : acc) {
            // get account icon
            Drawable icon = Util.getIconForAccount(account, accountManager, this);
            AccountInfo accountInfo = new AccountInfo(account.name, account.type, icon);
            accounts.add(accountInfo);
            mapAccounts.put(accountInfo.getName() + ";" + accountInfo.getType(), accountInfo);
        }

        // get the contacts
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = null;
        try {
            cursor =
                    contentResolver.query(Contacts.CONTENT_URI, COLUMNS_CONTACT, null, null,
                            Contacts.DISPLAY_NAME);

            List<ContactInfo> list_contacts = new ArrayList<ContactInfo>(cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
                    String lookUpKey = cursor.getString(cursor.getColumnIndex(Contacts.LOOKUP_KEY));
                    Uri uri = ContactsContract.Contacts.getLookupUri(id, lookUpKey);

                    // get accounts of the contact
                    Cursor cursorAccount = null;
                    try {

                        cursorAccount =
                                contentResolver.query(RawContacts.CONTENT_URI, COLUMNS_ACCOUNT,
                                        RawContacts.CONTACT_ID + "=?", new String[] { "" + id },
                                        null);

                        if (cursorAccount.getCount() > 0) {

                            AccountInfo[] contactAccount =
                                    new AccountInfo[cursorAccount.getCount()];
                            int j = 0;
                            while (cursorAccount.moveToNext()) {
                                String accountName =
                                        cursorAccount.getString(cursorAccount
                                                .getColumnIndex(RawContacts.ACCOUNT_NAME));
                                String accountType =
                                        cursorAccount.getString(cursorAccount
                                                .getColumnIndex(RawContacts.ACCOUNT_TYPE));
                                Log.i("Account Name", accountName);
                                Log.i("Account Type", accountType);
                                AccountInfo accountInfo =
                                        mapAccounts.get(accountName + ";" + accountType);
                                if (accountInfo == null) {
                                    // add new account info not found by AccountManager
                                    accountInfo = new AccountInfo(accountName, accountType, null);
                                }
                                contactAccount[j] = accountInfo;

                                j++;
                            }

                            String displayName =
                                    cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
                            /*
                             * Gets the photo thumbnail column index if platform version >=
                             * Honeycomb
                             */
                            int mThumbnaidColumn;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                mThumbnaidColumn =
                                        cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI);
                                // Otherwise, sets the thumbnail column to the _ID column
                            }
                            else {
                                int idColumnIndex = cursor.getColumnIndex(Contacts._ID);
                                mThumbnaidColumn = idColumnIndex;
                            }
                            Bitmap thumbnail =
                                    Util.loadContactPhotoThumbnail(getContentResolver(),
                                            cursor.getString(mThumbnaidColumn));

                            list_contacts.add(new ContactInfo(id, lookUpKey, uri, contactAccount,
                                    displayName, thumbnail));
                        }
                    }
                    catch (Exception e) {
                        Log.e("", "Error reading account of contact", e);
                    }
                    finally {
                        if (cursorAccount != null)
                            cursorAccount.close();
                    }

                }
            }
            contacts = new ContactInfo[list_contacts.size()];
            list_contacts.toArray(contacts);
        }
        catch (Exception e) {
            contacts = new ContactInfo[0];
            Log.e(getClass().getName(), "Error reading contacts", e);
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
    }

}
