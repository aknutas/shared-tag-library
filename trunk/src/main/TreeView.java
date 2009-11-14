package main;

import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Bookshelf;

public class TreeView extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode top = new DefaultMutableTreeNode("Libraries");
	private JTree tree = new JTree(top);
	JScrollPane treeView = new JScrollPane(tree);

	private Controller control = null;
	private SearchResults results = null;

	/**
	 * @param l
	 */
	public TreeView(Controller c, SearchResults s) {

		super();
		control = c;
		results = s;
		initialize();
	}

	protected void draw() {

		((DefaultTreeModel) tree.getModel()).reload();
		this.repaint();
	}

	public void addChild(Bookshelf shelf) {

		if (shelf != null) {
			top.add(new TreeNode(shelf));
		}
	}

	public void refresh() {

		top.removeAllChildren();

		Iterator<Bookshelf> bookshelves = control.retrieveLibrary();

		while (bookshelves.hasNext()) {
			top.add(new TreeNode(bookshelves.next()));
		}

		draw();
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
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(getScrollPane());
		tree.setModel(new DefaultTreeModel(top));
		tree.addTreeSelectionListener(this);

		try {
			UIManager
					.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Cannot set new Theme for Java Look and Feel.");
		}

		setVisible(true);

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		try {
			Bookshelf node = ((TreeNode) tree.getLastSelectedPathComponent())
					.getUserObject().getShelf();
			control.setFocus(node, 0);
			results.setResults(node);
			draw();
		} catch (ClassCastException e1) {
			System.out.println("You don't want to browse the library root...");
		} catch (NullPointerException e2) {

		}
	}

} // @jve:decl-index=0:visual-constraint="388,31"
