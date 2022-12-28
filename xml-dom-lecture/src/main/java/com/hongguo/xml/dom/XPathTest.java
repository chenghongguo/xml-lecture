package com.hongguo.xml.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class XPathTest {
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = XPathTest.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = builder.parse(inputStream);

        // 构造XPath对象
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        // 获取所有节点
        NodeList nodeList = (NodeList) xPath.evaluate("/inventory/book/title", document, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item instanceof Element) {
                System.out.println("element " + item.getNodeName() + " = " + item.getTextContent());
            } else if (item instanceof Attr) {
                System.out.println("Attr " + item);
            } else {
                System.out.println(item);
            }
        }

        System.out.println("--------");

        // 获取数量
        Double count = (Double) xPath.evaluate("count(/inventory/book/title)", document, XPathConstants.NUMBER);
        System.out.println("count = " + count.intValue());

        // 获取第一个节点
        Node node = (Node) xPath.evaluate("/inventory/book/title[1]", document, XPathConstants.NODE);
        if (node instanceof Element) {
            System.out.println("element " + node.getNodeName() + " = " + node.getTextContent());
        } else if (node instanceof Attr) {
            System.out.println("Attr " + node);
        } else {
            System.out.println(node);
        }

        // 获取文本
        String text = (String) xPath.evaluate("/inventory/book/title[1]/text()", document, XPathConstants.STRING);
        System.out.println("text = " + text);

        // 获取属性
        String year = (String) xPath.evaluate("/inventory/book/attribute::year", document, XPathConstants.STRING);
        System.out.println("year = " + year);

    }
}
