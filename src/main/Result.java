package main;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import data.*;

public class Result extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel RightNavigation = null;
	private JPanel Title = null;
	private JButton save = null;
	private JButton send = null;
	private JLabel title = null;
	private JPanel Content = null;
	private JProgressBar progressBar = null;
	
	private Book book;
	private Bookshelf bookshelf;
	private Library library;
	private String tag;
	
	/**
	 * This is the default constructor
	 */
	public Result() {
		super();
		initialize();
	}
	
	public Result(Book b) {
		super();
		book = b;
		initialize();
	}

	public Result(Bookshelf b) {
		super();
		bookshelf = b;
		initialize();
	}
	
	public Result(Library l) {
		super();
		library = l;
		initialize();
	}
	
	public Result(String t) {
		super();
		tag = t;
		initialize();
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
	}

	/**
	 * This method initializes RightNavigation	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRightNavigation() {
		if (RightNavigation == null) {
			RightNavigation = new JPanel();
			RightNavigation.setLayout(new BoxLayout(getRightNavigation(), BoxLayout.Y_AXIS));
			RightNavigation.add(getSave(), null);
			RightNavigation.add(getSend(), null);
		}
		return RightNavigation;
	}

	/**
	 * This method initializes Title	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTitle() {
		if (Title == null) {
			title = new JLabel();
			title.setText("Node Title");
			Title = new JPanel();
			Title.setLayout(new BoxLayout(getTitle(), BoxLayout.X_AXIS));
			Title.add(title, null);
		}
		return Title;
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
		}
		return send;
	}

	/**
	 * This method initializes Content	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContent() {
		if (Content == null) {
			Content = new JPanel();
			Content.setLayout(new GridBagLayout());
		}
		return Content;
	}

	/**
	 * This method initializes progressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 * 
	 * @deprecated So this isn't really "deprecated" but you should know that is should be disabled if there isn't any progress...
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
		}
		return progressBar;
	}

}  //  @jve:decl-index=0:visual-constraint="207,107"
