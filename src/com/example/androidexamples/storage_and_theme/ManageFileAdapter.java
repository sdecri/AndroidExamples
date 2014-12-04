/**
 * ManageFileAdapter.java
 */
package com.example.androidexamples.storage_and_theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.example.androidexamples.R;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

/**
 * @author Simone
 * 13/feb/2013
 */
public class ManageFileAdapter extends BaseAdapter{

	//ATTRIBUTES
	private boolean checkAll;
	private List<MyCheckedFile> list_files;
	private HashMap<String, Integer> hash_myCheckedFile;
	private LayoutInflater mInflater;
	
	//CONSTRUCTORS
	public ManageFileAdapter(Context context, MyCheckedFile[] files) {
		super();
		this.checkAll=false;
		this.list_files = new ArrayList<MyCheckedFile>(files.length);
		hash_myCheckedFile=new HashMap<String, Integer>(files.length);
		for (int i=0;i<files.length;i++) {
			this.list_files.add(files[i]);
			hash_myCheckedFile.put(files[i].getName(), i);
		}
		
		mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * @return the checkAll
	 */
	public boolean isCheckAll() {
		return checkAll;
	}

	/**
	 * @param checkAll the checkAll to set
	 */
	public void setCheckAll(boolean status) {
		this.checkAll = status;
		Iterator<MyCheckedFile> it=list_files.iterator();
		while (it.hasNext()) {
			MyCheckedFile myCheckedFile = (MyCheckedFile) it.next();
			myCheckedFile.setChecked(checkAll);
		}
		notifyDataSetChanged();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.list_files.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int index) {
		return this.list_files.get(index);
	}

	/**
	 * Removes all the elements
	 */
	public void clear() {
		list_files.clear();
		hash_myCheckedFile.clear();
	}
	
	/**
	 * Return all the <code>MyCheckedFile</code> contained in the adapter
	 * @return
	 */
	public MyCheckedFile[] getItems() {
		MyCheckedFile[] myCheckedFiles=new MyCheckedFile[list_files.size()];
		list_files.toArray(myCheckedFiles);
		return myCheckedFiles;
	}
	
	public MyCheckedFile remove(String fileName) {
		MyCheckedFile myCheckedFile=list_files.get(hash_myCheckedFile.get(fileName));
		list_files.remove(hash_myCheckedFile.get(fileName).intValue());
		hash_myCheckedFile.clear();
		for(int i=0;i<list_files.size();i++)
			hash_myCheckedFile.put(list_files.get(i).getName(), i);
		
		return myCheckedFile;
	}
		
	public void remove(String[] filesToDelete) {
		for(int i=0;i<filesToDelete.length;i++)
			remove(filesToDelete[i]);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return this.list_files.get(arg0).hashCode();
	}
	
	public void notifyFileSelected(int position) {
		String fileName=list_files.get(position).getName();
		list_files.get(hash_myCheckedFile.get(fileName)).setChecked(! (list_files.get(hash_myCheckedFile.get(fileName)).isChecked()) );
		notifyDataSetChanged();
	}
	
	public String[] getCheckedFiles() {
		if (list_files==null || list_files.size()==0) {
			return null;
		}
		Iterator<MyCheckedFile> it=list_files.iterator();
		List<String> tmp_files=new ArrayList<String>();
		while (it.hasNext()) {
			MyCheckedFile myCheckedFile = (MyCheckedFile) it.next();
			if(myCheckedFile.isChecked())
				tmp_files.add(myCheckedFile.getName());
		}
		String[] checkedFiles=new String[tmp_files.size()];
		tmp_files.toArray(checkedFiles);
		return checkedFiles;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewInAdapter viewInAdapter=null;
		boolean fileIsChecked=false;
		if(convertView==null) {
			convertView=(LinearLayout) mInflater.inflate(R.layout.storage_and_theme_fileitem, null);			
			viewInAdapter = new ViewInAdapter();
	        viewInAdapter.checkBox_file =(CheckedTextView) convertView.findViewById(R.id.storage_and_theme_fileItem_listitem_checkBox);;
	        convertView.setTag(viewInAdapter);
		}else {
			viewInAdapter=(ViewInAdapter) convertView.getTag();
		}
				
		viewInAdapter.checkBox_file.setText(list_files.get(position).getName());
		fileIsChecked=list_files.get( hash_myCheckedFile.get(list_files.get(position).getName()) ).isChecked();
		viewInAdapter.checkBox_file.setChecked(fileIsChecked);
		viewInAdapter.checkBox_file.setCheckMarkDrawable(fileIsChecked?android.R.drawable.checkbox_on_background:android.R.drawable.checkbox_off_background);
		
		return convertView;
		
		/*
		  La classe View ci consente - tramite il metodo setTag() - di associare un oggetto alla propria istanza di classe. 
		  Nel nostro caso abbiamo creato la classe ViewHolder che utilizzeremo per mantenere il riferimento al button
		  del contatto. Cosi facendo possiamo mantenere il riferimento solo alla prima invocazione 
		  del metodo getView(), in questo modo si evita di ripetere l'operazione per ogni elemento della lista.
		  Nel caricamento della lista, tramite questo adapter possono verificarsi due stati di caricamento. 
		  Il primo quando il convertView è uguale a null, quindi ci troviamo al primo elemento della lista. 
		  In questo caso eseguiamo l'inflating e assegniamo gli elementi al viewHolder creandone un'istanza 
		  e settandolo nella View attraverso il setTag(). Cosi facendo il viewHolder viene reso disponibile 
		  nei successivi caricamenti. Nel secondo caso, quando il convertView è diverso da null, 
		  procediamo solo nell'ottenere il riferimento al ViewHolder
		 */
	}



	/**
	 * Class to store the view contained in the adapter
	 * @author Simone
	 * 06/feb/2013
	 */
	private class ViewInAdapter{
		public CheckedTextView checkBox_file;
	}

}