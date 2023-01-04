package com.hongguo.xml.dom;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StAXTest {
    public static void main(String[] args) throws Exception {
        InputStream in = Files.newInputStream(Paths.get("xml-dom-lecture/student.xml"));
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                System.out.println(reader.getLocalName() + ", " + reader.getName() + ", " + reader.getAttributeValue(0));
            }
        }
    }
}
