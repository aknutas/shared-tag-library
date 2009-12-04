package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import data.Book;

/**
 * @author patrick
 * 
 */
public class Result extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int tagOffset = 1;
    private JPanel Title = null;
    private JButton delete = null;
    private JLabel title = null;
    private JPanel Content = null;
    private Book book = null;

    private JTextField tagContent = null;

    GridBagConstraints tagContentConstraints;

    GridBagConstraints tagTitleConstraints;
    GridBagConstraints tagConstraints;
    GridBagConstraints tags;  //  @jve:decl-index=0:
    private SearchResults results;

    private Result self;
    private JToggleButton jToggleButton = null;
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

	    if (tags.gridx < 10) {
		    tags.gridx = tags.gridx + tagOffset;
		} else {
		    tags.gridx = 1;
		    tags.gridy++;
		}
	    //tags.gridx = tags.gridx + tagOffset;

	    Content.add(value, tags);
	    revalidate();
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
	    tagContentConstraints.fill = GridBagConstraints.NONE;

	    tagConstraints = new GridBagConstraints();
	    tagConstraints.gridx = 0;
	    tagConstraints.gridy = 1;
	    tagConstraints.insets = new Insets(8, 5, 5, 5);
	    tagConstraints.anchor = GridBagConstraints.NORTHWEST;
	    tagConstraints.fill = GridBagConstraints.NONE;
	    tagConstraints.weighty = 1;
	    
	    tags = new GridBagConstraints();
	    tags.gridx = 0;
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

	    Content.add(addTag, tagTitleConstraints);
	    Content.add(tagLabel, tagConstraints);
	    Content.add(getJTextField(), tagContentConstraints);
	    this.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	if (book != null) {

	    Iterator<Entry<String, Integer>> properties = book.enumerateTags();
	    while (properties.hasNext()) {

		Entry<String, Integer> e = properties.next();

		JLabel value = new JLabel(e.getKey());
		value.setFont(new Font("Sans", Font.BOLD, 14));

		if (tags.gridx < 10) {
		    tags.gridx = tags.gridx + tagOffset;
		} else {
		    tags.gridx = 1;
		    tags.gridy++;
		}

		Content.add(value, tags);
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

		    book.tag(tagContent.getText());
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
			
			Content.setVisible(!Content.isVisible());

			revalidate();//draw();
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
	    
	    GridBagConstraints deleteB  = new GridBagConstraints();
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
		title.setText(book.getProperty("author") + "'s " + book.getProperty("title"));
	    }

	    Title = new JPanel();
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
	this.setSize(1000, 200);
	this.setBounds(new Rectangle(0, 0, 1000, 200));
	this.add(getTitle(), BorderLayout.NORTH);
	this.add(getContent(), BorderLayout.CENTER);
	Content.setVisible(false);
	try {
	    UIManager
		    .setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
	} catch (UnsupportedLookAndFeelException ex) {
	    System.out.println("Cannot set new Theme for Java Look and Feel.");
	}

	draw();
    }

}
