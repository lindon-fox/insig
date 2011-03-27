package ehe.insig.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author dovescrywolf
  * Next steps:
  * 1. Make this object capable of handling multiple versions of heisig. -done
  * 2. add the primitive elements.
 */
public class HeisigItem {
	protected String heisigIndex;
	protected int insigIndex;
	protected String kanji;
	protected List<KeywordVersions> keywords;
	protected int kanjiStrokeCount;
	protected int indexOrdinal; //not sure what this represents. Can't see how it relates to the index.
	protected int lessonNumber;
	
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
	
	public HeisigItem(String heisigIndex, String kanji, 
			int kanjiStrokeCount, int indexOrdinal, int lessonNumber) {
		super();
		this.heisigIndex = heisigIndex;
		this.kanji = kanji;
		this.kanjiStrokeCount = kanjiStrokeCount;
		this.indexOrdinal = indexOrdinal;
		this.lessonNumber = lessonNumber;
		this.keywords = new ArrayList<KeywordVersions>();
	}
	public String getHeisigIndex() {
		return heisigIndex;
	}
	public void setHeisigIndex(String heisigNumber) {
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
	
	/**
	 * this method will add a new keyword for the version specified
	 * If there is already a keyword for that version, it will be replaced.
	 * @param version
	 * @param keyword
	 */
	public void addOrReplaceKeyword(int version, String keyword){
		for (KeywordVersions keywordVersion : keywords) {
			if(keywordVersion.getKeyword().equals(keyword)){
				keywordVersion.addVersionNumber(version);
				return;
			}
		}
		KeywordVersions newKeywordVersions = new KeywordVersions(keyword, version);
		keywords.add(newKeywordVersions);
	}
	
	public String getKeywords(){
		return getFormattedKeywords();
	}

	@Override
	public String toString() {
			return getKanji() + " = " + getFormattedKeywords() + " [ "+ getKanjiStrokeCount()+" ] {" + getHeisigIndex() + "} <" + getLessonNumber() + ">";
	}
	/**
	 * This method returns the keyword(s) of the item. If all the version have the same keyword, then
	 * it will only return one word. It will only return the minimal amount of keywords. It will provide details of version number if it needs to return more than one keyword.
	 * 
	 * @return
	 */
	private String getFormattedKeywords() {
		if(keywords.size() == 1){
			//then there is only one keyword for all the versions, so can just show
			return this.keywords.get(0).keyword;
		}
		if(keywords.size() == 0){
			return "<no keyword set!>";
		}
		//if we are here, then there should be more than one keyword
		StringBuilder stringBuilder= new StringBuilder(); 
		for (KeywordVersions keywordVersion : this.keywords) {
			stringBuilder.append(keywordVersion.keyword);
			stringBuilder.append(" (Ver");
			for (Integer versionNumber : keywordVersion.getVersions()) {
				stringBuilder.append(", ");
				stringBuilder.append(versionNumber);
			}
			stringBuilder.append("). ");
		}
		return stringBuilder.toString();
	}
	class KeywordVersions{

		public String keyword;
		public List<Integer> versions;
		
		public KeywordVersions(String keyword, int version){
			this.keyword = keyword;
			versions = new ArrayList<Integer>();
			versions.add(version);
		}
		
		public String getKeyword() {
			return keyword;
		}
		
		public void addVersionNumber(int version){
			versions.add(version);
		}
		public List<Integer> getVersions(){
			return versions;
		}
	}
	/**
	 * 
	 * @return a nicely formatted summary that is useful for headings
	 */
	public String getHeadingSummary() {
		return this.getKeywords() + " " + this.getKanji() + " {#" + this.getHeisigIndex() + "}";
	}
	public String getStory() {
		return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc sagittis sem sit amet urna commodo eget vestibulum nunc elementum. Donec et libero enim, eget tempus nunc. Aliquam non sem augue, a auctor metus. Aliquam vitae lectus turpis, non vestibulum enim. Cras suscipit vulputate sapien, et vulputate nisl tempor eu. Nam eu mi tellus. Etiam ut ante nulla. Maecenas placerat fermentum sodales. ";
	}
	
}
