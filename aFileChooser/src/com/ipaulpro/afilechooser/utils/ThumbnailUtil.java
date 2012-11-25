package com.ipaulpro.afilechooser.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class ThumbnailUtil {

	/** TAG for log messages. */
	static final String TAG = "ThumbnailFetcher";
	private static final boolean DEBUG = false; // Set to true to enable logging
	
	public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
		Bitmap bm = null;
		if (uri != null) {
			final ContentResolver resolver = context.getContentResolver();
			Cursor cursor = null;
			try {
				cursor = resolver.query(uri, null, null, null, null);
				if (cursor.moveToFirst()) {
					final int id = cursor.getInt(0);
					if (DEBUG)
						Log.d(TAG, "Got thumb ID: " + id);

					if (mimeType.contains("video")) {
						bm = MediaStore.Video.Thumbnails
								.getThumbnail(resolver, id,
										MediaStore.Video.Thumbnails.MINI_KIND,
										null);
					} else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
						bm = MediaStore.Images.Thumbnails.getThumbnail(
								resolver, id,
								MediaStore.Images.Thumbnails.MINI_KIND, null);
					}
				}
			} catch (Exception e) {
				if (DEBUG)
					Log.e(TAG, "getThumbnail", e);
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return bm;
	}
}
