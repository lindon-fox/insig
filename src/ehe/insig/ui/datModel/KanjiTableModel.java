package ehe.insig.ui.datModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ehe.insig.dataModel.HeisigItem;

public class KanjiTableModel extends AbstractTableModel {

	public static final int KANJI_COLUMN_INDEX = 0;
	public static final int HEISIG_INDEX_COLUMN_INDEX = 1;
	public static final int KEYWORD_COLUMN_INDEX = 2;
	public static final int STROKE_COUNT_COLUMN_INDEX = 3;
	public static final int LESSON_NUMBER_COLUMN_INDEX = 4;
	
	protected List<String> columnNames;
	protected List<HeisigItem> items;//collection, maintaining order
	protected HashMap<String, HeisigItem> itemMap;//a hashmap indexed on the heisig number
	public KanjiTableModel(List<HeisigItem> items) {
		super();
		this.items = items;
		itemMap = new HashMap<String, HeisigItem>();
		for (HeisigItem item : items) {
			itemMap.put(item.getHeisigIndex(), item);
		}
		columnNames = new ArrayList<String>();
		columnNames.add("Kanji");
		columnNames.add("Index");
		columnNames.add("Keyword");
		columnNames.add("Stroke Count");
		columnNames.add("Lesson Number");
	}

	
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}
	
	@Override
	public int getRowCount() {
		if(items == null){
			return 0;
		}
		return items.size();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object item = null;
		//eventually replace this with something more generic.
		switch (columnIndex) {
		case KanjiTableModel.KANJI_COLUMN_INDEX:
			item = items.get(rowIndex).getKanji();
			break;
		case KanjiTableModel.HEISIG_INDEX_COLUMN_INDEX:
			item = items.get(rowIndex).getHeisigIndex();
			break;
		case KanjiTableModel.KEYWORD_COLUMN_INDEX:
			item = items.get(rowIndex).getKeywordsFormatted();
			break;
		case STROKE_COUNT_COLUMN_INDEX:
			item = items.get(rowIndex).getKanjiStrokeCount();
			break;
		case LESSON_NUMBER_COLUMN_INDEX:
			item = items.get(rowIndex).getLessonNumber();
			break;
		default:
			item = null;
			break;
		}
		return item;
	}


	public HeisigItem get(String heisigIndex) {
		return itemMap.get(heisigIndex);
	}

}
