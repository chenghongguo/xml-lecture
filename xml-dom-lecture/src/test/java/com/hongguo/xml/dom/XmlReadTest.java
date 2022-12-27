package com.hongguo.xml.dom;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class XmlReadTest {

    private DocumentBuilder builder;

    private String fileName = "config-xsd.xml";

    private Map<Class<?>, Class<?>> toPrimitive = new HashMap<>();

    @Before
    public void before() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);

        if (fileName.contains("-xsd")) {
            factory.setNamespaceAware(true);
            final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
            final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
            factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        }

        builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println("warning:" + exception);
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println("error:" + exception);
                System.exit(0);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println("fatalError:" + exception);
                System.exit(0);
            }
        });

        toPrimitive.put(Integer.class, int.class);
        toPrimitive.put(Boolean.class, boolean.class);
    }

    @Test
    public void testRead() throws Exception {
        //InputStream resource = XmlReadTest.class.getResourceAsStream("config-xsd.xml");
        Document document = builder.parse("config-xsd.xml");
        Element root = document.getDocumentElement();

        Map<String, Object> map = parseConfig(root);
        System.out.println(map);
    }

    private Map<String, Object> parseConfig(Element root) throws Exception {
        Map<String, Object> result = new HashMap<>();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element child = (Element) childNodes.item(i);
            String name = child.getAttribute("id");
            Object value = parseObject((Element) child.getFirstChild());
            result.put(name, value);
        }
        return result;
    }

    private Object parseObject(Element element) throws Exception {
        String tagName = element.getTagName();
        if (tagName.equals("factory")) {
            return parseFactory(element);
        } else if (tagName.equals("construct")) {
            return parseConstruct(element);
        } else {
            String childData = ((CharacterData) element.getFirstChild()).getData();
            if (tagName.equals("int")) {
                return Integer.valueOf(childData);
            } else if (tagName.equals("boolean")) {
                return Boolean.valueOf(childData);
            } else {
                return childData;
            }
        }
    }

    private Object parseFactory(Element element) throws Exception {
        String className = element.getAttribute("class");
        String methodName = element.getAttribute("method");
        Object[] args = parseArgs(element.getChildNodes());
        Class<?>[] parameterTypes = parseParameterTypes(args);
        Method method = Class.forName(className).getMethod(methodName, parameterTypes);
        return method.invoke(null, args);
    }

    private Object parseConstruct(Element element) throws Exception {
        String className = element.getAttribute("class");
        Object[] args = parseArgs(element.getChildNodes());
        Class<?>[] parameterTypes = parseParameterTypes(args);
        Constructor<?> constructor = Class.forName(className).getConstructor(parameterTypes);
        return constructor.newInstance(args);
    }

    private Object[] parseArgs(NodeList elements) throws Exception {
        Object[] result = new Object[elements.getLength()];
        for (int i = 0; i < result.length; i++) {
            result[i] = parseObject((Element) elements.item(i));
        }
        return result;
    }

    private Class<?>[] parseParameterTypes(Object[] args) {
        Class<?>[] result = new Class<?>[args.length];
        for (int i = 0; i < result.length; i++) {
            Class<?> cl = args[i].getClass();
            result[i] = toPrimitive.get(cl);
            if (result[i] == null) {
                result[i] = cl;
            }
        }
        return result;
    }
}
