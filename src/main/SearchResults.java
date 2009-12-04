package main;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Book;
import data.Bookshelf;

public class SearchResults extends JScrollPane {

    private static final long serialVersionUID = 1L;

    public static JPanel panel = new JPanel();

    private Bookshelf bookshelf; // @jve:decl-index=0:
    List<Result> results;
    Controller control = null;
    GridBagConstraints gbc;

    /**
     * This is the default constructor
     */
    public SearchResults(Controller c) {
	super(panel);
	control = c;
	initialize();
    }

    protected Result addResult(Result result) {
	panel.add(result, gbc);
	gbc.gridy = gbc.gridy + 1;
	panel.repaint();
	this.repaint();
	return result;
    }

    private void addResults() {

	for (Result r : results) {
	    panel.add(r, gbc);
	    gbc.gridy = gbc.gridy + 1;
	    r.draw();
	}

	validate();
	this.repaint();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	GridBagLayout layout = new GridBagLayout();
	panel.setLayout(layout);
	panel.setBackground(Color.WHITE);
	results = new ArrayList<Result>();

	gbc = new GridBagConstraints();
	gbc.gridy = 0;
	gbc.weightx = 1; // 1: Fill, 0: Center
	gbc.weighty = 0;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.insets = new Insets(5, 5, 5, 5);

	try {
	    UIManager
		    .setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    System.out.println("Cannot set new Theme for Java Look and Feel.");
	}
    }

    protected Result removeResult(Result result) {

	panel.remove(result);

	control.removeBook(bookshelf, result.getBook());
	resetResults();

	return result;
    }

    private void removeResults() {
	for (Result r : results) {
	    panel.remove(r);
	}
	results.clear();
    }

    protected void removeSelected() {

	for (Result r : results) {
	    if (r.isSelected()) {
		panel.remove(r);
		control.removeBook(bookshelf, r.getBook());
	    }
	}

	resetResults();
    }

    public void resetResults() {
	setResults(bookshelf);
    }

    protected void setResults(Bookshelf shelf) {
	if (shelf != null) {
	    bookshelf = shelf;
	    removeResults();

	    Iterator<Book> result = bookshelf.iterator();
	    System.out.println("hasnext:" + result.hasNext());
	    int count = 0;
	    while (result.hasNext() && count < 20) {
		Book b = result.next();
		System.out.println("TITLE: " + b.getProperty("title"));
		results.add(new Result(b, this));
		count++;
	    }

	    addResults();
	}
    }

} // @jve:decl-index=0:visual-constraint="266,31"
