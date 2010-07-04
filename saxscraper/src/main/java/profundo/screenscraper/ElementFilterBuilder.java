package profundo.screenscraper;

import org.xml.sax.Attributes;

public class ElementFilterBuilder {
	private ElementFilterHandler handler;

	public ElementFilterBuilder(ElementFilterHandler handler) {
		this.handler = handler;
	}

	public ElementFilterBuilder withTag(final String tagName) {
		handler.addFilter(new Filter() {

			public boolean accept(ElementFilterHandler handler,
					String localName, Attributes attributes) {
				return tagName.equalsIgnoreCase(localName);
			}
		});
		return this;
	}

	public ElementFilterBuilder withAttributeValue(final String attributeName,
			final String attributeValue) {
		handler.addFilter(new Filter() {

			public boolean accept(ElementFilterHandler handler,
					String localName, Attributes attributes) {
				return attributeValue
						.equals(attributes.getValue(attributeName));
			}
		});

		return this;
	}

	public ElementFilterBuilder depth(final int depth) {
		handler.addFilter(new Filter() {

			public boolean accept(ElementFilterHandler handler,
					String localName, Attributes attributes) {
				return handler.depth == depth;
			}
		});
		return this;
	}

	public ElementFilterBuilder index(final int index) {
		handler.addFilter(new Filter() {

			public boolean accept(ElementFilterHandler handler,
					String localName, Attributes attributes) {
				if (handler.userVariable == null) {
					if (index == 0) {
						return true;
					} else {
						handler.userVariable = new int[] { 0 };
						return false;
					}
				} else {
					if (((int[]) handler.userVariable)[0] == index) {
						return true;
					} else {
						return false;
					}
				}
			}
		});

		return this;
	}

	public ElementFilterBuilder addFilter(Filter filter) {
		handler.addFilter(filter);
		return this;
	}

	public ActionBuilder onMatch() {
		return new ActionBuilder(handler);
	}
}
