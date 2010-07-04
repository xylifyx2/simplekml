package profundo.pushparser.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.ElementHandler;

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

public class DelegatingElementHandler implements ElementHandler {
	ElementHandler delegatee;

	public DelegatingElementHandler(ElementHandler delegatee) {
		this.delegatee = delegatee;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (delegatee != null)
			delegatee.characters(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (delegatee != null)
			delegatee.endElement(uri, localName, qName);
	}

	public void endHandler(ElementHandler parentHandler, String uri,
			String localName, String qName) {
		if (delegatee != null)
			delegatee.endHandler(parentHandler, uri, localName, qName);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (delegatee != null)
			delegatee.startElement(uri, localName, qName, attributes);
	}

	public void startHandler(ElementHandler parentHandler, String uri,
			String localName, String qName, Attributes attributes) {
		if (delegatee != null)
			delegatee.startHandler(parentHandler, uri, localName, qName,
					attributes);
	}
}