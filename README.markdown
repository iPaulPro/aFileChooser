# fork addition:
        - menu to open Root and Home directories

# aFileChooser - Android File Chooser

*(Complete rewrite 10/30/2012)*

Android developers often desire a way to present a user with a method of selecting a file from "external" storage. Android's `Intent` system gives developers the ability to implicitly hook into other app's components, but if the user doesn't have a file explorer installed, the developer must instruct them to install one, or build one, themselves. 

aFileChooser is an __Android Library Project__ that simplifies this process.

### Features:

 * Provides a built-in file explorer
 * Streamlines the `Intent.ACTION_GET_CONTENT` Intent calling process
 * Easily convert a URI into s java `File` object
 * Determine MIME data types
 * Follows Android conventions (Fragments, Loaders) and is extremely simple to implement

![screenshot-1](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-1.png) ![screenshot-2](https://raw.github.com/iPaulPro/aFileChooser/master/screenshot-2.png)

## Installation

Add aFileChooser to your project as an Android Library Project. If you are unfamiliar with Android Library Projects, refer to the official documentation [here](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject).

Add FileChooserActivity to your project's AndroidManifest.xml file. The `android:label` and `android:icon` are displayed in the Action Chooser Dialog.

__Important__ It must have the `intent-filter` set as seen bellow:

    <activity
        android:name="com.ipaulpro.afilechooser.FileChooserActivity"
        android:icon="@drawable/ic_chooser"
        android:label="@string/choose_file" >
            <intent-filter>
                <action android:name="android.intent.action.GET_CONTENT" />

                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.OPENABLE" />

                <data android:mimeType="*/*" />
            </intent-filter>
        </activity>

## Usage

aFileChooser uses `startActivityForResult` to return the Uri of the file selected. The `FileUtils` class provides a helper method to construct an `ACTION_GET_CONTENT` Intent that can be used to create the Action Chooser Dialog. 

    private static final int REQUEST_CODE = 1234;
    private static final String CHOOSER_TITLE = "Select a file";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target, CHOOSER_TITLE);
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CODE:	
            if (resultCode == RESULT_OK) {	
                // The URI of the selected file	
                final Uri uri = data.getData();
                // Create a File from this Uri
                File file = FileUtils.getFile(uri);
            }
        }
    }

A more robust example can be found in the aFileChooserExample folder.

## Developed By

Paul Burke [paulburke.co](http://paulburke.co/)

## License

    Copyright (C) 2011 - 2012 Paul Burke

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
