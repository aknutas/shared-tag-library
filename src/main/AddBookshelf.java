package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Bookshelf;
import java.awt.Dimension;

public class AddBookshelf extends JDialog {

    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JLabel jLabel1 = null;
    private JTextField jTextField = null;
    private JButton commit = null;
    private JButton cancel = null;

    private Controller control = null;
    private Bookshelf shelf = null;
    private SearchResults results = null;
    private TreeView tree = null;

    /**
     * @param owner
     * @param treeView
     */
    public AddBookshelf(Frame owner, Controller ctl, SearchResults r,
	    TreeView treeView) {
	super(owner);
	control = ctl;
	results = r;
	tree = treeView;
	initialize();
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton() {
	if (commit == null) {
	    commit = new JButton("Commit");
	    commit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    shelf = control.addBookshelf(jTextField.getText());

		    if (shelf != null) {
			results.setResults(shelf);
			tree.addChild(shelf);
			tree.draw();
		    }

		    setVisible(false);

		}
	    });
	}
	return commit;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getJButton2() {
	if (cancel == null) {
	    cancel = new JButton("Cancel");
	    cancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setVisible(false);

		}
	    });
	}
	return cancel;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {

	    GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
	    gridBagConstraints5.gridx = 2;
	    gridBagConstraints5.gridy = 1;
	    gridBagConstraints5.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
	    gridBagConstraints4.gridx = 1;
	    gridBagConstraints4.gridy = 1;
	    gridBagConstraints4.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints2.gridy = 0;
	    gridBagConstraints2.gridwidth = 2;
	    gridBagConstraints2.gridx = 1;
	    gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 0;
	    gridBagConstraints1.gridy = 0;
	    gridBagConstraints1.anchor = GridBagConstraints.EAST;
	    gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
	    jLabel1 = new JLabel();
	    jLabel1.setText("Name: ");
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());
	    jContentPane.add(jLabel1, gridBagConstraints1);
	    jContentPane.add(getJTextField(), gridBagConstraints2);
	    jContentPane.add(getJButton(), gridBagConstraints4);
	    jContentPane.add(getJButton2(), gridBagConstraints5);

	    try {
		UIManager
			.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
	    } catch (UnsupportedLookAndFeelException ex) {
		System.out
			.println("Cannot set new Theme for Java Look and Feel.");
	    }
	}
	return jContentPane;
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
	if (jTextField == null) {
	    jTextField = new JTextField(14);
	}
	return jTextField;
    }

    public Bookshelf getShelf() {
	return shelf;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(265, 154);
	this.setContentPane(getJContentPane());
    }

} // @jve:decl-index=0:visual-constraint="10,10"
