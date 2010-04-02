/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.profundo.drpodcasts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import profundo.pushparser.ElementAdapter;
import profundo.pushparser.XmlPushParser;

/**
 *
 * @author ermh
 */
public class ClassContentHandler extends ElementAdapter {
    private final ItemListener listener;
    public ClassContentHandler(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if ("content".equals(attributes.getValue("class"))) {
            XmlPushParser.getParser().pushHandler(new ItemElementHandler(listener),
                    uri, localName, qName, attributes);
        }
    }
}
