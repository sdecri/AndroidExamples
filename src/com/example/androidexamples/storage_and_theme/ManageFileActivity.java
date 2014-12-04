package com.example.androidexamples.storage_and_theme;

import com.example.androidexamples.R;
import com.example.androidexamples.R.layout;
import com.example.androidexamples.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;

public class ManageFileActivity extends Activity implements OnItemClickListener,OnClickListener{

	//ATTRIBUTES
	private ListView listView_files;
//	private HashMap<String,Integer> coll_selectedFiles;
	private CheckedTextView checkBox_selectAll;
	private ManageFileAdapter myManageFileAdapter;
	private String[] filesToDelete;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storage_and_theme_example_manage_files);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String[] tmp_files=fileList();
		MyCheckedFile[] files = new MyCheckedFile[tmp_files.length];
		for(int i=0;i<tmp_files.length;i++)
			files[i]=new MyCheckedFile(tmp_files[i]);
		
		listView_files=(ListView) findViewById(R.id.ListView_storage_and_theme);
		myManageFileAdapter=new ManageFileAdapter(this, files);
		listView_files.setAdapter(myManageFileAdapter);
		listView_files.setOnItemClickListener(this);
	
		
		checkBox_selectAll=(CheckedTextView) findViewById(R.id.storage_and_theme_manaFiles_checkbox_selectAll);
		checkBox_selectAll.setOnClickListener(this);
//		coll_selectedFiles=new HashMap<String, Integer>();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_manage_file, menu);
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
			
		case R.id.manageFile_actionItem_delete:
			filesToDelete=myManageFileAdapter.getCheckedFiles();
			if (filesToDelete==null || filesToDelete.length==0) {
				Toast.makeText(this
						, getString(R.string.noItemSelected)
						, Toast.LENGTH_LONG)
				.show();
			}else {
				StringBuilder sb=new StringBuilder();
				for(int i=0;i<filesToDelete.length;i++)
					sb.append(filesToDelete[i]).append(", ");
				sb.delete(sb.length()-2, sb.length());
				if (filesToDelete.length>=2) 
					sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",")+1, " "+getResources().getString(R.string.and) +" ");
				AlertDialog.Builder builder=new Builder(this);
				builder.setCancelable(true);
				builder.setMessage(getResources().getString(R.string.confirmDeleteFile) + ": " + sb.toString()  + " ?");
				builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						myManageFileAdapter.remove(filesToDelete);
						for(int i=0;i<filesToDelete.length;i++)
							deleteFile(filesToDelete[i]);
						myManageFileAdapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.show();				
			}
			
		}
		return super.onOptionsItemSelected(item);
	}

	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		((ManageFileAdapter) adapterView.getAdapter()).notifyFileSelected(position);		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.storage_and_theme_manaFiles_checkbox_selectAll) {
			checkBox_selectAll.setChecked(!checkBox_selectAll.isChecked());
			checkBox_selectAll.setCheckMarkDrawable(checkBox_selectAll.isChecked()? android.R.drawable.checkbox_on_background:android.R.drawable.checkbox_off_background);
			myManageFileAdapter.setCheckAll(checkBox_selectAll.isChecked());

		}
		
	}

}
