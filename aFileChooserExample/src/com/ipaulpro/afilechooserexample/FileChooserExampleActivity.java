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

package com.ipaulpro.afilechooserexample;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * @author paulburke (ipaulpro)
 */
public class FileChooserExampleActivity extends Activity {

	private static final int REQUEST_CODE = 6384; // onActivityResult request code
	private static final ArrayList<String> INCLUDE_EXTENSIONS_LIST = new ArrayList<String>();
	static{
		INCLUDE_EXTENSIONS_LIST.add(".apk");
		INCLUDE_EXTENSIONS_LIST.add(".bin");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a simple button to start the file chooser process
		Button button = new Button(this);
		button.setText(R.string.choose_file);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Display the file chooser dialog
				//showChooser();
				startFileChooserActivity();
			}
		});
		
		setContentView(button);
	}
	
	private void showChooser() {
		// Use the GET_CONTENT intent from the utility class
		Intent target = FileUtils.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(
				target, getString(R.string.chooser_title));
		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
		}				
	}
	
	private void startFileChooserActivity(){
		startActivityForResult(
				new Intent(this, FileChooserActivity.class).putStringArrayListExtra(
						FileChooserActivity.EXTRA_FILTER_INCLUDE_EXTENSIONS, INCLUDE_EXTENSIONS_LIST), 
						REQUEST_CODE);
						
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:	
			// If the file selection was successful
			if (resultCode == RESULT_OK) {		
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();

					try {
						// Create a file instance from the URI
						final File file = FileUtils.getFile(uri);
						Toast.makeText(FileChooserExampleActivity.this, 
								"File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			} 
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}