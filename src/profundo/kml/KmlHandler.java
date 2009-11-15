package profundo.kml;

/*
 * 
 * Copyright 2009 Erik Martino Hansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.ElementAdapter;
import profundo.pushparser.XmlPushParser;

public class KmlHandler extends ElementAdapter {
	int level = 0;
	private PlacemarkHandler placemarkHandler;
	private LinkHandler linkHandler;
	private List<Placemark> placemarks = new ArrayList<Placemark>();
	public String link;

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("Placemark".equals(localName)) {
			placemarkHandler = new PlacemarkHandler();
			XmlPushParser.getParser().pushHandler(placemarkHandler, uri, localName,
					qName, attributes);
		} else if ("href".equals(localName)) {
			linkHandler = new LinkHandler();
			XmlPushParser.getParser().pushHandler(linkHandler, uri, localName,
					qName, attributes);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (placemarkHandler != null) {
			Placemark bean = placemarkHandler.getBean();
			placemarks.add(bean);
			placemarkHandler = null;
		} else if (linkHandler != null) {
			this.link = linkHandler.link;
			linkHandler = null;
			// System.out.println(link);
		}
	}
}
