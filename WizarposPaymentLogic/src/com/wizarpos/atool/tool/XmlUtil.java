package com.wizarpos.atool.tool;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class XmlUtil {

	public static String writeXml(Map<String, Object> map, String startTag) {
		XmlSerializer serializer = Xml.newSerializer();
		try {
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("utf8", true);
			
			serializer.startTag("", startTag);
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				serializer.startTag("", key);
				serializer.text(String.valueOf(entry.getValue()));
				serializer.endTag("", key);
			}
			serializer.endTag("", startTag);
			serializer.endDocument();
			
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * xml读取，只适用于一层结构
	 * @param xml
	 * @return
	 */
	public static Map<String, String> readXml(String xml) {
		Map<String, String> map = new HashMap<String, String>();
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			while (eventType !=  XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				
				if (eventType == XmlPullParser.START_DOCUMENT) {
					continue;
				} else if (eventType == XmlPullParser.START_TAG) {
					map.put(tagName, parser.getText());
				}
				eventType = parser.next();
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
}
