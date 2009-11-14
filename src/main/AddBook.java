package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.Book;
import data.Bookshelf;

public class AddBook extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField = null;
	private JTextField jTextField1 = null;
	private JButton commit = null;
	private JButton cancel = null;

	private Controller control = null;
	private Bookshelf shelf = null;
	private Book book = null;
	private SearchResults results = null;
	private TreeView tree = null;

	public Bookshelf getShelf() {
		return shelf;
	}

	public Book getBook() {
		return book;
	}

	/**
	 * @param owner
	 * @param treeView
	 */
	public AddBook(Frame owner, Controller ctl, Bookshelf b,
			SearchResults searchResults, TreeView treeView) {
		super(owner);

		control = ctl;

		tree = treeView;

		if (ctl.focus == null) {
			shelf = control.addBookshelf("New Bookshelf");
			tree.refresh();
		} else {
			shelf = b;
		}

		results = searchResults;

		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {

			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 3;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Book Author");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Book Title: ");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabel, gridBagConstraints);
			jContentPane.add(jLabel1, gridBagConstraints1);
			jContentPane.add(getJTextField(), gridBagConstraints2);
			jContentPane.add(getJTextField1(), gridBagConstraints3);
			jContentPane.add(getJButton(), gridBagConstraints4);
			jContentPane.add(getJButton2(), gridBagConstraints5);

			try {
				UIManager
						.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
			} catch (UnsupportedLookAndFeelException ex) {
				System.out
						.println("Cannot set new Theme for Java Look and Feel.");
			}
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField(14);
		}
		return jTextField;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField1() {
		if (jTextField1 == null) {
			jTextField1 = new JTextField(14);
		}
		return jTextField1;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (commit == null) {
			commit = new JButton("Commit");
			commit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					book = control.addBook(jTextField.getText(), jTextField1
							.getText());

					results.setResults(control.focus);

					setVisible(false);

				}
			});
		}

		return commit;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton2() {
		if (cancel == null) {
			cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {

					setVisible(false);

				}
			});
		}
		return cancel;
	}

}
