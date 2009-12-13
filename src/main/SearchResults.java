package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.Controller;
import data.Book;
import data.Bookshelf;

public class SearchResults extends JScrollPane {

    public static JPanel panel = new JPanel();

    private static final long serialVersionUID = 1L;

    private Bookshelf bookshelf = null; // @jve:decl-index=0:
    Controller control = null;

    GridBagConstraints gbc = null;
    List<Result> results = null;

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

    public Bookshelf getBookshelf() {
	return bookshelf;
    }

    protected ArrayList<Book> getSelected() {
	ArrayList<Book> books = new ArrayList<Book>();

	for (Result r : results) {
	    if (r.isSelected()) {
		books.add(r.getBook());
	    }
	}

	return books;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	GridBagLayout layout = new GridBagLayout();
	this.setMinimumSize(new Dimension(400, 300));
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
	} else {
	    removeResults();
	    bookshelf = null;
	}
    }

} // @jve:decl-index=0:visual-constraint="266,31"
