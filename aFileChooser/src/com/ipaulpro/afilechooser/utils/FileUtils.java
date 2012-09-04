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
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import android.content.ContentResolver;
import android.content.Context;
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
	private static final boolean DEBUG = true; // Set to false to disable logging

	public static final String TYPE_AUDIO = "audio"; 
	public static final String TYPE_DOC = "doc"; 
	public static final String TYPE_IMAGE = "image"; 
	public static final String TYPE_OTHER = "other"; 
	public static final String TYPE_VIDEO = "video"; 
	public static final String TYPE_APP = "app";

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
	public static boolean isMediaUri(String uri) {
		if (uri.startsWith(Audio.Media.INTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Audio.Media.EXTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Video.Media.INTERNAL_CONTENT_URI.toString())
				|| uri.startsWith(Video.Media.EXTERNAL_CONTENT_URI.toString())) {
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
	 * @param context
	 * @return
	 */
	public static MimeTypes getMimeTypes(Context context) {
		MimeTypes mimeTypes = null;
		final MimeTypeParser mtp = new MimeTypeParser();
		final XmlResourceParser in = context.getResources().getXml(R.xml.mimetypes);

		try {
			mimeTypes = mtp.fromXmlResource(in);
		} catch (Exception e) {
			if(DEBUG) Log.e(TAG, "getMimeTypes", e);
		}
		return mimeTypes;
	} 

	/**
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
	 * @param context
	 * @param uri
	 * @param mimeType
	 * @return
	 */
	public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
		if(DEBUG) Log.d(TAG, "Attempting to get thumbnail");
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
					else if (mimeType.contains("image")) {
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
}
