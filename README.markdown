# aFileChooser - Android File Chooser

aFileChooser is an __Android Library Project__ that simplifies the process of presenting a file chooser.

Intents provide the ability to hook into third-party app components for content selection. This works well for media files, but if you want users to be able to select *any* file, they must have an existing File Explorer app installed. Because many Android devices don't have stock File Explorers, the developer must often instruct the user to install one, or build one, themselves. aFileChooser solves this issue.

### Features:

 * Full file explorer
 * Simplify `GET_CONTENT` Intent creation
 * Easily convert a `Uri` into a `File`
 * Determine MIME data types
 * Follows Android conventions (Fragments, Loaders, Intents, etc.) 

![screenshot-1](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-1.png) ![screenshot-2](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-2.png)

## Installation

Add aFileChooser to your project as an [Android Library Project](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject).

Add `FileChooserActivity` to your project's AndroidManifest.xml file with a fully-qualified `name`. The `label` and `icon` set here will be shown in the "Intent Chooser" dialog.

__Important__ `FileChooserActivity` must have the `<intent-filter>` set as follows:

    <activity
        android:name="com.ipaulpro.afilechooser.FileChooserActivity"
        android:icon="@drawable/ic_chooser"
        android:label="@string/chooser_label" >
        <intent-filter>
            <action android:name="android.intent.action.GET_CONTENT" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.OPENABLE" />

            <data android:mimeType="*/*" />
        </intent-filter>
    </activity>

## Usage

Use `startActivityForResult(Intent, int)` to launch the "Intent Chooser" dialog (shown below), or `FileChooserActivity` directly. `FileChooserActivity` returns the `Uri` of the file selected as the `Intent` data in `onActivityResult(int, int, Intent)`.

    private static final int REQUEST_CHOOSER = 1234;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                	File file = FileUtils.getFile(uri);
            	}
        }
    }

A more robust example can be found in the aFileChooserExample folder.

__Note__ The `FileUtils` class provides a helper method to construct an `ACTION_GET_CONTENT` Intent (`FileUtils.createGetContentIntent()`). It also contains a method to convert a `Uri` into a `File` (`FileUtils.getFile(Uri)`).

## Credits

Developed by Paul Burke (iPaulPro) - [paulburke.co](http://paulburke.co/)

Translations by Thomas Taschauer (TomTasche) - [tomtasche.at](http://tomtasche.at)

## License

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

    ---

    FileUtils.java, MimeTypeParser.java and MimeTypes.java:

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
