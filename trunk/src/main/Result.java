package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * @author patrick
 * 
 */
public class Result extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel RightNavigation = null;
	private JPanel Title = null;
	private JButton save = null;
	private JButton send = null;
	private JLabel title = null;
	private JPanel Content = null;
	private JProgressBar progressBar = null;

	private Book book = null;
	private Bookshelf bookshelf = null;
	private Library library = null;
	private JTextField tagContent = null;

	GridBagConstraints tagContentConstraints;
	GridBagConstraints tagTitleConstraints;
	GridBagConstraints tagConstraints;

	/**
     * 
     */
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

	/**
	 * @param l
	 */
	public Result(Library l) {
		super();
		library = l;
		initialize();
	}

	/**
	 * @param b
	 */
	public Result(Book b) {
		super();
		book = b;
		initialize();
	}

	/**
	 * @param b
	 */
	public Result(Bookshelf b) {
		super();
		bookshelf = b;
		initialize();
	}

	/**
	 * This method initializes Content
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContent() {
		if (Content == null) {

			tagContentConstraints = new GridBagConstraints();
			tagContentConstraints.gridx = 1;
			tagContentConstraints.gridy = 0;
			tagContentConstraints.weightx = .8;
			tagContentConstraints.insets = new Insets(5, 0, 5, 5);
			tagContentConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			tagConstraints = new GridBagConstraints();
			tagConstraints.gridx = 2;
			tagConstraints.gridy = 0;
			tagConstraints.weightx = .8;
			tagConstraints.insets = new Insets(5, 0, 5, 5);
			tagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			tagTitleConstraints = new GridBagConstraints();
			tagTitleConstraints.insets = new Insets(8, 5, 5, 5);
			tagTitleConstraints.gridy = 0;
			tagTitleConstraints.gridx = 0;
			tagTitleConstraints.anchor = GridBagConstraints.FIRST_LINE_END;

			Content = new JPanel();
			Content.setLayout(new GridBagLayout());

			Content.add(new JLabel("Add Tag: "), tagTitleConstraints);
			Content.add(new JLabel("Tags: "), tagConstraints);
			Content.add(getJTextField(), tagContentConstraints);
		}

		int yOffset = 10;
		if (book != null) {

			Iterator<Entry<String, Integer>> properties = book.enumerateTags();
			while (properties.hasNext()) {

				Entry<String, Integer> e = properties.next();

				JLabel value = new JLabel(e.getKey());
				value.setFont(new Font("Sans", Font.BOLD, 14));

				tagConstraints.gridy = yOffset;

				Content.add(value, tagConstraints);

				++yOffset;
			}

			draw();

		}

		return Content;
	}

	private void addTag(String s) {

		if (Content != null && !s.isEmpty()) {

			JLabel value = new JLabel(s);
			value.setFont(new Font("Sans", Font.BOLD, 14));

			tagConstraints.gridy = tagConstraints.gridy + 10;

			Content.add(value, tagConstraints);
		}
	}

	/**
	 * This method initializes progressBar
	 * 
	 * @return javax.swing.JProgressBar
	 * 
	 * @deprecated So this isn't really "deprecated" but you should know that is
	 *             should be disabled if there isn't any progress...
	 */
	@Deprecated
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
		this.setSize(500, 200);
		this.setBounds(new Rectangle(0, 0, 500, 200));
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
	 * This method initializes tagContent
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (tagContent == null) {
			tagContent = new JTextField(15);
		}

		tagContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (book != null) {

					book.tag(tagContent.getText());
					addTag(tagContent.getText());
				}

				draw();
			}
		});

		return tagContent;
	}

}
