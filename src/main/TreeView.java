package main;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.Controller;
import data.Bookshelf;
import data.Library;

/**
 * Tree view for bookshelves.
 * 
 * @author patrick
 * 
 */
public class TreeView extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;
    private Controller control = null;
    private SearchResults results = null;
    private DefaultMutableTreeNode top = new DefaultMutableTreeNode("Libraries");

    private JTree tree = new JTree(top);
    JScrollPane treeView = new JScrollPane(tree);
    private Root root = null;

    /**
     * Tree view for bookshelves
     */
    public TreeView(Root r, Controller c, SearchResults s) {

	super();
	root = r;
	control = c;
	results = s;
	initialize();
    }

    protected void draw() {

	// ((DefaultTreeModel) tree.getModel()).reload();
	this.repaint();
    }

    /**
     * This method initializes tree
     * 
     * @return javax.swing.JTree
     */
    private JScrollPane getScrollPane() {

	Iterator<Bookshelf> bookshelves = control.retrieveLibrary();

	while (bookshelves.hasNext()) {

	    TreeNode lib = new TreeNode(bookshelves.next());

	    // This loads an initial book into memory (Speed Optimization)
	    if (lib.getShelf().iterator().hasNext())
		lib.getShelf().iterator().next();

	    top.add(lib);
	}

	return treeView;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(400, 637);
	this.setMinimumSize(new Dimension(150, 200));
	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	this.add(getScrollPane());
	tree.setRootVisible(false);
	tree.setModel(new DefaultTreeModel(top));
	tree.addTreeSelectionListener(this);
	setVisible(true);

    }

    protected void refresh() {
	top.removeAllChildren();

	Iterator<Bookshelf> bookshelves = control.retrieveLibrary();
	while (bookshelves.hasNext()) {

	    Bookshelf shelf = bookshelves.next();
	    top.add(new TreeNode(shelf));
	}

	Vector<Library> nBookshelves = control.retrieveRemoteLibraries();
	for (Library l : nBookshelves) {

	    Iterator<Bookshelf> bs = l.iterator();
	    while (bs.hasNext()) {

		top.add(new TreeNode(bs.next()));
	    }
	}

	tree.setModel(new DefaultTreeModel(top));
	((DefaultTreeModel) tree.getModel()).reload();
	draw();
    }

    /**
     * Value changed. A new bookshelf is selected. Make sure it's displayed on
     * search results.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
	try {
	    Bookshelf node = ((TreeNode) tree.getLastSelectedPathComponent())
		    .getUserObject().getShelf();
	    control.setFocus(node);
	    results.setResults(node);
	    draw();
	} catch (ClassCastException e1) {
	    root.setStatus("Error Displaying Bookshelf");
	} catch (NullPointerException e2) {
	    root.setStatus("Error Processing Bookshelf");
	} catch (IllegalArgumentException e2) {

	    root.setStatus("Processing Remote Bookshelf...");
	    Bookshelf node = ((TreeNode) tree.getLastSelectedPathComponent())
		    .getUserObject().getShelf();
	    results.setResults(node);
	    draw();
	}
    }

} // @jve:decl-index=0:visual-constraint="388,31"
