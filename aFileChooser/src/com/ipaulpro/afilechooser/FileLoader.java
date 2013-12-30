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

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Loader that returns a list of Files in a given file path.
 *
 * @version 2013-12-11
 * @author paulburke (ipaulpro)
 */
public class FileLoader extends AsyncTaskLoader<List<File>> {

	private static final int FILE_OBSERVER_MASK = FileObserver.CREATE
			| FileObserver.DELETE | FileObserver.DELETE_SELF
			| FileObserver.MOVED_FROM | FileObserver.MOVED_TO
			| FileObserver.MODIFY | FileObserver.MOVE_SELF;

	private FileObserver mFileObserver;

	private List<File> mData;
	private final String mPath;
        private ArrayList<String> mFilterIncludeExtensions;

	public FileLoader(
           Context context,
           String path,
           ArrayList<String> filterIncludeExtensions) {
		super(context);
		this.mPath = path;
                this.mFilterIncludeExtensions = filterIncludeExtensions;
	}

	@Override
	public List<File> loadInBackground() {

        final ArrayList<File> list = new ArrayList<File>();

        // Current directory File instance
        final File pathDir = new File(mPath);

        // List file in this directory with the directory filter
        final File[] dirs = pathDir.listFiles(FileUtils.sDirFilter);
        if (dirs != null) {
            // Sort the folders alphabetically
            Arrays.sort(dirs, FileUtils.sComparator);
            // Add each folder to the File list for the list adapter
           java.util.Collections.addAll (list, dirs);
        }

        // List file in this directory with the file filter
        final File[] files = pathDir.listFiles(
           new FileUtils.FileExtensionFilter (mFilterIncludeExtensions));
        if (files != null) {
            // Sort the files alphabetically
            Arrays.sort(files, FileUtils.sComparator);
            // Add each file to the File list for the list adapter
           java.util.Collections.addAll (list, files);
        }

        return list;
	}

	@Override
	public void deliverResult(List<File> data) {
		if (isReset()) {
			onReleaseResources(data);
			return;
		}

		List<File> oldData = mData;
		mData = data;

		if (isStarted())
			super.deliverResult(data);

		if (oldData != null && oldData != data)
			onReleaseResources(oldData);
	}

	@Override
	protected void onStartLoading() {
		if (mData != null)
			deliverResult(mData);

		if (mFileObserver == null) {
			mFileObserver = new FileObserver(mPath, FILE_OBSERVER_MASK) {
				@Override
				public void onEvent(int event, String path) {
					onContentChanged();
				}
			};
		}
		mFileObserver.startWatching();

		if (takeContentChanged() || mData == null)
			forceLoad();
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (mData != null) {
			onReleaseResources(mData);
			mData = null;
		}
	}

	@Override
	public void onCanceled(List<File> data) {
		super.onCanceled(data);

		onReleaseResources(data);
	}

	protected void onReleaseResources(List<File> data) {

		if (mFileObserver != null) {
			mFileObserver.stopWatching();
			mFileObserver = null;
		}
	}
}