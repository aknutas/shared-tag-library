package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import data.Book;

/**
 * @author patrick
 * 
 */
public class Result extends JPanel {

    private static final long serialVersionUID = 1L;
    private Book book = null;
    private JPanel Content = null;
    private JButton delete = null;
    private JToggleButton jToggleButton = null;
    private SearchResults results;

    private Boolean selected = false;

    private Result self = null;

    private JPanel tagArea = null;
    GridBagConstraints tagConstraints = null;
    private JTextField tagContent = null;
    GridBagConstraints tagContentConstraints = null;
    GridBagConstraints tags = null;

    GridBagConstraints tagTitleConstraints = null;
    private JLabel title = null;
    private JPanel Title = null;

    /**
     * @param b
     */
    public Result(Book b, SearchResults c) {
	super();
	self = this;
	results = c;
	book = b;
	initialize();
    }

    private void addTag(String s) {

	if (Content != null && !s.isEmpty()) {

	    JLabel value = new JLabel(s);
	    value.setFont(new Font("Sans", Font.BOLD, 14));

	    tagArea.add(value);
	    tagArea.revalidate();
	}
    }

    /**
     * This method initializes send
     * 
     * @return javax.swing.JButton
     */
    private JButton deleteResult() {

	if (delete == null) {
	    delete = new JButton();
	    delete.setText("Delete");
	    delete.setName("Delete");
	    delete.setAlignmentY(CENTER_ALIGNMENT);
	    delete.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    if (book != null) {
			results.removeResult(self);
		    }
		}
	    });
	}
	return delete;
    }

    /**
     * 
     */
    protected void draw() {

	validate();
	this.repaint();
	super.repaint();
    }

    public Book getBook() {
	return book;
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
	    tagContentConstraints.weightx = 1;
	    tagContentConstraints.weighty = 0;
	    tagContentConstraints.gridwidth = 10;
	    tagContentConstraints.insets = new Insets(5, 5, 5, 5);
	    tagContentConstraints.anchor = GridBagConstraints.WEST;
	    tagContentConstraints.fill = GridBagConstraints.HORIZONTAL;

	    tagConstraints = new GridBagConstraints();
	    tagConstraints.gridx = 0;
	    tagConstraints.gridy = 1;
	    tagConstraints.insets = new Insets(8, 5, 5, 5);
	    tagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    tagConstraints.fill = GridBagConstraints.NONE;
	    tagConstraints.weighty = 1;

	    tags = new GridBagConstraints();
	    tags.gridx = 1;
	    tags.gridy = 1;
	    tags.weighty = 1;
	    tags.insets = new Insets(5, 5, 5, 5);
	    tags.anchor = GridBagConstraints.NORTHWEST;
	    tags.fill = GridBagConstraints.NONE;

	    tagTitleConstraints = new GridBagConstraints();
	    tagTitleConstraints.insets = new Insets(8, 5, 5, 5);
	    tagTitleConstraints.gridy = 0;
	    tagTitleConstraints.gridx = 0;
	    tagTitleConstraints.anchor = GridBagConstraints.WEST;
	    tagTitleConstraints.fill = GridBagConstraints.NONE;

	    Content = new JPanel();
	    Content.setLayout(new GridBagLayout());

	    JLabel addTag = new JLabel("Add Tag: ");
	    addTag.setFont(new Font("Sans", Font.PLAIN, 14));

	    JLabel tagLabel = new JLabel("Tags: ");
	    tagLabel.setFont(new Font("Sans", Font.PLAIN, 14));

	    tagArea = new JPanel();
	    Content.add(tagArea, tags);

	    Content.add(addTag, tagTitleConstraints);
	    Content.add(tagLabel, tagConstraints);
	    Content.add(getJTextField(), tagContentConstraints);
	    this.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	if (book != null) {

	    int count = 0;
	    Iterator<Entry<String, Integer>> properties = book.enumerateTags();
	    while (properties.hasNext() && count < 20) {
		count++;
		Entry<String, Integer> e = properties.next();

		int weight = e.getValue();
		JLabel value = new JLabel(e.getKey() + "( " + weight + " )");
		value.setFont(new Font("Sans", Font.BOLD, 14));

		tagArea.add(value);
	    }

	    draw();
	}
	return Content;
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

		    Boolean duplicate = false;
		    Iterator<Entry<String, Integer>> b = book.enumerateTags();
		    while (b.hasNext()) {
			if (b.next().getKey().compareTo(tagContent.getText()) == 0) {
			    duplicate = true;
			    break;
			}
		    }

		    book.tag(tagContent.getText());
		    if (!duplicate)
			addTag(tagContent.getText());

		    tagContent.setText("");
		}

		draw();
	    }
	});

	return tagContent;
    }

    /**
     * This method initializes jToggleButton
     * 
     * @return javax.swing.JToggleButton
     */
    private JToggleButton getJToggleButton() {
	if (jToggleButton == null) {
	    jToggleButton = new JToggleButton("Details");
	    jToggleButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    if (!Content.isVisible()) {

			tagArea.setPreferredSize(new Dimension(
				Title.getWidth() - 150, 300));
			tagArea.revalidate();
		    }
		    Content.setVisible(!Content.isVisible());

		    revalidate();// draw();
		}
	    });
	}
	return jToggleButton;
    }

    /**
     * This method initializes Title
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTitle() {
	if (Title == null) {

	    GridBagConstraints deleteB = new GridBagConstraints();
	    deleteB.gridx = 2;
	    deleteB.gridy = 0;
	    deleteB.weightx = 0;
	    deleteB.insets = new Insets(5, 5, 5, 5);
	    deleteB.anchor = GridBagConstraints.CENTER;

	    GridBagConstraints expand = new GridBagConstraints();
	    expand.gridx = 0;
	    expand.gridy = 0;
	    expand.weightx = 0;
	    expand.insets = new Insets(5, 5, 5, 5);
	    expand.anchor = GridBagConstraints.CENTER;

	    GridBagConstraints name = new GridBagConstraints();
	    name.gridx = 1;
	    name.gridy = 0;
	    name.weightx = 1;
	    name.insets = new Insets(5, 5, 5, 5);
	    name.anchor = GridBagConstraints.CENTER;
	    name.fill = GridBagConstraints.NONE;

	    title = new JLabel("Title?");
	    title.setFont(new Font("Sans", Font.BOLD, 14));

	    if (book != null) {
		title.setText(book.getProperty("author") + "'s "
			+ book.getProperty("title"));
	    }

	    Title = new JPanel();

	    Title.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent event) {
		    toggleSelected();
		}
	    });

	    GridBagLayout layout = new GridBagLayout();
	    Title.setLayout(layout);

	    Title.add(title, name);
	    Title.add(getJToggleButton(), expand);
	    Title.add(deleteResult(), deleteB);
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
	this.add(getTitle(), BorderLayout.NORTH);
	this.add(getContent(), BorderLayout.CENTER);
	Content.setVisible(false);
	draw();
    }

    public Boolean isSelected() {
	return selected;
    }

    private void toggleSelected() {

	selected = !selected;
	if (selected)
	    this.setBorder(BorderFactory.createLineBorder(Color.red));
	else
	    this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

}
