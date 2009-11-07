package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

import data.Bookshelf;
import data.Library;

public class TreeView extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode top =
        new DefaultMutableTreeNode("Libraries");
	private JTree tree = new JTree(top);
	JScrollPane treeView = new JScrollPane(tree);

	private List<Library> library = new ArrayList<Library>();

	/**
	 * This is the default constructor
	 */
	public TreeView() {
		super();
		initialize();
	}

	/**
	 * @param l
	 */
	public TreeView(Library l) {
		super();
		library.add(l);
		initialize();
	}

	
	protected void draw() {
		validate();
		tree.repaint();
		this.repaint();
		super.repaint();
	}
	
	
	/**
	 * This method initializes tree
	 * 
	 * @return javax.swing.JTree
	 */
	private JScrollPane getScrollPane() {
	    
		for ( Library l : library ) {
			
			Bookshelf masterShelf = l.getMasterShelf();
			DefaultMutableTreeNode lib = new DefaultMutableTreeNode(masterShelf.getProperty("name"));
			top.add(lib);
			
			// lib.add(/* Additional Bookshelves */);
		}
		
		return treeView;
	}


	/**
	 * Add Library to tree
	 * 
	 * @return javax.swing.JTree
	 */
	public JScrollPane addLibrary(Library l) {
		library.add(l);
		return getScrollPane();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(321, 637);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(getScrollPane());
		try {
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Cannot set new Theme for Java Look and Feel.");
		}
		
        setVisible(true);

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

} // @jve:decl-index=0:visual-constraint="388,31"
