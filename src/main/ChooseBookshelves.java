package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;

import controller.Controller;
import data.Library;

public class ChooseBookshelves extends JDialog {

    private static final long serialVersionUID = 1L;
    private JList bookshelfList = null;
    private JButton cancelButton = null;
    private Controller control = null;
    private JButton importButton = null;

    private JPanel jContentPane = null;
    private Library library = null;
    private TreeView treeView = null;

    /**
     * @param owner
     */
    public ChooseBookshelves(Frame owner, Controller c, TreeView t, Library l) {
	super(owner);

	if (l != null) {
	    control = c;
	    treeView = t;
	    library = l;

	    initialize();
	} else {
	    setVisible(false);
	}
    }

    /**
     * This method initializes bookshelfList
     * 
     * @return javax.swing.JList
     */
    private JList getBookshelfList() {
	if (bookshelfList == null) {

	    List<String> names = new ArrayList<String>();
	    Iterator<String> bookshelves = library.getBookshelfNames().iterator();
	    while (bookshelves.hasNext()) {
		names.add(bookshelves.next());
	    }

	    bookshelfList = new JList(names.toArray(new String[0]));
	}
	return bookshelfList;
    }

    /**
     * This method initializes cancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
	if (cancelButton == null) {
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setVisible(false);

		}
	    });
	}
	return cancelButton;
    }

    /**
     * This method initializes importButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getImportButton() {
	if (importButton == null) {
	    importButton = new JButton("Import");
	    importButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    // TODO Import (String[])bookshelfList.getSelectedValues() bookshelves into local library
		    // control.importSelectBookshelves(local, remote, select)
		    
		    treeView.refresh();
		    setVisible(false);
		}
	    });
	}
	return importButton;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.gridx = 0;
	    gridBagConstraints2.gridy = 1;
	    gridBagConstraints2.weightx = 1.0;
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = 1;
	    gridBagConstraints1.weightx = 1.0;
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.fill = GridBagConstraints.BOTH;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.gridwidth = 2;
	    gridBagConstraints.weighty = 1.0;
	    gridBagConstraints.gridx = 0;
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());
	    jContentPane.add(getBookshelfList(), gridBagConstraints);
	    jContentPane.add(getCancelButton(), gridBagConstraints1);
	    jContentPane.add(getImportButton(), gridBagConstraints2);
	}
	return jContentPane;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(380, 271);
	this.setTitle("Import Bookshelves");
	this.setContentPane(getJContentPane());
    }

} // @jve:decl-index=0:visual-constraint="298,9"
