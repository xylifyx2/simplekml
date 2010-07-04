package profundo.screenscraper;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import profundo.pushparser.ElementHandler;
import profundo.pushparser.util.NoopElementHandler;

public class ElementFilterHandler extends NoopElementHandler {
	protected int depth;
	protected Filter filter;
	protected Action match;
	protected Action mismatch;
	protected Object userVariable;

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Action getMatch() {
		return match;
	}

	public void setMatch(Action match) {
		this.match = match;
	}

	public Action getMismatch() {
		return mismatch;
	}

	public void setMismatch(Action mismatch) {
		this.mismatch = mismatch;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		depth--;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		depth++;
	}

	public void addFilter(Filter filter2) {
		if (filter != null) {
			filter = new AndFilter(filter, filter2);
		}
	}

	@Override
	public void startHandler(ElementHandler parentHandler, String uri,
			String localName, String qName, Attributes attributes) {
		userVariable = null;
	}

	public void addAction(Action action) {
		// TODO Auto-generated method stub
		
	}

}
