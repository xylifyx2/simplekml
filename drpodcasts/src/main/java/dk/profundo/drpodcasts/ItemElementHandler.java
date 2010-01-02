/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.profundo.drpodcasts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import profundo.pushparser.ElementHandler;

/**
 *
 * @author ermh
 */
public class ItemElementHandler implements ElementHandler {
    PodCastItem item = new PodCastItem();
    State state = State.START;
    enum State {
        START,
        TITLE
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        switch(state) {
            case TITLE:
                item.title = new String(ch,start,length);
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (state == State.START && "a".equals(qName)) {
            state = State.TITLE;
        }
    }

    public void startHandler(ElementHandler parentHandler, String uri, String localName, String qName, Attributes attributes) {

    }

    public void endHandler(ElementHandler parentHandler, String uri, String localName, String qName) {

    }

}
