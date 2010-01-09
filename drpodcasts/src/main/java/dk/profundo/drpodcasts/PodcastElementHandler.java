/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.profundo.drpodcasts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import profundo.pushparser.ElementHandler;
import profundo.pushparser.XmlPushParser;

/**
 *
 * @author ermh
 */
public class PodcastElementHandler implements ElementHandler {

	public void characters(char[] ch, int start, int length) throws SAXException {
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("content".equals(attributes.getValue("class"))) {
			XmlPushParser.getParser().pushHandler(new ItemElementHandler(),
					uri, localName, qName, attributes);
		}
	}

	public void startHandler(ElementHandler parentHandler, String uri,
			String localName, String qName, Attributes attributes) {
	}

	public void endHandler(ElementHandler parentHandler, String uri,
			String localName, String qName) {
	}
}
