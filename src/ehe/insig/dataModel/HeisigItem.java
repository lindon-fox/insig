package ehe.insig.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lindon-fox Next steps: 1. Make this object capable of handling
 *         multiple versions of heisig. -done 2. add the primitive elements.
 */
public class HeisigItem {
	protected int heisigIndex;
	protected int insigIndex = -1;
	protected String kanji;
	protected List<KeywordWithVersionsNumbers> keywords;//this lists all the keywords and their version numbers

	protected int kanjiStrokeCount = -1;
	protected int indexOrdinal = -1; //not sure what this represents. Can't see how it relates to the index.
	protected int lessonNumber = -1;
	protected int kanjiRanking = -1;
	protected List<String> kanjiPrimitiveList;

	public HeisigItem(int heisigIndex, String kanji, int kanjiStrokeCount,
			int indexOrdinal, int lessonNumber) {
		super();
		this.heisigIndex = heisigIndex;
		this.kanji = kanji;
		this.kanjiStrokeCount = kanjiStrokeCount;
		this.indexOrdinal = indexOrdinal;
		this.lessonNumber = lessonNumber;
		this.keywords = new ArrayList<KeywordWithVersionsNumbers>();
		this.kanjiPrimitiveList = new ArrayList<String>();
	}
	public int getHeisigIndex() {
		return heisigIndex;
	}
	public void setHeisigIndex(int heisigNumber) {
		this.heisigIndex = heisigNumber;
	}
	public int getInsigIndex() {
		return insigIndex;
	}
	public void setInsigIndex(int insigIndex) {
		this.insigIndex = insigIndex;
	}
	public String getKanji() {
		return kanji;
	}
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

	public int getKanjiStrokeCount() {
		return kanjiStrokeCount;
	}
	public void setKanjiStrokeCount(int kanjiStrokeCount) {
		this.kanjiStrokeCount = kanjiStrokeCount;
	}
	public int getIndexOrdinal() {
		return indexOrdinal;
	}
	public void setIndexOrdinal(int indexOrdinal) {
		this.indexOrdinal = indexOrdinal;
	}
	public int getLessonNumber() {
		return lessonNumber;
	}
	public void setLessonNumber(int lessonNumber) {
		this.lessonNumber = lessonNumber;
	}
	public int getKanjiRanking() {
		return kanjiRanking;
	}
	public void setKanjiRanking(int kanjiRanking) {
		this.kanjiRanking = kanjiRanking;
	}

	public void addKanjiPrimitive(String kanjiPart) {
		kanjiPrimitiveList.add(kanjiPart);
	}
	/**
	 * this method will add a new keyword for the version specified If there is
	 * already a keyword for that version, it will be replaced.
	 * 
	 * @param version
	 * @param keyword
	 */
	public void addOrReplaceKeyword(int version, String keyword) {
		for (KeywordWithVersionsNumbers keywordVersion : keywords) {
			if (keywordVersion.getKeyword().equals(keyword)) {
				keywordVersion.addVersionNumber(version);
				return;
			}
		}
		KeywordWithVersionsNumbers newKeywordVersions = new KeywordWithVersionsNumbers(
				keyword, version);
		keywords.add(newKeywordVersions);
	}

	public List<String> getKanjiPrimitiveList() {
		return kanjiPrimitiveList;
	}

	public List<KeywordWithVersionsNumbers> geKeywords() {
		return keywords;
	}

	public String getKeywordsFormatted() {
		return getFormattedKeywords();
	}

	@Override
	public String toString() {
		return getKanji() + " = " + getFormattedKeywords() + " ("
				+ kanjiPrimitivesToString() + ") - " + ",{ #"
				+ getHeisigIndex() + "} [ " + getKanjiStrokeCount() + " ] <"
				+ getLessonNumber() + "> R:" + getKanjiRanking();
	}

	public String kanjiPrimitivesToString() {
		return kanjiPrimitivesToString(", ");
	}

