package com.hongguo.xml.dom;

import org.junit.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class Dom2Test {

    @Test
    public void testParse() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = Dom2Test.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = builder.parse(inputStream);

        Element root = document.getDocumentElement();
        NodeList bookNodes = root.getElementsByTagName("book");
        int length = bookNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node item = bookNodes.item(i);
            if (item instanceof Element) {
                Element bookEle = ((Element) item);
                String year = bookEle.getAttribute("year");
                String isbn = bookEle.getAttribute("isbn");
                System.out.println(bookEle.getTagName() + ": attr: year = " + year + ", isbn = " + isbn);
                NodeList childNodes = bookEle.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node childItem = childNodes.item(j);
                    if (childItem instanceof Element) {
                        Element childEle = (Element) childItem;
                        System.out.println(childEle.getTagName() + " = " + childEle.getTextContent());
                    }
                }
            }
        }
    }
}
