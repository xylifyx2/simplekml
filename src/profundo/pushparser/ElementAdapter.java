package profundo.pushparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

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