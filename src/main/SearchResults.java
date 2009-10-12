package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class SearchResults extends JPanel {

    private static final long serialVersionUID = 1L;

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

    protected Result addResult(Result result) {

	this.add(result);
	this.repaint();
	super.repaint();
	return result;
    }

    protected Result removeResult(Result result) {

	this.remove(result);
	return result;
    }

    private void addDemoResult() {
	results.add(new Result());
	results.add(new Result());
	results.add(new Result());
	addResults();
    }

    private void addResults() {
	for (Result r : results) {
	    this.add(r);
	}
    }

    private void removeResults() {
	for (Result r : results) {
	    this.remove(r);
	}
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(529, 381);
	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	results = new ArrayList<Result>();
	try {
	    UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    System.out.println("Cannot set new Theme for Java Look and Feel.");
	}
    }

} // @jve:decl-index=0:visual-constraint="266,31"
