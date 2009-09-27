package main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

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
		addDemoResult();
	}
	

	protected Result addResult(Result result) {
		
		this.add(result);
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
	
	private void addResults() {		for (Result r : results) {
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
	}

}  //  @jve:decl-index=0:visual-constraint="266,31"
