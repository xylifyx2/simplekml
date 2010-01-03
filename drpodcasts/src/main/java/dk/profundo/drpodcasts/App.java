package dk.profundo.drpodcasts;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.tidy.JTidySAXParserFactory;
import org.xml.sax.InputSource;
import profundo.pushparser.XmlPushParser;

/**
 * Hello world!
 *
 */
public class App {

    static String[] urls = {
        "http://www.dr.dk/Podcast/A-G",
        "http://www.dr.dk/Podcast/H-N",
        "http://www.dr.dk/Podcast/O-AA",
        "http://www.dr.dk/podcast/Emner",
        "http://www.dr.dk/podcast/Udvalgte_serier",
        "http://www.dr.dk/podcast/Video"
    };

    public static void main(String[] args) throws Exception {
        for (String url : urls) {
            InputSource is = new InputSource(url);
            is.setEncoding("UTF-8");

            PodcastElementHandler eh = new PodcastElementHandler();
            XmlPushParser xpp = new XmlPushParser(eh);

            xpp.parseHtml(is,xpp);
        }
        System.out.println("Hello World!\u00f8");
    }
}
