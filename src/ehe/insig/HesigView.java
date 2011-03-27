package ehe.insig;

import java.util.List;

import javax.swing.SwingUtilities;

import ehe.insig.dataModel.HeisigItem;
import ehe.insig.io.HTMLKanjiDetailsGenerator;
import ehe.insig.io.HesigDataReader;
import ehe.insig.ui.ViewAll;

public class HesigView {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HesigDataReader hesigDataReader = new HesigDataReader(null);
				List<HeisigItem> kanji = hesigDataReader.readCoreData();
				HTMLKanjiDetailsGenerator htmlKanjiDetailsGenerator = new HTMLKanjiDetailsGenerator(kanji);
				
				//do not generate html files... atm
				htmlKanjiDetailsGenerator.generateSummaryHTMLPages();
				htmlKanjiDetailsGenerator.generateDetailHTMLPages();
				
				System.out.println(hesigDataReader.toString());
				ViewAll inst = new ViewAll(kanji);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

}
