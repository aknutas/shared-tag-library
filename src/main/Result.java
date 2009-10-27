package main;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

import data.Book;
import data.Bookshelf;
import data.Library;
import data.VirtualBook;
import data.VirtualBookshelf;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.JTextField;

public class Result extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel RightNavigation = null;
    private JPanel Title = null;
    private JButton save = null;
    private JButton send = null;
    private JLabel title = null;
    private JPanel Content = null;
    private JProgressBar progressBar = null;

    private VirtualBook book = null;
    private VirtualBookshelf bookshelf = null;
    private Library library = null;
    private JTextField jTextField = null;

    protected void draw() {
	validate();
	this.repaint();
	super.repaint();
    }

    /**
     * This is the default constructor
     */
    public Result() {
	super();
	initialize();

    }

    public Result(Library l) {
	super();
	library = l;
	initialize();
    }

    public Result(Book b) {
	super();
	book = (VirtualBook) b;
	initialize();
    }

    public Result(Bookshelf b) {
	super();
	bookshelf = (VirtualBookshelf) b;
	initialize();
    }

    /**
     * This method initializes Content
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getContent() {
	if (Content == null) {

	    Content = new JPanel();
	    Content.setLayout(new FlowLayout());
	}

	if (book != null) {
	    Iterator<Entry<String, String>> properties = book
		    .enumerateProperties();
	    while (properties.hasNext()) {

		Entry<String, String> e = properties.next();
		if ((e.getKey().compareTo("title") != 0)
			&& (e.getKey().compareTo("author") != 0)) {
		    JLabel title = new JLabel(e.getKey());
		    JLabel value = new JLabel(e.getValue());
		    Content.add(title, null);
		    Content.add(value, null);
		}

		draw();
		// TODO: Make labels editable
	    }

	} else if (bookshelf != null) {
	    // not implemented
	} else {
	    // not implemented (library or tag)
	}

	JLabel add = new JLabel("Add Tag");
	Content.add(add, null);
	Content.add(getJTextField(), null);
	return Content;
    }

    /**
     * This method initializes progressBar
     * 
     * @return javax.swing.JProgressBar
     * 
     * @deprecated So this isn't really "deprecated" but you should know that is
     *             should be disabled if there isn't any progress...
     */
    private JProgressBar getProgressBar() {
	if (progressBar == null) {
	    progressBar = new JProgressBar();
	}
	return progressBar;
    }

    /**
     * This method initializes RightNavigation
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getRightNavigation() {
	if (RightNavigation == null) {
	    RightNavigation = new JPanel();
	    RightNavigation.setLayout(new BoxLayout(getRightNavigation(),
		    BoxLayout.Y_AXIS));
	    // RightNavigation.add(getSave(), null);
	    // RightNavigation.add(getSend(), null);
	}
	return RightNavigation;
    }

    /**
     * This method initializes save
     * 
     * @return javax.swing.JButton
     */
    private JButton getSave() {
	if (save == null) {
	    save = new JButton();
	    save.setName("jButton");
	    save.setText("Save");
	    save.setAlignmentY(CENTER_ALIGNMENT);
	}
	return save;
    }

    /**
     * This method initializes send
     * 
     * @return javax.swing.JButton
     */
    private JButton getSend() {
	if (send == null) {
	    send = new JButton();
	    send.setText("Send");
	    send.setName("jButton1");
	    send.setText("Send");
	    send.setAlignmentY(CENTER_ALIGNMENT);
	}
	return send;
    }

    /**
     * This method initializes Title
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTitle() {
	if (Title == null) {

	    title = new JLabel("Title?");
	    if (book != null) {
		title.setText("Book: " + book.getProperty("title") + " | By: "
			+ book.getProperty("author"));
	    } else if (bookshelf != null) {
		title.setText("Bookshelf: " + bookshelf.getProperty("name"));
	    }

	    Title = new JPanel();
	    Title.setLayout(new BoxLayout(getTitle(), BoxLayout.X_AXIS));
	    Title.add(title, null);
	    Title.setAlignmentX(CENTER_ALIGNMENT);
	    Title.setAlignmentY(CENTER_ALIGNMENT);
	}
	return Title;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setLayout(new BorderLayout());
	this.setSize(827, 260);
	this.add(getRightNavigation(), BorderLayout.EAST);
	this.add(getTitle(), BorderLayout.NORTH);
	this.add(getContent(), BorderLayout.CENTER);
	this.add(getProgressBar(), BorderLayout.SOUTH);
	try {
	    UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    System.out.println("Cannot set new Theme for Java Look and Feel.");
	}

	draw();
    }

    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getJTextField() {
	if (jTextField == null) {
	    jTextField = new JTextField(15);
	}

	jTextField.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {

		if (book != null) {
		    book.setProperty("tag", jTextField.getText());
		}
		
		draw();
		// Refresh current bookshelf view
	    }
	});

	return jTextField;
    }

} // @jve:decl-index=0:visual-constraint="207,107"
