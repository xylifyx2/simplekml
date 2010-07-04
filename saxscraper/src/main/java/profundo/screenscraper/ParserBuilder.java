package profundo.screenscraper;

import java.net.URL;

import profundo.pushparser.ElementHandler;

public class ParserBuilder {
	private URL url;

	public ElementHandlerBuilder onHtmlPage(final URL url) {
		this.url = url;
		return null;
	}

	public static void main(String[] args) throws Exception {
		TextHandler print = new TextHandler() {
			public void onValue(String value) {
				System.out.println("title is " + value);
			}
		};
		ParserBuilder b = new ParserBuilder();

		ValueRetrieverBuilder expr = b.onHtmlPage(new URL("http://www.dr.dk/"))
				.descendenOrSelf().withTag("title").index(0)
				.onMatch().text().call(print);

		expr.run();
	}

	public void set(ElementHandler handler) {
		// TODO Auto-generated method stub

	}
}
