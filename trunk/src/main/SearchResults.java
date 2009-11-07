package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

import data.Book;
import data.Bookshelf;
import data.VirtualBook;
import java.awt.FlowLayout;

public class SearchResults extends JPanel {

	private static final long serialVersionUID = 1L;

	private Bookshelf bookshelf;
	List<Result> results;

	/**
	 * This is the default constructor
	 */
	public SearchResults() {
		super();
		initialize();

		// Just a demo, will be adding full event/interaction logic...
		// addDemoResult();
	}

	private void addDemoResult() {
		results.add(new Result(new VirtualBook("Andre Gorz", "Traitor")));
		results
				.add(new Result(
						new VirtualBook("Vegan Cooks", "Vegan Cookbook")));
		results.add(new Result(new VirtualBook("John Cage", "Silence")));
		addResults();
	}

	protected Result addResult(Result result) {

		this.add(result);
		this.repaint();
		super.repaint();
		return result;
	}

	protected void setResults(Bookshelf shelf) {

		bookshelf = shelf;
		removeResults();

		Iterator<Book> result = bookshelf.enumerate();
		while (result.hasNext()) {
			results.add(new Result(result.next()));
		}

		addResults();
	}

	public void resetResults() {
		setResults(bookshelf);
	}

	private void addResults() {

		for (Result r : results) {
			this.add(r);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(529, 381);
		this.setLayout(new FlowLayout());

		results = new ArrayList<Result>();
		try {
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Cannot set new Theme for Java Look and Feel.");
		}
	}

	protected Result removeResult(Result result) {

		this.remove(result);
		return result;
	}

	private void removeResults() {
		for (Result r : results) {
			this.remove(r);
		}
		results.clear();
	}

} // @jve:decl-index=0:visual-constraint="266,31"
