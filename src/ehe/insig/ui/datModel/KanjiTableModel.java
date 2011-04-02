package ehe.insig.ui.datModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ehe.insig.dataModel.HeisigItem;

public class KanjiTableModel extends AbstractTableModel {

	public enum ColumnModel {
		kanji(0, "Kanji"), heisigIndex(1, "Index"), keywords(2, "Keyword"), primitives(
				3, "Primitives"), strokeCount(4, "Stroke Count"), lessonNumber(
				5, "Lesson Number");

		private String dispalyName;
		private int index;

		ColumnModel(int index, String displayName) {
			this.index = index;
			this.dispalyName = displayName;
		}

		public String getDispalyName() {
			return dispalyName;
		}

		public int getIndex() {
			return index;
		}

		public static ColumnModel convertFromInt(int index) {
			for (int i = 0; i < ColumnModel.values().length; i++) {
				if (ColumnModel.values()[i].getIndex() == index) {
					return ColumnModel.values()[i];
				}
			}
			return null;
		}

		public static boolean canConvertFromInt(int index) {
			return convertFromInt(index) != null;
		}

		public Object getCorrespondingValue(HeisigItem heisigItem) {
			switch (this) {
				case kanji :
					return heisigItem.getKanji();
				case heisigIndex :
					return heisigItem.getHeisigIndex();
				case keywords :
					return heisigItem.getKeywordsFormatted();
				case primitives:
					return heisigItem.getPrimitivesFormatted();
				case strokeCount :
					return heisigItem.getKanjiStrokeCount();
				case lessonNumber :
					return heisigItem.getLessonNumber();
				default :
					throw new IllegalArgumentException(
							"The ColumnModel ("
									+ this
									+ ") is not recognised. This is a coding error. Sorry..");
			}
		}

	}

	protected List<HeisigItem> items;//collection, maintaining order
	protected HashMap<String, HeisigItem> itemMap;//a hashmap indexed on the heisig number
	public KanjiTableModel(List<HeisigItem> items) {
		super();
		this.items = items;
		itemMap = new HashMap<String, HeisigItem>();
		for (HeisigItem item : items) {
			itemMap.put(item.getHeisigIndex(), item);
		}
	}

	@Override
	public int getColumnCount() {
		return ColumnModel.values().length;
	}

	@Override
	public String getColumnName(int column) {
		return ColumnModel.convertFromInt(column).getDispalyName();
	}

	@Override
	public int getRowCount() {
		if (items == null) {
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
		ColumnModel columnModel = ColumnModel.convertFromInt(columnIndex);
		if (columnModel != null) {
			item = columnModel.getCorrespondingValue(items.get(rowIndex));
		}
		return item;
	}

	public HeisigItem get(String heisigIndex) {
		return itemMap.get(heisigIndex);
	}

}
