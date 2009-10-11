package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

import data.VirtualBook;
import data.VirtualBookshelf;

public class Root extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JToolBar jToolBar = null;
	private JSplitPane jSplitPane = null;
	private TreeView treeView = null;
	private SearchResults searchResults = null;
	private ArrayList<VirtualBookshelf> bookshelves = new ArrayList<VirtualBookshelf>();  //  @jve:decl-index=0:
	int currentBookshelfIndex = -1;

	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
		}

		// TODO: Create & Add Icons
		ImageIcon icon = new ImageIcon(getClass().getResource("addBookshelf.png"), "Add Bookshelf");
		JButton addBookshelf = new JButton(icon);
		addBookshelf.setBorder(null);
		addBookshelf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				// Add functionality for changing name
				bookshelves.add(new VirtualBookshelf("Default Name"));
				currentBookshelfIndex = bookshelves.size() - 1;
				
				searchResults.addResult(new Result(bookshelves.get(currentBookshelfIndex)));
				
				draw();
			}
		});
		jToolBar.add(addBookshelf);
		
		ImageIcon icon2 = new ImageIcon(getClass().getResource("addBook.png"), "Add Book (to Current Bookshelf)");
		JButton addBook = new JButton(icon2);
		addBook.setBorder(null);
		addBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				// Add safety checks
				// Add functionality for changing name
				if (currentBookshelfIndex == -1) {
					bookshelves.add(new VirtualBookshelf("Default Name"));
					currentBookshelfIndex = bookshelves.size() - 1;
				}
				bookshelves.get(currentBookshelfIndex).insert(new VirtualBook("The Traitor", "Andre Gorz"));
				searchResults.addResult(new Result(bookshelves.get(currentBookshelfIndex)));
				
				draw();
				// Refresh current bookshelf view
			}
		});
		jToolBar.add(addBook);

		return jToolBar;
	}
	
	protected void draw(){
		validate();
		repaint();
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getTreeView());
			jSplitPane.setRightComponent(getSearchResults());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes treeView	
	 * 	
	 * @return main.TreeView	
	 */
	private TreeView getTreeView() {
		if (treeView == null) {
			//treeView = new TreeView();
		}
		return treeView;
	}

	/**
	 * This method initializes searchResults	
	 * 	
	 * @return main.SearchResults	
	 */
	private SearchResults getSearchResults() {
		if (searchResults == null) {
			searchResults = new SearchResults();
		}
		return searchResults;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Root thisClass = new Root();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				try {
					UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel()); 
				}
				catch ( UnsupportedLookAndFeelException ex ){
					System.out.println("Cannot set new Theme for Java Look and Feel.");
				}
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public Root() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(1038, 509);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJToolBar(), BorderLayout.NORTH);
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
