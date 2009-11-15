package profundo.pushparser.test;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

import profundo.kml.KmlHandler;
import profundo.pushparser.XmlPushParser;

public class LoadKml {
	
	
	public static void main(String[] args) throws SAXException, IOException {
		KmlHandler kml = new KmlHandler();
		URL resource = LoadKml.class.getResource("test3.xml");
		XmlPushParser parser = new XmlPushParser(kml);
		parser.parse(resource.openStream());
	}
}
