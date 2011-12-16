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

package com.ipaulpro.afilechooser;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author paulburke (ipaulpro)
 */
public class FileListAdapter extends BaseAdapter {
	ArrayList<File> mFiles = new ArrayList<File>();
	private Context mContext;
	private Drawable mFolderDrawable;
	private Drawable mFileDrawable;

	public FileListAdapter(Context context) {
		mContext = context;
		Resources res = mContext.getResources();
		mFolderDrawable = res.getDrawable(R.drawable.ic_folder);
		mFileDrawable = res.getDrawable(R.drawable.ic_file);
	}

	public void setListItems(ArrayList<File> files) {
		this.mFiles = files;
	}

	public int getCount() {
		return mFiles.size();
	}

	public void add(File file) {
		mFiles.add(file);
	}

	public void clear() {
		mFiles.clear();
	}

	public Object getItem(int position) {
		return mFiles.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) { 
			final LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.file, parent, false);
			
			holder = new ViewHolder();
			holder.layoutView = (LinearLayout) convertView.findViewById(R.id.folder_layout);
			holder.nameView = (TextView) convertView.findViewById(R.id.folder_name);
			holder.iconView = (ImageView) convertView.findViewById(R.id.folder_icon);
			
			convertView.setTag(holder);
		} else {
			// Reduce, reuse, recycle!
			holder = (ViewHolder) convertView.getTag();
		}

		// Get the file at the current position
		final File file = mFiles.get(position);
		Drawable drawable = null;
		// Set the TextView as the file name
		holder.nameView.setText(file.getName());

		if (file.isDirectory()) {
			// If the item is a directory, use the folder icon
			drawable = mFolderDrawable;
		} else {
			// Otherwise, use the file icon
			drawable = mFileDrawable;
		}
		// Set the icon as the ImageView
		holder.iconView.setBackgroundDrawable(drawable);

		final int white = R.drawable.list_selector_background;
		final int off_white = R.drawable.list_selector_background_gray;
		if (position % 2 == 0) {
			// If the row is even, use the gray background selector
			holder.layoutView.setBackgroundResource(off_white);
		} else {
			// Otherwise, use the white background selector
			holder.layoutView.setBackgroundResource(white);
		}

		return convertView;
	}

	static class ViewHolder {
		LinearLayout layoutView;
		TextView nameView;
		ImageView iconView;
	}
}