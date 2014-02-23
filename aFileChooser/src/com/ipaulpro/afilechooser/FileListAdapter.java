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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * List adapter for Files.
 * 
 * @version 2013-12-11
 * @author paulburke (ipaulpro)
 */
public class FileListAdapter extends BaseAdapter {

    private final static int ICON_FOLDER = R.drawable.ic_folder;
    private final static int ICON_FILE = R.drawable.ic_file;

    private final LayoutInflater mInflater;

    private List<File> mData = new ArrayList<File>();

    public FileListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void add(File file) {
        mData.add(file);
        notifyDataSetChanged();
    }

    public void remove(File file) {
        mData.remove(file);
        notifyDataSetChanged();
    }

    public void insert(File file, int index) {
        mData.add(index, file);
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public File getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public List<File> getListItems() {
        return mData;
    }

    /**
     * Set the list items without notifying on the clear. This prevents loss of
     * scroll position.
     *
     * @param data
     */
    public void setListItems(List<File> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null)
            row = mInflater.inflate(R.layout.file, parent, false);

        TextView view = (TextView) row;

        // Get the file at the current position
        final File file = getItem(position);

        // Set the TextView as the file name
        if(position == 0){
        	view.setText(R.string.go_up);
        	view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_undo, 0, 0, 0);
        } else {
        	view.setText(file.getName());

            // If the item is not a directory, use the file icon
            int icon = file.isDirectory() ? ICON_FOLDER : ICON_FILE;
            view.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        }


        return row;
    }

}