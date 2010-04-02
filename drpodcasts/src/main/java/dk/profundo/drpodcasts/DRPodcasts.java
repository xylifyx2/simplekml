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
		loadDRFeedList(new ItemListener() {
            public void onItem(PodCastItem item) {
                System.out.println(item);
            }
        });
	}

	public static void loadDRFeedList(ItemListener listener) throws SAXException, ParserConfigurationException, IOException {
		for (String url : urls) {
			InputSource is = new InputSource(url);
			is.setEncoding("UTF-8");
			MainAreaFinder eh = new MainAreaFinder(listener);
			XmlPushParser xpp = new XmlPushParser(eh);
			System.out.println("======== " + url + " =======");
			xpp.parseHtml(is, xpp);
		}
	}
}
