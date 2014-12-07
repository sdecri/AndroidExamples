/**
 * ConcactAdapter.java
 */
package com.example.androidexamples.contacts;

import java.util.HashMap;

import com.example.androidexamples.R;
import com.example.utils.Util;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

/**
 * @author Simone 06/dic/2014
 */
public class ContactAdapter extends CursorAdapter {

    // STATIC ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final String[] COLUMNS_ACCOUNT = new String[] { RawContacts.ACCOUNT_NAME,
            RawContacts.ACCOUNT_TYPE };

    // INNER CLASS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private class ViewHolder {

        QuickContactBadge badge;
        TextView textView_name;
    }

    // ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private LayoutInflater layoutInflater;
    /** Used to retrieve information about {@link Account} */
    private ContentResolver contentResolver;
    private HashMap<String, AccountInfo> mapAccounts;
    private LongSparseArray<ContactInfo> mapContacts;

    // CONSTRUCTORS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * @param context
     * @param c
     * @param autoRequery
     */
    public ContactAdapter(Context context, Cursor c, boolean autoRequery,
            ContentResolver contentResolver) {

        super(context, c, autoRequery);
        layoutInflater = LayoutInflater.from(context);
        this.contentResolver = contentResolver;
        mapAccounts = new HashMap<String, AccountInfo>();
        mapContacts = new LongSparseArray<ContactInfo>();
    }

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * {@inheritDoc}
     */
    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {

        View view = layoutInflater.inflate(R.layout.contact, arg2, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.badge = (QuickContactBadge) view.findViewById(R.id.quickContactBadge_contact);
        viewHolder.textView_name = (TextView) view.findViewById(R.id.textView_contact);
        view.setTag(viewHolder);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindView(View arg0, Context arg1, Cursor arg2) {

        long id = arg2.getLong(arg2.getColumnIndex(Contacts._ID));

        
        ContactInfo contact = mapContacts.get(id);
        if (contact == null){
            contact = createContactInfo(arg2);
            mapContacts.put(contact.getId(), contact);
        }
        ViewHolder viewHolder = (ViewHolder) arg0.getTag();
        viewHolder.badge.assignContactUri(contact.getUri());
        Bitmap bitmap = contact.getThumbnail();
        viewHolder.badge.setImageBitmap(bitmap);
        viewHolder.textView_name.setText(contact.getDisplayName());

    }

    private ContactInfo createContactInfo(Cursor cursor) {

        ContactInfo toReturn = null;
        long id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
        String lookUpKey = cursor.getString(cursor.getColumnIndex(Contacts.LOOKUP_KEY));
        Uri uri = ContactsContract.Contacts.getLookupUri(id, lookUpKey);

        // get accounts of the contact
        Cursor cursorAccount = null;
        try {

            cursorAccount =
                    contentResolver.query(RawContacts.CONTENT_URI, COLUMNS_ACCOUNT,
                            RawContacts.CONTACT_ID + "=?", new String[] { "" + id }, null);

            if (cursorAccount.getCount() > 0) {

                AccountInfo[] contactAccount = new AccountInfo[cursorAccount.getCount()];
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
                    AccountInfo accountInfo = mapAccounts.get(accountName + ";" + accountType);
                    if (accountInfo == null) {
                        // add new account info not found by AccountManager
                        accountInfo = new AccountInfo(accountName, accountType, null);
                    }
                    contactAccount[j] = accountInfo;

                    j++;
                }

                String displayName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
                /*
                 * Gets the photo thumbnail column index if platform version >=
                 * Honeycomb
                 */
                int mThumbnaidColumn;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mThumbnaidColumn = cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI);
                    // Otherwise, sets the thumbnail column to the _ID column
                }
                else {
                    int idColumnIndex = cursor.getColumnIndex(Contacts._ID);
                    mThumbnaidColumn = idColumnIndex;
                }
                Bitmap thumbnail =
                        Util.loadContactPhotoThumbnail(contentResolver,
                                cursor.getString(mThumbnaidColumn));

                toReturn =
                        new ContactInfo(id, lookUpKey, uri, contactAccount, displayName, thumbnail);
            }
        }
        catch (Exception e) {
            Log.e("", "Error reading account of contact", e);
        }
        finally {
            if (cursorAccount != null)
                cursorAccount.close();
        }
        return toReturn;
    }

    public ContactInfo getContactInfo(long id){
        return mapContacts.get(id);
    }

}
