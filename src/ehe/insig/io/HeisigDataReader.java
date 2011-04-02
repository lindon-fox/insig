package ehe.insig.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class HeisigDataReader {

	private static final String coreDataResourcePath = "/ehe/insig/data/heisig-data.csv";
	protected String coreDataFilePath;
	protected List<String> problems;

	public HeisigDataReader(String coreDataPath) {
		if (coreDataPath == null) {
			this.coreDataFilePath = "./src/ehe/insig/data/heisig-data.csv";

		} else {
			this.coreDataFilePath = coreDataPath;
		}
	}

	public List<String> getProblems() {
		return problems;
	}

	public String getCoreDataPath() {
		return coreDataFilePath;
	}

	/**
	 * This method take a new-line delimited file and turnes it into a csv file.
	 * The format expected is: Kanji Frame Keyword Parts Rank
	 * 
	 * @param pathIn
	 * @param pathOut
	 */
	public List<HeisigItem> readListFormatCoreData(String pathIn,
			String resourcePathIn) {
		List<HeisigItem> kanjiList = null;
		DataInputStream dataInputStream = null;
		try {
			InputStream inputStream = getInputStream(resourcePathIn, pathIn);
			dataInputStream = new DataInputStream(inputStream);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream, Charset
							.forName("UTF-8")));
			String line;
			kanjiList = new ArrayList<HeisigItem>();
			//read off the header:

			line = bufferedReader.readLine();
			System.out.println(line);
			line = bufferedReader.readLine();
			System.out.println(line);
			line = bufferedReader.readLine();
			System.out.println(line);
			line = bufferedReader.readLine();
			System.out.println(line);
			line = bufferedReader.readLine();
			System.out.println(line);

			while ((line = bufferedReader.readLine()) != null) {
				String kanji = line;
				line = bufferedReader.readLine();
				String frameString = line;
				int frame;
				if (frameString == null || frameString.equals("")) {
					frame = -1;
				} else {
					try {
						frame = Integer.parseInt(frameString);
					} catch (NumberFormatException e) {
						System.err
								.println("was expecting a number for the index, but that is not what i got: "
										+ frameString);
						frame = -1;
					}
				}
				line = bufferedReader.readLine();
				String keyword = line;
				line = bufferedReader.readLine();
				String primitives = line;
				line = bufferedReader.readLine();
				String rank = line;
				HeisigItem item = new HeisigItem(frame, kanji, -1, -1, -1);
				item.addOrReplaceKeyword(2, keyword);//two is a guess - it may be wrong... I guessed three originally, but then some differences started showing up in the data sets - some of that could have just been human error, but then found some clear differences...
				if (rank == null || rank.equals("")) {
					item.setKanjiRanking(-1);
				} else {
					try {

						item.setKanjiRanking(Integer.parseInt(rank));
					} catch (NumberFormatException nfe) {
						System.err
								.println("The rank was expected as a number; "
										+ rank);
					}
				}
				String[] tokens = primitives.split(", ");
				for (int i = 0; i < tokens.length; i++) {
					String token = tokens[i];
					item.addKanjiPrimitive(token);
				}
				kanjiList.add(item);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kanjiList;
	}

	public List<HeisigItem> readCoreData() {
		List<HeisigItem> kanji = null;
		problems = new ArrayList<String>();
		assert this.coreDataFilePath != null;
		assert this.coreDataResourcePath != null;
		DataInputStream dataInputStream = null;
		try {
			InputStream inputStream = getInputStream();
			dataInputStream = new DataInputStream(inputStream);

			// Get the object of DataInputStream
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream, Charset
							.forName("UTF-8")));
			String line;
			kanji = new ArrayList<HeisigItem>();

			while ((line = bufferedReader.readLine()) != null) {
				HeisigItem heisigItem = processLine(line);
				if (heisigItem != null) {
					kanji.add(heisigItem);
				}
			}
		} catch (Exception e) {
			problems
					.add("There was a problem that I don't know what to do with. Here are some details that may or may not be useful; "
							+ e.toString());
			System.err.println("There was a problem! \n" + e.toString());
		} finally {
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

	/**
	 * gets the input stream for the core data.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getInputStream() throws FileNotFoundException {
		return getInputStream(coreDataResourcePath, coreDataFilePath);
	}

	/**
	 * @param resourcePath
	 * @return an input stream from the file system or from a jar file
	 * @throws FileNotFoundException
	 */
	private InputStream getInputStream(String resourcePath, String filePath)
			throws FileNotFoundException {
		InputStream inputStream = null;
		System.out.println("creating inputdata");
		File inputData = new File(filePath);
		System.out.println("Created input data.");
		if (inputData.exists() == false) {
			System.out.println("maybe we are in a jar file...");
			inputStream = getClass().getResourceAsStream(resourcePath);
			System.out.println("go input stream" + inputStream);
			if (inputStream == null) {
				System.err
						.println("Could not find the resoure in the jar file: "
								+ coreDataResourcePath);
			}
		} else {
			inputStream = new FileInputStream(filePath);
		}
		return inputStream;
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
		if (entry == null || entry.equals("")) {
			heisigIndex = -1;
		} else {
			try {

				heisigIndex = Integer.parseInt(entry);
			} catch (NumberFormatException e) {
				System.err
						.println("Was expecting a numbwe doe rhw heisig index, but I got this instead: "
								+ entry);
				heisigIndex = -1;
			}
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
		return coreDataFilePath;
	}
}