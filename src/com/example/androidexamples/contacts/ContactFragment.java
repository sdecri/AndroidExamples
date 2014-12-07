/**
 * ContactFragment.java
 */
package com.example.androidexamples.contacts;

import com.example.androidexamples.R;
import com.example.androidexamples.fragment_example.MenuFragment.MenuFragmentListener;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

/**
 * @author Simone 06/dic/2014
 */
public class ContactFragment extends ListFragment implements OnItemClickListener,
        OnQueryTextListener, OnCloseListener, LoaderCallbacks<Cursor> {

    // STATIC ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final String[] COLUMNS_CONTACT = new String[] { RawContacts._ID,
            Contacts.LOOKUP_KEY, Contacts.DISPLAY_NAME, Contacts.PHOTO_THUMBNAIL_URI };

    // INNER CLASS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private Resources res;
    private ContactAdapter contactAdapter;
    private SearchView mSearchView;
    private String mCurFilter;

    // COMUNICATION INTERFACE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public interface Comunicable {

        void onContactSelected(ContactInfo contactInfo);
    }

    private Comunicable caller;

    // METHOD <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        // check if the activity attached implements the requested interface
        try {
            caller = (Comunicable) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + MenuFragmentListener.class.getSimpleName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        res = getActivity().getResources();
        // Give some text to display if there is no data. In a real
        // application this would come from a resource.
        setEmptyText(res.getString(R.string.noDataLoaded));

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // Create an empty adapter we will use to display the loaded data.
        contactAdapter =
                new ContactAdapter(getActivity(), null, false, getActivity().getContentResolver());
        setListAdapter(contactAdapter);

        // Start out with a progress indicator.
        setListShown(false);

        getListView().setOnItemClickListener(this);

        // Prepare the loader. Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Place an action bar item for searching.
        MenuItem item = menu.add(res.getString(R.string.search));
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mSearchView = new SearchView(getActivity());
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);
        item.setActionView(mSearchView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // This is called when a new Loader needs to be created. This
        // sample only has one Loader, so we don't care about the ID.

        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(mCurFilter));
        }
        else {
            baseUri = Contacts.CONTENT_URI;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        // String select =
        // "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND (" + Contacts.HAS_PHONE_NUMBER
        // + "=1) AND (" + Contacts.DISPLAY_NAME + " != '' ))";
        // return new CursorLoader(getActivity(), baseUri, CONTACTS_SUMMARY_PROJECTION, select, null,
        // Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        return new CursorLoader(getActivity(), baseUri, COLUMNS_CONTACT, null, null,
                Contacts.DISPLAY_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Swap the new cursor in. (The framework will take care of closing the
        // old cursor once we return.)
        contactAdapter.swapCursor(cursor);

        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        }
        else {
            setListShownNoAnimation(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed. We need to make sure we are no
        // longer using it.
        contactAdapter.swapCursor(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onClose() {

        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {

        // Called when the action bar search text has changed. Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
        // Don't do anything if the filter hasn't actually changed.
        // Prevents restarting the loader when restoring state.
        if (mCurFilter == null && newFilter == null) {
            return true;
        }
        if (mCurFilter != null && mCurFilter.equals(newFilter)) {
            return true;
        }
        mCurFilter = newFilter;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        caller.onContactSelected((ContactInfo) contactAdapter.getContactInfo(id));
    }

}
