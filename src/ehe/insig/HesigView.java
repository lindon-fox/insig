package ehe.insig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ehe.insig.dataModel.HeisigItem;
import ehe.insig.io.HTMLKanjiDetailsGenerator;
import ehe.insig.io.HeisigDataReader;
import ehe.insig.io.HeisigDataWriter;
import ehe.insig.ui.ViewAll;

public class HesigView {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HeisigDataReader hesigDataReader = new HeisigDataReader(null);
				List<HeisigItem> kanji = hesigDataReader.readCoreData();
				//				HTMLKanjiDetailsGenerator htmlKanjiDetailsGenerator = new HTMLKanjiDetailsGenerator(kanji);

				//do not generate html files... atm... later this can be done through a direct command. 
				//				htmlKanjiDetailsGenerator.generateSummaryHTMLPages();
				//				htmlKanjiDetailsGenerator.generateDetailHTMLPages();

				//				System.out.println(hesigDataReader.toString());

				//test
				
				
				
				List<HeisigItem> tempList = hesigDataReader.readListFormatCoreData("/Users/" + System.getProperty("user.name") + "/Documents/Japanese/Kanji with primitives.txt", "");
				
				HeisigDataWriter heisigDataWriter = new HeisigDataWriter();
				try {
					heisigDataWriter.writeHeisigData("/Users/" + System.getProperty("user.name") + "/Documents/Notes/Japanese/workspace/Insig/output/compare.csv", tempList);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if (kanji == null) {
					JOptionPane.showMessageDialog(null,
							"Coult not read the data for the kanji." +
							"\nProbably there is an error in the program, but maybe you can fix it if you know where the program is looking for the file.\n" +
							"Here is the location: \n" + hesigDataReader.getCoreDataPath() +"\nTo see more details of the problem,\n" +
									"run the program from a command prompt, 'java -jar insig.jar'");
				} else {
					ViewAll inst = new ViewAll(kanji);
					inst.setLocationRelativeTo(null);
					inst.setVisible(true);
				}
			}
		});
	}

}
