package com.example.androidexamples;

import com.example.androidexamples.dialog_fragment.DialogChoiceActivity;
import com.example.androidexamples.fragment_example.JanusActivity;
import com.example.androidexamples.sqlite.SqliteActivity;
import com.example.androidexamples.storage_and_theme.StorageAndThemeActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Resources res;
	
	//GUI
	private Button button_dialog;
	private Button button_fragment;
	private Button button_storageAndTheme;
	private Button button_sqlite;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		res=getResources();
		button_dialog=(Button) findViewById(R.id.button_dialog);
		button_dialog.setOnClickListener(this);
		button_fragment=(Button) findViewById(R.id.button_fragment);
		button_fragment.setOnClickListener(this);
		button_storageAndTheme=(Button) findViewById(R.id.button_storageAndTheme);
		button_storageAndTheme.setOnClickListener(this);
		button_sqlite=(Button) findViewById(R.id.button_sqlite);
		button_sqlite.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if(v==button_dialog) {
			Intent intent=new Intent(this,DialogChoiceActivity.class);
			intent.putExtra("example", res.getString(R.string.dialog));
			startActivity(intent);
//			new AlertDialog.Builder(this).setTitle("ERROR").setMessage("Not Yet Implemented")
//		    .setNeutralButton("Ok", null).setIcon(android.R.drawable.ic_dialog_alert).show();
		}else if(v==button_fragment) {
			Intent intent=new Intent(this,JanusActivity.class);
			intent.putExtra("example", res.getString(R.string.fragment));
			startActivity(intent);
		}else if(v==button_storageAndTheme) {
			Intent intent=new Intent(this,StorageAndThemeActivity.class);
			intent.putExtra("example", res.getString(R.string.storageAndTheme));
			startActivity(intent);
		}else if(v==button_sqlite) {
			Intent intent=new Intent(this,SqliteActivity.class);
			intent.putExtra("example", "sqlite");
			startActivity(intent);
		}

		
	}

}
