package profundo.pushparser;

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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface ElementHandler {
	/**
	 * Called on inner content of element
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException;

	/**
	 * Called on inner content of element
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException;

	/**
	 * Called on inner content of element
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException;

	/**
	 * Called on start of this element
	 * @param parentHandler 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 */
	public void startHandler(ElementHandler parentHandler, String uri, String localName, String qName,
			Attributes attributes);

	/**
	 * Called on end of this element
	 * @param attributes 
	 * @param qName 
	 * @param localName 
	 * @param parentHandler 
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	public void endHandler(ElementHandler parentHandler, String uri, String localName, String qName);
}
