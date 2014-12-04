package com.example.androidexamples.storage_and_theme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.example.androidexamples.R;
import com.example.androidexamples.dialog_fragment.DialogChoiceActivity;
import com.example.androidexamples.storage_and_theme.LoadDialogFragment.LoadFragmentListener;
import com.example.androidexamples.storage_and_theme.SaveAsFragment.SaveAsFragmentListener;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;

public class StorageAndThemeActivity 
	extends FragmentActivity 
	implements OnClickListener, SaveAsFragmentListener, LoadFragmentListener
{

	//ATTRIBUTES
	private Button button_save;
	private Button button_saveAs;
	private Button button_load;
	private Button button_manage;
	private String currentFileName;
	private TextView textView_currentFileLoadedName;
	private FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storage_and_theme);
		
		if(getIntent()!=null && getIntent().getExtras()!=null)
			Toast.makeText(this, getResources().getString(R.string.tryWith)+": " + getIntent().getExtras().getString("example"), Toast.LENGTH_LONG).show();

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		currentFileName=null;
		button_save=(Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
		button_load=(Button) findViewById(R.id.button_load);
		button_load.setOnClickListener(this);
		button_saveAs=(Button) findViewById(R.id.button_saveAs);
		button_saveAs.setOnClickListener(this);
		button_manage=(Button) findViewById(R.id.button_ManageFile);
		button_manage.setOnClickListener(this);
		
		textView_currentFileLoadedName=(TextView) findViewById(R.id.textView_currentFileName);
		textView_currentFileLoadedName.setText(R.string.noFileLoaded);
		
		fm = getSupportFragmentManager();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_storage_and_theme, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_save:
				
			if(currentFileName==null)//No file open --> open the dialog to set the name of the file
				saveAsAction();
			else
				save(currentFileName);
			break;
		case R.id.button_saveAs:
			saveAsAction();
			break;
			
		case R.id.button_load:
			LoadDialogFragment loadFragment=new LoadDialogFragment();
			loadFragment.show(fm, "load_fragment");
			break;
			
		case R.id.button_ManageFile:
			Intent intent=new Intent(this,ManageFileActivity.class);
			startActivity(intent);
			
		default:
			break;
		}
		
	}

	private boolean save(String filename) {
		boolean saved=true;
		EditText textArea = (EditText) findViewById(R.id.TextArea_fileContent);
		String text = textArea.getText().toString();
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(openFileOutput(filename,MODE_PRIVATE));
			writer.write(text);
			Toast.makeText(this, getResources().getString(R.string.textSaved), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			saved=false;
			Log.e("FileDemo", getResources().getString(R.string.errorSaveingText), e);
			Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Throwable t) {
				}
			}
		}
		return saved;
	}

	private boolean load(String fileName) {
		boolean loaded=false;
		String text;
		Reader reader = null;
		try {
			reader = new InputStreamReader(openFileInput(fileName));
			StringBuffer aux = new StringBuffer();
			char[] buf = new char[1024];
			int len;
			while ((len = reader.read(buf)) != -1) {
				aux.append(buf, 0, len);
			}
			text = aux.toString();
			Toast.makeText(this, getResources().getString(R.string.fileLoaded), Toast.LENGTH_LONG).show();
			loaded=true;
		} catch (FileNotFoundException e) {
			text = "";
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Log.e("FileDemo", getResources().getString(R.string.errorLoadingFile), e);
			text = "";
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Throwable t) {
				}
			}
		}
		EditText textArea = (EditText) findViewById(R.id.TextArea_fileContent);
		textArea.setText(text);
		return loaded;
	}

	
	private void saveAsAction() {
		SaveAsFragment saveAsFragment=new SaveAsFragment();
		saveAsFragment.show(fm, "save_as_fragment");
	}
	
	/* (non-Javadoc)
	 * @see com.example.androidexamples.storage_and_theme.SaveAsFragment.onSaveAsDialogInterface#onSave(java.lang.String)
	 */
	@Override
	public void onSave(String fileName) {
		if (fileName!=null) {
			save(fileName);
			currentFileName=fileName;
			textView_currentFileLoadedName.setText(currentFileName);
		}
	}

	/* (non-Javadoc)
	 * @see com.example.androidexamples.storage_and_theme.LoadDialogFragment.onLoadFragmentListener#onLoad(java.lang.String)
	 */
	@Override
	public void onLoad(String fileName) {
		if (fileName!=null) {
			load(fileName);
			currentFileName=fileName;
			textView_currentFileLoadedName.setText(currentFileName);
		}
	}

	
}
