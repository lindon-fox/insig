package ehe.insig.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jdesktop.application.Application;

import ehe.insig.dataModel.HeisigItem;
import ehe.insig.ui.datModel.KanjiTableModel;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ViewAll extends javax.swing.JFrame {

	private static final String COPY_KEYWORD_TO_CLIPBOARD_TOOLTIP = "Copies a summary of the selected items onto the system clipboard (accessible through 'paste')";
	private static final String COPY_KEYWORD_SUMMARY_TO_CLIPBOARD_TEXT = "Copy keyword summary to clipboard...";
	private JScrollPane mainScrollPanel;
	private JTable dataTable;
	private JTextField searchTextField;
	private JPanel searchPanel;
	private JPanel searchButtonPanel;
	private JToggleButton keywordToggleButton;
	private JToggleButton kanjiToggleButton;
	private JToggleButton indexToggleButton;
	private TableRowSorter<KanjiTableModel> tableRowSorter;
	private List<Integer> filterIndicies;
	private boolean filterByAllFields;
	Font kanjiFont = new Font("Hiragino Mincho Pro", Font.PLAIN, 26);
	private JCheckBoxMenuItem rtk3ReadingDataSetCheckBoxMenuItem;
	private JCheckBoxMenuItem rtk3WritingDataSetCheckBoxMenuItem;
	private JCheckBoxMenuItem rtk2DataSetCheckboxMenuItem;
	private JCheckBoxMenuItem rtk1DataSetCheckBoxMenuItem;
	private JMenuItem aboutMenuItem;
	private JSeparator helpMenuSeparator;
	private JMenuItem noHelpMenuItem;
	private JMenu helpMenu;
	private JMenuItem copyKeywordSummaryToClipboardFromSelectionMenuItem;
	private JSeparator toolsSummarySeparator;
	private JMenuItem appendKeywordMeaningsMenuItem;
	private JMenu toolsMenu;
	private JMenuItem searchORMenuItem;
	private JPanel searchTextButtonPanel;
	private JTextField searchORTextField;
	private JMenu dataSetMenu;
	private JMenuItem filterHeadingPlaceholderMenuItem;
	private JCheckBoxMenuItem lessonCheckboxButtonMenuItem;
	private JSeparator navigateMenuSeparator;
	private JMenuItem searchMenuItem;
	private JMenu navigateMenu;
	private JMenuBar menuBar;
	private JToggleButton primitivesToggleButton;
	private JScrollPane jScrollPane1;
	private JTextPane detailsPrimitiveTextArea;
	private JTextArea detailsStoryTextArea;
	private JPanel detailsMainContentBodyPanel;
	private JScrollPane detailsScrollPanel;
	private JPanel detailsStrokeAndKanjiPanel;
	private JTextField detailsStrokeCountLabel;
	private JPanel detailsMainContentHeadingPanel;
	private JTextField detailsHeisigNumberLabel;
	private JTextField detailsKeywordLabel;
	Font kanjiLargeFont = new Font("Hiragino Mincho Pro", Font.PLAIN, 132);
	private JTextField detailsKanjiLabel;
	private JPanel detailsMainContentPanel;
	private JPanel detailsKanjiContentPanel;
	private JPanel detailsPanel;
	private JSplitPane summaryAndDetailsSplitPanel;
	Font romanFont = new Font("Helvetica", Font.PLAIN, 16);
	Font searchTextFieldFont = new Font("Lucida Grande", Font.PLAIN, 19);
	private KanjiTableModel kanjiTableModel;
	private JCheckBoxMenuItem kanjiCheckboxButtonMenuItem;
	private JCheckBoxMenuItem indexCheckboxButtonMenuItem;
	private JCheckBoxMenuItem wordsCheckboxButtonMenuItem;
	private JCheckBoxMenuItem partsCheckboxButtonMenuItem;
	private JCheckBoxMenuItem strokesCheckboxButtonMenuItem;

	public ViewAll(List<HeisigItem> items) {
		super("insig");
		kanjiTableModel = new KanjiTableModel(items);
		filterByAllFields = true;
		filterIndicies = new ArrayList<Integer>();
		initGUI();

		clearDetailsView();
		checkClipboard();
		searchTextField.requestFocus();
	}

	private void clearDetailsView() {
		detailsKanjiLabel.setText("");
		detailsStrokeCountLabel.setText("");
		detailsHeisigNumberLabel.setText("");
		detailsKeywordLabel.setText("");
		detailsStoryTextArea.setText("");
		detailsPrimitiveTextArea.setText("");
	}

	private void checkClipboard() {
		Transferable contents = Toolkit.getDefaultToolkit()
				.getSystemClipboard().getContents(this);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				searchTextField.setText((String) contents
						.getTransferData(DataFlavor.stringFlavor));
				updateFilter();
				if (tableRowSorter.getViewRowCount() > 0) {
					dataTable.setRowSelectionInterval(0, 0);
				}
			} catch (UnsupportedFlavorException ex) {
				//highly unlikely since we are using a standard DataFlavor two
				System.out.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.out.println(ex);
				ex.printStackTrace();
			}
		}
	}

	private void initGUI() {
		try {
			{
				this.setMinimumSize(new java.awt.Dimension(600, 500));
				{
					menuBar = new JMenuBar();
					setJMenuBar(menuBar);
					{
						navigateMenu = new JMenu();
						navigateMenu.setText("Navigate");
						menuBar.add(navigateMenu);
						navigateMenu.setName("navigateMenu");
						{
							searchMenuItem = new JMenuItem();
							searchMenuItem.setText("Search");
							navigateMenu.add(searchMenuItem);
							searchMenuItem.setName("searchMenuItem");
							searchMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											searchTextField.requestFocus();
										}
									});
							searchMenuItem.setAccelerator(KeyStroke
									.getKeyStroke(KeyEvent.VK_F, Toolkit
											.getDefaultToolkit()
											.getMenuShortcutKeyMask()));
						}
						{
							searchORMenuItem = new JMenuItem();
							navigateMenu.add(searchORMenuItem);
							searchORMenuItem.setName("searchORMenuItem");
							searchORMenuItem.setText("Search (multiple kanji)");
							searchORMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											searchORTextField.requestFocus();
										}
									});
							searchORMenuItem
									.setAccelerator(KeyStroke
											.getKeyStroke(
													KeyEvent.VK_F,
													KeyEvent.SHIFT_DOWN_MASK
															| Toolkit
																	.getDefaultToolkit()
																	.getMenuShortcutKeyMask()));
						}
						{
							navigateMenuSeparator = new JSeparator();
							navigateMenu.add(navigateMenuSeparator);
						}
						{
							filterHeadingPlaceholderMenuItem = new JMenuItem();
							navigateMenu.add(filterHeadingPlaceholderMenuItem);
							filterHeadingPlaceholderMenuItem
									.setName("filterHeadingPlaceholderMenuItem");
							filterHeadingPlaceholderMenuItem
									.setText("Filters:");
							filterHeadingPlaceholderMenuItem.setEnabled(false);
						}
						{
							//kanji
							kanjiCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(kanjiCheckboxButtonMenuItem);
							kanjiCheckboxButtonMenuItem.setText("\tkanji");
							kanjiCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_1, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
							kanjiCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											kanjiToggleButtonActionPerformed(evt);
										}
									});
							//index
							indexCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(indexCheckboxButtonMenuItem);
							indexCheckboxButtonMenuItem.setText("\tindex");
							indexCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_2, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
							indexCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											indexToggleButtonActionPerformed(evt);
										}
									});
							//words
							wordsCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(wordsCheckboxButtonMenuItem);
							wordsCheckboxButtonMenuItem.setText("\twords");
							wordsCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_3, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
							wordsCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											keywordToggleButtonActionPerformed(evt);
										}
									});
							//prats
							partsCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(partsCheckboxButtonMenuItem);
							partsCheckboxButtonMenuItem.setText("\tparts");
							partsCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_4, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
							partsCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											primitivesToggleButtonActionPerformed(evt);
										}
									});
							//strokes
							strokesCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(strokesCheckboxButtonMenuItem);
							strokesCheckboxButtonMenuItem.setText("\tstrokes");
							strokesCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_5, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
							strokesCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											strokeCountToggleButtonActionPerformed(evt);
										}
									});
							//lesson
							lessonCheckboxButtonMenuItem = new JCheckBoxMenuItem();
							navigateMenu.add(lessonCheckboxButtonMenuItem);
							lessonCheckboxButtonMenuItem
									.setName("lessonCheckboxButtonMenuItem");
							lessonCheckboxButtonMenuItem.setText("\tlesson");
							lessonCheckboxButtonMenuItem
									.addActionListener(new ActionListener() {
										public void actionPerformed(
												ActionEvent evt) {
											lessonNumberToggleButtonActionPerformed(evt);
										}
									});
							lessonCheckboxButtonMenuItem
									.setAccelerator(KeyStroke.getKeyStroke(
											KeyEvent.VK_6, Toolkit
													.getDefaultToolkit()
													.getMenuShortcutKeyMask()));
						}
					}
					{
						dataSetMenu = new JMenu();
						menuBar.add(dataSetMenu);
						dataSetMenu.setName("Data");
						dataSetMenu.setText("Data");
						{
							rtk1DataSetCheckBoxMenuItem = new JCheckBoxMenuItem();
							dataSetMenu.add(rtk1DataSetCheckBoxMenuItem);
							rtk1DataSetCheckBoxMenuItem.setName("RTK1");
							rtk1DataSetCheckBoxMenuItem.setText("RTK1");
						}
						{
							rtk2DataSetCheckboxMenuItem = new JCheckBoxMenuItem();
							dataSetMenu.add(rtk2DataSetCheckboxMenuItem);
							rtk2DataSetCheckboxMenuItem.setName("RTK2");
							rtk2DataSetCheckboxMenuItem.setText("RTK2");
						}
						{
							rtk3WritingDataSetCheckBoxMenuItem = new JCheckBoxMenuItem();
							dataSetMenu.add(rtk3WritingDataSetCheckBoxMenuItem);
							rtk3WritingDataSetCheckBoxMenuItem
									.setName("RTK3 (Writing)");
							rtk3WritingDataSetCheckBoxMenuItem
									.setText("RTK3 (Writing)");
						}
						{
							rtk3ReadingDataSetCheckBoxMenuItem = new JCheckBoxMenuItem();
							dataSetMenu.add(rtk3ReadingDataSetCheckBoxMenuItem);
							rtk3ReadingDataSetCheckBoxMenuItem
									.setName("RTK3 (Reading)");
							rtk3ReadingDataSetCheckBoxMenuItem
									.setText("RTK3 (Reading)");
						}
					}
					{
						toolsMenu = new JMenu();
						menuBar.add(toolsMenu);
						toolsMenu.setName("toolsMenu");
						toolsMenu.setText("Tools");
						{
							copyKeywordSummaryToClipboardFromSelectionMenuItem = new JMenuItem();
							copyKeywordSummaryToClipboardFromSelectionMenuItem
									.setText(COPY_KEYWORD_SUMMARY_TO_CLIPBOARD_TEXT);
							copyKeywordSummaryToClipboardFromSelectionMenuItem
									.setToolTipText(COPY_KEYWORD_TO_CLIPBOARD_TOOLTIP);
							copyKeywordSummaryToClipboardFromSelectionMenuItem
									.addActionListener(new ActionListener() {

										@Override
										public void actionPerformed(
												ActionEvent e) {
											copyKeywordSummaryToClipboardAction();
										}

									});
							toolsMenu
									.add(copyKeywordSummaryToClipboardFromSelectionMenuItem);
						}
						{
							toolsSummarySeparator = new JSeparator();
							toolsMenu.add(toolsSummarySeparator);
						}
						{
							appendKeywordMeaningsMenuItem = new JMenuItem();
							toolsMenu.add(appendKeywordMeaningsMenuItem);
							appendKeywordMeaningsMenuItem
									.setName("appendKeywordMeaningsMenuItem");
							appendKeywordMeaningsMenuItem
									.setText("Append keyword meanings to file...");
							appendKeywordMeaningsMenuItem.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									appendKeywordMeaningAction(e);
								}
							});
						}
					}
					{
						helpMenu = new JMenu();
						helpMenu.setText("Help");
						menuBar.add(helpMenu);
						{
							noHelpMenuItem = new JMenuItem();
							noHelpMenuItem.setText("This is not the help you are looking for.");
							noHelpMenuItem.setEnabled(false);
							noHelpMenuItem.setToolTipText("There be no help here.");
							helpMenu.add(noHelpMenuItem);
						}
						{
							helpMenuSeparator = new JSeparator();
							helpMenu.add(helpMenuSeparator);
						}
						{
							aboutMenuItem = new JMenuItem();
							aboutMenuItem.setText("About insig");
							aboutMenuItem.addActionListener(new ActionListener() {
								
								@Override
								public void actionPerformed(ActionEvent e) {
									aboutAction(e);
								}
							});
							helpMenu.add(aboutMenuItem);
						}
					}
				}
			}
			{
				//////////////////////////////////////////////////////////
				// The panel for the search bar and the search buttons.
				//////////////////////////////////////////////////////////
				searchPanel = new JPanel();
				BorderLayout mainPanelLayout = new BorderLayout();
				getContentPane().add(searchPanel, BorderLayout.NORTH);
				searchPanel.setLayout(mainPanelLayout);
				searchPanel.setPreferredSize(new java.awt.Dimension(900, 35));
				{
					searchButtonPanel = new JPanel();
					FlowLayout searchButtonLayout = new FlowLayout();
					searchButtonLayout.setAlignment(FlowLayout.LEFT);
					searchButtonLayout.setHgap(1);
					searchButtonPanel.setLayout(searchButtonLayout);
					searchPanel.add(searchButtonPanel, BorderLayout.WEST);
					searchButtonPanel.setPreferredSize(new java.awt.Dimension(
							322, 35));
					{
						kanjiToggleButton = new JToggleButton();
						kanjiToggleButton.setText("kanji");
						//						kanjiToggleButton.setBorder(BorderFactory.createLineBorder(Color.BLACK)); TODO make the buttons a bit nicer...
						searchButtonPanel.add(kanjiToggleButton);
						kanjiToggleButton.setName("kanjiToggleButton");
						kanjiToggleButton
								.setToolTipText("add or remove filter by kanji");
						kanjiToggleButton
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										kanjiToggleButtonActionPerformed(evt);
									}
								});
					}
					{
						indexToggleButton = new JToggleButton();
						indexToggleButton.setText("index");
						searchButtonPanel.add(indexToggleButton);
						indexToggleButton.setName("indexToggleButton");
						indexToggleButton
								.setToolTipText("add or remove filter by heisig index");
						indexToggleButton
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										indexToggleButtonActionPerformed(evt);
									}
								});
					}
					{
						keywordToggleButton = new JToggleButton();
						keywordToggleButton.setText("word");
						searchButtonPanel.add(keywordToggleButton);
						keywordToggleButton.setName("keywordToggleButton");
						keywordToggleButton
								.setToolTipText("add or remove filter by keyword");
						keywordToggleButton
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										keywordToggleButtonActionPerformed(evt);
									}
								});
					}
					{
						primitivesToggleButton = new JToggleButton();
						primitivesToggleButton.setText("parts");
						searchButtonPanel.add(primitivesToggleButton);
						primitivesToggleButton
								.setName("primitivesToggleButton");
						primitivesToggleButton
								.setToolTipText("add or remove filter by primitives");
						primitivesToggleButton
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) {
										primitivesToggleButtonActionPerformed(evt);
									}
								});

					}
				}
				{
					searchTextButtonPanel = new JPanel();
					FlowLayout searchTextButtonPanelLayout = new FlowLayout();
					searchTextButtonPanelLayout.setVgap(0);
					searchTextButtonPanel
							.setLayout(searchTextButtonPanelLayout);
					searchPanel.add(searchTextButtonPanel, BorderLayout.EAST);
					{
						searchORTextField = new JTextField();
						searchTextButtonPanel.add(searchORTextField);
						searchORTextField.setName("searchORTextField");
						searchORTextField.setFont(searchTextFieldFont);
						searchORTextField.setToolTipText("enter search items (the more words, the fewer results)");
						searchORTextField
								.setPreferredSize(new java.awt.Dimension(160,
										35));
						searchORTextField.addKeyListener(new KeyAdapter() {
							public void keyReleased(KeyEvent evt) {
								searchTextFieldKeyReleased(evt);
							}
						});
					}
					{
						searchTextField = new JTextField();
						searchTextButtonPanel.add(searchTextField);
						searchTextField.setName("searchTextField");
						searchTextField
								.setToolTipText("enter search items (for multiple kanji - the more characters, the more results)");
						searchTextField
								.setPreferredSize(new java.awt.Dimension(401,
										35));
						searchTextField.addKeyListener(new KeyAdapter() {
							public void keyReleased(KeyEvent evt) {
								searchTextFieldKeyReleased(evt);
							}
						});

						searchTextField.setFont(searchTextFieldFont);
					}
				}
			}

			//////////////////////////////////////////////////////////
			// results area
			//////////////////////////////////////////////////////////
			{
				mainScrollPanel = new JScrollPane();
				mainScrollPanel.setBorder(new LineBorder(new java.awt.Color(
						153, 153, 153), 1, false));
				{
					dataTable = new JTable();
					mainScrollPanel.setViewportView(dataTable);

					dataTable.setName("dataTable");
					dataTable.setModel(kanjiTableModel);
					tableRowSorter = new TableRowSorter<KanjiTableModel>(
							kanjiTableModel);
					dataTable.setRowSorter(tableRowSorter);
					dataTable
							.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//this can be changed in the future to show multiple entries in the details view (like the kanji dictionary does)
					dataTable.addFocusListener(new FocusAdapter() {
						public void focusGained(FocusEvent evt) {
							if (dataTable.getSelectedRow() < 0) {
								dataTable.setRowSelectionInterval(0, 0);
								mainScrollPanel.getViewport().setViewPosition(
										new Point(0, 0));
							}
						}
					});
					ListSelectionModel listSelectionModel = dataTable
							.getSelectionModel();
					listSelectionModel
							.addListSelectionListener(new ListSelectionListener() {
								@Override
								public void valueChanged(ListSelectionEvent e) {
									int viewRow = dataTable.getSelectedRow();
									if (viewRow < 0) {
										//selection got filtered away...
										//do nothing atm, but something in the future...
									} else {
										int modelRow = dataTable
												.convertRowIndexToModel(viewRow);
										Integer heisigIndex = (Integer) dataTable
												.getModel()
												.getValueAt(
														modelRow,
														KanjiTableModel.ColumnModel.heisigIndex
																.getIndex());
										setDetails(heisigIndex);
									}
								}
							});
					//comparing integers
					Comparator<Integer> intComparator = new Comparator<Integer>() {
						@Override
						public int compare(Integer o1, Integer o2) {
							if (o1 > o2) {
								return 1;
							} else if (o2 > o1) {
								return -1;
							}
							return 0;
						}
					};
					tableRowSorter.setComparator(
							KanjiTableModel.ColumnModel.heisigIndex.getIndex(),
							intComparator);
					tableRowSorter.setComparator(
							KanjiTableModel.ColumnModel.strokeCount.getIndex(),
							intComparator);
					tableRowSorter
							.setComparator(
									KanjiTableModel.ColumnModel.lessonNumber
											.getIndex(), intComparator);

					//setting the columns
					TableColumnModel tableColumnModel = dataTable
							.getColumnModel();
					TableColumn column;

					DefaultTableCellRenderer tableRomanCellRenderer = new DefaultTableCellRenderer() {
						@Override
						public Font getFont() {
							return romanFont;
						}
					};
					for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
						if (i != KanjiTableModel.ColumnModel.kanji.getIndex()) {
							tableColumnModel.getColumn(i).setCellRenderer(
									tableRomanCellRenderer);
						}
					}
					for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
						if (i != KanjiTableModel.ColumnModel.keywords
								.getIndex()
								&& i != KanjiTableModel.ColumnModel.primitives
										.getIndex()) {
							tableColumnModel.getColumn(i).setPreferredWidth(25);
						} else {
							tableColumnModel.getColumn(i)
									.setPreferredWidth(250);
						}
					}
					FontMetrics fontMetrics = dataTable
							.getFontMetrics(kanjiFont);
					dataTable.setRowHeight(fontMetrics.getHeight()); // set row height to match font
					DefaultTableCellRenderer tableKanjiCellRenderer = new DefaultTableCellRenderer() {
						@Override
						public Font getFont() {
							return kanjiFont;
						}
					};
					//					tableCellRenderer.setFont(customFont);
					column = tableColumnModel
							.getColumn(KanjiTableModel.ColumnModel.kanji
									.getIndex());
					column.setCellRenderer(tableKanjiCellRenderer);
				}
			}
			{
				detailsScrollPanel = new JScrollPane();
				detailsScrollPanel
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				detailsScrollPanel.setPreferredSize(new java.awt.Dimension(489,
						329));
				detailsScrollPanel.setBackground(null);
			}

			//////////////////////////////////////////////////////////
			// The split panel that holds the results and details
			//////////////////////////////////////////////////////////
			summaryAndDetailsSplitPanel = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT, mainScrollPanel, detailsPanel);
			summaryAndDetailsSplitPanel.setBorder(BorderFactory
					.createEmptyBorder(0, 0, 0, 0));
			summaryAndDetailsSplitPanel.setOneTouchExpandable(true);
			summaryAndDetailsSplitPanel.setContinuousLayout(true);
			summaryAndDetailsSplitPanel.add(detailsScrollPanel,
					JSplitPane.RIGHT);
			detailsScrollPanel.setName("detailsScrollPanel");
			detailsScrollPanel.setBorder(new LineBorder(new java.awt.Color(153,
					153, 153), 1, false));
			{
				detailsPanel = new JPanel();
				detailsScrollPanel.setViewportView(detailsPanel);
				BorderLayout detailsScrollPaneLayout = new BorderLayout();
				detailsPanel.setLayout(detailsScrollPaneLayout);
				{
					detailsKanjiContentPanel = new JPanel();
					detailsKanjiContentPanel.setBackground(Color.WHITE);
					BorderLayout detailsKanjiContentPanelLayout = new BorderLayout();
					detailsKanjiContentPanel
							.setLayout(detailsKanjiContentPanelLayout);
					detailsPanel.add(detailsKanjiContentPanel,
							BorderLayout.WEST);
					{
						detailsStrokeAndKanjiPanel = new JPanel();
						detailsStrokeAndKanjiPanel.setBackground(Color.WHITE);
						BoxLayout detailsStrokeAndKanjiPanelLayout = new BoxLayout(
								detailsStrokeAndKanjiPanel,
								javax.swing.BoxLayout.Y_AXIS);
						detailsStrokeAndKanjiPanel
								.setLayout(detailsStrokeAndKanjiPanelLayout);
						detailsKanjiContentPanel.add(
								detailsStrokeAndKanjiPanel, BorderLayout.NORTH);
						{
							detailsKanjiLabel = new JTextField();
							detailsStrokeAndKanjiPanel.add(detailsKanjiLabel);
							detailsKanjiLabel.setFont(kanjiLargeFont);
							detailsKanjiLabel
									.setAlignmentX(Component.CENTER_ALIGNMENT);
							makeTextFieldLookLikeLabel(detailsKanjiLabel);
							detailsKanjiLabel.setName("detailKanjiLanel");
							detailsKanjiLabel.setToolTipText("kanji");
						}
						{
							detailsStrokeCountLabel = new JTextField();
							detailsStrokeCountLabel.setFont(romanFont);
							detailsStrokeAndKanjiPanel
									.add(detailsStrokeCountLabel);
							detailsStrokeCountLabel
									.setAlignmentX(Component.CENTER_ALIGNMENT);
							detailsStrokeCountLabel
									.setHorizontalAlignment(JTextField.CENTER);
							detailsStrokeCountLabel
									.setToolTipText("stroke count");
							makeTextFieldLookLikeLabel(detailsStrokeCountLabel);
							detailsStrokeCountLabel
									.setName("detailsStrokeCountLabel");
						}
					}
				}
				{
					detailsMainContentPanel = new JPanel();
					detailsMainContentPanel.setBackground(Color.WHITE);
					BorderLayout detailsMainContentPanelLayout = new BorderLayout();
					detailsMainContentPanel
							.setLayout(detailsMainContentPanelLayout);
					detailsPanel.add(detailsMainContentPanel,
							BorderLayout.CENTER);
					{
						detailsMainContentHeadingPanel = new JPanel();
						detailsMainContentHeadingPanel
								.setBackground(Color.WHITE);
						BorderLayout detailsMainContentHeadingPanelLayout = new BorderLayout();
						detailsMainContentHeadingPanel
								.setLayout(detailsMainContentHeadingPanelLayout);
						detailsMainContentPanel.add(
								detailsMainContentHeadingPanel,
								BorderLayout.NORTH);
						{
							detailsKeywordLabel = new JTextField();
							makeTextFieldLookLikeLabel(detailsKeywordLabel);
							detailsMainContentHeadingPanel.add(
									detailsKeywordLabel, BorderLayout.EAST);
							Font keywordFont = new Font(romanFont.getName(),
									romanFont.getStyle(), 60);
							detailsKeywordLabel.setFont(keywordFont);
							detailsKeywordLabel.setName("detailsKeywordLabel");
							detailsKeywordLabel.setToolTipText("keyword(s)");
						}
						{
							detailsHeisigNumberLabel = new JTextField();
							makeTextFieldLookLikeLabel(detailsHeisigNumberLabel);
							detailsMainContentHeadingPanel
									.add(detailsHeisigNumberLabel,
											BorderLayout.WEST);
							Font heisigNumberFont = new Font(romanFont
									.getName(), romanFont.getStyle(), 40);
							detailsHeisigNumberLabel.setFont(heisigNumberFont);
							detailsHeisigNumberLabel.setForeground(Color.GRAY);
							detailsHeisigNumberLabel
									.setName("detailsHeisigNumberLabel");
							detailsHeisigNumberLabel
									.setToolTipText("heisig index");
						}
					}
					{
						detailsMainContentBodyPanel = new JPanel();
						BorderLayout detailsMainContentBodyPanelLayout = new BorderLayout();
						detailsMainContentPanel.add(
								detailsMainContentBodyPanel,
								BorderLayout.CENTER);
						detailsMainContentBodyPanel
								.setLayout(detailsMainContentBodyPanelLayout);
						{
							jScrollPane1 = new JScrollPane();
							detailsMainContentBodyPanel.add(jScrollPane1,
									BorderLayout.CENTER);
							jScrollPane1
									.setPreferredSize(new java.awt.Dimension(
											300, 150));
							jScrollPane1
									.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
							jScrollPane1.setBorder(BorderFactory
									.createEmptyBorder(0, 0, 0, 0));
							//							jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
							{
								detailsStoryTextArea = new JTextArea();
								jScrollPane1
										.setViewportView(detailsStoryTextArea);
								detailsStoryTextArea.setLineWrap(true);
								detailsStoryTextArea.setWrapStyleWord(true);
								detailsStoryTextArea.setFont(romanFont);
								detailsStoryTextArea
										.setForeground(Color.DARK_GRAY);
								detailsStoryTextArea
										.setName("detailsStoryTextArea");
								detailsStoryTextArea.setToolTipText("story");
							}
						}
						{
							detailsPrimitiveTextArea = new JTextPane();
							Font detailsPrimitiveFont = new Font(romanFont
									.getName(), romanFont.getStyle(), 30);
							StyledDocument doc = detailsPrimitiveTextArea
									.getStyledDocument();
							SimpleAttributeSet right = new SimpleAttributeSet();
							StyleConstants.setAlignment(right,
									StyleConstants.ALIGN_RIGHT);
							doc.setParagraphAttributes(0, doc.getLength(),
									right, false);

							detailsPrimitiveTextArea
									.setFont(detailsPrimitiveFont);
							detailsPrimitiveTextArea.setForeground(Color.GRAY);
							detailsMainContentBodyPanel
									.add(detailsPrimitiveTextArea,
											BorderLayout.EAST);
							detailsPrimitiveTextArea
									.setName("detailsPrimitiveTextArea");
							detailsPrimitiveTextArea
									.setToolTipText("primitive(s)");
						}
					}
				}
			}
			//////////////////////////////////////////////////////////
			// The main split panel with the relos on the RHS and the others on the left
			//////////////////////////////////////////////////////////
			{
				getContentPane().add(summaryAndDetailsSplitPanel,
						BorderLayout.CENTER);
				summaryAndDetailsSplitPanel
						.setName("summaryAndDetailsSplitPanel");
			}
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void aboutAction(ActionEvent e) {
		JOptionPane.showMessageDialog(this, "insig (heisig index)\ninsig uses data from the following sources:\n" +
				"http://ziggr.com/heisig/\n" +
				"http://www.ravenbrook.com/project/etp24/tool/cgi/heisig-index/?action=frame_order\n" +
				"http://www.csse.monash.edu.au/~jwb/kanjidic.html", "About insig", JOptionPane.INFORMATION_MESSAGE);
	}

	protected void appendKeywordMeaningAction(ActionEvent e) {
		AppendDetailsToCSVWindow appendDetailsToCSVAction = new AppendDetailsToCSVWindow(kanjiTableModel, this);
		appendDetailsToCSVAction.setVisible(true);
		
	}

	private void makeTextFieldLookLikeLabel(JTextField textField) {
		textField.setEditable(false);
		textField.setBorder(null);
		textField.setBackground(null);
	}

	private void searchTextFieldKeyReleased(KeyEvent evt) {
		// add filter
		updateFilter();
		//check for shortcuts?
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println("enter bttn pressed...");
			dataTable.requestFocus();
		}
	}

	private void updateFilter() {
		String searchText = searchTextField.getText();
		String[] searchItems = searchText.split(" ");

		List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
		for (String searchItem : searchItems) {
			if (searchItem != null && searchItem.equals("") == false) {
				addFilter(filters, searchItem);
			}
		}
		List<RowFilter<Object, Object>> orFilters = new ArrayList<RowFilter<Object, Object>>();
		String searchORText = searchORTextField.getText();
		char[] searchORItems = searchORText.toCharArray();
		for (char searchItem : searchORItems) {
			addFilter(orFilters, "" + searchItem);
		}
		//		String[] searchORItems = searchText.TODO
		if (orFilters.size() > 0) {
			filters.add(RowFilter.orFilter(orFilters));
		}
		RowFilter<Object, Object> rowFilter = RowFilter.andFilter(filters);

		// RowFilter<KanjiTableModel, Object> rowFilter = RowFilter
		// .regexFilter(searchText);
		tableRowSorter.setRowFilter(rowFilter);

		//		searchTextField.requestFocus();
	}

	private void addFilter(List<RowFilter<Object, Object>> filters,
			String searchItem) {
		//TODO ignore CAPS...
		//TODO o something about bad characters (like parenthasis) when passed in.
		try {
			if (filterByAllFields) { // if we should search all of the fields
				filters.add(RowFilter.regexFilter(searchItem));
			} else {// if we should only search some of them.
				filters.add(RowFilter.regexFilter(searchItem,
						getFilterIndiciesArray()));
			}
		} catch (Exception exception) {
			System.err.println(exception.toString());
			System.out.println("the search item is not allowed, will ignore: "
					+ searchItem);
		}
	}

	private int[] getFilterIndiciesArray() {
		int[] indicies = new int[filterIndicies.size()];
		for (int i = 0; i < indicies.length; i++) {
			indicies[i] = filterIndicies.get(i);
		}
		return indicies;
	}

	private void indexToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.heisigIndex
				.getIndex(), indexToggleButton);
	}

	private void kanjiToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.kanji
				.getIndex(), kanjiToggleButton);
	}

	private void keywordToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.keywords
				.getIndex(), keywordToggleButton);
	}

	private void strokeCountToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.strokeCount
				.getIndex(), null);
	}

	private void lessonNumberToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.lessonNumber
				.getIndex(), null);
	}
	private void primitivesToggleButtonActionPerformed(ActionEvent evt) {
		toggleButtonActionPerformed(KanjiTableModel.ColumnModel.primitives
				.getIndex(), primitivesToggleButton);
	}

	private void toggleButtonActionPerformed(Integer columnIndex,
			JToggleButton toggleButton) {
		if (toggleButton != null) {
			if (toggleButton.isSelected()) {
				filterIndicies.add(columnIndex);
			} else {
				if (filterIndicies.contains(columnIndex)) {
					filterIndicies.remove(filterIndicies.indexOf(columnIndex));
				}
			}
		}
		if (toggleButtonSelected()) {
			filterByAllFields = false;
		} else {
			filterByAllFields = true;
		}
		updateFilter();
	}

	private boolean toggleButtonSelected() {
		return indexCheckboxButtonMenuItem.isSelected()
				|| kanjiCheckboxButtonMenuItem.isSelected()
				|| keywordToggleButton.isSelected()
				|| strokesCheckboxButtonMenuItem.isSelected()
				|| lessonCheckboxButtonMenuItem.isSelected()
				|| primitivesToggleButton.isSelected();
	}

	private void setDetails(Integer heisigIndex) {
		HeisigItem item = kanjiTableModel.get(heisigIndex);
		detailsHeisigNumberLabel.setText("(#" + item.getHeisigIndex() + ")");
		detailsKeywordLabel.setText(item.getKeywordsFormattedSimply());
		if (item.hasMultipleKeywordVersions()) {
			detailsKeywordLabel.setToolTipText("keyword, multiple versions: "
					+ item.getKeywordsFormatted());
		} else {
			detailsKeywordLabel.setToolTipText("keyword: "
					+ item.getKeywordsFormatted());
		}
		detailsKanjiLabel.setText(item.getKanji());
		detailsStrokeCountLabel.setText("[ " + item.getKanjiStrokeCount()
				+ " ]");
		StringBuilder stringBuilder = new StringBuilder();
		boolean firstPass = true;
		for (String primitive : item.getKanjiPrimitiveList()) {
			if (firstPass == true) {
				firstPass = false;
			} else {
				stringBuilder.append("\n");
			}
			stringBuilder.append(primitive);
		}
		detailsPrimitiveTextArea.setText(stringBuilder.toString());
		detailsStoryTextArea.setText(item.getStory());
		this.validate(); // this is to make sure that the new text does not make the UI 'funny'.
	}

	private void copyKeywordSummaryToClipboardAction() {
		int[] selectedRows = dataTable.getSelectedRows();
		for (int selectedRow : selectedRows) {
			int modelRow = dataTable.convertRowIndexToModel(selectedRow);
			Integer heisigIndex = (Integer) dataTable.getModel().getValueAt(
					modelRow,
					KanjiTableModel.ColumnModel.heisigIndex.getIndex());
			HeisigItem heisigItem = kanjiTableModel.get(heisigIndex);
			//need to append this to the clipboard...
			// Get the system toolkit
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			// Use the toolkit to get the system clipboard
			final Clipboard clipboard = toolkit.getSystemClipboard();
			// Set the contents of the clipboard to our string
			clipboard.setContents(new StringSelection(heisigItem
					.getKeywordKanjiSummary()), null);

		}
		System.out.println("Items put onto clipboard");
	}
}
