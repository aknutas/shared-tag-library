package main;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;

public class StoredResults extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * This is the default constructor
	 */
	public StoredResults() {
		super();
		initialize();
	}
	
	protected Result addResult(Result result) {
		
		this.add(result);
		return result;
	}
	
	protected Result removeResult(Result result) {
		
		this.remove(result);
		return result;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(241, 378);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		try {
			UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel()); 
		    }
		    catch ( UnsupportedLookAndFeelException ex ){
		      System.out.println("Cannot set new Theme for Java Look and Feel.");
		    }
	}

}  //  @jve:decl-index=0:visual-constraint="419,27"
