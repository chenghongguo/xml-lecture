package com.hongguo.xml.dom;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.InputStream;

public class DomTest {

    private Document document = null;

    private DocumentBuilder documentBuilder = null;

    @Before
    public void before() throws Exception {
        // 创建工厂对象
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        // 是否将CDATA片段转换为Text节点，默认：false
        factory.setCoalescing(true);
        // 是否进行有效性校验，默认不校验XML有效性，默认：false
        factory.setValidating(false);
        // 是否忽略注释，默认：false
        factory.setIgnoringComments(true);
        // 是否删除元素内容里的空格，默认：false
        factory.setIgnoringElementContentWhitespace(true);
        documentBuilder = factory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println("---warning---");
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println("---error---");
                System.out.println(exception.getSystemId() + " 文档第 " + exception.getLineNumber()
                        + " 行，第 " + exception.getColumnNumber()
                        + " 发生有效性错误，错误信息：" + exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println("---fatalError---");
            }
        });
        // 解析成Document文件对象
        InputStream in = DomTest.class.getClassLoader().getResourceAsStream("inventory.xml");
        document = documentBuilder.parse(in);
    }

    @Test
    public void testParse() throws Exception {
        // 获取XML文档的根结点
        Element documentElement = document.getDocumentElement();
        System.out.println(documentElement.getNodeType() == Node.ELEMENT_NODE);
        System.out.println(documentElement.getNodeName() + ":" + documentElement.getNodeValue() + ":" + documentElement.getNodeType());

        // 获取根元素下所有"book"子元素，传*则代表获取所有子元素
        NodeList bookLists = documentElement.getElementsByTagName("book");

        // 遍历子元素
        for (int i = 0; i < bookLists.getLength(); i++) {
            System.out.println("第" + (i + 1) + "本书");
            Node item = bookLists.item(i);
            // 获取元素全部属性
            NamedNodeMap attributes = item.getAttributes();
            System.out.println("\tattrs=" + JSONObject.toJSONString(attributes));
            // 获取元素某个属性
            Node isbn = attributes.getNamedItem("isbn");
            if (isbn != null) {
                System.out.println("\t该书的ISBN是：" + isbn.getTextContent());
            }

            NodeList childNodes = item.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childItem = childNodes.item(j);
                if (Node.TEXT_NODE != childItem.getNodeType()) {
                    System.out.println("\t" + childItem.getNodeName() + ":" + childItem.getTextContent().trim());
                }
            }
        }
    }

    @Test
    public void testCreate() throws Exception {
        // 创建一个新的Document对象
        Document document = documentBuilder.newDocument();
        document.setXmlVersion("1.0");
        // 创建处理命令
        ProcessingInstruction pi = document.createProcessingInstruction("hongguo", "href=http://www.crazyit.org");
        document.appendChild(pi);

        Comment comment = document.createComment("根元素之前的注释");
        document.appendChild(comment);

        Element root = document.createElement("student");
        // 为根元素添加学号属性
        root.setAttribute("学号", "1993333910");

        // 创建name元素
        Element item = document.createElement("name");
        item.appendChild(document.createTextNode("张三"));
        root.appendChild(item);

        // 创建age元素
        item = document.createElement("age");
        item.appendChild(document.createTextNode("28"));
        root.appendChild(item);

        // 创建high元素
        item = document.createElement("high");
        item.appendChild(document.createTextNode("183"));
        root.appendChild(item);

        item = document.createElement("score");
        Element lesson = document.createElement("Java");
        lesson.appendChild(document.createTextNode("96"));
        item.appendChild(lesson);
        lesson = document.createElement("Spring");
        lesson.appendChild(document.createTextNode("90"));
        item.appendChild(lesson);
        lesson = document.createElement("Mybatis");
        lesson.appendChild(document.createTextNode("80"));
        item.appendChild(lesson);

        root.appendChild(item);

        document.appendChild(root);

        // 获取DOMImplementationRegistry对象， 它是获取DOMImplementation的工厂
        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS domImplLS = (DOMImplementationLS) registry.getDOMImplementation("LS");
        // 获取LSSerializer对象，用于序列化DOM树的工具
        LSSerializer serializer = domImplLS.createLSSerializer();
        serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
        serializer.getDomConfig().setParameter("xml-declaration", Boolean.TRUE);
        LSOutput out = domImplLS.createLSOutput();
        // 指定输出文档编码所用的字符集
        out.setEncoding("UTF-8");
        FileWriter outString = new FileWriter("student.xml");
        out.setCharacterStream(outString);
        serializer.write(document, out);
    }

    @Test
    public void testUpdate() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("student.xml");
        NodeList names = doc.getElementsByTagName("name");
        if (names != null && names.getLength() > 0) {
            Node name = names.item(0);
            name.setTextContent("孙悟空");
        }

        DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS domImplLS = (DOMImplementationLS) registry.getDOMImplementation("LS");
        LSSerializer serializer = domImplLS.createLSSerializer();
        serializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
        LSOutput out = domImplLS.createLSOutput();
        out.setEncoding("UTF-8");
        FileWriter writer = new FileWriter("update.xml");
        out.setCharacterStream(writer);
        serializer.write(doc, out);
    }
}
