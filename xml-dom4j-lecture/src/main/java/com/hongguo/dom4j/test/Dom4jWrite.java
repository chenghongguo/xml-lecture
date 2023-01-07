package com.hongguo.dom4j.test;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class Dom4jWrite {
    public static void main(String[] args) throws Exception {
        DocumentFactory factory = new DocumentFactory();
        Document document = factory.createDocument(StandardCharsets.UTF_8.name());

        document.addProcessingInstruction("hongguo", "website=\"http://www.baidu.com\"");

        Element root = document.addElement("书籍列表");
        for (int i = 0; i < 4; i++) {
            Element pcBook = root.addElement("计算机书籍");
            pcBook.addAttribute("isbn", Math.round(Math.random() * 1000) + "");
            Element name = pcBook.addElement("书名");
            name.setText("书籍" + i);
            Element price = pcBook.addElement("价格");
            price.setText(i * 10 + "");
        }

        // 定义输出格式
        OutputFormat format = new OutputFormat("    ", true, StandardCharsets.UTF_8.name());
        FileWriter fw = new FileWriter("book2.xml");

        // 定义XMLWriter对象
        XMLWriter writer = new XMLWriter(fw, format);
        writer.write(document);
        fw.close();
    }
}
