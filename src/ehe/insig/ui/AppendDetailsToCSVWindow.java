package ehe.insig.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import org.jdesktop.application.Application;

import ehe.insig.ui.datModel.KanjiTableModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * @author lindon-fox
 *
 */
public class AppendDetailsToCSVWindow extends JDialog {
	private KanjiTableModel kanjiTableModel;
	private JButton fileChooserButton;
	private JTextField fileLoadedTextFile;
	private JLabel fileToLoadLabel;
	private JTextField indexingFieldTargetIndexTextField;
	private JLabel indexingFieldTargetLabel;
	private JSeparator separator;
	private JLabel indexingFieldLabel;
	private JTextField indexingFieldTextField;

	public AppendDetailsToCSVWindow(KanjiTableModel kanjiTableModel, JFrame frame) {
		super(frame);
		this.kanjiTableModel = kanjiTableModel;
		initGUI();
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			{
				fileLoadedTextFile = new JTextField();
				fileLoadedTextFile.setEditable(false);
				getContentPane().add(fileLoadedTextFile, new AnchorConstraint(162, 790, 236, 442, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				fileLoadedTextFile.setPreferredSize(new java.awt.Dimension(236, 22));
			}
			{
				fileToLoadLabel = new JLabel();
				fileToLoadLabel.setText("File to load:");
				getContentPane().add(fileToLoadLabel, new AnchorConstraint(172, 220, 223, 25, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				fileToLoadLabel.setPreferredSize(new java.awt.Dimension(132, 15));
				fileToLoadLabel.setName("fileToLoadLabel");
			}
			{
				fileChooserButton = new JButton();
				fileChooserButton.setText("load file...");
				getContentPane().add(fileChooserButton, new AnchorConstraint(169, 977, 229, 808, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				fileChooserButton.setPreferredSize(new java.awt.Dimension(114, 18));
				fileChooserButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						chooseNewFileAction(e);
					}
				});
			}
			{
				indexingFieldTargetIndexTextField = new JTextField();
				indexingFieldTargetIndexTextField.setText("0");
				getContentPane().add(indexingFieldTargetIndexTextField, new AnchorConstraint(303, 789, 377, 442, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				indexingFieldTargetIndexTextField.setPreferredSize(new java.awt.Dimension(235, 22));
			}
			{
				indexingFieldTargetLabel = new JLabel();
				indexingFieldTargetLabel.setText("Column number of index (first column is 1):");
				getContentPane().add(indexingFieldTargetLabel, new AnchorConstraint(280, 424, 394, 22, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				indexingFieldTargetLabel.setPreferredSize(new java.awt.Dimension(272, 34));
			}
			{
				separator = new JSeparator();
				getContentPane().add(separator, new AnchorConstraint(135, 978, 169, 26, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				separator.setPreferredSize(new java.awt.Dimension(520, 10));
			}
			{
				indexingFieldLabel = new JLabel();
				indexingFieldLabel.setText("Index field (on the insig side):");
				getContentPane().add(indexingFieldLabel, new AnchorConstraint(41, 422, 115, 22, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				indexingFieldLabel.setPreferredSize(new java.awt.Dimension(218, 22));
			}
			{
				indexingFieldTextField = new JTextField();
				indexingFieldTextField.setText("heisig index");
				getContentPane().add(indexingFieldTextField, new AnchorConstraint(41, 977, 115, 444, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				indexingFieldTextField.setPreferredSize(new java.awt.Dimension(291, 22));
			}
			{
				this.setSize(677, 320);
			}
			Application.getInstance().getContext().getResourceMap(getClass()).injectComponents(getContentPane());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void chooseNewFileAction(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
