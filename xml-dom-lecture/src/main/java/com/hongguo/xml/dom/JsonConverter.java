package com.hongguo.xml.dom;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JsonConverter {

    private static final Map<Character, String> REPLACEMENTS = new HashMap<>();

    static {
        REPLACEMENTS.put('\b', "\\b");
        REPLACEMENTS.put('\f', "\\f");
        REPLACEMENTS.put('\n', "\\n");
        REPLACEMENTS.put('\r', "\\r");
        REPLACEMENTS.put('\t', "\\t");
        REPLACEMENTS.put('"', "\\\"");
        REPLACEMENTS.put('\\', "\\\\");
    }

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = JsonConverter.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = builder.parse(inputStream);
        Element root = document.getDocumentElement();
        System.out.println(convert(root, 0));
    }

    public static StringBuilder convert(Node node, int level) {
        if (node instanceof Element) {
            return elementObject((Element) node, level);
        } else if (node instanceof CharacterData) {
            return characterString((CharacterData) node, level);
        } else {
            return pad(new StringBuilder(), level).append(jsonEscape(node.getClass().getName()));
        }
    }

    private static StringBuilder jsonEscape(String str) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String replacement = REPLACEMENTS.get(ch);
            if (replacement == null) {
                result.append(ch);
            } else {
                result.append(replacement);
            }
        }
        result.append("\"");
        return result;
    }

    private static StringBuilder characterString(CharacterData node, int level) {
        StringBuilder result = new StringBuilder();
        StringBuilder data = jsonEscape(node.getData());
        pad(result, level).append(data);
        return result;
    }

    private static StringBuilder elementObject(Element elem, int level) {
        StringBuilder result = new StringBuilder();
        pad(result, level).append("{\n");
        pad(result, level + 1).append("\"name\":");
        result.append(jsonEscape(elem.getTagName()));

        NamedNodeMap attrs = elem.getAttributes();
        if (attrs.getLength() > 1) {
            pad(result.append(",\n"), level + 1).append("\"attributes\":");
            result.append(attributeObject(attrs));
        }

        NodeList childNodes = elem.getChildNodes();
        if (childNodes.getLength() > 0) {
            pad(result.append(",\n"), level + 1).append("\"children\":[\n");
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (i > 0) {
                    result.append(",\n");
                }
                result.append(convert(childNodes.item(i), level + 2));
            }
            result.append("\n");
            pad(result, level + 1).append("]\n");
        }
        pad(result, level).append("}");
        return result;
    }

    private static StringBuilder pad(StringBuilder builder, int level) {
        for (int i = 0; i < level; i++) {
            builder.append("  ");
        }
        return builder;
    }

    private static StringBuilder attributeObject(NamedNodeMap attrs) {
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < attrs.getLength(); i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(jsonEscape(attrs.item(i).getNodeName()));
            result.append(": ");
            result.append(jsonEscape(attrs.item(i).getNodeValue()));
        }
        result.append("}");
        return result;
    }
}
