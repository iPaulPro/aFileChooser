# aFileChooser - Android File Chooser

aFileChooser is an __Android Library Project__ that simplifies the process of presenting a file chooser on Android 2.1+.

Intents provide the ability to hook into third-party app components for content selection. This works well for media files, but if you want users to be able to select *any* file, they must have an existing "file explorer" app installed. Because many Android devices don't have stock File Explorers, the developer must often instruct the user to install one, or build one, themselves. aFileChooser solves this issue.

### Features:

 * Full file explorer
 * Simplify `GET_CONTENT` Intent creation
 * Determine MIME data types
 * Follows Android conventions (Fragments, Loaders, Intents, etc.)
 * Supports API 7+

![screenshot-1](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-1.png) ![screenshot-2](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-2.png)

## Installation

Add aFileChooser to your project as an [Android Library Project](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject).

Add `FileChooserActivity` to your project's AndroidManifest.xml file with a fully-qualified `name`. The `label` and `icon` set here will be shown in the "Intent Chooser" dialog.

__Important__ `FileChooserActivity` must have `android:exported="true"` and have the `<intent-filter>` set as follows:

    <activity
        android:name="com.ipaulpro.afilechooser.FileChooserActivity"
        android:icon="@drawable/ic_chooser"
		android:enabled="@bool/use_activity"
        android:exported="true"
        android:label="@string/chooser_label" >
        <intent-filter>
            <action android:name="android.intent.action.GET_CONTENT" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.OPENABLE" />

            <data android:mimeType="*/*" />
        </intent-filter>
    </activity>

If you want to use the Storage Access Framework (API 19+), include [Ian Lake](https://github.com/ianhanniballake/)'s `LocalStorageProvider` (included in this library) in your `<application>`:

	<provider
        android:name="com.ianhanniballake.localstorage.LocalStorageProvider"
        android:authorities="com.ianhanniballake.localstorage.documents"
		android:enabled="@bool/use_provider"
        android:exported="true"
        android:grantUriPermissions="true"
        android:permission="android.permission.MANAGE_DOCUMENTS" >
            <intent-filter>
                <action android:name="android.content.action.DOCUMENTS_PROVIDER" />
            </intent-filter>
    </provider>

Using `FileChooserActivity` and `LocalStorageProvider` together are redundant. Therefore, you should enable/disable based on the API level (above: `@bool/use_provider` and `@bool/use_activity`). See the aFileChooserExample project for their values.

## Usage

Use `startActivityForResult(Intent, int)` to launch `FileChooserActivity` directly. `FileChooserActivity` returns the `Uri` of the file selected as the `Intent` data in `onActivityResult(int, int, Intent)`. Alternatively, you can use the helper method `FileUtils.createGetContentIntent()` to construct an `ACTION_GET_CONTENT` Intent that will show an "Intent Chooser" dialog on pre Kit-Kat devices, and the "Documents UI otherwise. E.g.:

    private static final int REQUEST_CHOOSER = 1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ACTION_GET_CONTENT Intent
        Intent getContentIntent = FileUtils.createGetContentIntent();
		
        Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        	case REQUEST_CHOOSER:	
            	if (resultCode == RESULT_OK) {
					
                	final Uri uri = data.getData();
					
					// Get the File path from the Uri
                	String path = FileUtils.getPath(this, uri);
            	}
				break;
        }
    }

A more robust example can be found in the aFileChooserExample project.

__Note__ the `FileUtils` method to get a file path from a `Uri` (`FileUtils.getPath(Context, Uri)`). This works for `File`, `MediaStore`, and `DocumentProvider` `Uri`s.

## Credits

Developed by Paul Burke (iPaulPro) - [paulburke.co](http://paulburke.co/)

Translations by Thomas Taschauer (TomTasche) - [tomtasche.at](http://tomtasche.at)

Folder (ic_provider.png) by [Sergio Calcara](http://thenounproject.com/fallacyaccount) from The Noun Project

Document (ic_file.png) by [Melvin Salas](http://thenounproject.com/msalas10) from The Noun Project

## Licenses

    Copyright (C) 2011 - 2013 Paul Burke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Portions of FileUtils.java:

    Copyright (C) 2007-2008 OpenIntents.org
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

LocalStorageProvider.java:

	Copyright (c) 2013, Ian Lake
	All rights reserved.

	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	- Neither the name of the <ORGANIZATION> nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.	