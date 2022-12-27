package com.hongguo.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class DomTest {
    public static void main(String[] args) throws Exception {
        // 创建工厂对象
        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();
        initFactory(documentBuilderFactory);

        // 创建文件构建者
        DocumentBuilder documentBuilder =
                documentBuilderFactory.newDocumentBuilder();
        // 解析成Document文件对象
        InputStream in = DomTest.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = documentBuilder.parse(in);
        NodeList book = document.getElementsByTagName("book");
        for (int i = 0; i < book.getLength(); i++) {
            Node item = book.item(i);
            NamedNodeMap attributes = item.getAttributes();
            Node item1 = attributes.item(i);
            System.out.println(item1.getNodeName() + ":" + item1.getNodeValue());
            NodeList childNodes = item.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNode = childNodes.item(j);
                System.out.println(childNode.getNodeName() + ":" + childNode.getTextContent());
            }
        }
    }

    private static void initFactory(DocumentBuilderFactory factory) {
        // 是否将CDATA片段转换为Text节点，默认：false
        factory.setCoalescing(true);
        // 是否进行有效性校验，默认不校验XML有效性，默认：false
        factory.setValidating(false);
        // 是否忽略注释，默认：false
        factory.setIgnoringComments(true);
        // 是否删除元素内容里的空格，默认：false
        factory.setIgnoringElementContentWhitespace(true);
    }
}
