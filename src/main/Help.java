package main;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Help extends JDialog {

    private static final long serialVersionUID = 1L;
    private String[] content = {
	    "Hello, I am trying to figure this stuff out...", "TSP Group 1" };
    private int focus = 0;
    private JPanel jContentPane = null;
    private JList jList = null;
    private JTextPane jTextPane = null;
    private String[] topics = { "Intro", "About" };

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
	    focus = 1;
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
	this.setSize(424, 318);
	this.setContentPane(getJContentPane());
	this.setTitle("Help - About");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
