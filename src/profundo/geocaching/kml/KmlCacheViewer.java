package profundo.geocaching.kml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.xml.sax.SAXException;

import profundo.kml.KmlHandler;
import profundo.pushparser.XmlPushParser;

public class KmlCacheViewer {
	public static void main(String[] args) throws SAXException, IOException {
		KmlHandler kml = new KmlHandler();
		URL url = KmlCacheViewer.class.getResource("/META-INF/kml.link");
		BufferedReader r = new BufferedReader(
				new InputStreamReader(url.openStream(),"UTF-8"));
		String kmllink = r.readLine();
		r.close();
		
		URL kmlurl = new URL(kmllink);
		
		XmlPushParser parser = new XmlPushParser(kml);
		parser.parse(kmlurl.openStream());
		
		System.out.println(kml.link);
	}
}
