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
import java.util.List;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * Loader that returns a list of Files in a given file path.
 * 
 * @version 2012-10-28
 * 
 * @author paulburke (ipaulpro)
 * 
 */
public class FileLoader extends AsyncTaskLoader<List<File>> {

	private static final int FILE_OBSERVER_MASK = FileObserver.CREATE
			| FileObserver.DELETE | FileObserver.DELETE_SELF
			| FileObserver.MOVED_FROM | FileObserver.MOVED_TO
			| FileObserver.MODIFY | FileObserver.MOVE_SELF;
	
	private FileObserver mFileObserver;
	
	private List<File> mData;
	private String mPath;
	private String mMimeType;

	public FileLoader(Context context, String path, String mimeType) {
		super(context);
		this.mPath = path;
		this.mMimeType = mimeType;
	}

	@Override
	public List<File> loadInBackground() {
		return FileUtils.getFileList(getContext(), mPath, mMimeType);
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