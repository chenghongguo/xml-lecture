package com.hongguo.xml.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class XmlWriterTest {

    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
        Document doc = newDrawing(600, 400);
        writeDoc(doc, "drawing.svg");
        writeNewDoc(600, 400, "drawing2.svg");
    }

    private static void writeNewDoc(int width, int height, String filename) throws Exception {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(Files.newOutputStream(Paths.get(filename)), "UTF-8");
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeDTD("<!DOCTYPE svg PUBLIC \"-//W3c//DTD SVG 20000802//EN\" \"https://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd\">");
        writer.writeStartElement("svg");
        writer.setDefaultNamespace("http://www.w3.org/2000/svg");
        writer.writeAttribute("width", width + "");
        writer.writeAttribute("height", height + "");
        int n = 10 + random.nextInt(20);
        for (int i = 1; i <= n; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int w = random.nextInt(width - x);
            int h = random.nextInt(height - y);
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);

            writer.writeEmptyElement("rect");
            writer.writeAttribute("x", "" + x);
            writer.writeAttribute("y", "" + y);
            writer.writeAttribute("width", "" + w);
            writer.writeAttribute("height", "" + h);
            writer.writeAttribute("fill", String.format("#%02x%02x%02x", r, g, b));
        }
        writer.writeEndDocument();
        writer.close();
    }

    /**
     * Saves a document using DOM/XLST
     */
    private static void writeDoc(Document document, String filename) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                "-//W3C//DTD SVG 20000802//EN");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xlst}indent-amount", "2");
        transformer.transform(new DOMSource(document), new StreamResult(Files.newOutputStream(Paths.get(filename))));
    }

    private static Document newDrawing(int width, int height) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        String ns = "http://www.w3.org/2000/svg";
        Document document = builder.newDocument();
        Element svgEle = document.createElementNS(ns, "svg");
        document.appendChild(svgEle);
        svgEle.setAttribute("width", "" + width);
        svgEle.setAttribute("height", "" + height);
        int n = 10 + random.nextInt(20);
        for (int i = 1; i <= n; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int w = random.nextInt(width - x);
            int h = random.nextInt(height - y);
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);

            Element rectEle = document.createElementNS(ns, "rect");
            rectEle.setAttribute("x", "" + x);
            rectEle.setAttribute("y", "" + y);
            rectEle.setAttribute("width", "" + w);
            rectEle.setAttribute("height", "" + h);
            rectEle.setAttribute("fill", String.format("#%02x%02x%02x", r, g, b));

            svgEle.appendChild(rectEle);
        }
        return document;
    }
}
