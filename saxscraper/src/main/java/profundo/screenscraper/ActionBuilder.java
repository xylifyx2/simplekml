package profundo.screenscraper;

import org.xml.sax.Attributes;

import profundo.pushparser.XmlPushParser;

public class ActionBuilder {

	private ElementFilterHandler handler;

	public ActionBuilder(ElementFilterHandler handler) {
		this.handler = handler;
	}

	public void run(final Runnable run) {
		this.handler.addAction(new Action() {

			public void run(ElementFilterHandler handler, String uri,
					String localName, String qName, Attributes attributes) {
				run.run();
			}

		});
	}

	public void run(final Action run) {
		this.handler.addAction(run);
	}
	
	public ElementFilterBuilder newHandler() {
		return newHandler(new ElementFilterHandler());
	}
	
	public ElementFilterBuilder text() {
		return newHandler(new ElementFilterHandler());
	}

	private ElementFilterBuilder newHandler(ElementFilterHandler handler) {
		handler.addAction(new Action() {

			public void run(ElementFilterHandler handler, String uri,
					String localName, String qName, Attributes attributes) {
				XmlPushParser.getParser().pushHandler(handler, uri, localName,
						qName, attributes);
			}
		});
		return new ElementFilterBuilder(handler);
	}
}
