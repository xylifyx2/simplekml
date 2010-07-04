package profundo.screenscraper;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

public class OrFilter implements Filter {
	protected List<Filter> filters = new ArrayList<Filter>();
	
	public void addFilter(Filter filter) {
		filters.add(filter);
	}
	
	public boolean accept(ElementFilterHandler handler, String localName,
			Attributes attributes) {
		for(Filter f : filters) {
			if (f.accept(handler, localName, attributes)) {
				return true;
			}
		}
		
		return false;
	}

}
