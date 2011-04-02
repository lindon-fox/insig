package ehe.insig.dataModel;

import java.util.ArrayList;
import java.util.List;

import ehe.insig.dataModel.HeisigItem.KeywordWithVersionsNumbers;

public class HeisigItemMerge {
	/**
	 * This method is used to merge two heisigItems with two halfs of
	 * information about the one entry. For example, one entry might have the
	 * primitives and the other might have the stroke count.
	 * 
	 * @param preferedItem
	 * @param alternateItem
	 * @return a merged HeisigItem. This method will try and merge the two items
	 *         into one. Some properties must match:
	 *         <ul>
	 *         <li>HeisigIndex</li>
	 *         <li>Kanji</li>
	 *         </ul>
	 *         If these do not match, then an error will be printed and only one
	 *         of the two entries will be used (from the preferred item). The
	 *         rest of the properties will be merged together loosely. If one is
	 *         empty, the other will be used.
	 * 
	 */
	public static HeisigItem merge(HeisigItem preferedItem,
			HeisigItem alternateItem) {
		HeisigItem mergedItem = null;
		//test critical properties
		String heisigIndex = null;
		String kanji = null;
		testCriticalProperty(preferedItem.getHeisigIndex(), alternateItem
				.getHeisigIndex());
		heisigIndex = preferedItem.getHeisigIndex();
		testCriticalProperty(preferedItem.getKanji(), alternateItem.getKanji());
		kanji = preferedItem.getKanji();
		//test the non-critical properties
		//do the easy ones first
		int lessonNumber;
		int indexOrdinal;
		int kanjiRanking;
		int kanjiStrokeCount;
		lessonNumber = mergeNonCriticalProperty("lesson number", preferedItem.getLessonNumber(),
				alternateItem.getLessonNumber());
		indexOrdinal = mergeNonCriticalProperty("index ordinal", preferedItem.getIndexOrdinal(),
				alternateItem.getIndexOrdinal());
		kanjiRanking = mergeNonCriticalProperty("kanji ranking" , preferedItem.getKanjiRanking(),
				alternateItem.getKanjiRanking());
		kanjiStrokeCount = mergeNonCriticalProperty("kanji stroke count", preferedItem
				.getKanjiStrokeCount(), alternateItem.getKanjiStrokeCount());
		//next do the difficult ones...
		//the keywords and the primitives
		List<KeywordWithVersionsNumbers> keywords = new ArrayList<KeywordWithVersionsNumbers>();
		List<String> primitives = new ArrayList<String>();

		List<KeywordWithVersionsNumbers> preferredKeywordList = preferedItem
				.geKeywords();
		List<KeywordWithVersionsNumbers> alternateKeywordList = alternateItem
				.geKeywords();
		List<String> preferedPrimitiveList = preferedItem
				.getKanjiPrimitiveList();
		List<String> alternatePrimitiveList = alternateItem
				.getKanjiPrimitiveList();

		//add all the keywords from the prefered source to the merged
		for (KeywordWithVersionsNumbers keyword : preferredKeywordList) {
			keywords.add(keyword);
		}
		//add the remaing keywords and versions
		for (KeywordWithVersionsNumbers secondKeywordVersions : alternateKeywordList) {
			String compareKeyword = secondKeywordVersions.getKeyword();
			boolean containsKeyword = false;
			for (KeywordWithVersionsNumbers keywordVersions : keywords) {
				if (keywordVersions.getKeyword().equals(compareKeyword)) {
					containsKeyword = true;
					//check that all the version are represented.
					for (Integer versionNumber : secondKeywordVersions
							.getVersions()) {
						if (keywordVersions.getVersions().contains(
								versionNumber) == false) {
							//then the version number is not there, so add it
							keywordVersions.addVersionNumber(versionNumber);
						}
					}
				}
			}
			if (containsKeyword == false) {
				//then add this keywordversion
				keywords.add(secondKeywordVersions);
			}
		}

		//check for clashes between the two mergeing lists...
		compareListForVersionClashes(preferedItem, alternateItem);

		//now add the primitives from the prefered list
		for (String primitive : preferedPrimitiveList) {
			primitives.add(primitive);
		}
		//now add the primitive from the alternate list, if they don't already exist
		for (String primitive : alternatePrimitiveList) {
			if(primitives.contains(primitive) == false){
				primitives.add(primitive);
			}
		}

		mergedItem = new HeisigItem(heisigIndex, kanji, kanjiStrokeCount, indexOrdinal, lessonNumber);
		mergedItem.setKanjiRanking(kanjiRanking);
		for (String primitive : primitives) {
			mergedItem.addKanjiPrimitive(primitive);
		}
		for (KeywordWithVersionsNumbers keywordWithVersionsNumbers : keywords) {
			String keyword = keywordWithVersionsNumbers.getKeyword();
			for (Integer version : keywordWithVersionsNumbers.getVersions()) {
				mergedItem.addOrReplaceKeyword(version, keyword);
			}
		}
		return mergedItem;
	}
	
	
	/**
	 * Compares the two version/keyword entries to make sure that corresponding versions do not clash
	 * @param firstItem
	 * @param secondItem
	 */
	private static void compareListForVersionClashes(HeisigItem firstItem,
			HeisigItem secondItem) {
		for (Integer versionNumber : firstItem.getKeywordVersionNumbers()) {
			KeywordWithVersionsNumbers preferedKeywordWithVersionsNumbers = firstItem
					.getKeywordForVersionNumber(versionNumber);
			KeywordWithVersionsNumbers alternateKeywordWithVersionsNumbers = secondItem
					.getKeywordForVersionNumber(versionNumber);
			if (preferedKeywordWithVersionsNumbers != null
					&& alternateKeywordWithVersionsNumbers != null) {
				if (!preferedKeywordWithVersionsNumbers.getKeyword().equals(
						alternateKeywordWithVersionsNumbers.getKeyword())) {
					System.err.println("There was a clash with the keywords ("
							+ firstItem
									.getKeywordForVersionNumber(versionNumber).getKeyword()
							+ ") for a version number: " + versionNumber);
				}
			}
		}
	}
	private static int mergeNonCriticalProperty(String propertyName, int preferedPropertyValue,
			int secondPropertyValue) {
		int propertyValue = -1;
		if (preferedPropertyValue == secondPropertyValue) {
			propertyValue = preferedPropertyValue;
		} else if (preferedPropertyValue == -1) {
			propertyValue = secondPropertyValue;
		} else if (secondPropertyValue == -1) {
			propertyValue = preferedPropertyValue;
		} else {
			//worth reporting... later
			System.out.println("unresolvable disparity with the properties ("+propertyName+"): "
					+ preferedPropertyValue + " vs " + secondPropertyValue);
			propertyValue = preferedPropertyValue;
		}
		return propertyValue;
	}
	private static void testCriticalProperty(String preferedProperty,
			String secondPreoprty) {
		if (preferedProperty.equals(secondPreoprty) == false) {
			System.err
					.println("Was expecting the two properties to be equal, but they were not: "
							+ preferedProperty + " vs " + secondPreoprty);
		}
	}
}
