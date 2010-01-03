package profundo.pushparser.test;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.ElementHandler;
import profundo.pushparser.XmlPushParser;

public class Test1ElementHandler implements ElementHandler {

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		System.out.println("characters=" + new String(ch, start, length));
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println("</" + localName + ">");
	}

	public void endHandler(ElementHandler handler, String uri,
			String localName, String qName) {
		System.out.println("done " + localName);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		System.out.println("<" + localName + ">");
		XmlPushParser.getParser().pushHandler(new Test1ElementHandler(),uri,localName,qName,attributes);
	}

	public void startHandler(ElementHandler handler, String uri,
			String localName, String qName, Attributes attributes) {
		System.out.println("start " + localName);
	}

}
