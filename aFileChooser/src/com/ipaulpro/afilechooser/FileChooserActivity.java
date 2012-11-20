/* 
 * Copyright (C) 2012 Paul Burke
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */ 

package com.ipaulpro.afilechooser;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

/**
 * Main Activity that handles the FileListFragments 
 * 
 * @version 2012-10-28
 * 
 * @author paulburke (ipaulpro)
 * 
 */
public class FileChooserActivity extends FragmentActivity implements
		OnBackStackChangedListener {

	public static final String PATH = "path";
	public static final String TYPE = "type";
	public static final String EXTERNAL_BASE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private FragmentManager mFragmentManager;
	private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, R.string.storage_removed, Toast.LENGTH_LONG).show();
			finishWithResult(null);
		}
	};
	
	private String mPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.chooser);

		mFragmentManager = getSupportFragmentManager();
		mFragmentManager.addOnBackStackChangedListener(this);

		if (savedInstanceState == null) {
			mPath = EXTERNAL_BASE_PATH;
			addFragment(mPath);
		} else {
			mPath = savedInstanceState.getString(PATH);
		}

		setTitle(mPath);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterStorageListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerStorageListener();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString(PATH, mPath);
	}

	@Override
	public void onBackStackChanged() {
		mPath = EXTERNAL_BASE_PATH;
		
		int count = mFragmentManager.getBackStackEntryCount();
		if (count > 0) {
			BackStackEntry fragment = mFragmentManager
					.getBackStackEntryAt(count - 1);
			mPath = fragment.getName();
		}
		
		setTitle(mPath);
	}

	/**
	 * Add the initial Fragment with given path.
	 * 
	 * @param path The absolute path of the file (directory) to display.
	 */
	private void addFragment(String path) {
		FileListFragment explorerFragment = FileListFragment.newInstance(mPath, getIntent().getType());
		mFragmentManager.beginTransaction()
				.add(R.id.explorer_fragment, explorerFragment).commit();
	}

	/**
	 * "Replace" the existing Fragment with a new one using given path.
	 * We're really adding a Fragment to the back stack.
	 * 
	 * @param path The absolute path of the file (directory) to display.
	 */
	private void replaceFragment(String path) {
		FileListFragment explorerFragment = FileListFragment.newInstance(path, getIntent().getType());
		mFragmentManager.beginTransaction()
				.replace(R.id.explorer_fragment, explorerFragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				.addToBackStack(path).commit();
	}

	/**
	 * Finish this Activity with a result code and URI of the selected file.
	 * 
	 * @param file The file selected.
	 */
	private void finishWithResult(File file) {
		if (file != null) {
			Uri uri = Uri.fromFile(file);
			setResult(RESULT_OK, new Intent().setData(uri));
			finish();
		} else {
			setResult(RESULT_CANCELED);	
			finish();
		}
	}
	
	/**
	 * Called when the user selects a File
	 * 
	 * @param file The file that was selected
	 */
	protected void onFileSelected(File file) {
		if (file != null) {
			mPath = file.getAbsolutePath();
			
			if (file.isDirectory()) {
				replaceFragment(mPath);
			} else {
				finishWithResult(file);	
			}
		} else {
			Toast.makeText(FileChooserActivity.this, R.string.error_selecting_file, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Register the external storage BroadcastReceiver.
	 */
	private void registerStorageListener() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		registerReceiver(mStorageListener, filter);
	}

	/**
	 * Unregister the external storage BroadcastReceiver.
	 */
	private void unregisterStorageListener() {
		unregisterReceiver(mStorageListener);
	}
}
