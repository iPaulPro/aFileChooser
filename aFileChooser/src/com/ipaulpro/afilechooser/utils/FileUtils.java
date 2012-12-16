/* 
 * Copyright (C) 2007-2008 OpenIntents.org
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

package com.ipaulpro.afilechooser.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.ipaulpro.afilechooser.R;

/**
 * @version 2009-07-03
 * 
 * @author Peli
 *
 */
public class FileUtils {
	/** TAG for log messages. */
	static final String TAG = "FileUtils";
	private static final boolean DEBUG = false; // Set to true to enable logging

	public static final String MIME_TYPE_AUDIO = "audio/*"; 
	public static final String MIME_TYPE_TEXT = "text/*"; 
	public static final String MIME_TYPE_IMAGE = "image/*"; 
	public static final String MIME_TYPE_VIDEO = "video/*"; 
	public static final String MIME_TYPE_APP = "application/*";
	
	private static MimeTypes sMimeTypeSingleton = null;

	/**
	 * Whether the filename is a video file.
	 * 
	 * @param filename
	 * @return
	 *//*
	public static boolean isVideo(String filename) {
		String mimeType = getMimeType(filename);
		if (mimeType != null && mimeType.startsWith("video/")) {
			return true;
		} else {
			return false;
		}
	}*/

