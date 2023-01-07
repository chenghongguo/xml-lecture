package com.hongguo.dom4j.test;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class Dom4jRead {
    public static void main(String[] args) throws Exception {
        SAXReader reader = new SAXReader();
        InputStream resource = Dom4jRead.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = reader.read(resource);
        Element root = document.getRootElement();
        parse(root);
    }

    private static void parse(Element root) {
        // 处理元素包含的所有属性
        parseAttribute(root);
        List<Element> elements = root.elements();
        elements.forEach(element -> {
            if (!element.isTextOnly()) {
                parse(element);
            } else {
                parseAttribute(element);
                System.out.println(element.getQName().getName()
                        + "---->" + element.getTextTrim());
            }
        });
    }

    private static void parseAttribute(Element element) {
        List<Attribute> attributes = element.attributes();
        attributes.forEach(attribute -> {
            System.out.println(element.getQName().getName() + "元素的"
                    + attribute.getQName().getQualifiedName() + "属性值为："
                    + attribute.getValue());
        });
    }
}
