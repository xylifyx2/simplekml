/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.profundo.drpodcasts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import profundo.pushparser.ElementAdapter;
import profundo.pushparser.ElementHandler;
import profundo.pushparser.XmlPushParser;

/**
 *
 * @author ermh
 */
public class ContentMiddleHandler extends ElementAdapter {
	int c;

	@Override
	public void startHandler(ElementHandler parentHandler, String uri, String localName, String qName, Attributes attributes) {
		c = 0;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (c == 0 && "div".equals(qName)) {
			String cl = attributes.getValue("class");
			if (cl != null && cl.startsWith("compWrapper")) {
				c++;
				XmlPushParser.getParser().pushHandler(new PodcastElementHandler(),
						uri, localName, qName, attributes);
			}
		}
	}
}

