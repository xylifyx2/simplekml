package profundo.pushparser;

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
