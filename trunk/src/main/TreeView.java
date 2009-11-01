package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

import data.*;

public class TreeView extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTree tree = null;

    private List<Library> library;

    /**
     * This is the default constructor
     */
    public TreeView() {
	super();
	library = new ArrayList<Library>();
	initialize();
    }

    /**
     * @param l
     */
    public TreeView(Library l) {
	super();
	library = new ArrayList<Library>();
	library.add(l);
	initialize();
    }

    /**
     * This method initializes tree
     * 
     * @return javax.swing.JTree
     */
    private JTree getTree() {
	if (tree == null) {
	    tree = new JTree();
	}
	return tree;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(321, 637);
	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	this.add(getTree(), null);
	try {
	    UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    System.out.println("Cannot set new Theme for Java Look and Feel.");
	}
    }

} // @jve:decl-index=0:visual-constraint="388,31"
