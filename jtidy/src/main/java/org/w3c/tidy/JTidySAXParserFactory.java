/*
 *  Java HTML Tidy - JTidy
 *  HTML parser and pretty printer
 *
 *  Copyright (c) 1998-2000 World Wide Web Consortium (Massachusetts
 *  Institute of Technology, Institut National de Recherche en
 *  Informatique et en Automatique, Keio University). All Rights
 *  Reserved.
 *
 *  Contributing Author(s):
 *
 *     Dave Raggett <dsr@w3.org>
 *     Andy Quick <ac.quick@sympatico.ca> (translation to Java)
 *     Gary L Peskin <garyp@firstech.com> (Java development)
 *     Sami Lempinen <sami@lempinen.net> (release management)
 *     Fabrizio Giustina <fgiust at users.sourceforge.net>
 *
 *  The contributing author(s) would like to thank all those who
 *  helped with testing, bug fixes, and patience.  This wouldn't
 *  have been possible without all of you.
 *
 *  COPYRIGHT NOTICE:
 *
 *  This software and documentation is provided "as is," and
 *  the copyright holders and contributing author(s) make no
 *  representations or warranties, express or implied, including
 *  but not limited to, warranties of merchantability or fitness
 *  for any particular purpose or that the use of the software or
 *  documentation will not infringe any third party patents,
 *  copyrights, trademarks or other rights.
 *
 *  The copyright holders and contributing author(s) will not be
 *  liable for any direct, indirect, special or consequential damages
 *  arising out of any use of the software or documentation, even if
 *  advised of the possibility of such damage.
 *
 *  Permission is hereby granted to use, copy, modify, and distribute
 *  this source code, or portions hereof, documentation and executables,
 *  for any purpose, without fee, subject to the following restrictions:
 *
 *  1. The origin of this source code must not be misrepresented.
 *  2. Altered versions must be plainly marked as such and must
 *     not be misrepresented as being the original source.
 *  3. This Copyright notice may not be removed or altered from any
 *     source or altered source distribution.
 *
 *  The copyright holders and contributing author(s) specifically
 *  permit, without fee, and encourage the use of this source code
 *  as a component for supporting the Hypertext Markup Language in
 *  commercial products. If you use this source code in a product,
 *  acknowledgment is not required but would be appreciated.
 *
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
 * Implementation of a SAX Parser that parses real any HTML using the lexer of
 * JTidy. It guesses where absent end tags should be placed, based on the CM_EMPTY
 * flag in the JTidy tag table and by searching the ancestor axis for the matching
 * start tag and closes all the unclosed element in between.
 *
 * @author Erik Martino <erik.martino@gmail.com>
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

    	@SuppressWarnings("deprecation")
        public Parser getParser() throws SAXException {
            throw new UnsupportedOperationException();
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
            // throw new SAXNotRecognizedException(propertyKey);
        }

        public Object getProperty(String propertyKey) throws SAXNotRecognizedException, SAXNotSupportedException {
            throw new SAXNotRecognizedException(propertyKey);
        }
    }

    /**
     * XMLReader using JTidy
     */
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
                parse(new URL(is.getSystemId()).openStream(), is.getEncoding());
            }
        }

        public void parse(String systemId) throws IOException, SAXException {
            parse(new InputSource(systemId));
        }

        private void parse(InputStream byteStream, String encoding) throws SAXException {
            parseInputStream(byteStream, encoding, contentHandler, errorHandler);
        }
    }

    /**
     * Parses an input stream using JTidy and generates sax events to the
     * contentHandler.
     *
     * @param byteStream
     * @param encoding
     * @param contentHandler
     * @param errorHandler
     * @throws SAXException
     */
    @SuppressWarnings("unchecked")
	static protected void parseInputStream(InputStream byteStream, String encoding, ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        Report report = new Report();
        Configuration configuration = new Configuration(report);
        configuration.setInCharEncodingName(encoding);
        configuration.setInOutEncodingName(encoding);

        StreamIn streamIn = StreamInFactory.getStreamIn(configuration, byteStream);

        TagTable tt = new TagTable();
        tt.setConfiguration(configuration);
        configuration.tt = tt;

        Lexer lexer = new Lexer(streamIn, configuration, report);
        lexer.errout = new PrintWriter(System.out);

        Node node;
        JTidySAXAttributes attr = new JTidySAXAttributes();
        ArrayList stack = new ArrayList();
        contentHandler.startDocument();


        while ((node = lexer.getToken(Lexer.IGNORE_WHITESPACE)) != null) {
            switch (node.type) {
                case Node.START_TAG:
                    stack.add(node.element);
                    attr.setNode(node);
                    contentHandler.startElement("", node.element, node.element, attr);
                    Dict s = tt.lookup(node.element);
                    if (s == null || (s.model & Dict.CM_EMPTY) == 0) {
                        break;
                    }
                case Node.END_TAG:
                    int closeDepth = stack.lastIndexOf(node.element);
                    if (closeDepth >= 0) {
                        for (int j = stack.size() - 1; j >= closeDepth; j--) {
                        	String localName = (String) stack.get(j);
                			contentHandler.endElement("", localName, localName);
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
            String localName = (String) stack.get(j);
			contentHandler.endElement("", localName, localName);
            stack.remove(stack.size() - 1);
        }

        contentHandler.endDocument();
    }

    /**
     * SAX element attributes wrapper for JTidy
     */
    static class JTidySAXAttributes implements Attributes {

        Node node;

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
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

        public String toString() {
            StringBuilder b = new StringBuilder();
            for (AttVal a = node.attributes; a != null; a = a.next) {
                if (b.length() > 0) {
                    b.append(" ");
                }
                b.append(a.attribute);
                b.append("=\"");
                b.append(a.value);
                b.append("\"");
            }
            return b.toString();
        }
    }

    /**
     * small demo that demonstrates how to use JTidySAXParserFactory. TODO create
     * testcase instread
     * 
     * @param args
     * @throws Exception
     */
    public static void main(
            String[] args) throws Exception {
    	System.getProperties().setProperty("javax.xml.parsers.SAXParserFactory", JTidySAXParserFactory.class.getName());
        SAXParserFactory sf = SAXParserFactory.newInstance();
        SAXParser sp = sf.newSAXParser();

        InputSource is = new InputSource("http://www.jp.dk/");
        DefaultHandler ih = new DefaultHandler() {

            int depth = 0;

            public void endElement(String uri, String localName, String qName) throws SAXException {
                depth--;
                indent();
                System.out.println("</" + qName + ">");
            }

            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                indent();
                System.out.println("<" + qName + ">");
                depth++;
            }

            private void indent() {
                for (int i = 0; i < depth; i++) {
                    System.out.print(" ");
                }
            }
        };


        XMLReader reader = sp.getXMLReader();
        reader.setContentHandler(ih);
        reader.parse(is);
    }
}
