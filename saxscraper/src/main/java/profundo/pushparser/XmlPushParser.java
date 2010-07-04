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

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlPushParser extends DefaultHandler {
	static ThreadLocal<XmlPushParser> parser = new ThreadLocal<XmlPushParser>();
	private XmlPushParser oldParser;
	int currentDepth;
	private ElementHandler rootHandler;

	public XmlPushParser(ElementHandler rootHandler) {
		this.rootHandler = rootHandler;
	}

	public static XmlPushParser getParser() {
		return parser.get();
	}

	public void parse(InputSource in) throws SAXException, IOException {
		XMLReader parser = org.xml.sax.helpers.XMLReaderFactory
				.createXMLReader();
		parser.setContentHandler(this);
		parser.parse(in);
	}

	static SAXParserFactory sf;

	private static SAXParserFactory jtidySaxParser() {
		if (sf == null) {
			synchronized (XmlPushParser.class) {
				if (sf == null) {
					newHtmlParserFactory();
				}
			}
		}
		return sf;
	}

	private static void newHtmlParserFactory() {
		try {
			sf = (SAXParserFactory) Class.forName(
			"org.w3c.tidy.JTidySAXParserFactory")
			.newInstance();
		} catch (Exception e) {
			Logger logger = Logger.getAnonymousLogger();
			logger.log(Level.SEVERE,
					"no org.w3c.tidy.JTidySAXParserFactory", e); // TODO
			sf = SAXParserFactory.newInstance();
		}
	}

	public void parseHtml(InputSource in, DefaultHandler ch)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParser parser = jtidySaxParser().newSAXParser();
		parser.parse(in, ch);
	}

	Stack<SubHander> handlerStack = new Stack<SubHander>();

	public void pushHandler(ElementHandler subHandler, String uri,
			String localName, String qName, Attributes attributes) {
		SubHander sub = new SubHander();
		sub.handler = subHandler;
		sub.depth = currentDepth;

		ElementHandler parentHandler = handlerStack.size() > 0 ? handlerStack
				.get(handlerStack.size() - 1).handler : null;

		subHandler.startHandler(parentHandler, uri, localName, qName,
				attributes);

		handlerStack.push(sub);
	}

	protected void pushHandler(ElementHandler subParser) {
		pushHandler(subParser, null, null, null, null);
	}

	protected void popHandler() {
		popHandler(null, null, null);
	}

	public void popHandler(String uri, String localName, String qName) {

		ElementHandler parentHandler = handlerStack.size() > 1 ? handlerStack
				.get(handlerStack.size() - 2).handler : null;
		currentHandler().endHandler(parentHandler, uri, localName, qName);
		handlerStack.pop();
	}

	private static class SubHander {
		public ElementHandler handler;
		public int depth;
	}

	private ElementHandler currentHandler() {
		return current().handler;
	}

	private SubHander current() {
		return handlerStack.peek();
	}

	@Override
	public void startDocument() throws SAXException {
		this.oldParser = parser.get();
		parser.set(this);
		pushHandler(rootHandler);
	}

	@Override
	public void endDocument() throws SAXException {
		popHandler();
		parser.set(oldParser);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() > 0)
			currentHandler().characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentDepth--;
		SubHander current = current();
		if (currentDepth < current.depth) {
			popHandler(uri, localName, qName);
			current = current();
		}
		current.handler.endElement(uri, localName, qName);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentDepth++;
		SubHander current = current();
		current.handler.startElement(uri, localName, qName, attributes);
	}

	public void parse(InputStream in) throws SAXException, IOException {
		parse(new InputSource(in));
	}
}
