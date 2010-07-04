package profundo.screenscraper;

import org.xml.sax.Attributes;

public class ElementHandlerBuilder {
	ElementFilterHandler handler = new ElementFilterHandler();

	public ElementFilterBuilder descendenOrSelf() {
		return new ElementFilterBuilder(handler);
	}

	public ElementFilterBuilder child() {
		ElementFilterBuilder a = new ElementFilterBuilder(handler);
		a.addFilter(new Filter() {

			public boolean accept(ElementFilterHandler handler,
					String localName, Attributes attributes) {
				return handler.depth == 1;
			}

		});
		return a;
	}
}
