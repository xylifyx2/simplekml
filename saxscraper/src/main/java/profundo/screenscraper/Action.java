package profundo.screenscraper;

import org.xml.sax.Attributes;

public interface Action {
	public void run(ElementFilterHandler handler, String uri, String localName,
			String qName, Attributes attributes);
}
