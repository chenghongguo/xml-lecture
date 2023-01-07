package com.hongguo.dom4j.test;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Dom4jUpdate {
    public static void main(String[] args) throws Exception {
        SAXReader reader = new SAXReader();
        reader.setStripWhitespaceText(true);
        reader.setMergeAdjacentText(true);

//        InputStream resource = Dom4jRead.class.getClassLoader().getResourceAsStream("inventory.xml");
        Document document = reader.read(Files.newInputStream(Paths.get("book2.xml")));

        Element root = document.getRootElement();
        Element pcBook = root.addElement("计算机书籍");
        pcBook.addAttribute("isbn", Math.round(Math.random() * 1000) + "");
        Element name = pcBook.addElement("书名");
        name.setText("疯狂Java讲义");
        Element price = pcBook.addElement("价格");
        price.setText("99.00");

        // 定义输出格式
        OutputFormat format = new OutputFormat("    ", true, StandardCharsets.UTF_8.name());
        FileWriter fw = new FileWriter("book.xml");

        // 定义XMLWriter对象
        XMLWriter writer = new XMLWriter(fw, format);
        writer.write(document);
        fw.close();
    }
}
