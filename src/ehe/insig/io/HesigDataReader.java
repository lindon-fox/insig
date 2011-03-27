package ehe.insig.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ehe.insig.dataModel.HeisigItem;

/**
 * 
 * 
 * This is the loader for the main hesig data file. The file contains the hesig
 * index number(most important), the key word meaning, the number of strokes and
 * the actual kanji. It would also be GREAT if it had the primitives that it
 * used.
 */
public class HesigDataReader {

	protected String coreDataPath;
	protected List<String> problems;

	public HesigDataReader(String coreDataPath) {
		if (coreDataPath == null) {
			this.coreDataPath = "./Data/heisig-data.csv";

		} else {
			this.coreDataPath = coreDataPath;
		}
	}

	public List<HeisigItem> readCoreData() {
		List<HeisigItem> kanji = null;
		problems = new ArrayList<String>();
		assert this.coreDataPath != null;
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		try {
			File inputData = new File(coreDataPath);
			assert inputData.exists();

			// Open the file that is the first
			// command line parameter
			fileInputStream = new FileInputStream(coreDataPath);
			// Get the object of DataInputStream
			dataInputStream = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream));
			String line;
			kanji = new ArrayList<HeisigItem>();

			while ((line = bufferedReader.readLine()) != null) {
				HeisigItem heisigItem = processLine(line);
				if (heisigItem != null) {
					kanji.add(heisigItem);
				}
			}

			for (HeisigItem heisigItem : kanji) {
				System.out.println(heisigItem.toString());
			}
		} catch (Exception e) {
			problems
					.add("There was a problem that I don't know what to do with. Here are some details that may or may not be useful; "
							+ e.toString());
			System.err.println("There was a problem!");
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dataInputStream != null) {
				try {
					dataInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (problems.size() == 1) {
			System.out.println("Parsing finished. There was one problem.");
		} else if (problems.size() == 0) {
			System.out.println("Parsing finsihed. No problems found!");
		} else {
			System.out.println("Parsing finished. There were "
					+ problems.size() + " problems.");
			for (String problem : problems) {
				System.err.println(problem);
			}
		}
		return kanji;
	}

	public HeisigItem processLine(String line) {
		HeisigItem heisigItem;
		int heisigIndex;
		String kanji;
		String keywordVersion3;
		String keywordVersion4;
		String keywordVersion5;
		int kanjiStrokeCount;
		int indexOrdinal;
		int lessonNumber;

		String[] entries = line.split(",");
		assert entries.length > 8;// there should be at least index, kanji, 3
		// versions of keyword, stroke count, index
		// ordinal and lesson number
		// /////////////////////////////////
		// key word
		// /////////////////////////////////
		String entry = entries[0];
		try {
			heisigIndex = Integer.parseInt(entry);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			problems
					.add("There was a problen when trying to parse this line:"
							+ line
							+ ". I am going to ignore this line and try and parse the rest. Try and fix this line for next time. In the mean time here are some of the details of the pronlem that you may ot may not find useful: "
							+ e.toString());
			return null;
		}
		// /////////////////////////////////
		// Kanji
		// /////////////////////////////////
		entry = entries[1];
		// want to strip quotation marks at start and end.
		entry = stripLeadingAndTrailingQuotationMark(entry);
		kanji = entry;

		// /////////////////////////////////
		// 3rd edition key word
		// /////////////////////////////////
		entry = entries[2];
		entry = stripLeadingAndTrailingQuotationMark(entry);
		keywordVersion3 = entry;

		// /////////////////////////////////
		// 4th edition key word
		// /////////////////////////////////
		entry = entries[3];
		entry = stripLeadingAndTrailingQuotationMark(entry);
		keywordVersion4 = entry;
		
		// /////////////////////////////////
		// 5th edition key word
		// /////////////////////////////////
		entry = entries[4];
		entry = stripLeadingAndTrailingQuotationMark(entry);
		keywordVersion5 = entry;
		
		// /////////////////////////////////
		// Stroke count
		// /////////////////////////////////
		entry = entries[5];
		try {
			kanjiStrokeCount = Integer.parseInt(entry);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			problems
					.add("There was a problen when trying to parse this line:"
							+ line
							+ ". I am going to ignore this line and try and parse the rest. Try and fix this line for next time. In the mean time here are some of the details of the pronlem that you may ot may not find useful: "
							+ e.toString());
			return null;
		}

		// /////////////////////////////////
		// Index ordinal
		// /////////////////////////////////
		entry = entries[6];
		try {
			indexOrdinal = Integer.parseInt(entry);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			problems
					.add("There was a problen when trying to parse this line:"
							+ line
							+ ". I am going to ignore this line and try and parse the rest. Try and fix this line for next time. In the mean time here are some of the details of the pronlem that you may ot may not find useful: "
							+ e.toString());
			return null;
		}

		// /////////////////////////////////
		// Lesson Number
		// /////////////////////////////////
		entry = entries[7];
		try {
			lessonNumber = Integer.parseInt(entry);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			problems
					.add("There was a problen when trying to parse this line:"
							+ line
							+ ". I am going to ignore this line and try and parse the rest. Try and fix this line for next time. In the mean time here are some of the details of the pronlem that you may ot may not find useful: "
							+ e.toString());
			return null;
		}
		heisigItem = new HeisigItem(heisigIndex, kanji, kanjiStrokeCount,indexOrdinal, lessonNumber);
		heisigItem.addOrReplaceKeyword(3, keywordVersion3);
		heisigItem.addOrReplaceKeyword(4, keywordVersion4);
		heisigItem.addOrReplaceKeyword(5, keywordVersion5);
		return heisigItem;
	}

	private String stripLeadingAndTrailingQuotationMark(String entry) {
		if (entry.charAt(0) == '"' && entry.charAt(entry.length() - 1) == '"') {
			entry = entry.substring(1, entry.length() - 1);
		}
		return entry;
	}

	@Override
	public String toString() {
		return coreDataPath;
	}
}