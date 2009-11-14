package main;

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

	static JPanel panel = new JPanel();

	private Bookshelf bookshelf;
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
		super.repaint();
		return result;
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

	public void resetResults() {
		setResults(bookshelf);
	}

	private void addResults() {

		for (Result r : results) {
			panel.add(r, gbc);
			gbc.gridy = gbc.gridy + 1;
			r.draw();
		}

		validate();
		panel.repaint();
		this.repaint();
		super.repaint();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		panel.setSize(600, 600);
		panel.setLayout(new GridBagLayout());

		results = new ArrayList<Result>();

		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTH;
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

} // @jve:decl-index=0:visual-constraint="266,31"
