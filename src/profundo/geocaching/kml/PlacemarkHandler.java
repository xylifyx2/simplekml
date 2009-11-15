package profundo.geocaching.kml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.BeanWrapper;
import profundo.pushparser.ElementAdapter;

public class PlacemarkHandler extends ElementAdapter {
	private String characters;
	Placemark bean = new Placemark();
	private String key;
	static BeanWrapper bw = new BeanWrapper(Placemark.class);

	public PlacemarkHandler() {

	}

	public Placemark getBean() {
		return bean;
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		this.characters = new String(ch, start, length).trim();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("value".equals(localName)) {
			bw.set(bean, key, characters);
		} else if ("Point".equals(localName)) {
		} else if (characters != null && characters.length() > 0) {
			bw.set(bean, localName, characters);
		}
		characters = null;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.characters = null;
		if ("Data".equals(localName)) {
			key = attributes.getValue("name");
		}
	}
}
