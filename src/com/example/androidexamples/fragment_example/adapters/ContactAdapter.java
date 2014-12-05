/**
 * ButtonAdapter.java
 */
package com.example.androidexamples.fragment_example.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.androidexamples.R;
import com.example.androidexamples.fragment_example.support.Contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;

/**
 * @author Simone
 * 27/gen/2013
 */
public class ContactAdapter extends BaseAdapter {

	//ATTRIBUTES
	private List<Contact> list_contacts;
	private LayoutInflater layoutInflater;
	private Resources res;
	
	//CONSTRUCTORS
	public ContactAdapter(Context context, Contact[] contacts) {
		super();
		this.list_contacts = new ArrayList<Contact>(contacts.length);
		for (int i=0;i<contacts.length;i++)
			this.list_contacts.add(contacts[i]);
		layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		res=context.getResources();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.list_contacts.size();
	}



	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return this.list_contacts.get(index);
	}

	/**
	 * Removes all the elements
	 */
	public void clear() {
		this.list_contacts.clear();
	}
	
	/**
	 * Remove the elements at the specified position and return it
	 * @param position
	 * @return
	 */
	public Contact remove(int position) {
		return list_contacts.remove(position);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return this.list_contacts.get(arg0).hashCode();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewInAdapter viewInAdapter=null;
		if(convertView==null) {
			convertView=(LinearLayout) layoutInflater.inflate(R.layout.fragment_example_listitem, null);			
			viewInAdapter = new ViewInAdapter();
	        viewInAdapter.button_contact =(Button) convertView.findViewById(R.id.listItem_button);
	        viewInAdapter.imageView=(ImageView) convertView.findViewById(R.id.listitem_image);
	        convertView.setTag(viewInAdapter);
		}else {
			viewInAdapter=(ViewInAdapter) convertView.getTag();
		}
		viewInAdapter.button_contact.setText(list_contacts.get(position).getFirstName() + " " + list_contacts.get(position).getLastName());
		viewInAdapter.imageView.setImageDrawable(res.getDrawable(R.drawable.squal));
		return convertView;
		
		/*
		  La classe View ci consente - tramite il metodo setTag() - di associare un oggetto alla propria istanza di classe. 
		  Nel nostro caso abbiamo creato la classe ViewHolder che utilizzeremo per mantenere il riferimento al button
		  del contatto. Cosi facendo possiamo mantenere il riferimento solo alla prima invocazione 
		  del metodo getView(), in questo modo si evita di ripetere l'operazione per ogni elemento della lista.
		  Nel caricamento della lista, tramite questo adapter possono verificarsi due stati di caricamento. 
		  Il primo quando il convertView è uguale a null, quindi ci troviamo al primo elemento della lista. 
		  In questo caso eseguiamo l'inflating per associare l'xml di layout all'oggetto View da ritornare
		  e assegniamo gli elementi al viewHolder creandone un'istanza e memorizzandolo nella View attraverso il setTag().
		  Cosi facendo il viewHolder viene reso disponibile nei successivi caricamenti in modo da dover essere solo aggiornato. 
		  Nel secondo caso, quando il convertView è diverso da null, procediamo solo nell'ottenere il riferimento al ViewHolder
		 */
	}

	/**
	 * Class to store the view contained in the adapter
	 * @author Simone
	 * 06/feb/2013
	 */
	private class ViewInAdapter{
		public Button button_contact;
		public ImageView imageView;
	}
}
