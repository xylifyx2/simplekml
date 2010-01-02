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

    
    public SAXParser newSAXParser() throws ParserConfigurationException, SAXException {
        return new JTidySAXParser();
    }

    
    public void setFeature(String string, boolean bln) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
    }

    
    public boolean getFeature(String string) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        return false;
    }

    static class JTidySAXParser extends SAXParser {

        
        public Parser getParser() throws SAXException {
            throw new UnsupportedOperationException("Parser not supported");
        }

        
        public XMLReader getXMLReader() throws SAXException {
            return new JTidyXMLReader();
        }

        
        public boolean isNamespaceAware() {
            return false;
        }

        
        public boolean isValidating() {
            return false;
        }

        
        public void setProperty(String propertyKey, Object propertyValue) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException(propertyKey);
        }

        
        public Object getProperty(String propertyKey) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException(propertyKey);
        }
    }

    static class JTidyXMLReader implements XMLReader {

        private ContentHandler contentHandler;
        private ErrorHandler errorHandler;
        private EntityResolver entityResolver;

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
            this.entityResolver = er;
        }

        public EntityResolver getEntityResolver() {
            return entityResolver;
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
        configuration.setInCharEncodingName(encoding);

        // new URL("http://www.jp.dk/").openStream();
        StreamIn streamIn = StreamInFactory.getStreamIn(configuration, byteStream);

        TagTable tt = new TagTable();
        tt.setConfiguration(configuration);
        configuration.tt = tt;

        Lexer lexer = new Lexer(streamIn, configuration, report);
        lexer.errout = new PrintWriter(System.out);

        Node node;
        ArrayList stack = new ArrayList();
        contentHandler.startDocument();
        while ((node = lexer.getToken(Lexer.IGNORE_WHITESPACE)) != null) {
            switch (node.type) {
                case Node.START_TAG:
                    stack.add(node.element);
                    contentHandler.startElement("", node.element, node.element, new JTidySAXAttributes(node));
                    Dict s = tt.lookup(node.element);
                    if (s == null || (s.model & Dict.CM_EMPTY) == 0) {
                        break;
                    }
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

        contentHandler.endDocument();
    }

    static class JTidySAXAttributes implements Attributes {

        Node node;

        public JTidySAXAttributes(Node node) {
            this.node = node;
        }

        public int getLength() {
            int length = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                length++;
            }
            return length;
        }

        public String getURI(int index) {
            return "";

        }

        public String getLocalName(int index) {
            int i = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (i == index) {
                    return a.attribute;
                }
                i++;
            }
            return null;
        }

        public String getQName(int index) {
            int i = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (i == index) {
                    return a.attribute;
                }
                i++;
            }
            return null;
        }

        public String getType(int index) {
            return "CDATA";
        }

        public String getValue(int index) {
            int i = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (i == index) {
                    return a.value;
                }
                i++;
            }
            return null;
        }

        public int getIndex(String uri, String localName) {
            int i = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (localName.equals(a.attribute) && (uri == null || uri.length() == 0)) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public int getIndex(String qName) {
            int i = 0;
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (qName.equals(a.attribute)) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public String getType(String uri, String localName) {
            return "CDATA";
        }

        public String getType(String qName) {
            return "CDATA";
        }

        public String getValue(String uri, String localName) {
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (localName.equals(a.attribute) && (uri == null || uri.length() == 0)) {
                    return a.value;
                }
            }
            return null;
        }

        public String getValue(String qName) {
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (qName.equals(a.attribute)) {
                    return a.value;
                }
            }
            return null;
        }
    }

    public static void main(
            String[] args) throws Exception {
        DefaultHandler df = new DefaultHandler() {

            int i = 0;

            
            public void characters(char[] ch, int start, int length) throws SAXException {
                for (int j = 0; j < i; j++) {
                    System.out.print(" ");
                }
                System.out.println(new String(ch,start,length));
            }



            
            public void endDocument() throws SAXException {
                System.out.println("--end--");
            }

            
            public void endElement(String uri, String localName, String qName) throws SAXException {
                i--;
                for (int j = 0; j < i; j++) {
                    System.out.print(" ");
                }
                System.out.println("</" + qName + ">");
            }

            
            public void startDocument() throws SAXException {
                System.out.println("--start--");
            }

            
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                for (int j = 0; j < i; j++) {
                    System.out.print(" ");
                }
                System.out.print("<" + qName);
                int n=atts.getLength();
                for(int j=0; j<n; j++) {
                    System.out.print(" "+atts.getQName(j)+"='"+atts.getValue(j)+"'");
                }
                System.out.println(">");
                i++;
            }
        };

        SAXParserFactory sf = SAXParserFactory.newInstance(JTidySAXParserFactory.class.getName(), null);
        SAXParser sp = sf.newSAXParser();

        InputSource is = new InputSource("http://www.jp.dk/");

        sp.parse(is, df);
    }
}
