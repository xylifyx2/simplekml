package profundo.kml;

import org.xml.sax.SAXException;

import profundo.pushparser.ElementAdapter;

public class LinkHandler extends ElementAdapter {

	public String link;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch,start,length);
		link = link == null ? s : link + s;
	}

	
	
	
}
