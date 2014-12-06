/**
 * ContactFragment.java
 */
package com.example.androidexamples.contacts;

import com.example.androidexamples.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author Simone 06/dic/2014
 */
public class ContactFragment extends Fragment implements OnItemClickListener {

    // COMUNICATION INTERFACE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public interface Comunicable {

        void onContactSelected(ContactInfo contactInfo);
    }

    private Comunicable caller;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            caller = (Comunicable) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + Comunicable.class.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View
            onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_fragment_listview);
        listView.setAdapter(new ContactAdapter(getActivity(), ((ContactActivity) getActivity())
                .getContacts()));
        listView.setOnItemClickListener(this);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        caller.onContactSelected((ContactInfo) ((ContactAdapter) parent.getAdapter()).getItem(position));
    }

}
