package ehe.insig.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.text.html.HTMLDocument;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ehe.insig.dataModel.HeisigItem;

public class HTMLKanjiDetailsGenerator {

	protected List<HeisigItem> heisigItems;

	public HTMLKanjiDetailsGenerator(List<HeisigItem> heisigItems) {
		super();
		this.heisigItems = heisigItems;
	}

	public void generateDetailHTMLPages() {
		System.out.println("Generating html detail files");
		for (HeisigItem item : heisigItems) {
			generateDetailHTMLPage(item);
		}
		System.out.println("Finished generation.");
	}

	public void generateSummaryHTMLPages() {
		System.out.println("Generating html summary file");
		FileOutputStream fileOutputStream;
		try {
			File file = new File("./output/html/summary/heisig-items.html");
			fileOutputStream = new FileOutputStream(file);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory
					.createXMLStreamWriter(fileOutputStream);

			xml.writeStartDocument();
			for (HeisigItem item : heisigItems) {
				generateHTMLSection(item, fileOutputStream, xml);
				xml.writeEmptyElement("br");
			}

			xml.writeEndElement();//body
			xml.writeEndElement();//html
			xml.writeEndDocument();
			fileOutputStream.flush();
			fileOutputStream.close();
			//			System.out.println(file.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e);

		}
		System.out.println("Finished generation.");
	}

	private void generateDetailHTMLPage(HeisigItem item) {
		//Create the HTMLDocument that holds necessary document properties
		FileOutputStream fileOutputStream;
		try {
			File file = new File("./output/html/details/heisig-item-"
					+ item.getHeisigIndex() + ".html");
			fileOutputStream = new FileOutputStream(file);
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xml = outputFactory
					.createXMLStreamWriter(fileOutputStream);

//			xml.writeStartDocument();
			generateHTMLSection(item, fileOutputStream, xml);

			xml.writeEndElement();//body
			xml.writeEndElement();//html
			xml.writeEndDocument();
			fileOutputStream.flush();
			fileOutputStream.close();
			//			System.out.println(file.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e);

		}
	}

	private void generateHTMLSection(HeisigItem item,
			FileOutputStream fileOutputStream, XMLStreamWriter xml)
			throws XMLStreamException, IOException {

		xml.writeStartElement("html");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");

		xml.writeStartElement("head");
		xml.writeStartElement("title");
		xml.writeCharacters(item.getHeadingSummary());
		xml.writeEndElement();//title
		//<link id="ext_css" rel="stylesheet" type="text/css" href="http://skins.gmodules.com/ig/skin_xml_to_css?hl=en&amp;gl=au&amp;v2=1&amp;url=http://water-bottle.googlecode.com/svn/trunk/themes/gkac-ssa.xml&amp;skindx=ix:0&amp;fp=8GXCH9JQH70"/>
		xml.writeEmptyElement("link");
		xml.writeAttribute("REL", "STYLESHEET");
		xml.writeAttribute("type", "text/css");
		String username = System.getProperty("user.name");
		//note, this will not work on most computers...
		xml
				.writeAttribute(
						"href",
						"/Users/" + username + "/Documents/Notes/Japanese/workspace/Insig/output/insig.css");
		xml.writeEndElement();//head
		xml.writeStartElement("body");

		xml.writeStartElement("table");
		xml.writeAttribute("class", "kanji-frame-table");
		xml.writeStartElement("tr");
		xml.writeStartElement("td");//lhs for kanji, stroke count, lesson number

		xml.writeStartElement("span");//span for KANJI
		xml.writeAttribute("class", "main-kanji-span");
		xml.writeCharacters(item.getKanji());
		xml.writeEndElement();//span
		xml.writeEmptyElement("br");
		//bullet points for properties
		xml.writeStartElement("ul");
		xml.writeStartElement("li");
		xml.writeCharacters("Strokes: [" + item.getKanjiStrokeCount() + "]");
		xml.writeEndElement();//li
		xml.writeStartElement("li");
		xml.writeCharacters("Lesson: " + item.getLessonNumber());
		xml.writeEndElement();//li
		xml.writeEndElement();//ul

		xml.writeEndElement();//td
		xml.writeStartElement("td");//rhs for index number, keyword and story

		xml.writeStartElement("table");//table for keyword and index
		xml.writeAttribute("class", "keyword-index-table");
		xml.writeStartElement("tr");
		xml.writeStartElement("td");
		xml.writeCharacters("{#" + item.getHeisigIndex() + "}");
		xml.writeEndElement();//td
		xml.writeStartElement("td");
		xml.writeAttribute("class", "keyword-heading-cell");
		xml.writeStartElement("h1");
		xml.writeCharacters(item.getKeywords());
		xml.writeEndElement();//h1

		xml.writeEndElement();//td
		xml.writeEndElement();//tr
		xml.writeEndElement();//table
		//story goes here
		xml.writeCharacters(item.getStory());
		xml.writeEndElement();//td
		xml.writeEndElement();//tr
		xml.writeEndElement();//table
	}
}
