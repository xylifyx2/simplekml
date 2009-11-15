package profundo.geocaching.kml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.ElementAdapter;
import profundo.pushparser.XmlPushParser;

public class KmlHandler extends ElementAdapter {
	int level = 0;
	private PlacemarkHandler parser;
	private List<Placemark> placemarks = new ArrayList<Placemark>();

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("Placemark".equals(localName)) {
			parser = new PlacemarkHandler();
			XmlPushParser.getParser().pushHandler(parser, uri, localName,
					qName, attributes);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (parser != null) {
			Placemark bean = parser.getBean();
			placemarks.add(bean);
			System.out.println(bean);
			parser = null;
		}
	}
}
