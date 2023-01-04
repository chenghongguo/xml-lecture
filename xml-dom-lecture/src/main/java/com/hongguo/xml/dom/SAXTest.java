package com.hongguo.xml.dom;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXTest {
    public static void main(String[] args) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        parser.parse("xml-dom-lecture/student.xml", new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                System.out.println("startElement=" + uri + ", " + localName + ", " + qName + ", " + attributes.getQName(0));
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                System.out.println("endElement=" + uri + ", " + localName + ", " + qName);
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                System.out.println("characters=" + new String(ch, start, length));
            }
        });
    }
}