	/**
	 * Whether the URI is a local one.
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isLocal(String uri) {
		if (uri != null && !uri.startsWith("http://")) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the extension of a file name, like ".png" or ".jpg".
	 * 
	 * @param uri
	 * @return Extension including the dot("."); "" if there is no extension;
	 *         null if uri was null.
	 */
	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(".");
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			// No extension.
			return "";
		}
	}

	/**
	 * Returns true if uri is a media uri.
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isMediaUri(Uri uri) {
		String uriString = uri.toString();
		if (uriString.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString())
				|| uriString.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
				|| uriString.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
				|| uriString.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Convert File into Uri.
	 * @param file
	 * @return uri
	 */
	public static Uri getUri(File file) {
		if (file != null) {
			return Uri.fromFile(file);
		}
		return null;
	}

	/**
	 * Convert Uri into File.
	 * @param uri
	 * @return file
	 */
	public static File getFile(Uri uri) {
		if (uri != null) {
			String filepath = uri.getPath();
			if (filepath != null) {
				return new File(filepath);
			}
		}
		return null;
	}

	/**
	 * Returns the path only (without file name).
	 * @param file
	 * @return
	 */
	public static File getPathWithoutFilename(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				// no file to be split off. Return everything
				return file;
			} else {
				String filename = file.getName();
				String filepath = file.getAbsolutePath();

				// Construct path without file name.
				String pathwithoutname = filepath.substring(0, filepath.length() - filename.length());
				if (pathwithoutname.endsWith("/")) {
					pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
				}
				return new File(pathwithoutname);
			}
		}
		return null;
	}

	/**
	 * Constructs a file from a path and file name.
	 * 
	 * @param curdir
	 * @param file
	 * @return
	 */
	public static File getFile(String curdir, String file) {
		String separator = "/";
		if (curdir.endsWith("/")) {
			separator = "";
		}
		File clickedFile = new File(curdir + separator
				+ file);
		return clickedFile;
	}

	public static File getFile(File curdir, String file) {
		return getFile(curdir.getAbsolutePath(), file);
	}


	/**
	 * Get a file path from a Uri.
	 * 
	 * @param context
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 * 
	 * @author paulburke
	 */
	public static String getPath(Context context, Uri uri) throws URISyntaxException {

		if(DEBUG) Log.d(TAG+" File -", 
				"Authority: " + uri.getAuthority() + 
				", Fragment: " + uri.getFragment() + 
				", Port: " + uri.getPort() +
				", Query: " + uri.getQuery() +
				", Scheme: " + uri.getScheme() +
				", Host: " + uri.getHost() +
				", Segments: " + uri.getPathSegments().toString()
		);

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor
				.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the file size in a human-readable string.
	 * 
	 * @param size
	 * @return
	 * 
	 * @author paulburke
	 */
	public static String getReadableFileSize(int size) {
		final int BYTES_IN_KILOBYTES = 1024;
		final DecimalFormat dec = new DecimalFormat("###.#");
		final String KILOBYTES = " KB";
		final String MEGABYTES = " MB";
		final String GIGABYTES = " GB";
		float fileSize = 0;
		String suffix = KILOBYTES;

		if (size > BYTES_IN_KILOBYTES) {
			fileSize = size/BYTES_IN_KILOBYTES;
			if (fileSize > BYTES_IN_KILOBYTES) {
				fileSize = fileSize/BYTES_IN_KILOBYTES;
				if (fileSize > BYTES_IN_KILOBYTES) {
					fileSize = fileSize/BYTES_IN_KILOBYTES;
					suffix = GIGABYTES;
				} else {
					suffix = MEGABYTES;
				}
			}
		}
		return String.valueOf(dec.format(fileSize)+suffix);
	}

	/**
	 * Load MIME types from XML
	 * 
	 * @param context
	 * @return
	 */
	private static MimeTypes getMimeTypes(Context context) {
	    MimeTypes mimeTypes = sMimeTypeSingleton;
	    
	    if (mimeTypes == null) {
	        final MimeTypeParser mtp = new MimeTypeParser();
	        final XmlResourceParser in = context.getResources().getXml(R.xml.mimetypes);

	        try {
	            mimeTypes = mtp.fromXmlResource(in);
	        } catch (Exception e) {
	            if(DEBUG) Log.e(TAG, "getMimeTypes", e);
	        }
	    }

		return mimeTypes;
	} 

	/**
	 * Get the file MIME type
	 * 
	 * @param context
	 * @param file
	 * @return
	 */
	public static String getMimeType(Context context, File file) {
		String mimeType = null;
		final MimeTypes mimeTypes = getMimeTypes(context);
		if (file != null) mimeType = mimeTypes.getMimeType(file.getName());
		return mimeType;
	}
	
	/**
     * Determines if child is a subset of parent. <br>
     * e.g.: foo/bar is a subset of * / *, foo/*, or * / bar
     * 
     * @param parent
     * @param childCandidate
     * @return <code>True</code> if the child is contained within the parent
     */
    public static boolean isMimeTypeSubsetOfMimeType(String parent, String childCandidate) {
        if (parent == null || childCandidate == null) {
            throw new IllegalArgumentException("Cannot compare null MIME types");
        }

        String[] parentMimeParts = parent.split("/");
        String[] childMimeParts = childCandidate.split("/");

        if (parentMimeParts.length != 2 || childMimeParts.length != 2) {
            throw new IllegalArgumentException("Illegal MIME type formats: Parent: " + parent
                + " Child: " + childCandidate);
        }

        boolean containedInFirstPart = parentMimeParts[0].equals("*");
        if (!containedInFirstPart) {
            containedInFirstPart = parentMimeParts[0].equalsIgnoreCase(childMimeParts[0]);
        }

        boolean containedInSecondPart = parentMimeParts[1].equals("*");
        if (!containedInSecondPart) {
            containedInSecondPart = parentMimeParts[1].equalsIgnoreCase(childMimeParts[1]);
        }

        return containedInFirstPart && containedInSecondPart;
    }
    
	/**
	 * Attempt to retrieve the thumbnail of given File from the MediaStore.
	 * 
	 * This should not be called on the UI thread.
	 * 
	 * @param context
	 * @param file
	 * @return
	 * 
	 * @author paulburke
	 */
	public static Bitmap getThumbnail(Context context, File file) {
		return getThumbnail(context, getUri(file), getMimeType(context, file));
	}
	
	/**
	 * Attempt to retrieve the thumbnail of given Uri from the MediaStore.
	 * 
	 * This should not be called on the UI thread.
	 * 
	 * @param context
	 * @param uri
	 * @return
	 * 
	 * @author paulburke
	 */
	public static Bitmap getThumbnail(Context context, Uri uri) {
		return getThumbnail(context, uri, getMimeType(context, getFile(uri)));
	}
	
	/**
	 * Attempt to retrieve the thumbnail of given Uri from the MediaStore.
	 * 
	 * This should not be called on the UI thread.
	 * 
	 * @param context
	 * @param uri
	 * @param mimeType
	 * @return
	 * 
	 * @author paulburke
	 */
	public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
		if(DEBUG) Log.d(TAG, "Attempting to get thumbnail");
		
		if (isMediaUri(uri)) {
			Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
			return null;
		}
		
		Bitmap bm = null;
		if (uri != null) {
			final ContentResolver resolver = context.getContentResolver();
			Cursor cursor = null; 				
			try {
				cursor = resolver.query(uri, null, null,null, null); 
				if (cursor.moveToFirst()) {
					final int id = cursor.getInt(0);
					if(DEBUG) Log.d(TAG, "Got thumb ID: "+id);					

					if (mimeType.contains("video")) {
						bm = MediaStore.Video.Thumbnails.getThumbnail(
								resolver, 
								id, 
								MediaStore.Video.Thumbnails.MINI_KIND, 
								null);
					} 
					else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
						bm = MediaStore.Images.Thumbnails.getThumbnail(
								resolver, 
								id, 
								MediaStore.Images.Thumbnails.MINI_KIND, 
								null);
					}
				}	
			} catch (Exception e) {
				if(DEBUG) Log.e(TAG, "getThumbnail", e);
			} finally {
				if (cursor != null) cursor.close();
			}
		}
		return bm;
	}
	
	private static final String HIDDEN_PREFIX = ".";

	/**
	 * File and folder comparator.
	 * TODO Expose sorting option method 
	 * 
	 * @author paulburke
	 */
	private static Comparator<File> mComparator = new Comparator<File>() {
		@Override
        public int compare(File f1, File f2) {
			// Sort alphabetically by lower case, which is much cleaner
			return f1.getName().toLowerCase().compareTo(
					f2.getName().toLowerCase());
		}
	};
	
	/**
	 * Filters out directories, hidden files, and files that do not
	 * match the MIME type.
	 */
	private static class MimeFileFilter implements FileFilter {

	    private String mMimeType;
	    private Context mContext;

	    public MimeFileFilter(Context context, String mimeType) {
	        mMimeType = mimeType;
	        mContext = context;
	    }
	    
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories), skip hidden files, and has a valid MIME type
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX)
                && isMimeTypeSubsetOfMimeType(mMimeType, getMimeType(mContext, file));
        }
	    
	}
	
	/**
	 * Folder (directories) filter.
	 * 
	 * @author paulburke
	 */
	private static FileFilter mDirFilter = new FileFilter() {
		@Override
        public boolean accept(File file) {
			final String fileName = file.getName();
			// Return directories only and skip hidden directories
			return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
		}
	};
	
	/**
	 * Get a list of Files in the give path
	 * 
	 * @param context
	 * @param path
	 * @param mimeType The MIME type to filter the file list.
	 * @return Collection of files in give directory

	 * @author paulburke
	 */
	public static List<File> getFileList(Context context, String path, String mimeType) {
		ArrayList<File> list = new ArrayList<File>();

		// Current directory File instance
		final File pathDir = new File(path);
		
		// List file in this directory with the directory filter
		final File[] dirs = pathDir.listFiles(mDirFilter);
		if (dirs != null) {
			// Sort the folders alphabetically
			Arrays.sort(dirs, mComparator);
			// Add each folder to the File list for the list adapter
			for (File dir : dirs) list.add(dir);
		}

		// List file in this directory with the file filter
		final File[] files = pathDir.listFiles(new MimeFileFilter(context, mimeType));
		if (files != null) {
			// Sort the files alphabetically
			Arrays.sort(files, mComparator);
			// Add each file to the File list for the list adapter
			for (File file : files) list.add(file);
		}		
		
		return list;
	}

	/**
	 * Get the Intent for selecting content to be used in an Intent Chooser.
	 * 
	 * @return The intent for opening a file with Intent.createChooser()
	 * 
	 * @author paulburke
	 */
	public static Intent createGetContentIntent() {
		// Implicitly allow the user to select a particular kind of data
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		// The MIME data type filter
		intent.setType("*/*"); 
		// Only return URIs that can be opened with ContentResolver
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}
}
