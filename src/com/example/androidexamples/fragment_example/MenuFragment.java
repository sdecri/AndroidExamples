/**
 * MenuFragment.java
 */
package com.example.androidexamples.fragment_example;

import com.example.androidexamples.fragment_example.adapters.ContactAdapter;
import com.example.androidexamples.fragment_example.support.Contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.nfc.FormatException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.androidexamples.*;

/**
 * @author Simone
 * 06/gen/2013
 */
public class MenuFragment extends Fragment implements OnItemClickListener{

	//ATTRIBUTES
	//private Button[] buttons_contacts;
	private Contact[] contacts;
	//private LinearLayout linearLayout_contatcList;
	
	//COMUNICATION INTERFACE
	/**
	 * Listner that must be implemented by the activity
	 * containing the Fragment.
	 * Interface for communication with activity
	 */
	public interface MenuFragmentListener{
		
		/**
		 * Method used to pass to the owner activity
		 * the selected contact
		 * @param c The <code>Contact</code> selected
		 */
		public void onSelectContact(Contact c);
	}
	
	/**
	 * Listner of the activity that contains the Fragment
	 */
	private MenuFragmentListener menuFragmentListener;
	
	//METHODS
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		//check if the activity attached implements the requested interface
		try {
			menuFragmentListener=(MenuFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + MenuFragmentListener.class.getSimpleName());
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		//assign the xml layout to the fragment
		View view = inflater.inflate(R.layout.fragment_example_fr_menu,container,false);

//		//get the linear layoout that represent the container of the list of contacts
//		linearLayout_contatcList=(LinearLayout)view.findViewById(R.id.LinearLayout_frag_menu);
		
		
		try {
			//contacts=Contact.getRandomContact((int) ((10-1)*Math.random()) + 1);
			contacts=Contact.getRandomContact(15);
		} catch (FormatException e) {
			new AlertDialog.Builder(getActivity()).setTitle("ERROR").setMessage("An error occured creating contacts:\n" + e.getMessage())
		    .setNeutralButton("Ok", null).setIcon(android.R.drawable.ic_dialog_alert).show();
			e.printStackTrace();
		}
				
		ListView listView=(ListView) view.findViewById(R.id.ListView_fragment_menu);
		listView.setAdapter(new ContactAdapter(view.getContext(), contacts));
		listView.setOnItemClickListener(this);
		
//		buttons_contacts=new Button[contacts.length];
//		
//		for(int i=0;i<buttons_contacts.length;i++) {
//			buttons_contacts[i]=new Button(getActivity());
//			buttons_contacts[i].setId(i);
//			buttons_contacts[i].setText(contacts[i].getFirstName()+" "+contacts[i].getLastName());
//			buttons_contacts[i].setClickable(true);
//			buttons_contacts[i].setOnClickListener(this);
//			linearLayout_contatcList.addView(buttons_contacts[i]);
//		}
		
		return view;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		//pass to the owner activity the contatc selected
		//using the "onMenuFragListener"
		
		menuFragmentListener.onSelectContact( ( (Contact)  ((ContactAdapter) (adapterView.getAdapter())).getItem(position))  );//onMenufragListener --> is a mask for the owner activity
	}


	
}