	public String kanjiPrimitivesToString(String seperator) {
		if (kanjiPrimitiveList.size() == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean firstPass = true;
		for (String part : kanjiPrimitiveList) {
			if (firstPass == true) {
				firstPass = false;
			} else {
				builder.append(seperator);
			}
			builder.append(part);
		}
		return builder.toString();
	}
	/**
	 * This method returns the keyword(s) of the item. If all the version have
	 * the same keyword, then it will only return one word. It will only return
	 * the minimal amount of keywords. It will provide details of version number
	 * if it needs to return more than one keyword.
	 * 
	 * @return
	 */
	private String getFormattedKeywords() {
		if (keywords.size() == 1) {
			//then there is only one keyword for all the versions, so can just show
			return this.keywords.get(0).keyword;
		}
		if (keywords.size() == 0) {
			return "<no keyword set!>";
		}
		//if we are here, then there should be more than one keyword
		StringBuilder stringBuilder = new StringBuilder();
		for (KeywordWithVersionsNumbers keywordVersion : this.keywords) {
			stringBuilder.append(keywordVersion.keyword);
			stringBuilder.append(" (v");
			boolean firstPass = true;
			for (Integer versionNumber : keywordVersion.getVersions()) {
				if (firstPass == true) {
					firstPass = false;
				} else {
					stringBuilder.append(", ");
				}
				stringBuilder.append(versionNumber);
			}
			stringBuilder.append("). ");
		}
		return stringBuilder.toString();
	}

	/**
	 * @return a formatted list to display as one string.
	 */
	public String getPrimitivesFormatted() {
		StringBuilder stringBuilder = new StringBuilder();
		boolean firstPass = true;
		for (String primitive : kanjiPrimitiveList) {
			if(firstPass == true){
				firstPass = false;
			}
			else{
				stringBuilder.append(", ");
			}
			stringBuilder.append(primitive);
		}
		return stringBuilder.toString();
	}

	
	/**
	 * @author lindon-fox
	 * 
	 */
	class KeywordWithVersionsNumbers {

		public String keyword;
		public List<Integer> versions;

		public KeywordWithVersionsNumbers(String keyword, int version) {
			this.keyword = keyword;
			versions = new ArrayList<Integer>();
			versions.add(version);
		}

		public String getKeyword() {
			return keyword;
		}

		public void addVersionNumber(int version) {
			versions.add(version);
		}
		public List<Integer> getVersions() {
			return versions;
		}
	}
	/**
	 * 
	 * @return a nicely formatted summary that is useful for headings
	 */
	public String getHeadingSummary() {
		return this.getKeywordsFormatted() + " " + this.getKanji() + " {#"
				+ this.getHeisigIndex() + "}";
	}
	public String getStory() {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sagittis sem sit amet urna commodo eget vestibulum nunc elementum. Donec et libero enim, eget tempus nunc. Aliquam non sem augue, a auctor metus. Aliquam vitae lectus turpis, non vestibulum enim. Cras suscipit vulputate sapien, et vulputate nisl tempor eu. Nam eu mi tellus. Etiam ut ante nulla. Maecenas placerat fermentum sodales. ";
	}
	public String keywordsToString(String seperator) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean firstPass = true;
		for (KeywordWithVersionsNumbers keywordVersion : keywords) {
			if (firstPass == true) {
				firstPass = false;
			} else {
				stringBuilder.append(seperator);
			}
			boolean firstInnerPass = true;
			for (Integer version : keywordVersion.versions) {
				if (firstInnerPass == true) {
					firstInnerPass = false;
				} else {
					stringBuilder.append(seperator);
				}
				stringBuilder.append(version);
				stringBuilder.append(":");
				stringBuilder.append(keywordVersion.keyword);
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * @return a list of version numbers (relating to the books) that this item
	 *         has a keyword for
	 */
	public List<Integer> getKeywordVersionNumbers() {
		List<Integer> keywordVersionNumbers = new ArrayList<Integer>();
		for (KeywordWithVersionsNumbers keywordEntry : keywords) {
			keywordVersionNumbers.addAll(keywordEntry.getVersions());
		}
		return keywordVersionNumbers;
	}
	
	/**
	 * @param versionNumber
	 * @return the keyword for the version number. Null if not found.
	 */
	public KeywordWithVersionsNumbers getKeywordForVersionNumber(
			Integer versionNumber) {
		for (KeywordWithVersionsNumbers keywordEntry : keywords) {
			if (keywordEntry.getVersions().contains(versionNumber)) {
				return keywordEntry;
			}
		}
		//the keyword for the version number has not been found, so return null.
		return null;
	}
	
	public String getKeywordsFormattedSimply() {
		StringBuilder stringBuilder = new StringBuilder();
		boolean firstPass = true;
		for (KeywordWithVersionsNumbers keyword : keywords) {
			if(firstPass == true){
				firstPass = false;
			}
			else{
				stringBuilder.append(" / ");
			}
			stringBuilder.append(keyword.getKeyword());
		}
		return stringBuilder.toString();
	}
	
	public boolean hasMultipleKeywordVersions() {
		return keywords.size() > 1;
	}
	
	public String getKeywordKanjiSummary() {
		return this.kanji + " - " + this.getKeywordsFormattedSimply();
	}

}
