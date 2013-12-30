/*
 * Copyright (C) 2013 Paul Burke
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

import android.annotation.SuppressLint;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 * Main Activity that handles the FileListFragments
 *
 * @version 2013-06-25
 * @author paulburke (ipaulpro)
 */
@SuppressWarnings ("CollectionDeclaredAsConcreteClass")
public class FileChooserActivity extends FragmentActivity implements
        OnBackStackChangedListener, FileListFragment.Callbacks {

    /**
     * TAG for log messages.
     * */
    static final String TAG = FileChooserActivity.class.getName ();

    public static final String SAVE_INSTANCE_PATH = "path";
    public static final String EXTRA_FILTER_INCLUDE_EXTENSIONS =
       "com.ipaulpro.afilechooser.EXTRA_FILTER_INCLUDE_EXTENSIONS";
   public static final String EXTRA_FILTER_BASE_PATH =
      "com.ipaulpro.afilechooser.EXTRA_FILTER_BASE_PATH";
    public static final String EXTERNAL_BASE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();

    private static final boolean HAS_ACTIONBAR = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
   /**
    * <p>start activity</p>
    *
    * @param callingActivity activity opening the chooser
    * @param requestCode request code used to identiefy the result
    * @param filterIncludeExtensions file extensions to display
    */
   public static void startActivity (
      final android.app.Activity callingActivity,
      final int requestCode,
      final java.util.ArrayList<String> filterIncludeExtensions) {
      android.util.Log.d (TAG, "+ startActivity");
      android.util.Log.v (TAG, "> callingActivity         = " + callingActivity);
      android.util.Log.v (TAG, "> requestCode             = " + requestCode);
      android.util.Log.v (TAG, "> filterIncludeExtensions = " + filterIncludeExtensions);

      final Intent intent = new Intent (callingActivity, FileChooserActivity.class);

      intent.putStringArrayListExtra (
          FileChooserActivity.EXTRA_FILTER_INCLUDE_EXTENSIONS,
          filterIncludeExtensions);

      android.util.Log.v (TAG, "> intent                  = " + intent);

      try {
         callingActivity.startActivityForResult (intent, requestCode);
      } catch (@NotNull final android.content.ActivityNotFoundException e) {
         // The reason for the existence of aFileChooser
         android.util.Log.e (TAG, "LOG02230:", e);
      }

      android.util.Log.d (TAG, "- startActivity");
      return;
   } // startActivity

   /**
    * <p>start activity</p>
    *
    * @param callingActivity activity opening the chooser
    * @param requestCode request code used to identify the result
    * @param baseDirectory base directory to show
    * @param filterIncludeExtensions file extensions to display
    */
   public static void startActivity (
      final android.app.Activity callingActivity,
      final int requestCode,
      final String baseDirectory,
      final java.util.ArrayList<String> filterIncludeExtensions) {
      android.util.Log.d (TAG, "+ startActivity");
      android.util.Log.v (TAG, "> callingActivity         = " + callingActivity);
      android.util.Log.v (TAG, "> requestCode             = " + requestCode);
      android.util.Log.v (TAG, "> baseDirectory           = " + baseDirectory);
      android.util.Log.v (TAG, "> filterIncludeExtensions = " + filterIncludeExtensions);

      final Intent intent = new Intent (callingActivity, FileChooserActivity.class);

      intent.putExtra (
         FileChooserActivity.EXTRA_FILTER_BASE_PATH,
         baseDirectory);
      intent.putStringArrayListExtra (
         FileChooserActivity.EXTRA_FILTER_INCLUDE_EXTENSIONS,
         filterIncludeExtensions);

      android.util.Log.v (TAG, "> intent             = " + intent);

      try {
         callingActivity.startActivityForResult (intent, requestCode);
      } catch (@NotNull final android.content.ActivityNotFoundException e) {
         // The reason for the existence of aFileChooser
         android.util.Log.e (TAG, "LOG02230:", e);
      }

      android.util.Log.d (TAG, "- startActivity");
      return;
   } // startActivity
    private FragmentManager mFragmentManager;
    @NotNull private final BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Toast.makeText(context, R.string.storage_removed, Toast.LENGTH_LONG).show();
            finishWithResult(null);
        }
    };
   /**
    * <p>path to open first</p>
    */
    private String mPath;
   /**
    * <p>extenstion to display</p>
    */
    private ArrayList<String> mFilterIncludeExtensions = new ArrayList<String> ();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        android.util.Log.d (TAG, "+ onCreate");
        android.util.Log.v (TAG, "> savedInstanceState       = " + savedInstanceState);

        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();

        android.util.Log.v (TAG, "> intent                   = " + intent);

        if(intent != null){
            mFilterIncludeExtensions = intent.getStringArrayListExtra (
                EXTRA_FILTER_INCLUDE_EXTENSIONS);
            mPath = intent.getStringExtra (
              EXTRA_FILTER_BASE_PATH);
            android.util.Log.v (TAG, "> mFilterIncludeExtensions = " + mFilterIncludeExtensions);
            android.util.Log.v (TAG, "> mPath                    = " + mPath);
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener (this);

        if (savedInstanceState == null) {
            if (mPath == null) {
               mPath = EXTERNAL_BASE_PATH;
            } // if
            addFragment();
        } else {
            mPath = savedInstanceState.getString(SAVE_INSTANCE_PATH);
        }

       setTitle(mPath);

       android.util.Log.d (TAG, "- onCreate");
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
    protected void onSaveInstanceState(@NotNull final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVE_INSTANCE_PATH, mPath);
    }

    @SuppressLint("NewApi") // Usages of New APIs are surrounded by sufficient conditional checks
    @Override
    public void onBackStackChanged() {

        final int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            final BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
        } else {
            mPath = EXTERNAL_BASE_PATH;
        }

        setTitle(mPath);
        if (HAS_ACTIONBAR)
            invalidateOptionsMenu();
    }

    @SuppressLint("NewApi") // Usages of New APIs are surrounded by sufficient conditional checks
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (HAS_ACTIONBAR) {
            final boolean hasBackStack = mFragmentManager.getBackStackEntryCount() > 0;
            final ActionBar actionBar = getActionBar();

            actionBar.setDisplayHomeAsUpEnabled(hasBackStack);
            actionBar.setHomeButtonEnabled(hasBackStack);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mFragmentManager.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the initial Fragment with given path.
     */
    private void addFragment() {
        final FileListFragment fragment = FileListFragment.newInstance(
           mPath,
           mFilterIncludeExtensions);
        mFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment).commit();
    }

    /**
     * "Replace" the existing Fragment with a new one using given path. We're
     * really adding a Fragment to the back stack.
     *
     * @param file The file (directory) to display.
     */
    private void replaceFragment(@NotNull final File file) {
        mPath = file.getAbsolutePath();

        final FileListFragment fragment = FileListFragment.newInstance(
           mPath,
           mFilterIncludeExtensions);
        mFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(mPath).commit();
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param file The file selected.
     */
    private void finishWithResult(@Nullable final File file) {
        if (file != null) {
            final Uri uri = Uri.fromFile(file);
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
    @Override
    public void onFileSelected(@Nullable final File file) {
        if (file != null) {
            if (file.isDirectory()) {
                replaceFragment(file);
            } else {
                finishWithResult(file);
            }
        } else {
            Toast.makeText(FileChooserActivity.this, R.string.error_selecting_file,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        final IntentFilter filter = new IntentFilter();
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
