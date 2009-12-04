package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
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

    private MsgTrigger msgTrigger;

    private javax.swing.Timer msgTimer;

    private Controller control = null;

    private JPanel jContentPane = null;

    private JSplitPane jSplitPane = null;

    private JToolBar jToolBar = null;

    private TextField searchField = null;

    private Label searchLabel = null;

    private Bookshelf shelf = null;

    private SearchResults searchResults = null;
    private TreeView treeView = null;
    private AddBookshelf addBookshelfDialog = null;
    private AddBook addBookDialog = null;
    private JMenuBar jJMenuBar = null;
    private JMenu jMenu = null;

    private JMenuItem quitMenuItem = null;
    private JMenuItem connectToMenuItem = null;

    private JMenu jMenu1 = null;
    private JMenuItem addBookshelfMenuItem = null;
    private JMenuItem addBookMenuItem = null;

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
	    connectToMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    ConnectTo dialog = new ConnectTo(thisClass, control,
			    treeView);
		    dialog.setLocationRelativeTo(jContentPane);
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
     * This method initializes addBookMenuItem
     * 
     * @return javax.swing.JMenuItem
     */
    private JMenuItem getAddBookMenuItem() {
	if (addBookMenuItem == null) {
	    addBookMenuItem = new JMenuItem("Add Book");
	    addBookMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    addBookDialog = new AddBook(thisClass, control, shelf,
			    searchResults, treeView);
		    addBookDialog.setLocationRelativeTo(jContentPane);
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
	    addBookshelfMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    addBookshelfDialog = new AddBookshelf(thisClass, control,
			    searchResults, treeView);
		    addBookshelfDialog.setLocationRelativeTo(jContentPane);
		    addBookshelfDialog.setVisible(true);

		    draw();
		}
	    });
	}
	return addBookshelfMenuItem;
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
	    jContentPane.add(getJToolBar(), BorderLayout.SOUTH);
	    jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
	}
	return jContentPane;
    }

    /**
     * This method initializes jJMenuBar
     * 
     * @return javax.swing.JMenuBar
     */
    private JMenuBar getJJMenuBar() {
	if (jJMenuBar == null) {
	    jJMenuBar = new JMenuBar();
	    jJMenuBar.add(getJMenu());
	    jJMenuBar.add(getJMenu1());
	}
	return jJMenuBar;
    }

    /**
     * This method initializes jMenu
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenu() {
	if (jMenu == null) {
	    jMenu = new JMenu("Start");

	    jMenu.add(quit());

	}
	return jMenu;
    }

    /**
     * This method initializes jMenu1
     * 
     * @return javax.swing.JMenu
     */
    private JMenu getJMenu1() {
	if (jMenu1 == null) {
	    jMenu1 = new JMenu("Library");
	    jMenu1.add(connectTo());
	    jMenu1.add(getAddBookshelfMenuItem());
	    jMenu1.add(getAddBookMenuItem());
	}
	return jMenu1;
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
     * This method initializes jToolBar
     * 
     * @return javax.swing.JToolBar
     */
    private JToolBar getJToolBar() {
	if (jToolBar == null) {
	    jToolBar = new JToolBar();
	    jToolBar.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

	    searchLabel = new Label("Search: ", Label.LEFT);
	    jToolBar.add(searchLabel);
	    jToolBar.add(getTextField());
	}

	return jToolBar;
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
     * This method initializes searchField
     * 
     * @return java.awt.TextField
     */
    private TextField getTextField() {
	if (searchField == null) {
	    searchField = new TextField();
	    searchField.setPreferredSize(new Dimension(800, 20));
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
	this.setSize(1000, 800);
	this.setJMenuBar(getJJMenuBar());
	this.setContentPane(getJContentPane());
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
