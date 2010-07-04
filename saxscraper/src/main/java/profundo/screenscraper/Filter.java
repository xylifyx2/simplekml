package profundo.screenscraper;

import org.xml.sax.Attributes;

public interface Filter {
	boolean accept(ElementFilterHandler handler, String localName,
			Attributes attributes);
}
