package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Help extends JDialog {

    private static final long serialVersionUID = 1L;
    private String[] content = {
	    "Welcometo the Book Butler!\n\nCreate and modify book collections and share them with other people. Along the way, use an intelligent tag weighting system to sort existing collections.\n\nPress links on the left to explore different application features.",
	    "Use the top menu to choose operations. Add a new bookshelf by selecting 'Library' and pressing 'Add Bookshelf'. Operations on the bookshelf, and its books, are located in 'Bookshelf' and 'Book'.\nSelect books by pressing the title of each book.",
	    "You can add tags by selecting the menu item 'Add Tag' under 'Book' with one or more books selected. You can also press 'Details' on a resultant book and use that add tag feature. Tags have unique properties called weights, that can be increased or decreased according to their relative priority. Their priority is used to automatically generate more bookshelves that contain the priority/relevancy sorted tags.",
	    "Michigan Technological University 2009\n\nTeam Software Projects: Team One\n\tAndrew Alm\n\tRobert Amundson\n\tAntti Knutas\n\tSteven Purol\n\tPatrick Ranspach" };
    private int focus = 0;
    private JPanel jContentPane = null;
    private JList jList = null;
    private JTextPane jTextPane = null;
    private String[] topics = { "Intro", "Basics", "Tags", "About" };

    /**
     * @param owner
     */
    public Help(Frame owner) {
	super(owner);
	initialize();
    }

    /**
     * @param owner
     */
    public Help(Frame owner, Boolean about) {
	super(owner);
	if (about)
	    focus = 3;
	initialize();
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
	    jContentPane.add(getJList(), BorderLayout.WEST);
	    jContentPane.add(getJTextPane(), BorderLayout.CENTER);
	}
	return jContentPane;
    }

    /**
     * This method initializes jList
     * 
     * @return javax.swing.JList
     */
    private JList getJList() {
	if (jList == null) {
	    jList = new JList(topics);
	    jList.setSelectedIndex(focus);
	    jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent listSelectionEvent) {
		    jTextPane.setText(content[((JList) listSelectionEvent
			    .getSource()).getSelectedIndex()]);
		}
	    };
	    jList.addListSelectionListener(listSelectionListener);
	}
	return jList;
    }

    /**
     * This method initializes jTextPane
     * 
     * @return javax.swing.JTextPane
     */
    private JTextPane getJTextPane() {
	if (jTextPane == null) {
	    jTextPane = new JTextPane();
	    jTextPane.setEditable(false);
	    jTextPane.setText(content[focus]);
	}
	return jTextPane;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(420, 320);
	this.setMinimumSize(new Dimension(400, 300));
	this.setContentPane(getJContentPane());
	this.setTitle("Help - About");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
