package profundo.screenscraper;

import org.xml.sax.SAXException;

public class CharacterHandler extends ElementFilterHandler {
	StringBuilder b = new StringBuilder();

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		b.append(ch, start, length);
	}

	@Override
	public String toString() {
		return b.toString();
	}
}
