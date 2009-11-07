package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.TextField;
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

import controller.Controller;
import data.VirtualBook;
import data.VirtualBookshelf;
import java.awt.Button;

/**
 * @author patrick
 * 
 */
public class Root extends JFrame {

	private static final long serialVersionUID = 1L;
	static Root thisClass = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				thisClass = new Root();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
				try {
					UIManager
							.setLookAndFeel(new SubstanceBusinessLookAndFeel());
				} catch (UnsupportedLookAndFeelException ex) {
					System.out
							.println("Cannot set new Theme for Java Look and Feel.");
				}
			}
		});
	}

	private ArrayList<VirtualBookshelf> bookshelves = new ArrayList<VirtualBookshelf>();

	private Button connectTo = null;

	private Controller control = null;

	int currentBookshelfIndex = -1;
	private JPanel jContentPane = null;
	private JSplitPane jSplitPane = null;
	private JToolBar jToolBar = null;
	private TextField searchField = null;
	private Label searchLabel = null;
	private VirtualBookshelf currentBookshelf = null;

	private SearchResults searchResults = null;
	private TreeView treeView = null;

	/**
	 * This is the default constructor
	 */
	public Root() {
		super();
		initialize();
	}

	/**
     * 
     */
	protected void draw() {
		validate();
		repaint();
	}

	/**
	 * This method initializes button
	 * 
	 * @return java.awt.Button
	 */
	private Button getButton() {
		if (connectTo == null) {
			connectTo = new Button();
			connectTo.setLabel("Connect To...");
			connectTo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					ConnectTo dialog = new ConnectTo(thisClass, control);
					dialog.setVisible(true);
					// Connect through Controller.
					// controller.setConnection ?

					// Update TreeView
					// Update SearchResults
				}
			});
		}
		return connectTo;
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
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			searchLabel = new Label("Search:", 0);
			searchLabel.setPreferredSize(new Dimension(100, 14));

			// searchLabel.setText("Search:");
			jToolBar = new JToolBar();
		}

		ImageIcon icon = new ImageIcon(getClass().getResource(
				"addBookshelf.png"), "Add Bookshelf");
		JButton addBookshelf = new JButton(icon);
		addBookshelf.setBorder(null);
		addBookshelf.setToolTipText("Add Bookshelf");
		jToolBar.add(searchLabel);
		jToolBar.add(getTextField());
		addBookshelf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				// Add functionality for changing name
				bookshelves.add(new VirtualBookshelf("New Bookshelf"));
				currentBookshelfIndex = bookshelves.size() - 1;

				bookshelves.get(currentBookshelfIndex).insert(
						new VirtualBook("Silence", "John Cage"));

				searchResults
						.setResults(bookshelves.get(currentBookshelfIndex));

				draw();
			}
		});
		jToolBar.add(addBookshelf);

		ImageIcon icon2 = new ImageIcon(getClass().getResource("addBook.png"),
				"Add Book (to Current Bookshelf)");
		JButton addBook = new JButton(icon2);
		addBook.setBorder(null);
		addBook.setToolTipText("Add Book");
		addBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				// Add safety checks
				// Add functionality for changing name
				if (currentBookshelfIndex == -1) {
					bookshelves.add(new VirtualBookshelf("New Bookshelf"));
					currentBookshelfIndex = bookshelves.size() - 1;
				}

				bookshelves.get(currentBookshelfIndex).insert(
						new VirtualBook("The Traitor", "Andre Gorz"));

				searchResults
						.setResults(bookshelves.get(currentBookshelfIndex));

				draw();
				// Refresh current bookshelf view
			}
		});
		jToolBar.add(addBook);

		jToolBar.add(getButton());
		return jToolBar;
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
	 * This method initializes searchField
	 * 
	 * @return java.awt.TextField
	 */
	private TextField getTextField() {
		if (searchField == null) {
			searchField = new TextField();
			searchField.setPreferredSize(new Dimension(800, 14));
			searchField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					// Search Through Controller

					draw();
					// Refresh current bookshelf view
				}
			});
		}
		return searchField;
	}

	/**
	 * This method initializes treeView
	 * 
	 * @return main.TreeView
	 */
	private TreeView getTreeView() {
		if (treeView == null) {
			treeView = new TreeView();
		}
		return treeView;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		control = new Controller();
		this.setSize(1280, 800);
		this.setContentPane(getJContentPane());
		this.setTitle("Book Butler");
	}
}
