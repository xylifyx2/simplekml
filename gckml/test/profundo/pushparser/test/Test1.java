package profundo.pushparser.test;

import java.io.IOException;

import org.xml.sax.SAXException;

import profundo.pushparser.XmlPushParser;

public class Test1 {
	public static void main(String[] args) throws SAXException, IOException {
		Test1ElementHandler a = new Test1ElementHandler();
		XmlPushParser parser = new XmlPushParser(a);
		parser.parse(Test1.class.getResource("testdata.xml").openStream());
	}
}
