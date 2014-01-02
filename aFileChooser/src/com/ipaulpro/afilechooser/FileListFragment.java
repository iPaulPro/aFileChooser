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

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Fragment that displays a list of Files in a given path.
 *
 * @version 2013-12-11
 * @author paulburke (ipaulpro)
 */
@SuppressWarnings ("CollectionDeclaredAsConcreteClass")
public class FileListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<List<File>> {

   /**
    * TAG for log messages.
    * */
   static final String TAG = FileListFragment.class.getName ();
    /**
     * Interface to listen for events.
     */
    public interface Callbacks {
        /**
         * Called when a file is selected from the list.
         *
         * @param file The file selected
         */
        public void onFileSelected(File file);
    }

    private static final int LOADER_ID = 0;

    @NotNull
    private FileListAdapter mAdapter;
   /**
    * <p>path to display. Not null after onCreate</p>
    */
    @NotNull
    private String mPath;
    @Nullable
    private ArrayList<String> mFilterIncludeExtensions = new ArrayList<String>();

    private Callbacks mListener;

    /**
     * Create a new instance with the given file path.
     *
     * @param path The absolute path of the file (directory) to display.
     * @return A new Fragment with the given file path.
     */
    @NotNull public static FileListFragment newInstance(
        @NotNull final String path,
        @Nullable final ArrayList<String> filterIncludeExtensions ) {
        //android.util.Log.d (TAG, "+ newInstance");
        //android.util.Log.v (TAG, "> path                     = " + path);
        //android.util.Log.v (TAG, "> filterIncludeExtensions  = " + filterIncludeExtensions);

        final FileListFragment fragment = new FileListFragment();
        final Bundle args = new Bundle();

        args.putString(FileChooserActivity.SAVE_INSTANCE_PATH, path);
        args.putStringArrayList (
           FileChooserActivity.EXTRA_FILTER_INCLUDE_EXTENSIONS,
           filterIncludeExtensions);
        fragment.setArguments(args);

        //android.util.Log.v (TAG, "> fragment                 = " + fragment);
        //android.util.Log.d (TAG, "+ newInstance");
        return fragment;
    }

    @Override
    public void onAttach(@NotNull final Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Callbacks) activity;
        } catch (@NotNull final ClassCastException e) {
            android.util.Log.e (TAG, "LOG02240:", e);
            throw new ClassCastException(activity.toString()
                    + " must implement FileListFragment.Callbacks");
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        //android.util.Log.d (TAG, "+ onCreate");
        //android.util.Log.v (TAG, "> savedInstanceState = " + savedInstanceState);

        super.onCreate(savedInstanceState);

        final android.os.Bundle arguments = getArguments ();

        mAdapter = new FileListAdapter(getActivity());

        //android.util.Log.v (TAG, "> mAdapter           = " + mAdapter);
        //android.util.Log.v (TAG, "> arguments          = " + arguments);

        mPath = arguments != null
           ? arguments.getString (FileChooserActivity.SAVE_INSTANCE_PATH)
           : Environment.getExternalStorageDirectory().getAbsolutePath();
        if(arguments != null){
             mFilterIncludeExtensions = arguments.getStringArrayList (
                FileChooserActivity.EXTRA_FILTER_INCLUDE_EXTENSIONS);
        }

       //android.util.Log.d (TAG, "+ onCreate");
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        setEmptyText(getString(R.string.empty_directory));
        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(@NotNull final ListView l, final View v, final int position, final long id) {
        final FileListAdapter adapter = (FileListAdapter) l.getAdapter();
        if (adapter != null) {
            final File file = adapter.getItem(position);
            mPath = file.getAbsolutePath();
            mListener.onFileSelected(file);
        }
    }

    @Nullable @Override
    public Loader<List<File>> onCreateLoader(final int id, final Bundle args) {
        return new FileLoader(getActivity(), mPath, mFilterIncludeExtensions);
    }

    @Override
    public void onLoadFinished(final Loader<List<File>> loader, final List<File> data) {
        mAdapter.setListItems(data);

        if (isResumed())
            setListShown(true);
        else
            setListShownNoAnimation(true);
    }

    @Override
    public void onLoaderReset(final Loader<List<File>> loader) {
        mAdapter.clear();
    }
}
