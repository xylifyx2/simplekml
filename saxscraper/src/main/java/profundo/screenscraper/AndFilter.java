package profundo.screenscraper;

import org.xml.sax.Attributes;

public class AndFilter implements Filter {
	private Filter left;
	private Filter right;

	public AndFilter(Filter left, Filter right) {
		this.left = left;
		this.right = right;
	}

	public boolean accept(ElementFilterHandler handler, String localName,
			Attributes attributes) {
		if (!left.accept(handler, localName, attributes)) {
			return false;
		} else if (!right.accept(handler, localName, attributes)) {
			return false;
		} else {
			return true;
		}
	}

}
