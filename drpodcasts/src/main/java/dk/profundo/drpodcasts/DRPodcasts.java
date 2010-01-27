package dk.profundo.drpodcasts;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import profundo.pushparser.XmlPushParser;

/**
 * Hello world!
 *
 */
public class DRPodcasts {

	private static String[] urls = {
		"http://www.dr.dk/Podcast/A-G",
		"http://www.dr.dk/Podcast/H-N",
		"http://www.dr.dk/Podcast/O-AA",
		"http://www.dr.dk/podcast/Emner",
		"http://www.dr.dk/podcast/Udvalgte_serier",
		"http://www.dr.dk/podcast/Video"
	};

	public static void main(String[] args) throws Exception {
		loadDRFeedList();
	}

	public static void loadDRFeedList() throws SAXException, ParserConfigurationException, IOException {
		for (String url : urls) {
			InputSource is = new InputSource(url);
			is.setEncoding("UTF-8");
			ContentMiddleHandler eh = new ContentMiddleHandler();
			XmlPushParser xpp = new XmlPushParser(eh);
			System.out.println("======== " + url + " =======");
			xpp.parseHtml(is, xpp);
		}
	}
}
