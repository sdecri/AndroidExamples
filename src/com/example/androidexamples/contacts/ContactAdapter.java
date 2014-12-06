/**
 * ConcactAdapter.java
 */
package com.example.androidexamples.contacts;

import com.example.androidexamples.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

/**
 * @author Simone 06/dic/2014
 */
public class ContactAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ContactInfo[] contacts;

    public ContactAdapter(Context context, ContactInfo[] contacts) {

        this.contacts = contacts;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {

        return contacts.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {

        return contacts[position];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {

        return contacts[position].getId();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewInAdapter viewInAdapter = null;
        // move the cursor to the correct position
        getItem(position);
        if (convertView == null) {
            convertView = (LinearLayout) layoutInflater.inflate(R.layout.contact, parent, false);
            viewInAdapter = new ViewInAdapter();
            viewInAdapter.badge =
                    (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge_contact);
            viewInAdapter.textView_name =
                    (TextView) convertView.findViewById(R.id.textView_contact);
            convertView.setTag(viewInAdapter);
        }
        else {
            viewInAdapter = (ViewInAdapter) convertView.getTag();
        }

        ContactInfo contact = contacts[position];
        viewInAdapter.badge.assignContactUri(contact.getUri());
        Bitmap bitmap = contact.getThumbnail();
        viewInAdapter.badge.setImageBitmap(bitmap);
        viewInAdapter.textView_name.setText(contact.getDisplayName());

        return convertView;
    }

    private class ViewInAdapter {

        QuickContactBadge badge;
        TextView textView_name;
    }

}
