package profundo.pushparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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

public class ElementAdapter implements ElementHandler {
	public void characters(char[] ch, int start, int length)
			throws SAXException {

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

	}

	public void endHandler(ElementHandler parentHandler, String uri,
			String localName, String qName) {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
	}

	public void startHandler(ElementHandler parentHandler, String uri,
			String localName, String qName, Attributes attributes) {
	}
}