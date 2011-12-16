/* 
 * Copyright (C) 2008 OpenIntents.org
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.res.XmlResourceParser;

public class MimeTypeParser {

	public static final String TAG_MIMETYPES = "MimeTypes";
	public static final String TAG_TYPE = "type";
	
	public static final String ATTR_EXTENSION = "extension";
	public static final String ATTR_MIMETYPE = "mimetype";
	
	private XmlPullParser mXpp;
	private MimeTypes mMimeTypes;
    
	public MimeTypeParser() {
	}
	
	public MimeTypes fromXml(InputStream in)
			throws XmlPullParserException, IOException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

		mXpp = factory.newPullParser();
		mXpp.setInput(new InputStreamReader(in));

		return parse();
	}
	
	public MimeTypes fromXmlResource(XmlResourceParser in)
	throws XmlPullParserException, IOException {
		mXpp = in;
		
		return parse();
	}

	public MimeTypes parse()
			throws XmlPullParserException, IOException {
		
		mMimeTypes = new MimeTypes();
		
		int eventType = mXpp.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = mXpp.getName();

			if (eventType == XmlPullParser.START_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {
					
				} else if (tag.equals(TAG_TYPE)) {
					addMimeTypeStart();
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (tag.equals(TAG_MIMETYPES)) {
					
				}
			}

			eventType = mXpp.next();
		}

		return mMimeTypes;
	}
	
	private void addMimeTypeStart() {
		String extension = mXpp.getAttributeValue(null, ATTR_EXTENSION);
		String mimetype = mXpp.getAttributeValue(null, ATTR_MIMETYPE);
		
		mMimeTypes.put(extension, mimetype);
	}
	
}
