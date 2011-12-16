# aFileChooser - Android Library Project

Android developers often desire to present a user with a method of selecting a file from the external storage. Android's Intent system gives developers the ability to implicitly hook into other app's components, but if the user does not have a file explorer present, the developer must instruct them to install one, or build one, themselves. 

aFileChooser is an Android Library Project that simplifies this process.

aFileChooser's features:

 * Streamlines the `Intent.ACTION_GET_CONTENT` Intent calling process
 * Provides a built-in file explorer
 * Converts URI's into java Files
 * Determine MIME data types
 * Retrieve image thumbnails for media files
 * Follows Android conventions and is extremely simple to implement

## Installation

First import the aFileChooser project into Eclipse. Then, add aFileChooser to your project as an Android Library Project. If you are unfamiliar with Android Library Projects, refer to the official documentation [here](http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject).

Next, in your project, create an Activity that extends FileChooserActivity and add it to your AndroidManifest.xml. 

__Important__ You must se the Intent Filters, `android:theme` and `android:configChanges` as bellow:

     <activity
            android:name="com.foo.ChooserActivity"
            android:label="Choose file from SD"
            android:theme="@android:style/Theme.Light"
            android:configChanges="orientation|keyboard|keyboardHidden" >
            <intent-filter >
                <action android:name="android.intent.action.GET_CONTENT" />
                
				<category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.OPENABLE" />
                
				<data android:mimeType="*/*" />
            </intent-filter>
    </activity>

The String used for `android:label` will be the text that shows on the IntentChooser Dialog.

## Usage

In your Activity you only need to implement `showFileChooser()` and `onFileSelect()` : 

    public class FileChooserTestActivity extends FileChooserActivity {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (Intent.ACTION_MAIN.equals(getIntent().getAction())) {
				showFileChooser();
			}
		}

		@Override
		protected void onFileSelect(File file) {
		       // Here you handle the file selection.
		}
    }

__Important__ - FileChooserActivity uses `Intent.ACTION_GET_INTENT` to show the file explorer. Your Activity must check the Intent Action, to ensure that it is not `ACTION_GET_INTENT`

### A more robust implementation

    public class FileChooserTestActivity extends FileChooserActivity 	{
		// TAG for log messages.
		private static final String TAG = "FileSelectorTestActivity";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// We must check to ensure that the calling Intent is not Intent.ACTION_GET_INTENT
			if (Intent.ACTION_MAIN.equals(getIntent().getAction())) {
				// Display the file chooser with all file types
				showFileChooser("*/*");
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

				// Get the Uri of the Selected File
				// final Uri uri = Uri.fromFile(file);
				
				// Get the thumbnail of the Selected File, if image/video
				// final Bitmap bm = FileUtils.getThumbnail(context, uri, mimeType);

				// Here you can return any data from above to the calling Activity  
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

Project also hosted at [Google Code](http://code.google.com/p/afilechooser/)