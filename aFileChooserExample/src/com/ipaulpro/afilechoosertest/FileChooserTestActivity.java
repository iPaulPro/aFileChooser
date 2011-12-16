/* 
 * Copyright (C) 2011 Paul Burke
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

package com.ipaulpro.afilechoosertest;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * @author paulburke (ipaulpro)
 */
public class FileChooserTestActivity extends FileChooserActivity {
	// TAG for log messages.
	private static final String TAG = "FileSelectorTestActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Intent.ACTION_MAIN.equals(getIntent().getAction())) {
			// Display the file chooser dialog with default options.
			showFileChooser();
		}
	}

	@Override
	protected void onFileSelect(File file) {
		if (file != null) {
			final Context context = getApplicationContext();
			
			// Get the path of the Selected File.
			final String path = file.getAbsolutePath();
			Log.d(TAG, "File path: " + path);

			// Get the MIME type of the Selected File.			
			String mimeType = FileUtils.getMimeType(context, file);
			Log.d(TAG, "File MIME type: " + mimeType);

			// Get the thumbnail of the Selected File, if image/video
//			final Uri uri = Uri.fromFile(file);
//			Bitmap bm = FileUtils.getThumbnail(context, uri, mimeType);

			finish();
		}	
	}

	@Override
	protected void onFileError(Exception e) {
		Log.e(TAG, "File select error", e);
		finish();
	}

	@Override
	protected void onFileSelectCancel() {
		Log.d(TAG, "File selections canceled");
		finish();
	}

	@Override
	protected void onFileDisconnect() {
		Log.d(TAG, "External storage disconneted");
		finish();
	}
}