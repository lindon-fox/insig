package ehe.insig.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
			this.coreDataPath = "./src/ehe/insig/data/heisig-data.csv";

		} else {
			this.coreDataPath = coreDataPath;
		}
	}

	public List<String> getProblems() {
		return problems;
	}

	public String getCoreDataPath() {
		return coreDataPath;
	}

	public List<HeisigItem> readCoreData() {
		List<HeisigItem> kanji = null;
		problems = new ArrayList<String>();
		assert this.coreDataPath != null;
		FileInputStream fileInputStream = null;
		DataInputStream dataInputStream = null;
		try {
			System.out.println("creating inputdata");
			File inputData = new File(coreDataPath);
			System.out.println("Created input data.");
			if (inputData.exists() == false) {
				System.out.println("maybe we are in a jar file...");
				InputStream is = getClass().getResourceAsStream(
						"/ehe/insig/data/heisig-data.csv");
				System.out.println("go input stream" + is);
				dataInputStream = new DataInputStream(is);
				System.out.println("got data input stream." + dataInputStream);
//				System.out.println("Could not find the file path: \nAbsolute: "
//						+ inputData.getAbsolutePath() + "\ngetCanonicalPath: "
//						+ inputData.getCanonicalPath() + "\ngetName: "
//						+ inputData.getName());
//				return null;
			} else {
				fileInputStream = new FileInputStream(coreDataPath);
				dataInputStream = new DataInputStream(fileInputStream);

			}
			System.out.println("Checked existance...");
			// Get the object of DataInputStream
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream, Charset.forName("UTF-8")));
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
			System.err.println("There was a problem! \n" + e.toString());
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
		String heisigIndex;
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
		heisigIndex = entry;
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
		heisigItem = new HeisigItem(heisigIndex, kanji, kanjiStrokeCount,
				indexOrdinal, lessonNumber);
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