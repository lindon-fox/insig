package ehe.insig.test;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableTest extends JFrame {

	private Object[] columnNames = {"Col1", "Col2", "Col3"};
	private Object[][] rowData = {
		{"Abc", "Def", "Ghi"}, {"Jkl", "Mno", "Pqr"}, {"Stu", "Vwx", "Yz"}
	};
	private Font customFont = new Font("Helvetica Bold", Font.PLAIN, 28);
	
	public TableTest() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		add(wrappedTable("using setFont, non-derived", false, false, false));
		add(wrappedTable("using getFont, non-derived", true, false, false));
		add(wrappedTable("using setFont, derived (int)", false, true, false));
		add(wrappedTable("using getFont, derived (int)", true, true, false));
		add(wrappedTable("using setFont, derived (float)", false, true, true));
		add(wrappedTable("using getFont, derived (float)", true, true, true));
		pack();
	}

	private JPanel wrappedTable(String title, boolean useGetFont,
			boolean deriveNewFont, boolean castToFloat) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(title));
		panel.add(getTable(useGetFont, deriveNewFont, castToFloat));
		return panel;
		
	}
	private JTable getTable(boolean useGetFont, boolean deriveNewFont, 
			boolean castToFloat) {
		JTable table = new JTable(rowData, columnNames);
		FontMetrics metrics = table.getFontMetrics(customFont);
		table.setRowHeight(metrics.getHeight()); // set row height to match font
		CellRenderer cellRenderer =	new CellRenderer(customFont, useGetFont,
				deriveNewFont, castToFloat);
		TableColumnModel columnModel = table.getColumnModel();
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(cellRenderer);
		}
		return table;
	}

    public static void main(String[] args) {
    	TableTest test = new TableTest();
    	test.setVisible(true);
    }
	
	public static class CellRenderer extends DefaultTableCellRenderer {

		private Font customFont;
	    private boolean overrideGetFont;
	    private boolean deriveNewFont;
	    private boolean castToFloat;
	    
		public CellRenderer(Font customFont, boolean overrideGetFont,
				boolean deriveNewFont, boolean castToFloat) {
			this.customFont = customFont;
			this.overrideGetFont = overrideGetFont;
			this.deriveNewFont = deriveNewFont;
			this.castToFloat = castToFloat;
		}

		public Font getFont() {
			if (!overrideGetFont) {
				return super.getFont();
			}
			if (deriveNewFont) {
				Font oldFont = super.getFont();
				int fontSize = customFont.getSize();
				if (castToFloat) {
					return oldFont.deriveFont((float) fontSize);
				}
				else {
					// Here's the problem I tripped over...
					return oldFont.deriveFont(fontSize);
				}
			}
			else {
				return customFont;
			}
		}

		public Component getTableCellRendererComponent(	JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table,	value,
					isSelected, hasFocus, row, column);
			if (!overrideGetFont) {
				if (deriveNewFont) {
					Font oldFont = super.getFont();
					int fontSize = customFont.getSize();
					Font newFont = null;
					if (castToFloat) {
						newFont = oldFont.deriveFont((float) fontSize);
					}
					else {
						// Here's the problem I tripped over...
						newFont = oldFont.deriveFont(fontSize);
					}
					setFont(newFont);
				}
				else {
					setFont(customFont);
				}
			}
			return this;
		}
	}
}