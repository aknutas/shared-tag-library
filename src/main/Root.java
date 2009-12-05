package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Bookshelf;
import data.RemoteObjectException;

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
		thisClass = new Root();
		thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thisClass.setVisible(true);
		try {
		    UIManager
			    .setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
		    System.out
			    .println("Cannot set new Theme for Java Look and Feel.");
		}
	    }
	});
    }

    private AddBook addBookDialog = null;

    private JMenuItem addBookMenuItem = null;

    private AddBookshelf addBookshelfDialog = null;

    private JMenuItem addBookshelfMenuItem = null;

    private JMenu bookMenu = null;

    private JMenu bookshelfMenu = null;

    private JMenuItem clearBookshelfMenuItem = null;

    private JMenuItem connectToMenuItem = null;

    private JPanel content = null;

    private Controller control = null;

    private JMenuItem deleteBookshelfMenuItem = null;

    private JMenuItem deleteSelectedBooksMenuItem = null;

    private JMenuItem disconnectMenuItem = null;

    private JMenuItem exportSelectedBooksMenuItem = null;

    private JMenuBar jJMenuBar = null;

    private JSplitPane jSplitPane = null;

    private JMenu libraryMenu = null;

    private javax.swing.Timer msgTimer;

    private MsgTrigger msgTrigger;
    private JMenuItem quitMenuItem = null;
    private RenameBookshelf renameBookshelfDialog = null;
    private JMenuItem renameBookshelfMenuItem = null;
    private TextField searchField = null;
    private Label searchLabel = null;

    private SearchResults searchResults = null;
    private JToolBar searchToolbar = null;

    private Bookshelf shelf = null;
    private JMenu startMenu = null;
    private TreeView treeView = null;

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

		    draw();
		}
	    });
	}
	return exportSelectedBooksMenuItem;
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
	    // Change Title/Author
	    // Add Tag
	    // Clear Tags
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

	    bookshelfMenu.add(getAddBookMenuItem());
	    bookshelfMenu.add(exportSelectedMenuItem());
	    bookshelfMenu.add(getClearBookshelfMenuItem());
	    bookshelfMenu.add(getDeleteSelectedBooksMenuItem());
	    bookshelfMenu.addSeparator();
	    bookshelfMenu.add(getRenameBookshelfMenuItem());
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

		    shelf = searchResults.getBookshelf();
		    if (shelf != null) {
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
	    content.add(getSearchToolbar(), BorderLayout.SOUTH);
	    content.add(getJSplitPane(), BorderLayout.CENTER);
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

			    System.out.println("Removing... "
				    + shelf.getProperty("name"));
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

		    draw();
		}
	    });
	}
	return disconnectMenuItem;
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
	}
	return libraryMenu;
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
	    searchToolbar = new JToolBar();
	    searchToolbar.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
	    searchLabel = new Label("Search: ", Label.LEFT);
	    searchToolbar.add(searchLabel);
	    searchToolbar.add(getTextField());
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
	    startMenu.add(quit());

	}
	return startMenu;
    }

    /**
     * This method initializes searchField
     * 
     * @return java.awt.TextField
     */
    private TextField getTextField() {
	if (searchField == null) {
	    searchField = new TextField();
	    searchField.setPreferredSize(new Dimension(300, 20));
	    searchField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    shelf = control.search(searchField.getText());
		    searchResults.setResults(shelf);

		    draw();
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
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	try {
	    control = new Controller();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (RemoteObjectException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	this.setSize(800, 800);
	this.setMinimumSize(new Dimension(650, 400));
	this.setJMenuBar(getJJMenuBar());
	this.setContentPane(getContent());
	this.setTitle("Book Butler");
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

		    dispose();
		    System.exit(0);
		}
	    });

	}
	return quitMenuItem;
    }

}
