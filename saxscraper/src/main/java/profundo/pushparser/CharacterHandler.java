/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package profundo.pushparser;

import org.xml.sax.SAXException;

import profundo.pushparser.util.NoopElementHandler;

/**
 *
 * @author ermh
 */
public class CharacterHandler extends NoopElementHandler {
    StringBuilder b = new StringBuilder();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        b.append(ch,start,length);
    }

    @Override
    public String toString() {
        return b.toString();
    }
}