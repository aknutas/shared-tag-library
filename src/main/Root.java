package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;

import controller.Controller;
import data.Book;
import data.Bookshelf;

/**
 * @author patrick
 * 
 */
public class Root extends JFrame {

    /**
     * This is the ActionListener that gets activated by the messagehandler
     * timer.
     * 
     * @Author Antti Knutas
     */
    private class MsgTrigger implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    control.messageHandler();
	}

    }

    private static final long serialVersionUID = 1L;
    static Root thisClass = null;

    /**
     * @param args
     */
    public static void main(String[] args) {

	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		try {
		    UIManager
			    .setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
		    System.out
			    .println("Cannot set new Theme for Java Look and Feel.");
		}
		thisClass = new Root();
		thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thisClass.setVisible(true);

	    }
	});
    }

    private JMenuItem aboutMenuItem = null;

    private AddBook addBookDialog = null;

    private JMenuItem addBookMenuItem = null;

    private AddBookshelf addBookshelfDialog = null;

    private JMenuItem addBookshelfMenuItem = null;

    private JMenuItem addTagDialogMenuItem = null;

    private JMenu bookMenu = null;

    private JMenu bookshelfMenu = null;

    private JMenuItem clearBookshelfMenuItem = null;

    private JMenuItem connectToMenuItem = null;

    private JPanel content = null;

    private Controller control = null;

    private JMenuItem deleteBookshelfMenuItem = null;

    private JMenuItem deleteSelectedBooksMenuItem = null;

    private JMenuItem disconnectMenuItem = null;

    private JMenuItem exportLibrary = null;

    private JMenuItem exportSelectedBooksMenuItem = null;

    private JMenuItem helpMenuItem = null;

    private JMenuItem importLibrary = null;

    private JMenuBar jJMenuBar = null;

    private JSplitPane jSplitPane = null;

    private JMenu libraryMenu = null;

    private javax.swing.Timer msgTimer;

    private MsgTrigger msgTrigger;

    private JMenuItem quitMenuItem = null;

    private JMenuItem removeTagDialogMenuItem = null;

    private JMenuItem renameBookDialogMenuItem = null;

    private RenameBookshelf renameBookshelfDialog = null;

    private JMenuItem renameBookshelfMenuItem = null;

    private JButton searchButton = null;
    private JTextField searchField = null;
    private JComboBox searchFilterComboBox = null;
    private SearchResults searchResults = null;
    private JToolBar searchToolbar = null;

    private Bookshelf shelf = null;
    private JMenuItem sortBookshelf = null;

    private JMenu startMenu = null;
    private JLabel status = null;
    JToolBar statusBar = null;
    private TreeView treeView = null;
    private JLabel withinLabel = null;

    /**
     * This is the default constructor
     */
    public Root() {
	super();
	// This is where message trigger handler gets created and started up.
	msgTrigger = new MsgTrigger();
	msgTimer = new javax.swing.Timer(1000, msgTrigger);
	msgTimer.setInitialDelay(2000);
	msgTimer.start();
	initialize();
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem addTagMenuItem() {
	if (addTagDialogMenuItem == null) {
	    addTagDialogMenuItem = new JMenuItem("Add Tag");
	    addTagDialogMenuItem.setMnemonic(KeyEvent.VK_A);
	    addTagDialogMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_A, ActionEvent.CTRL_MASK));
	    addTagDialogMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    ArrayList<Book> books = searchResults.getSelected();
		    if (books.size() > 0) {

			AddTag dialog = new AddTag(thisClass, searchResults,
				books);
			dialog.setLocationRelativeTo(content);
			dialog.setVisible(true);
			setStatus("Adding tags...");

			draw();
		    }
		}
	    });
	}
	return addTagDialogMenuItem;
    }

    /**
     * This method initializes connectToMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem connectTo() {
	if (connectToMenuItem == null) {
	    connectToMenuItem = new JMenuItem("Connect To...");
	    connectToMenuItem.setMnemonic(KeyEvent.VK_T);
	    connectToMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_T, ActionEvent.CTRL_MASK));
	    connectToMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    ConnectTo dialog = new ConnectTo(thisClass, control,
			    treeView);
		    dialog.setLocationRelativeTo(content);
		    dialog.setVisible(true);
		    setStatus("Connecting...");

		    draw();
		}
	    });
	}
	return connectToMenuItem;
    }

    /**
     * 
     */
    protected void draw() {
	treeView.draw();
	validate();
	repaint();
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem exportLibraryMenuItem() {
	if (exportLibrary == null) {
	    exportLibrary = new JMenuItem("Export Library...", KeyEvent.VK_E);
	    exportLibrary.setAccelerator(KeyStroke.getKeyStroke("F5"));
	    exportLibrary.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    JFileChooser export = new JFileChooser();
		    export.setName("Export to...");
		    int result = export.showOpenDialog(thisClass);

		    if (result == JFileChooser.APPROVE_OPTION) {

			setStatus("Exporting library...");
			File curFile = export.getSelectedFile();
			try {
			    curFile.createNewFile();
			    control.writeOut(curFile);
			} catch (IOException e) {
			    setStatus("Error Exporting Library");
			}
		    }
		}
	    });
	}
	return exportLibrary;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem exportSelectedMenuItem() {
	if (exportSelectedBooksMenuItem == null) {
	    exportSelectedBooksMenuItem = new JMenuItem("Export Selected Books");
	    exportSelectedBooksMenuItem.setMnemonic(KeyEvent.VK_X);
	    exportSelectedBooksMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_X, ActionEvent.CTRL_MASK));

	    exportSelectedBooksMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    addBookshelfDialog = new AddBookshelf(thisClass, control,
			    searchResults, treeView, searchResults
				    .getSelected());
		    addBookshelfDialog.setLocationRelativeTo(content);
		    addBookshelfDialog.setVisible(true);
		    setStatus("Exporting books...");

		    draw();
		}
	    });
	}
	return exportSelectedBooksMenuItem;
    }

    /**
     * This method initializes aboutMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAboutMenuItem() {

	if (aboutMenuItem == null) {
	    aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
	    aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
	    aboutMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    Help help = new Help(thisClass, true);
		    help.setLocationRelativeTo(content);
		    help.setVisible(true);

		    draw();
		}
	    });
	}

	return aboutMenuItem;
    }

    /**
     * This method initializes addBookMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAddBookMenuItem() {
	if (addBookMenuItem == null) {
	    addBookMenuItem = new JMenuItem("Add Book");
	    addBookMenuItem.setMnemonic(KeyEvent.VK_B);
	    addBookMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_B, ActionEvent.CTRL_MASK));
	    addBookMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    addBookDialog = new AddBook(thisClass, control, shelf,
			    searchResults, treeView);
		    addBookDialog.setLocationRelativeTo(content);
		    addBookDialog.setVisible(true);
		    setStatus("Adding a book...");

		    draw();
		}
	    });
	}
	return addBookMenuItem;
    }

    /**
     * This method initializes addBookshelfMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAddBookshelfMenuItem() {
	if (addBookshelfMenuItem == null) {
	    addBookshelfMenuItem = new JMenuItem("Add Bookshelf");
	    addBookshelfMenuItem.setMnemonic(KeyEvent.VK_S);
	    addBookshelfMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_S, ActionEvent.CTRL_MASK));

	    addBookshelfMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Adding a bookshelf...");
		    addBookshelfDialog = new AddBookshelf(thisClass, control,
			    searchResults, treeView);
		    addBookshelfDialog.setLocationRelativeTo(content);
		    addBookshelfDialog.setVisible(true);

		    draw();
		}
	    });
	}
	return addBookshelfMenuItem;
    }

    /**
     * This method initializes bookMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getBookMenu() {
	if (bookMenu == null) {
	    bookMenu = new JMenu("Book");
	    bookMenu.setMnemonic(KeyEvent.VK_K);
	    bookMenu.add(renameBookMenuItem());
	    bookMenu.addSeparator();
	    bookMenu.add(addTagMenuItem());
	    bookMenu.add(getRemoveTagDialogMenuItem());
	}
	return bookMenu;
    }

    /**
     * This method initializes bookshelfMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getBookshelfMenu() {
	if (bookshelfMenu == null) {
	    bookshelfMenu = new JMenu("Bookshelf");
	    bookshelfMenu.setMnemonic(KeyEvent.VK_O);

	    bookshelfMenu.add(getRenameBookshelfMenuItem());
	    bookshelfMenu.addSeparator();
	    bookshelfMenu.add(getAddBookMenuItem());
	    bookshelfMenu.add(getDeleteSelectedBooksMenuItem());
	    bookshelfMenu.add(getClearBookshelfMenuItem());
	    bookshelfMenu.addSeparator();
	    bookshelfMenu.add(exportSelectedMenuItem());
	    bookshelfMenu.add(sortBookshelfMenuItem());

	}
	return bookshelfMenu;
    }

    /**
     * This method initializes clearBookshelfMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getClearBookshelfMenuItem() {
	if (clearBookshelfMenuItem == null) {
	    clearBookshelfMenuItem = new JMenuItem("Clear Bookshelf");
	    clearBookshelfMenuItem.setMnemonic(KeyEvent.VK_C);
	    clearBookshelfMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_C, ActionEvent.CTRL_MASK));

	    clearBookshelfMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Clearing bookshelf...");
		    shelf = searchResults.getBookshelf();
		    if (shelf != null) {

			shelf.removeAll();
			searchResults.setResults(shelf);
			draw();
		    }
		}
	    });
	}
	return clearBookshelfMenuItem;
    }

    /**
     * This method initializes content
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getContent() {
	if (content == null) {
	    content = new JPanel();
	    content.setLayout(new BorderLayout());
	    content.add(getSearchToolbar(), BorderLayout.NORTH);
	    content.add(getJSplitPane(), BorderLayout.CENTER);
	    content.add(getStatusBar(), BorderLayout.SOUTH);
	}
	return content;
    }

    /**
     * This method initializes deleteBookshelfMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getDeleteBookshelfMenuItem() {
	if (deleteBookshelfMenuItem == null) {
	    deleteBookshelfMenuItem = new JMenuItem("Delete Bookshelf");
	    deleteBookshelfMenuItem.setMnemonic(KeyEvent.VK_E);
	    deleteBookshelfMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_E, ActionEvent.CTRL_MASK));

	    deleteBookshelfMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    if (searchResults != null) {
			Bookshelf shelf = searchResults.getBookshelf();
			if (shelf != null) {

			    setStatus("Deleting a bookshelf...");
			    control.removeBookshelf(shelf);
			    searchResults.setResults(null);
			    treeView.refresh();
			    draw();
			}
		    }
		}
	    });
	}
	return deleteBookshelfMenuItem;
    }

    /**
     * This method initializes deleteSelectedBooksMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getDeleteSelectedBooksMenuItem() {
	if (deleteSelectedBooksMenuItem == null) {
	    deleteSelectedBooksMenuItem = new JMenuItem("Delete Selected Books");
	    deleteSelectedBooksMenuItem.setMnemonic(KeyEvent.VK_D);
	    deleteSelectedBooksMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_D, ActionEvent.CTRL_MASK));
	    deleteSelectedBooksMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Removing books...");
		    searchResults.removeSelected();
		    draw();
		}
	    });
	}
	return deleteSelectedBooksMenuItem;
    }

    /**
     * This method initializes disconnectMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getDisconnectMenuItem() {
	if (disconnectMenuItem == null) {
	    disconnectMenuItem = new JMenuItem("Disconnect From...");
	    disconnectMenuItem.setMnemonic(KeyEvent.VK_F);
	    disconnectMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_F, ActionEvent.CTRL_MASK));
	    disconnectMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    Disconnect dialog = new Disconnect(thisClass, control,
			    treeView);
		    dialog.setLocationRelativeTo(content);
		    dialog.setVisible(true);
		    setStatus("Disconnecting...");

		    draw();
		}
	    });
	}
	return disconnectMenuItem;
    }

    /**
     * This method initializes helpMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getHelpMenuItem() {
	if (helpMenuItem == null) {
	    helpMenuItem = new JMenuItem("Help", KeyEvent.VK_H);
	    helpMenuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
	    helpMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    Help help = new Help(thisClass);
		    help.setLocationRelativeTo(content);
		    help.setVisible(true);

		    draw();
		}
	    });
	}
	return helpMenuItem;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
	if (jJMenuBar == null) {
	    jJMenuBar = new JMenuBar();
	    jJMenuBar.add(getStartMenu());
	    jJMenuBar.add(getLibraryMenu());
	    jJMenuBar.add(getBookshelfMenu());
	    jJMenuBar.add(getBookMenu());
	}
	return jJMenuBar;
    }

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
	if (jSplitPane == null) {
	    jSplitPane = new JSplitPane();
	    jSplitPane.setRightComponent(getSearchResults());
	    jSplitPane.setLeftComponent(getTreeView());
	}
	return jSplitPane;
    }

    /**
     * This method initializes libraryMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getLibraryMenu() {
	if (libraryMenu == null) {
	    libraryMenu = new JMenu("Library");
	    libraryMenu.setMnemonic(KeyEvent.VK_I);
	    libraryMenu.add(getAddBookshelfMenuItem());
	    libraryMenu.add(getDeleteBookshelfMenuItem());
	    libraryMenu.addSeparator();
	    libraryMenu.add(exportLibraryMenuItem());
	    libraryMenu.add(importLibraryMenuItem());
	}
	return libraryMenu;
    }

    /**
     * This method initializes removeTagDialogMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getRemoveTagDialogMenuItem() {
	if (removeTagDialogMenuItem == null) {
	    removeTagDialogMenuItem = new JMenuItem("Un-Tag");
	    removeTagDialogMenuItem.setMnemonic(KeyEvent.VK_U);
	    removeTagDialogMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_U, ActionEvent.CTRL_MASK));
	    removeTagDialogMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Removing tags...");
		    ArrayList<Book> books = searchResults.getSelected();
		    if (books.size() > 0) {

			UnTag dialog = new UnTag(thisClass, searchResults,
				books);
			dialog.setLocationRelativeTo(content);
			dialog.setVisible(true);

			draw();
		    }
		}
	    });
	}
	return removeTagDialogMenuItem;
    }

    /**
     * This method initializes renameBookshelfMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getRenameBookshelfMenuItem() {
	if (renameBookshelfMenuItem == null) {
	    renameBookshelfMenuItem = new JMenuItem("Rename Bookshelf");
	    renameBookshelfMenuItem.setMnemonic(KeyEvent.VK_N);
	    renameBookshelfMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	    renameBookshelfMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Renaming a bookshelf...");
		    shelf = searchResults.getBookshelf();
		    if (shelf != null) {
			renameBookshelfDialog = new RenameBookshelf(thisClass,
				control, shelf, treeView);
			renameBookshelfDialog.setLocationRelativeTo(content);
			renameBookshelfDialog.setVisible(true);

			draw();
		    }
		}
	    });
	}
	return renameBookshelfMenuItem;
    }

    /**
     * This method initializes searchButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getSearchButton() {
	if (searchButton == null) {
	    searchButton = new JButton("Search...");
	    searchButton.setBorderPainted(true);

	    searchButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Searching...");
		    shelf = control.searchOn((String) searchFilterComboBox
			    .getSelectedItem(), searchField.getText());
		    searchResults.setResults(shelf);

		    draw();
		}
	    });
	}
	return searchButton;
    }

    /**
     * This method initializes searchResults
     * 
     * @return main.SearchResults
     */
    private SearchResults getSearchResults() {
	if (searchResults == null) {
	    searchResults = new SearchResults(control);
	}
	return searchResults;
    }

    /**
     * This method initializes searchToolbar
     * 
     * @return javax.swing.JToolBar
     */
    private JToolBar getSearchToolbar() {
	if (searchToolbar == null) {
	    withinLabel = new JLabel();
	    withinLabel.setText(" in: ");
	    searchToolbar = new JToolBar();
	    searchToolbar.setFloatable(false);
	    searchToolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
	    searchToolbar.add(getTextField());
	    searchToolbar.add(withinLabel);
	    searchToolbar.add(searchFilter());
	    searchToolbar.add(getSearchButton());
	}

	return searchToolbar;
    }

    /**
     * This method initializes startMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getStartMenu() {
	if (startMenu == null) {
	    startMenu = new JMenu("Start");
	    startMenu.setMnemonic(KeyEvent.VK_T);

	    startMenu.add(connectTo());
	    startMenu.add(getDisconnectMenuItem());
	    startMenu.addSeparator();
	    startMenu.add(getHelpMenuItem());
	    startMenu.add(getAboutMenuItem());
	    startMenu.addSeparator();
	    startMenu.add(quit());
	}
	return startMenu;
    }

    public JToolBar getStatusBar() {
	if (statusBar == null) {

	    statusBar = new JToolBar();
	    statusBar.setFloatable(false);
	    statusBar.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
	    status = new JLabel();
	    status.setText("Idle");

	    statusBar.add(new JLabel("Status: "));
	    statusBar.add(status);
	}
	return statusBar;
    }

    /**
     * This method initializes searchField
     * 
     * @return java.awt.TextField
     */
    private JTextField getTextField() {
	if (searchField == null) {
	    searchField = new JTextField();
	    searchField.setColumns(30);
	    searchField.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    int key = e.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
			searchButton.doClick();
		    }
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
	    treeView = new TreeView(control, searchResults);
	}
	return treeView;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem importLibraryMenuItem() {
	if (importLibrary == null) {
	    importLibrary = new JMenuItem("Import Library...", KeyEvent.VK_I);
	    importLibrary.setAccelerator(KeyStroke.getKeyStroke("F6"));
	    importLibrary.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    JFileChooser importDialog = new JFileChooser();
		    importDialog.setName("Import File");
		    int result = importDialog.showOpenDialog(thisClass);

		    if (result == JFileChooser.APPROVE_OPTION) {

			File curFile = importDialog.getSelectedFile();
			setStatus("Importing a library...");
			try {
			    control.readInLibrary(curFile);
			} catch (FileNotFoundException e) {
			    setStatus("Error Importing Library");
			}
			treeView.refresh();

		    }
		}
	    });
	}
	return importLibrary;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	try {
	    control = new Controller();

	    this.setSize(800, 800);
	    this.setMinimumSize(new Dimension(650, 400));
	    this.setJMenuBar(getJJMenuBar());
	    this.setContentPane(getContent());
	    this.setTitle("Book Butler");

	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    e.printStackTrace();
	}
    }

    /**
     * This method initializes quitMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem quit() {
	if (quitMenuItem == null) {
	    quitMenuItem = new JMenuItem("Quit");
	    quitMenuItem.setMnemonic(KeyEvent.VK_Q);
	    quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
		    ActionEvent.CTRL_MASK));
	    quitMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setStatus("Quitting...");
		    dispose();
		    System.exit(0);
		}
	    });

	}
	return quitMenuItem;
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem renameBookMenuItem() {
	if (renameBookDialogMenuItem == null) {
	    renameBookDialogMenuItem = new JMenuItem("Edit Book");
	    renameBookDialogMenuItem.setMnemonic(KeyEvent.VK_I);
	    renameBookDialogMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		    KeyEvent.VK_I, ActionEvent.CTRL_MASK));
	    renameBookDialogMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    ArrayList<Book> result = searchResults.getSelected();
		    if (result.size() > 0) {

			RenameBook dialog = new RenameBook(thisClass,
				searchResults, result.get(0));
			dialog.setLocationRelativeTo(content);
			dialog.setVisible(true);
			setStatus("Renaming a book...");
			draw();
		    }
		}
	    });
	}
	return renameBookDialogMenuItem;
    }

    public void resetSearchOptions() {
	if (searchFilterComboBox != null && control != null) {
	    searchFilterComboBox.setModel(new DefaultComboBoxModel(control
		    .searchOptions().toArray(new String[0])));
	    searchFilterComboBox.setSelectedIndex(0);
	}
    }

    /**
     * This method initializes jComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox searchFilter() {
	if (searchFilterComboBox == null) {
	    searchFilterComboBox = new JComboBox();
	    resetSearchOptions();
	}
	return searchFilterComboBox;
    }

    public void setStatus(String s) {

	// Clear status after...
	ActionListener clearText = new ActionListener() {
	    public void actionPerformed(ActionEvent event) {

		status.setText("Idle");
	    }
	};
	Timer time = new javax.swing.Timer(5000, clearText);
	time.setInitialDelay(5000);
	time.start();
	time.setRepeats(false);
	status.setText(s);
    }

    /**
     * This method initializes jMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem sortBookshelfMenuItem() {
	if (sortBookshelf == null) {
	    sortBookshelf = new JMenuItem("Sort Bookshelf");
	    sortBookshelf.setMnemonic(KeyEvent.VK_S);
	    sortBookshelf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		    ActionEvent.CTRL_MASK));
	    sortBookshelf.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    Bookshelf oldShelf = searchResults.getBookshelf();
		    if (oldShelf != null) {

			// TODO Implement butler sorting in Controller
			// Set<Bookshelf> newShelves =
			// control.sortBookshelf(oldShelf, 10);
			// for (int x = 0; x < newShelves.size(); x++) {
			// control.addBookshelf(newShelves.get(x));
			// }
			treeView.refresh();
		    }
		}
	    });
	}
	return sortBookshelf;
    }

}
