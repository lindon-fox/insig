package ehe.insig.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import ehe.insig.dataModel.HeisigItem;

public class HeisigDataWriter {

	private static final String SEPERATOR = ",";
	private static final Object CLOSE_SESAME = "\"";
	private static final Object OPEN_SESAME = "\"";


	public void writeHeisigData(String path, List<HeisigItem> kanjiList) throws IOException{
		File file = new File(path);
		 if(!file.exists())
		 {
		   file.createNewFile();
		 }
		 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter
                 (new FileOutputStream(file),"UTF8"));

		 boolean firstPass = true;
		 for (HeisigItem heisigItem : kanjiList) {			
			 if(firstPass == true){
				 firstPass = false;
			 }
			 else{
					bufferedWriter.write("\n");
			 }
			bufferedWriter.write(generateFileEntry(heisigItem));
		}
		 bufferedWriter.close();
	}

	/**
	 * Index
	 * Kanji
	 * Keyword(s)
	 * primitive(s)
	 * kanji stroke count
	 * index ordinal
	 * lesson number
	 * @param heisigItem
	 * @return
	 */
	private String generateFileEntry(HeisigItem heisigItem) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.getHeisigIndex());
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.getKanji());
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.keywordsToString("~"));
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.kanjiPrimitivesToString("~"));
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.getKanjiStrokeCount());
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.getIndexOrdinal());
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		stringBuilder.append(HeisigDataWriter.SEPERATOR);
		stringBuilder.append(HeisigDataWriter.OPEN_SESAME);
		stringBuilder.append(heisigItem.getLessonNumber());
		stringBuilder.append(HeisigDataWriter.CLOSE_SESAME);
		return stringBuilder.toString();
	}
}
