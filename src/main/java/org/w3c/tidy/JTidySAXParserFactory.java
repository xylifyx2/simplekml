/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.w3c.tidy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author ermh
 */
public class JTidySAXParserFactory extends SAXParserFactory {

    @Override
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFeature(String string, boolean bln) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
    }

    @Override
    public boolean getFeature(String string) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    static class JTidySAXParser extends SAXParser {

        @Override
        public Parser getParser() throws SAXException {
            throw new UnsupportedOperationException("Parser not supported");
        }

        @Override
        public XMLReader getXMLReader() throws SAXException {
            return new JTidyXMLReader();
        }

        @Override
        public boolean isNamespaceAware() {
            return false;
        }

        @Override
        public boolean isValidating() {
            return false;
        }

        @Override
        public void setProperty(String propertyKey, Object propertyValue) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException(propertyKey);
        }

        @Override
        public Object getProperty(String propertyKey) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException(propertyKey);
        }
    }

    static class JTidyXMLReader implements XMLReader {

        private ContentHandler contentHandler;
        private ErrorHandler errorHandler;

        public boolean getFeature(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            return false;
        }

        public void setFeature(String string, boolean bln) throws SAXNotRecognizedException, SAXNotSupportedException {
        }

        public Object getProperty(String string) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException();
        }

        public void setProperty(String string, Object o) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException();
        }

        public void setEntityResolver(EntityResolver er) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public EntityResolver getEntityResolver() {
            return null;
        }

        public void setDTDHandler(DTDHandler dtdh) {
        }

        public DTDHandler getDTDHandler() {
            return null;
        }

        public void setContentHandler(ContentHandler ch) {
            this.contentHandler = ch;
        }

        public ContentHandler getContentHandler() {
            return contentHandler;
        }

        public void setErrorHandler(ErrorHandler eh) {
            this.errorHandler = eh;
        }

        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        public void parse(InputSource is) throws IOException, SAXException {
            if (is.getByteStream() != null) {
                parse(is.getByteStream(), is.getEncoding());
            } else if (is.getSystemId() != null) {
                parse(new URL(is.getSystemId()).openStream(), "ISO-8859-1");
            }
        }

        public void parse(String systemId) throws IOException, SAXException {
            URL url = new URL(systemId);
            parse(new InputSource(systemId));
        }

        private void parse(InputStream byteStream, String encoding) throws SAXException {
            parseInputStream(byteStream, encoding, contentHandler, errorHandler);
        }
    }

    static private void parseInputStream(InputStream byteStream, String encoding, ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        Report report = new Report();
        Configuration configuration = new Configuration(report);


        // new URL("http://www.jp.dk/").openStream();
        StreamIn streamIn = StreamInFactory.getStreamIn(configuration, byteStream);

        TagTable tt = new TagTable();
        tt.setConfiguration(configuration);
        configuration.tt = tt;

        Lexer lexer = new Lexer(streamIn, configuration, report);
        lexer.errout = new PrintWriter(System.out);

        Node node;
        ArrayList<String> stack = new ArrayList<String>();

        while ((node = lexer.getToken(Lexer.IGNORE_WHITESPACE)) != null) {
            switch (node.type) {
                case Node.START_TAG:
                    stack.add(node.element);
                    contentHandler.startElement("", node.element, node.element, null);
                    break;
                case Node.END_TAG:
                    int closeDepth = stack.lastIndexOf(node.element);
                    if (closeDepth >= 0) {
                        for (int j = stack.size() - 1; j >= closeDepth; j--) {
                            contentHandler.endElement("", node.element, node.element);
                            stack.remove(stack.size() - 1);
                        }
                    }
                    break;
                case Node.CDATA_TAG:
                case Node.TEXT_NODE: {
                    final String cs = TidyUtils.getString(node.textarray, node.start, node.end - node.start);
                    char[] cha = cs.toCharArray();

                    contentHandler.characters(cha, 0, cha.length);
                    break;
                }
            }
        }

        for (int j = stack.size() - 1; j >= 0; j--) {
            contentHandler.endElement("", node.element, node.element);
            stack.remove(stack.size() - 1);
        }
    }

    public static void main(
            String[] args) throws Exception {
        InputStream byteStream = JTidySAXParserFactory.class.getResourceAsStream("test.html");
        Report report = new Report();
        Configuration configuration = new Configuration(report);


        // new URL("http://www.jp.dk/").openStream();
        StreamIn streamIn = StreamInFactory.getStreamIn(configuration, byteStream);

        TagTable tt = new TagTable();
        tt.setConfiguration(configuration);
        configuration.tt = tt;

        Lexer lexer = new Lexer(streamIn, configuration, report);
        lexer.errout = new PrintWriter(System.out);

        Node node;

        org.xml.sax.ContentHandler ch = new DefaultHandler() {

            public void endElement(String ns, String localName, String qname) throws SAXException {
            }

            public void startElement(String string, String string1, String string2, Attributes atrbts) throws SAXException {
            }
        };


    }
}
