package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Book;
import data.Bookshelf;
import java.awt.Dimension;

public class RenameBookshelf extends JDialog {

    private static final long serialVersionUID = 1L;
    private JButton cancel = null;
    private JButton commit = null;
    private Controller control = null;
    private JPanel jContentPane = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;

    private JTextField jTextField = null;
    private JTextField jTextField1 = null;
    private SearchResults results = null;
    private Bookshelf shelf = null;
    private TreeView tree = null;

    /**
     * @param owner
     * @param treeView
     */
    public RenameBookshelf(Frame owner, Controller ctl, Bookshelf b,
	    TreeView treeView) {
	super(owner);

	control = ctl;

	tree = treeView;

	if (b != null) {
	    shelf = b;
	} else {
	    setVisible(false);
	}

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

		    shelf.setProperty("name", jTextField1.getText());

		    tree.refresh();

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
	    gridBagConstraints5.gridy = 2;
	    gridBagConstraints5.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
	    gridBagConstraints4.gridx = 1;
	    gridBagConstraints4.gridy = 2;
	    gridBagConstraints4.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
	    gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints3.gridy = 1;
	    gridBagConstraints3.gridwidth = 2;
	    gridBagConstraints3.gridx = 1;
	    gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints2.gridy = 0;
	    gridBagConstraints2.gridwidth = 2;
	    gridBagConstraints2.gridx = 1;
	    gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 0;
	    gridBagConstraints1.gridy = 1;
	    gridBagConstraints1.anchor = GridBagConstraints.EAST;
	    gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
	    jLabel1 = new JLabel();
	    jLabel1.setText("New Title: ");
	    JLabel jLabel2 = new JLabel();
	    jLabel2.setText((shelf != null) ? shelf.getProperty("name") : "");
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.EAST;
	    gridBagConstraints.insets = new Insets(2, 2, 2, 2);
	    jLabel = new JLabel();
	    jLabel.setText("Old Title: ");
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());
	    jContentPane.add(jLabel, gridBagConstraints);
	    jContentPane.add(jLabel1, gridBagConstraints1);
	    jContentPane.add(jLabel2, gridBagConstraints2);
	    jContentPane.add(getJTextField1(), gridBagConstraints3);
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
     * This method initializes jTextField1
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField1() {
	if (jTextField1 == null) {
	    jTextField1 = new JTextField(14);

	    jTextField1.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    int key = e.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
			commit.doClick();
		    } else if (key == KeyEvent.VK_ESCAPE) {
			cancel.doClick();
		    }
		}
	    });
	}
	return jTextField1;
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
	this.setSize(308, 158);
	this.setContentPane(getJContentPane());
	this.setTitle("Rename Bookshelf");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
