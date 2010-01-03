/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.profundo.drpodcasts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import profundo.pushparser.CharacterHandler;
import profundo.pushparser.ElementAdapter;
import profundo.pushparser.ElementHandler;
import profundo.pushparser.XmlPushParser;

/**
 *
 * @author ermh
 */
public class ItemElementHandler extends ElementAdapter {

    PodCastItem item = new PodCastItem();
    State state = State.START;
    CharacterHandler characters;

    enum State {

        START,
        TITLE,
        DESCRIPTION,
        FEED,
        DONE
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (state) {
            case TITLE:
                if ("a".equals(qName)) {
                    item.title = characters.toString();
                    characters = null;
                    state = State.DESCRIPTION;
                }
                break;
            case DESCRIPTION:
                if ("p".equals(qName) && characters != null) {
                    item.description = characters.toString();
                    characters = null;
                    state = State.FEED;
                }
                break;
            case FEED:
                break;
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (state == State.START && "a".equals(qName)) {
            state = State.TITLE;
            item.link = attributes.getValue("href");
            XmlPushParser.getParser().pushHandler(characters = new CharacterHandler(),
                    uri, localName, qName, attributes);
        } else if (state == State.DESCRIPTION && "p".equals(qName)) {
            XmlPushParser.getParser().pushHandler(characters = new CharacterHandler(),
                    uri, localName, qName, attributes);
        } else if ("a".equals(qName)
                && "XML".equals(attributes.getValue("title"))) {
            item.feed = attributes.getValue("href");
            state = State.DONE;
        }
    }

    public void startHandler(ElementHandler parentHandler, String uri, String localName, String qName, Attributes attributes) {
    }

    public void endHandler(ElementHandler parentHandler, String uri, String localName, String qName) {
        System.out.println(item);
    }
}
